/*
 * Copyright 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.searchable.internal.compass.search;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryParser.QueryParser;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.util.Assert;
import org.compass.core.util.ClassUtils;

/**
 * A Groovy CompassQuery builder, taking nested closures and dynamic method
 * invocation in the Groovy-builder style
 *
 * Note: Instances of this class are NOT thread-safe: you should create one
 * for the duration of your CompassSession then discard it
 *
 * @author Maurice Nicholson
 */
public class GroovyCompassQueryBuilder extends GroovyObjectSupport {
    private CompassQueryBuilder queryBuilder;

    /**
     * Constructor
     * @param queryBuilder a CompassQueryBuilder used internally to build the query
     */
    public GroovyCompassQueryBuilder(CompassQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    /**
     * Build a CompassQuery for the given closure and return it
     * @param closure a Closure which builds the query when executed
     * @return a CompassQuery built by the given Closure
     */
    public CompassQuery buildQuery(Closure closure) {
        CompassQueryBuildingClosureDelegate invoker = new CompassQueryBuildingClosureDelegate(queryBuilder);
        closure = (Closure) closure.clone();
        closure.setDelegate(invoker);
        Object result;
        if (closure.getMaximumNumberOfParameters() == 1) {
            result = closure.call(queryBuilder);
        } else {
            result = closure.call();
        }
        CompassQuery query = invoker.getQuery();
        return (CompassQuery) ((query == null) ? result : query);
    }

    /**
     * Directly invoke a builder method
     */
    @Override
    public Object invokeMethod(String name, Object args) {
        CompassQueryBuildingClosureDelegate invoker = new CompassQueryBuildingClosureDelegate(queryBuilder);
        InvokerHelper.invokeMethod(invoker, name, args);
        return invoker.getQuery();
    }

    /**
     * This class acts as the query-builder closure delegate, identifying
     * which method to call on which object and tying the result together at
     * the end
     *
     * Note: Instances of this class are NOT thread-safe
     */
    private static class CompassQueryBuildingClosureDelegate extends GroovyObjectSupport {
        private static final Log LOG = LogFactory.getLog(CompassQueryBuildingClosureDelegate.class);
        private static final Map SHORT_METHOD_NAMES = shortMethodNames();
        private static Map shortMethodNames() {
            Map shortMethodNames = new HashMap();
            shortMethodNames.put("should", "addShould");
            shortMethodNames.put("must", "addMust");
            shortMethodNames.put("mustNot", "addMustNot");
            shortMethodNames.put("sort", "addSort");
            return shortMethodNames;
        }
        private static final List BOOLEAN_ADDER_NAMES = booleanAdderNames();
        private static List booleanAdderNames() {
            List booleanAdderNames = new ArrayList();
            booleanAdderNames.add("addShould");
            booleanAdderNames.add("addMust");
            booleanAdderNames.add("addMustNot");
            return booleanAdderNames;
        }
        private static final Map SHORT_OPTION_NAMES = shortOptionNames();
        private static Map shortOptionNames() {
            Map shortOptionNames = new HashMap();
            shortOptionNames.put("defaultProperty", "defaultSearchProperty");
            shortOptionNames.put("andDefaultOperator", "useAndDefaultOperator");
            shortOptionNames.put("parser", "queryParser");
            return shortOptionNames;
        }

        private CompassQueryBuilder queryBuilder;
        private Stack stack = new Stack();
        private int depth = 0;
        private Object previous; // previous method invocation result, if any

        public CompassQueryBuildingClosureDelegate(CompassQueryBuilder queryBuilder) {
            this.queryBuilder = queryBuilder;
        }

        @Override
        public Object invokeMethod(String name, Object args) {
            if (isTraceEnabled()) {
                trace("invokeMethod(" + name + ", " + args + ")");
            }
            depth++;

            // Remove Closure and options Map
            Assert.isInstanceOf(Object[].class, args);
            List invokeArgs = new ArrayList();
            invokeArgs.addAll(Arrays.asList(((Object[]) args)));
            Closure closure = (Closure) remove(invokeArgs, Closure.class);
            Map options = (Map) remove(invokeArgs, Map.class);

            // Escape String queries?
            if (name.equals("queryString") && options != null && MapUtils.getBoolean(options, "escape")) {
                Assert.isInstanceOf(String.class, invokeArgs.get(0));
                invokeArgs.set(0, QueryParser.escape((String) invokeArgs.get(0)));
                options.remove("escape");
            }

            // Transform method name?
            if (SHORT_METHOD_NAMES.containsKey(name)) {
                name = (String) SHORT_METHOD_NAMES.get(name);
            }

            // Method to call is *on* CompassQuery but we have builder?
            boolean queryMethod = hasMethod(CompassQuery.class, name) && !hasMethod(queryBuilder, name);
            if (queryMethod && !hasMethod(peek(), name) && hasToQuery(peek())) {
                maybeAddPreviousShould(); // Lazy boolean?
                Object temp = pop();
                if (isTraceEnabled()) {
                    trace("converting " + getShortClassName(temp) + " to query for " + name);
                }
                temp = toQuery(temp);
                push(temp);
            } else if (queryMethod && !hasMethod(previous, name) && hasToQuery(previous)) {
                if (isTraceEnabled()) {
                    trace("converting " + getShortClassName(previous) + " to query for " + name);
                }
                previous = toQuery(previous);
            }

            // Implicit boolean?
            if (BOOLEAN_ADDER_NAMES.contains(name) && !isWithinBool()) {
                trace("Implicit boolean -- " + name + " called when the stack is " + stack + ", args " + invokeArgs);
                Object bool = queryBuilder.bool();
                push(bool);
            }

            // Method to call takes CompassQuery but we have Builder?
            if (BOOLEAN_ADDER_NAMES.contains(name)) { // && !isQuery(invokeArgs[0])) {
                if (invokeArgs.isEmpty()) {
                    Assert.notNull(closure, "Attempt to call " + name + " without a query or closure argument");
                    maybeAddPreviousShould();
                    trace("executing nested boolean closure");
                    Object temp = previous;
                    previous = null;
                    Object innerBool = InvokerHelper.invokeMethod(this, "bool", closure);
                    invokeArgs = new ArrayList();
                    invokeArgs.add(innerBool);
                    closure = null;
                    previous = temp;
                    trace("done nested boolean closure");
                }
                if (invokeArgs.size() > 0 && hasToQuery(invokeArgs.get(0))) {
                    CompassQuery query = toQuery(invokeArgs.get(0));
                    invokeArgs = new ArrayList();
                    invokeArgs.add(query);
                }
            }

            // Convert any BigDecimals to floats: simple but does the job for now
            for (int i = 0; i < invokeArgs.size(); i++) {
                if (invokeArgs.get(i) instanceof BigDecimal) {
                    invokeArgs.set(i, new Float(((BigDecimal) invokeArgs.get(i)).floatValue()));
                }
            }

            Object result = null;
            if (hasMethod(peek(), name)) {
                if (isTraceEnabled()) {
                    trace("invoking " + peek().getClass().getName() + "." + name + "(" + invokeArgs+ ")");
                }
                result = InvokerHelper.invokeMethod(pop(), name, invokeArgs.toArray());
                if (isTraceEnabled()) {
                    trace("result is " + result.getClass().getName() + " " + result);
                }
                push(result);
            } else if (hasMethod(queryBuilder, name)) {
                // Eager implicit boolean check for previous should clause
                if (previous != null && !isWithinBool()) {
                    trace("implicit boolean spotted");
                    CompassQueryBuilder.CompassBooleanQueryBuilder bool = queryBuilder.bool();
                    bool.addShould(toQuery(previous));
                    push(bool);
                    previous = null; // not necessary?
                } else {
                    // Lazy boolean?
                    maybeAddPreviousShould();
                }
                if (isTraceEnabled()) {
                    trace("invoking queryBuilder." + name + "(" + invokeArgs + ")");
                }
                result = InvokerHelper.invokeMethod(queryBuilder, name, invokeArgs.toArray());
                if (isTraceEnabled()) {
                    trace("result is " + result.getClass().getName() + " " + result);
                }
            } else if (hasMethod(previous, name)) {
                if (isTraceEnabled()) {
                    trace("invoking " + getShortClassName(previous) + "." + name + "(" + invokeArgs+ ")");
                }
                result = InvokerHelper.invokeMethod(previous, name, invokeArgs.toArray());
                if (isTraceEnabled()) {
                    trace("result is " + result.getClass().getName() + " " + result);
                }
            } else {
                throw new UnsupportedOperationException(
                    "No such method CompassQueryBuilder#" + name +
                    (peek() == null ? "" : " or " + getShortClassName(peek()) + "#" + name) +
                    (previous == null ? "" : " or " + getShortClassName(previous) + "#" + name) +
                    ". (Arguments were " + invokeArgs + ", stack is " + stack + ", result is " + result + ")" + //, this.result is ${this.result}) " +
                    ". Refer to the Compass API docs at http://www.opensymphony.com/compass/versions/1.1/api/org/compass/core/CompassQueryBuilder.html to see what methods are available"
                );
            }

            // Recurse into nested closure?
            if (closure != null) {
                trace("invoking the closure arg");
                push(result);
                Object temp = previous;
                previous = null;
                closure = (Closure) closure.clone();
                closure.setDelegate(this);
                depth++;
                closure.call();
                depth--;

                // Complete semi-implicit boolean?
                maybeAddPreviousShould();

                previous = temp;
                result = pop();
            }

            // Apply builder/query options?
            result = applyOptions(result, options);
            previous = result;
            depth--;

            if (isTraceEnabled()) {
                trace("after methods and closure, depth " + depth + ", stack " + stack + ", previous " + previous);
                trace("returning " + result);
            }
            return result;
        }

        private void maybeAddPreviousShould() {
            if (previous != null && isWithinBool() && !previous.equals(peek())) {
                trace("previous lazy boolean should clause spotted");
                InvokerHelper.invokeMethod(peek(), "addShould", toQuery(previous));
                previous = null; // not necessary?
            }
        }

        private Object applyOptions(Object result, Map options) {
            if (options == null || options.size() == 0) {
                return result;
            }

            // Convert shorthand to longhand names
            Map tmp = new HashMap();
            for (Iterator iter = options.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String optionName = (String) entry.getKey();
                if (SHORT_OPTION_NAMES.containsKey(optionName)) {
                    tmp.put(SHORT_OPTION_NAMES.get(optionName), entry.getValue());
                } else {
                    tmp.put(optionName, entry.getValue());
                }
            }
            options = tmp;

            // Convert any BigDecimals to floats: simple but does the job for now
            for (Iterator iter = options.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String optionName = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof BigDecimal) {
                    options.put(optionName, new Float(((BigDecimal) value).floatValue()));
                }
            }

            if (isTraceEnabled()) {
                trace("applying options " + options + " to " + result);
            }
            Map queryOptions = new HashMap();

            boolean hasToQuery = !isQuery(result) && hasToQuery(result);
            for (Iterator iter = options.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String optionName = (String) entry.getKey();
                Object optionValue = entry.getValue();
                // special case for this single no-parameter, non-setter string query builder method
                if (optionName.equals("useAndDefaultOperator")) {
                    if (optionValue != null) {
                        Assert.isTrue(result instanceof CompassQueryBuilder.CompassMultiPropertyQueryStringBuilder || result instanceof  CompassQueryBuilder.CompassQueryStringBuilder, "'useAndDefaultOperator' option provided when current query/builder is a " + getShortClassName(result) + ", but should be a Compass*QueryStringBuilder");
                        if (optionValue.equals(Boolean.TRUE)) {
                            InvokerHelper.invokeMethod(result, "useAndDefaultOperator", null);
                        } else {
                            InvokerHelper.invokeMethod(result, "useOrDefaultOperator", null);
                        }
                    }
                    continue;
                }
                if (optionName.equals("defaultOperator")) {
                    Assert.notNull(optionValue, "'defaultOperator' option value is null: it must be one of 'or' or 'and'");
                    Assert.isInstanceOf(String.class, optionValue, "'defaultOperator' option value is must be a String but is: [" + optionValue.getClass().getName() + "]");
                    if (((String)optionValue).equalsIgnoreCase("or")) {
                        InvokerHelper.invokeMethod(result, "useOrDefaultOperator", null);
                    } else if (((String)optionValue).equalsIgnoreCase("and")) {
                        InvokerHelper.invokeMethod(result, "useAndDefaultOperator", null);
                    } else {
                        throw new IllegalArgumentException("'defaultOperator' option value is not valid: it must be one of 'or' or 'and' but was '" + optionValue + "'");
                    }
                    continue;
                }
                String methodName = "set" + optionName.substring(0, 1).toUpperCase() + optionName.substring(1);
                trace("method is -- " + methodName + "(" + optionValue + ")");
                if (hasMethod(result, methodName)) {
                    if (isTraceEnabled()) {
                        trace("invoking " + getShortClassName(result) + "." + methodName + "(" + optionValue + ")");
                    }
                    result = InvokerHelper.invokeMethod(result, methodName, optionValue);
                } else if (hasToQuery && hasMethod(CompassQuery.class, methodName)) {
                    trace("added option to queryOptions, " + queryOptions);
                    queryOptions.put(optionName, optionValue);
                } else {
                    throw new UnsupportedOperationException(
                        "No such method " + getShortClassName(result) + "#" + methodName + (!hasToQuery ? "" : " or CompassQuery#" + methodName) +
                        ". (Arguments were " + optionValue + ") " +
                        ". Refer to the Compass API docs at http://www.opensymphony.com/compass/versions/1.1/api/org/compass/core/CompassQueryBuilder.html to see what methods are available"
                    );
                }
            }

            if (!queryOptions.isEmpty()) {
                trace("setting query options");
                result = toQuery(result);
                for (Iterator iter = queryOptions.entrySet().iterator(); iter.hasNext(); ) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String optionName = (String) entry.getKey();
                    Object optionValue = entry.getValue();
                    String methodName = "set" + optionName.substring(0, 1).toUpperCase() + optionName.substring(1);
                    if (isTraceEnabled()) {
                        trace("method is -- " + methodName + "(" + optionValue + ")");
                    }
                    Assert.isTrue(hasMethod(result, methodName));
                    if (isTraceEnabled()) {
                        trace("invoking " + getShortClassName(result) + "#" + methodName + "(" + optionValue + ")");
                    }
                    result = InvokerHelper.invokeMethod(result, methodName, optionValue);
                }
            }

            return result;
        }

