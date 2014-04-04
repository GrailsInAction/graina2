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
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;

/**
 * A thread-safe Compass query builder
 *
 * @author Maurice Nicholson
 */
public interface SearchableCompassQueryBuilder {

    /**
     * Build and return a CompassQuery
     *
     * @param grailsApplication
     * @param compassSession the current Compass session
     * @param options query options
     * @param args
     */
    CompassQuery buildQuery(GrailsApplication grailsApplication, CompassSession compassSession, Map options, Object args);
}
