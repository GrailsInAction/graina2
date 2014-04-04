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

import grails.plugin.searchable.internal.compass.converter.DefaultCompassConverterLookupHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.converter.Converter;
import org.compass.core.converter.ConverterLookup;
import org.compass.core.converter.DefaultConverterLookup;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * A searchable GrailsDomainClass class mapper for Compass
 *
 * @author Maurice Nicholson
 */
public class CompositeSearchableGrailsDomainClassCompassClassMapper extends AbstractSearchableGrailsDomainClassCompassClassMapper {
    private static final Log LOG = LogFactory.getLog(CompositeSearchableGrailsDomainClassCompassClassMapper.class);

    private SearchableGrailsDomainClassCompassClassMapper[] classMappers;

    public CompositeSearchableGrailsDomainClassCompassClassMapper init(
            CompassConfiguration configuration,
            Map<String, Converter> customConverters,
            List defaultExcludedProperties,
            Map defaultFormats) {
        // Code copied from Compass, since we don't want to initialise/build
        // a Compass instance just to get the converter lookup.
        ConverterLookup converterLookup = new DefaultConverterLookup();
        converterLookup.configure(configuration.getSettings().copy());
        for (String converterName : customConverters.keySet()) {
            converterLookup.registerConverter(converterName, customConverters.get(converterName));
        }

        DefaultCompassConverterLookupHelper converterLookupHelper = new DefaultCompassConverterLookupHelper();
        converterLookupHelper.setConverterLookup(converterLookup);

        SearchableGrailsDomainClassPropertyMappingFactory domainClassPropertyMappingFactory = new SearchableGrailsDomainClassPropertyMappingFactory();
        domainClassPropertyMappingFactory.setDefaultFormats(defaultFormats);
        domainClassPropertyMappingFactory.setConverterLookupHelper(converterLookupHelper);

        setDefaultExcludedProperties(defaultExcludedProperties);
        classMappers = getActualSearchableGrailsDomainClassCompassClassMappers(domainClassPropertyMappingFactory);

        return this;
    }

    /**
     * Get the property mappings for the given GrailsDomainClass
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @param searchableValue the searchable value: true|false|Map|Closure
     * @param excludedProperties a List of properties NOT to map; may be ignored by impl
     * @return a List of CompassClassPropertyMapping
     */
    public List getCompassClassPropertyMappings(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Object searchableValue, List excludedProperties) {
        SearchableGrailsDomainClassCompassClassMapper classMapper = getCompassClassMapperForSearchableValue(searchableValue);
        Assert.notNull(classMapper, "No class mapper found for class [" + grailsDomainClass.getClazz().getName() + "]. Does the class declare a searchable property?");
        return classMapper.getCompassClassPropertyMappings(grailsDomainClass, searchableGrailsDomainClasses, searchableValue, excludedProperties);
    }

    /**
     * Get the CompassClassMapping  for the given GrailsDomainClass and "searchable" value
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @param searchableValue the searchable value: true|false|Map|Closure
     * @param excludedProperties a List of properties NOT to map; may be ignored by impl
     * @return the CompassClassMapping
     */
    public CompassClassMapping getCompassClassMapping(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Object searchableValue, List excludedProperties) {
        SearchableGrailsDomainClassCompassClassMapper classMapper = getCompassClassMapperForSearchableValue(searchableValue);
        Assert.notNull(classMapper, "No class mapper found for class [" + grailsDomainClass.getClazz().getName() + "]. Does the class declare a searchable property?");
        return classMapper.getCompassClassMapping(grailsDomainClass, searchableGrailsDomainClasses, searchableValue, excludedProperties);
    }

    /**
     * Does the implementation handle a "searchable" value of the given type?
     * @param searchableValue the searchable value
     * @return true if this implementation understands the type
     */
    public boolean handlesSearchableValue(Object searchableValue) {
        return getCompassClassMapperForSearchableValue(searchableValue) != null;
    }

    private SearchableGrailsDomainClassCompassClassMapper getCompassClassMapperForSearchableValue(Object searchableValue) {
        for (int i = 0; i < classMappers.length; i++) {
            if (classMappers[i].handlesSearchableValue(searchableValue)) {
                return classMappers[i];
            }
        }
        return null;
    }

    public SearchableGrailsDomainClassCompassClassMapper[] getSearchableGrailsDomainClassCompassMappingDescriptionProviders() {
        return classMappers;
    }

    public void setSearchableGrailsDomainClassCompassMappingDescriptionProviders(SearchableGrailsDomainClassCompassClassMapper[] classMappers) {
        this.classMappers = classMappers;
    }

    private SearchableGrailsDomainClassCompassClassMapper[] getActualSearchableGrailsDomainClassCompassClassMappers(SearchableGrailsDomainClassPropertyMappingFactory domainClassPropertyMappingFactory) {
        SearchableGrailsDomainClassCompassClassMapper[] classMappers;
        try {
            SimpleSearchableGrailsDomainClassCompassClassMapper simpleClassMapper = new SimpleSearchableGrailsDomainClassCompassClassMapper();
            simpleClassMapper.setDomainClassPropertyMappingStrategyFactory(domainClassPropertyMappingFactory);
            simpleClassMapper.setParent(this);

            AbstractSearchableGrailsDomainClassCompassClassMapper closureClassMapper = (AbstractSearchableGrailsDomainClassCompassClassMapper) ClassUtils.forName("grails.plugin.searchable.internal.compass.mapping.ClosureSearchableGrailsDomainClassCompassClassMapper").newInstance();
            closureClassMapper.setDomainClassPropertyMappingStrategyFactory(domainClassPropertyMappingFactory);
            closureClassMapper.setParent(this);

            classMappers = new SearchableGrailsDomainClassCompassClassMapper[] {
                simpleClassMapper, closureClassMapper
            };
        } catch (Exception ex) {
            // Log and throw runtime exception
            LOG.error("Failed to find or create closure mapping provider class instance", ex);
            throw new IllegalStateException("Failed to find or create closure mapping provider class instance: " + ex);
        }
        return classMappers;
    }
}