        public CompassQuery getQuery() {
            if (previous != null) {
                boolean completeBoolean = false;
                if (isWithinBool() && !previous.equals(peek())) {
                    if (isTraceEnabled()) {
                        trace("Within bool " + peek() + ", and previous is " + previous);
                    }
                    completeBoolean = true;
                }
                if (!isQuery(previous)) {
                    trace("converting result to query for caller");
                    previous = toQuery(previous);
                }
                if (completeBoolean) {
                    trace("completing boolean");
                    InvokerHelper.invokeMethod(peek(), "addShould", previous);
                    previous = toQuery(pop());
                }
            }
            return (CompassQuery) previous;
        }

        private boolean isWithinBool() {
            return isBoolBuilder(peek());
        }

        private void push(Object object) {
            stack.push(object);
            if (isTraceEnabled()) {
                trace("pushed " + getShortClassName(object) + " " + object + " to stack");
            }
        }

        private Object pop() {
            if (isTraceEnabled()) {
                trace("popping " + (peek() == null ? "null" : (getShortClassName(peek()) + " " + peek())) + " from the stack");
            }
            return stack.empty() ? null : stack.pop();
        }

        private Object peek() {
            return stack.empty() ? null : stack.peek();
        }

        private boolean hasMethod(Object thing, String name) {
            if (thing == null) {
                return false;
            }
            Class clazz = (Class) (thing instanceof Class ? thing : thing.getClass());
            Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (method.getName().equals(name)) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasToQuery(Object object) {
            return object instanceof CompassQueryBuilder.ToCompassQuery;
        }

        private CompassQuery toQuery(Object object) {
            if (isQuery(object)) return (CompassQuery) object;
            return ((CompassQueryBuilder.ToCompassQuery) object).toQuery();
        }

        private boolean isQuery(Object object) {
            return object instanceof CompassQuery;
        }

        private boolean isBoolBuilder(Object object) {
            return object instanceof CompassQueryBuilder.CompassBooleanQueryBuilder;
        }

        private String getShortClassName(Object thing) {
            if (thing == null) {
                return "null";
            }
            Class clazz = (Class) (thing instanceof Class ? thing : thing.getClass());
            return ClassUtils.getShortName(clazz);
        }

        private Object remove(List args, Class clazz) {
            if (args == null) {
                return null;
            }
            for (int i = 0; i < args.size(); i++) {
                if (clazz.isAssignableFrom(args.get(i).getClass())) {
                    return args.remove(i);
                }
            }
            return null;
        }

        private boolean isTraceEnabled() {
            return LOG.isTraceEnabled();
        }

        private void trace(String message) {
            StringBuilder buf = new StringBuilder(message.length() + depth * 2);
            for (int i = 0; i < depth; i++) {
                buf.append("  ");
            }
            buf.append(message);
            LOG.trace(buf);
        }
    }
}
