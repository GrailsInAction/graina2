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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;
import org.compass.core.Compass;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * @author Maurice Nicholson
 */
public class SearchableGrailsDomainClassCompassMappingUtils {
    private static final Log LOG = LogFactory.getLog(SearchableGrailsDomainClassCompassMappingUtils.class);

    /**
     * Is the given GrailsDomainClass a root class in the Compass mapping?
     * @param grailsDomainClass the domain class to check for
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @return true unless it's an embedded class in another domain class without explicit searchable declaration
     */
    public static boolean isRoot(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses) {
        // TODO log warning when used as both component and non-component
        Object value = SearchableUtils.getSearchablePropertyValue(grailsDomainClass);
        if (value instanceof Boolean) {
            return (Boolean)value;
        }
        if (value == null) {
            return !SearchableUtils.isEmbeddedPropertyOfOtherDomainClass(grailsDomainClass, searchableGrailsDomainClasses);
        }
        return true;
    }

    /**
     * Get the mappable domain class properties
     * @param grailsDomainClass
     * @param searchableValue
     * @param searchableGrailsDomainClasses
     * @param excludedProperties
     * @param domainClassPropertyMappingFactory
     * @return
     */
    public static GrailsDomainClassProperty[] getMappableProperties(GrailsDomainClass grailsDomainClass, Object searchableValue, Collection searchableGrailsDomainClasses, final List excludedProperties, SearchableGrailsDomainClassPropertyMappingFactory domainClassPropertyMappingFactory) {
        boolean defaultExcludes = false;
        if (searchableValue instanceof Boolean) {
            if (!(Boolean)searchableValue) {
                return null;
            }
            searchableValue = new HashMap();
            ((Map)searchableValue).put("except", excludedProperties);
            defaultExcludes = true;
        }

        Class mappedClass = grailsDomainClass.getClazz();
        List properties = new ArrayList();
        GrailsDomainClassProperty[] domainClassProperties = grailsDomainClass.getProperties();
        for (int i = 0, max = domainClassProperties.length; i < max; i++) {
            GrailsDomainClassProperty property = domainClassProperties[i];
            String propertyName = property.getName();
            if (!GrailsDomainClassUtils.isIndentityProperty(property) && !SearchableUtils.isIncludedProperty(propertyName, searchableValue)) {
                LOG.debug(
                    "Not mapping [" + ClassUtils.getShortName(mappedClass) + "." + propertyName + "] because of " +
                    (defaultExcludes ? "default property exclusions" : "specified only/except rule")
                );
                continue;
            }

            if (domainClassPropertyMappingFactory.getGrailsDomainClassPropertyMapping(property, searchableGrailsDomainClasses) == null) {
                LOG.debug("Can't map [" + ClassUtils.getShortName(mappedClass) + "." + propertyName + "]");
                continue;
            }
            LOG.debug("Mapping [" + ClassUtils.getShortName(mappedClass) + "." + propertyName + "]");
            properties.add(property);
        }
        return (GrailsDomainClassProperty[]) properties.toArray(new GrailsDomainClassProperty[properties.size()]);
    }

    /**
     * Builds a CompassClassMapping for the GrailsDomainClass and property mappings
     * @param grailsDomainClass
     * @param searchableGrailsDomainClasses
     * @param propertyMappings
     * @return
     */
    public static CompassClassMapping buildCompassClassMapping(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, List propertyMappings) {
        CompassClassMapping classMapping = new CompassClassMapping();
        classMapping.setMappedClass(grailsDomainClass.getClazz());
        classMapping.setPropertyMappings(propertyMappings);
        if (classMapping.getRoot() == null) {
            classMapping.setRoot(Boolean.valueOf(SearchableGrailsDomainClassCompassMappingUtils.isRoot(grailsDomainClass, searchableGrailsDomainClasses)));
        }
        Collection superClasses = GrailsDomainClassUtils.getSuperClasses(grailsDomainClass, searchableGrailsDomainClasses);
        if (!superClasses.isEmpty()) {
            GrailsDomainClass parent = GrailsDomainClassUtils.getSuperClass(grailsDomainClass, superClasses);
            classMapping.setMappedClassSuperClass(parent.getClazz());
        }
        if (GrailsDomainClassUtils.isWithinInhertitanceHierarchy(grailsDomainClass, searchableGrailsDomainClasses)) {
            Map data = new HashMap();
            data.put("index", "not_analyzed");
            data.put("excludeFromAll", true);
            classMapping.addConstantMetaData("$/poly/class", data, Arrays.asList(grailsDomainClass.getClazz().getName()));
        }
        classMapping.setPoly(!grailsDomainClass.getSubClasses().isEmpty() || classMapping.getMappedClassSuperClass() != null);
        return classMapping;
    }

    /**
     * Merges the given property mappings, overriding parent mappings with child mappings
     * @param mappedProperties
     * @param parentClassPropertyMappings
     */
    public static void mergePropertyMappings(List mappedProperties, List parentClassPropertyMappings) {
        if (parentClassPropertyMappings == null) {
            return;
        }
        Assert.notNull(mappedProperties, "mappedProperties cannot be null");
        List temp = new ArrayList(parentClassPropertyMappings);
        temp.addAll(mappedProperties);
        for (Iterator citer = mappedProperties.iterator(); citer.hasNext(); ) {
            CompassClassPropertyMapping cmapping = (CompassClassPropertyMapping) citer.next();
            for (Iterator piter = parentClassPropertyMappings.iterator(); piter.hasNext(); ) {
                CompassClassPropertyMapping pmapping = (CompassClassPropertyMapping) piter.next();
                if (cmapping.getPropertyName().equals(pmapping.getPropertyName())) {
                    temp.remove(pmapping);
                }
            }
        }
        mappedProperties.clear();
        mappedProperties.addAll(temp);
    }

    /**
     * Get the mapping aliases for the given user-defined domain class and its subclasses if any
     * @param compass Compass instance
     * @param clazz the user-defined domain class
     * @param application the GrailsApplication
     * @return the Compass aliases for the hierarchy
     */
    public static String[] getPolyMappingAliases(Compass compass, Class clazz, GrailsApplication application) {
        List grailsDomainClasses = Arrays.asList(application.getArtefacts(DomainClassArtefactHandler.TYPE));
        GrailsDomainClass grailsDomainClass = GrailsDomainClassUtils.getGrailsDomainClass(clazz, grailsDomainClasses);
        Collection clazzes = new HashSet(GrailsDomainClassUtils.getClazzes(grailsDomainClass.getSubClasses()));
        clazzes.add(clazz);
        return CompassMappingUtils.getMappingAliases(compass, clazzes);
    }
}
