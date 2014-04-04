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

import grails.plugin.searchable.internal.compass.support.SearchableMethodUtils;
import groovy.lang.Closure;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.compass.core.Compass;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * The default query builder strategy
 *
 * @author Maurice Nicholson
 */
public class DefaultSearchableCompassQueryBuilder extends AbstractSearchableCompassQueryBuilder {
    private static final Log LOG = LogFactory.getLog(DefaultSearchableCompassQueryBuilder.class);

    private SearchableCompassQueryBuilder stringQueryBuilder;
    private Class closureQueryBuilderClass;

    public DefaultSearchableCompassQueryBuilder(Compass compass) {
        super(compass);
        stringQueryBuilder = new DefaultStringQuerySearchableCompassQueryBuilder(getCompass());
        String name = "grails.plugin.searchable.internal.compass.search.GroovyCompassQueryBuilder";
        try {
            closureQueryBuilderClass = ClassUtils.forName(name);
        } catch (Exception ex) {
            LOG.error("Class not found [" + name + "]", ex);
            throw new IllegalStateException("Class not found [" + name + "]");
        }
    }

    public CompassQuery buildQuery(GrailsApplication grailsApplication, CompassSession compassSession, Map options, Object args) {
        Object query = SearchableMethodUtils.getQueryArgument(args);
        Assert.notNull(query, "Missing String or Closure query argument");

        CompassQuery compassQuery;
        if (query instanceof String) {
            compassQuery = stringQueryBuilder.buildQuery(grailsApplication, compassSession, options, query);
        } else {
            Assert.isInstanceOf(Closure.class, query, "query is neither String nor Closure: must be one of these but is [" + query.getClass().getName() + "]");
            Object closureQueryBuilder = InvokerHelper.invokeConstructorOf(closureQueryBuilderClass, compassSession.queryBuilder());
            compassQuery = (CompassQuery) InvokerHelper.invokeMethod(closureQueryBuilder, "buildQuery", query );
        }
//        Object clazz = options.get("class");
//        if (clazz != null) {
//            LOG.warn("The 'class' option for the Searchable Plugin search method is deprecated. Please use 'match' instead");
//            System.out.println("WARN: The 'class' option for the Searchable Plugin search method is deprecated. Please use 'match' instead");
//            if (!options.containsKey("match")) {
//                options.put("match", )
//            }
//        }
        if (!options.containsKey("class") && options.containsKey("match")) {
            options.put("class", options.get("match"));
        }
        return applyOptions(grailsApplication, getCompass(), compassSession, compassQuery, options);
    }
}
