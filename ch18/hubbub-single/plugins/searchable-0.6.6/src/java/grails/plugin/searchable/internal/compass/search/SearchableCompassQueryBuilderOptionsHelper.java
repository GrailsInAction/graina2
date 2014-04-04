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

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;

/**
 * A helper for applying query options
 *
 * @author Maurice Nicholson
 */
public interface SearchableCompassQueryBuilderOptionsHelper {

    /**
     * Apply the options and return the (possibly new) query
     *
     * TODO reduce the number of parameters!
     * @param grailsApplication the GrailsApplication
     * @param compass Compass instance
     * @param compassSession the current Compass session
     * @param compassQuery the query to apply options to
     * @param options the options to apply, if any @return a (maybe new) query with options applied @return query with options applied
     */
    CompassQuery applyOptions(GrailsApplication grailsApplication, Compass compass, CompassSession compassSession, CompassQuery compassQuery, Map options);
}
