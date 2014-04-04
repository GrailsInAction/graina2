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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.SortField;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.compass.core.lucene.util.LuceneHelper;
import org.springframework.util.Assert;

/**
 * Post-processes a query to add sort
 *
 * @author Maurice Nicholson  PCR
 */
public class SearchableCompassQueryBuilderSortOptionHelper implements SearchableCompassQueryBuilderOptionsHelper {
    public static final String DIRECTION = "direction";
    public static final String ORDER = "order";
    public static final List VALID_SORT_DIRECTION_VALUES = Arrays.asList("asc", "desc", "auto", "reverse");

    private static final Log LOG = LogFactory.getLog(SearchableCompassQueryBuilderSortOptionHelper.class);

    public CompassQuery applyOptions(GrailsApplication grailsApplication, Compass compass, CompassSession compassSession, CompassQuery compassQuery, Map options) {
        addSortField(compassQuery,options);
        return addSort(compassQuery, options);
    }

    public void addSortField( CompassQuery compassQuery,Map options) {
        SortField sortField = (SortField) options.get("sortField");
        if (sortField != null) {
            LOG.debug("added sortfield " + sortField);
            LuceneHelper.getLuceneSearchEngineQuery(compassQuery).addSort(sortField);
        }
    }

    public CompassQuery addSort(CompassQuery compassQuery, Map options) {
        String sort = (String) options.get("sort");
        if (sort == null) {
            return compassQuery;
        }
        Object sortProperty = getSortProperty(sort);
        CompassQuery.SortDirection direction = getSortDirection(sortProperty, options);

        if (sortProperty instanceof CompassQuery.SortImplicitType) {
            compassQuery = compassQuery.addSort((CompassQuery.SortImplicitType) sortProperty, direction);
        } else {
            Assert.isInstanceOf(String.class, sortProperty, "Expected string");
            compassQuery = compassQuery.addSort((String) sortProperty, direction);
        }

        return compassQuery;
    }

    private Object getSortProperty(String sort) {
        Assert.notNull(sort, "sort cannot be null");
        if (sort.equals("SCORE")) {
            return CompassQuery.SortImplicitType.SCORE;
        }
        return sort;
    }

    /**
     * Get the CompassQuery.SortDirection for the given property and optional order/direction Map entry
     * @param property either CompassQuery.SortImplicitType.SCORE or a class property name (String)
     * @param options a Map containg
     * @return
     */
    public CompassQuery.SortDirection getSortDirection(Object property, Map options) {
        Assert.notNull(property, "sort property cannot be null");
        Assert.notNull(options, "options Map cannot be null");
        if (!options.containsKey(ORDER) && !options.containsKey(DIRECTION)) {
            return CompassQuery.SortDirection.AUTO;
        }

        Assert.isTrue((options.containsKey(ORDER) && !options.containsKey(DIRECTION)) || (!options.containsKey(ORDER) && options.containsKey(DIRECTION)), "Either specify a sort '" + ORDER + "' or '" + DIRECTION + "' or neither but not both");
        String value = (String) options.get(DIRECTION);
        if (value == null) {
            value = (String) options.get(ORDER);
        }
        Assert.isTrue(VALID_SORT_DIRECTION_VALUES.contains(value), "The sort order/direction '" + value + "' is not a valid value");
        return property.equals(CompassQuery.SortImplicitType.SCORE) ?
                value.equals("asc") || value.equals("reverse") ? CompassQuery.SortDirection.REVERSE : CompassQuery.SortDirection.AUTO :
                value.equals("asc") || value.equals("auto") ? CompassQuery.SortDirection.AUTO : CompassQuery.SortDirection.REVERSE;
    }
}
