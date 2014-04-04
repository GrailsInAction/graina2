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
package grails.plugin.searchable.internal.compass.mapping;

import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.converter.Converter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Maps a searchable GrailsDomainClass to Compass
 *
 * @author Maurice Nicholson
 */
public interface SearchableGrailsDomainClassCompassClassMapper {
    SearchableGrailsDomainClassCompassClassMapper init(
            CompassConfiguration configuration,
            Map<String, Converter> customConverters,
            List defaultExcludedProperties,
            Map defaultFormats);

    /**
     * Get the property mappings for the given GrailsDomainClass
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @param searchableValue the searchable value: true|false|Map|Closure
     * @param excludedProperties a List of properties NOT to map; may be ignored by impl
     * @return a List of CompassClassPropertyMapping
     */
    List getCompassClassPropertyMappings(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Object searchableValue, List excludedProperties);

    /**
     * Get the CompassClassMapping for the given GrailsDomainClass
     *
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @return the CompassClassMapping
     */
    CompassClassMapping getCompassClassMapping(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses);

    /**
     * Get the CompassClassMapping  for the given GrailsDomainClass and "searchable" value
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @param searchableValue the searchable value: true|false|Map|Closure
     * @param excludedProperties a List of properties NOT to map; may be ignored by impl
     * @return the CompassClassMapping
     */
    CompassClassMapping getCompassClassMapping(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Object searchableValue, List excludedProperties);

    /**
     * Does the implementation handle a "searchable" value of the given type?
     * @param searchableValue the searchable value
     * @return true if this implementation understands the type
     */
    boolean handlesSearchableValue(Object searchableValue);
}
