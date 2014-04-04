/*
 * Copyright 2008 the original author or authors.
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

import grails.plugin.searchable.internal.SearchableUtils;
import grails.plugin.searchable.internal.compass.mapping.CompassMappingUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.compass.core.Compass;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryBuilder;
import org.compass.core.CompassSession;

/**
 * @author Maurice Nicholson
 */
public class MoreLikeThisCompassQueryBuilder extends AbstractSearchableCompassQueryBuilder {
    private static final Log LOG = LogFactory.getLog(MoreLikeThisCompassQueryBuilder.class);

    public MoreLikeThisCompassQueryBuilder(Compass compass) {
        super(compass);
    }

    public CompassQuery buildQuery(GrailsApplication grailsApplication, CompassSession compassSession, Map options, Object args) {
        List argv = new ArrayList(Arrays.asList((Object[]) args));
        // discard options Map from args
        for (Iterator iter = argv.iterator(); iter.hasNext(); ) {
            if (iter.next() instanceof Map) {
                iter.remove();
                break;
            }
        }
        Object instance = null;
        Collection domainClasses = SearchableUtils.getGrailsDomainClasses(grailsApplication);
        for (Iterator iter = argv.iterator(); iter.hasNext(); ) {
            Object obj = iter.next();
            for (Iterator di = domainClasses.iterator(); di.hasNext(); ) {
                GrailsDomainClass gdc = (GrailsDomainClass) di.next();
                if (obj.getClass().equals(gdc.getClazz())) {
                    instance = obj;
                    break;
                }
            }
        }

        Serializable id;
        String alias;
        if (instance != null) {
            Class clazz = instance.getClass();
            alias = CompassMappingUtils.getMappingAlias(getCompass(), clazz);
            id = SearchableUtils.getIdent(getCompass(), alias, instance);
            if (id == null) {
                throw new IllegalArgumentException("Domain instance for MoreLikeThis query builder must have an ID");
            }
        } else {
            Class clazz = (Class) options.get("class");
            if (clazz == null && options.containsKey("match")) {
                clazz = (Class) options.get("match");
            }
            alias = (String) options.get("alias");

            for (Iterator iter = argv.iterator(); iter.hasNext(); ) {
                Object it = iter.next();
                if (it instanceof Class) {
                    clazz = (Class) it;
                    alias = null; // override alias with class instance
                    iter.remove();
                    break;
                }
            }
            if (alias == null) {
                if (clazz != null) {
                    alias = CompassMappingUtils.getMappingAlias(getCompass(), clazz);
                } else {
                    throw new IllegalArgumentException("Neither class nor alias was provided for MoreLikeThis query builder: please provide one in either options (Map) or arguments (Object[])");
                }
            }
            id = (Serializable) options.get("id");
            if (id == null) {
                if (!argv.isEmpty()) {
                    id = (Serializable) argv.get(0);
                } else {
                    throw new IllegalArgumentException("Unable to find id for MoreLikeThis query builder. Please provide one in either options (Map) or arguments (Object[])");
                }
            }
        }
        CompassQueryBuilder.CompassMoreLikeThisQuery moreLikeThisQuery = compassSession.queryBuilder().moreLikeThis(alias, id);
        if (options.size() > 0) {
            Method[] methods = CompassQueryBuilder.CompassMoreLikeThisQuery.class.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                String name = method.getName();
                if (!name.startsWith("set") || name.length() < 4) {
                    continue;
                }
                if (method.getParameterTypes().length != 1) {
                    continue;
                }
                name = name.substring(3, 4).toLowerCase() + (name.length() > 4 ? name.substring(4) : "");
                if (!options.containsKey(name)) {
                    continue;
                }
                Object value = options.get(name);
                // convert List to String[]?
                if (method.getParameterTypes()[0].equals(String[].class) && value instanceof List) {
                    value = ((List) value).toArray(new String[((List) value).size()]);
                }
                try {
                    method.invoke(moreLikeThisQuery, new Object[] {value});
                } catch (Exception ex) {
                    LOG.warn("Failed to invoke method " + moreLikeThisQuery.getClass().getName() + "#" + method.getName() + " with value [" + value + "]", ex);
                }
            }
        }

        CompassQuery query = moreLikeThisQuery.toQuery();
        options.remove("class"); // discard class option so it's not added as match alias in CompassQuery by option helper; todo change "class" option to "match: instead
        options.put("class", options.get("match"));
        query = applyOptions(grailsApplication, getCompass(), compassSession, query, options);
//        System.out.println("Query [" + query + "]");
        return query;
    }
}
