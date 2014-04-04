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

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Filter;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassQuery;
import org.compass.core.CompassQueryFilter;
import org.compass.core.CompassSession;
import org.compass.core.lucene.util.LuceneHelper;

/**
 * Post-processes a query to add a filter
 *
 * @author Phillip Rhodes PCR
 */
public class SearchableCompassQueryBuilderFilterOptionHelper implements SearchableCompassQueryBuilderOptionsHelper {
    public static final String FILTER = "filter";

    private static final Log LOG = LogFactory.getLog(SearchableCompassQueryBuilderFilterOptionHelper.class);

    public CompassQuery applyOptions(GrailsApplication grailsApplication, Compass compass, CompassSession compassSession, CompassQuery compassQuery, Map options) {
        return addFilter(compassQuery, options,compassSession);
    }

    public CompassQuery addFilter(CompassQuery compassQuery, Map options,CompassSession compassSession) {

        if (compassSession == null) {
            LOG.warn("compassSession is null in SearchableCompassQueryBuilderFilterOptionHelper  ");
        }

        try {
            Filter filter = (Filter) options.get("filter");
            if (filter == null) {
                return compassQuery;
            }
            CompassQueryFilter compassQueryFilter = LuceneHelper.createCompassQueryFilter(compassSession , filter);

            compassQuery.setFilter(compassQueryFilter);

            return compassQuery;
        }
        catch(Exception e) {
            LOG.error(e.getMessage(), e);
            return compassQuery;
        }
    }
}
