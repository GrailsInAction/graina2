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

import grails.plugin.searchable.internal.SearchableUtils;
import grails.plugin.searchable.internal.util.GrailsDomainClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;

/**
 * @author Maurice Nicholson
 */
public abstract class AbstractSearchableGrailsDomainClassCompassClassMapper implements SearchableGrailsDomainClassCompassClassMapper {
    private static final List<String> DEFAULT_EXCLUDED_PROPERTIES = Arrays.asList("password");

    private SearchableGrailsDomainClassPropertyMappingFactory domainClassPropertyMappingFactory;
    private List defaultExcludedProperties;
    private SearchableGrailsDomainClassCompassClassMapper parent;

    /**
     * Get the CompassClassMapping for the given GrailsDomainClass
     *
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @return the CompassClassMapping
     */
    public CompassClassMapping getCompassClassMapping(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses) {
        return getCompassClassMapping(grailsDomainClass, searchableGrailsDomainClasses, SearchableUtils.getSearchablePropertyValue(grailsDomainClass), getExcludedProperties());
    }

    protected CompassClassPropertyMapping getDefaultPropertyMapping(GrailsDomainClassProperty property, Collection searchableClasses) {
        return domainClassPropertyMappingFactory.getGrailsDomainClassPropertyMapping(property, searchableClasses);
    }

    protected List getInheritedPropertyMappings(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, List excludedProperties) {
        List parentMappedProperties = new ArrayList();
        GrailsDomainClass superClass = GrailsDomainClassUtils.getSuperClass(grailsDomainClass, searchableGrailsDomainClasses);
        while (superClass != null) {
            List parentClassPropertyMappings = parent.getCompassClassPropertyMappings(superClass, searchableGrailsDomainClasses, SearchableUtils.getSearchablePropertyValue(superClass), excludedProperties);
            SearchableGrailsDomainClassCompassMappingUtils.mergePropertyMappings(parentMappedProperties, parentClassPropertyMappings);
            superClass = GrailsDomainClassUtils.getSuperClass(superClass, searchableGrailsDomainClasses);
        }
        return parentMappedProperties;
    }

    protected List getExcludedProperties() {
        if (defaultExcludedProperties != null) {
            return defaultExcludedProperties;
        }
        return DEFAULT_EXCLUDED_PROPERTIES;
    }

    public void setDefaultExcludedProperties(List defaultExcludedProperties) {
        this.defaultExcludedProperties = defaultExcludedProperties;
    }

    public SearchableGrailsDomainClassPropertyMappingFactory getDomainClassPropertyMappingStrategyFactory() {
        return domainClassPropertyMappingFactory;
    }

    public void setDomainClassPropertyMappingStrategyFactory(SearchableGrailsDomainClassPropertyMappingFactory domainClassPropertyMappingFactory) {
        this.domainClassPropertyMappingFactory = domainClassPropertyMappingFactory;
    }

    public SearchableGrailsDomainClassCompassClassMapper getParent() {
        return parent;
    }

    public void setParent(SearchableGrailsDomainClassCompassClassMapper parent) {
        this.parent = parent;
    }
}
