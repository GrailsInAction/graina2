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
package grails.plugin.searchable.internal.compass.config.mapping;

import java.util.Collection;
import java.util.Map;

import org.compass.core.config.CompassConfiguration;

/**
 * Configures Compass with a searchable domain class mapping
 *
 * @author Maurice Nicholson
 */
public interface SearchableGrailsDomainClassMappingConfigurator {

    /**
     * Returns the collection of GrailsDomainClasses that are mapped by this instance
     * @param grailsDomainClasses ALL domain classes
     * @return the collection of domain classes mapped by this instance
     */
    Collection getMappedBy(Collection grailsDomainClasses);

    /**
     * Configure the Mapping in the CompassConfiguration for the given domain class
     *
     * @param compassConfiguration the CompassConfiguration instance
     * @param configurationContext a configuration context, for flexible parameter passing
     * @param searchableGrailsDomainClasses searchable domain classes to map
     * @param allSearchableGrailsDomainClasses all searchable domain classes, whether configured here or elsewhere
     */
    void configureMappings(CompassConfiguration compassConfiguration, Map configurationContext, Collection searchableGrailsDomainClasses, Collection allSearchableGrailsDomainClasses);

    /**
     * Get this strategy's name
     * @return name
     */
    String getName();
}
