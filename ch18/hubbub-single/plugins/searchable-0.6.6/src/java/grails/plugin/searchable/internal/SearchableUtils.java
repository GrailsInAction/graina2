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
package grails.plugin.searchable.internal;

import grails.plugin.searchable.internal.util.PatternUtils;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsClassUtils;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;
import org.compass.core.Compass;
import org.compass.core.mapping.Mapping;
import org.compass.core.mapping.ResourceMapping;
import org.compass.core.mapping.osem.ObjectMapping;
import org.compass.core.spi.InternalCompass;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * General purpose utilities for the Grails Searchable Plugin
 *
 * @author Maurice Nicholson
 */
public class SearchableUtils {
    private static Log log = LogFactory.getLog(SearchableUtils.class);
    private static final String PROJECT_META_FILE = "application.properties";
    public static final String SEARCHABLE_PROPERTY_NAME = "searchable";
    public static final String ONLY = "only";
    public static final String EXCEPT = "except";

    /**
     * Is the given class an emebedded property of another domain class?
     * @param grailsDomainClass the GrailsDomainClass to check as n embedded property
     * @param grailsDomainClasses all GrailsDomainClasses
     * @return true if the given class is an embedded property of another class
     */
    public static boolean isEmbeddedPropertyOfOtherDomainClass(GrailsDomainClass grailsDomainClass, Collection grailsDomainClasses) {
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass other = (GrailsDomainClass) iter.next();
            if (grailsDomainClass == other) {
                continue;
            }
            GrailsDomainClassProperty[] domainClassProperties = other.getProperties();
            for (int i = 0; i < domainClassProperties.length; i++) {
                GrailsDomainClass referencedDomainClass = domainClassProperties[i].getReferencedDomainClass();
                if (referencedDomainClass != null && referencedDomainClass == grailsDomainClass && domainClassProperties[i].isEmbedded()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the given domain class's searchable property value, if any
     *
     * @param grailsDomainClass the Grails domain class
     * @return the searchable property value, or null
     */
    public static Object getSearchablePropertyValue(GrailsDomainClass grailsDomainClass) {
        return GrailsClassUtils.getStaticPropertyValue(grailsDomainClass.getClazz(), SEARCHABLE_PROPERTY_NAME);
    }

    /**
     * Gets the GrailsDomainClass artefacts from the aplication
     * @param application the Grails app
     * @return the Lit of domain classes
     */
    public static Collection getGrailsDomainClasses(GrailsApplication application) {
        Assert.notNull(application, "GrailsApplication cannot be null");
        Set domainClasses = new HashSet();
        for (int i = 0, max = application.getArtefacts(DomainClassArtefactHandler.TYPE).length; i < max; i++) {
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) application.getArtefacts(DomainClassArtefactHandler.TYPE)[i];
            domainClasses.add(grailsDomainClass);
        }
        return domainClasses;
    }

    /**
     * Get the identifier of the given domain class instance
     * @param compass Compass
     * @param alias Compass alias
     * @param instance object instance
     * @return identifier
     */
    public static Serializable getIdent(Compass compass, String alias, Object instance) {
        Assert.notNull(alias, "alias cannot be null");
        ResourceMapping resourceMapping = ((InternalCompass) compass).getMapping().getMappingByAlias(alias);
        Mapping[] ids = resourceMapping.getIdMappings();
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("Null or empty id mappings for alias [" + alias + "]");
        }
        if (ids.length > 1) {
            throw new IllegalArgumentException("Too many id mappings for alias [" + alias + "]: expected only 1");
        }
        return (Serializable) ((ObjectMapping) ids[0]).getGetter().get(instance);
    }

    /**
     * Returns the class type of the searchable property
     * @param grailsDomainClass
     * @param propertyName
     * @param searchableGrailsDomainClasses
     * @return
     */
    public static Class getSearchablePropertyAssociatedClass(GrailsDomainClass grailsDomainClass, String propertyName, Collection searchableGrailsDomainClasses) {
        Assert.notNull(grailsDomainClass, "grailsDomainClass cannot be null");
        Assert.notNull(propertyName, "propertyName cannot be null");
        return getSearchablePropertyAssociatedClass(grailsDomainClass.getPropertyByName(propertyName), searchableGrailsDomainClasses);
    }

    /**
     * Returns the class type of the searchable property
     * @param property
     * @param searchableGrailsDomainClasses
     * @return
     */
    public static Class getSearchablePropertyAssociatedClass(GrailsDomainClassProperty property, Collection searchableGrailsDomainClasses) {
        Assert.notNull(property, "property cannot be null");
        Assert.notNull(property.getDomainClass(), "grailsDomainClass cannot be null");
        Class propertyType = property.getType();
        Collection classes = getClasses(searchableGrailsDomainClasses);
        if (classes.contains(propertyType)) {
            return propertyType;
        }
        propertyType = property.getDomainClass().getRelatedClassType(property.getName());
        if (propertyType != null && classes.contains(propertyType)) {
            return propertyType;
        }

        // Handle generic collection types, e.g. Set<MyDomainClass>, for transient properties.
        propertyType = property.getType();

        if (Collection.class.isAssignableFrom(propertyType)) {
            Class elementClass = getElementClass(property);
            if (classes.contains(elementClass)) return elementClass;
        }
        return null;
    }

    /**
     * If the given domain class property is a generic collection, this method
     * returns the element type of that collection. Otherwise, it returns
     * <code>null</code>.
     */
    public static Class getElementClass(GrailsDomainClassProperty property) {
        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(
                property.getDomainClass().getClazz(),
                property.getName());
        Type type = descriptor.getReadMethod().getGenericReturnType();
        if (type instanceof ParameterizedType) {
            for (Type argType : ((ParameterizedType) type).getActualTypeArguments()) {
                return (Class) argType;
            }
        }

        return null;
    }

    /**
     * Returns a collection of user classes for the given GrailsDomainClass instances
     * @param grailsDomainClasses a collection of GrailsDomainClass instances
     * @return a collection of user classes
     */
    public static Collection getClasses(Collection grailsDomainClasses) {
        Assert.notNull(grailsDomainClasses, "grailsDomainClasses cannot be null");
        Set classes = new HashSet();
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            classes.add(((GrailsDomainClass) iter.next()).getClazz());
        }
        return classes;
    }

    /**
     * Should the named property be included in the mapping, according to the value of "searchable"?
     * @param propertyName
     * @param searchable
     * @return true if included
     */
    public static boolean isIncludedProperty(String propertyName, Object searchable) {
        if (searchable == null || (searchable instanceof Boolean && ((Boolean)searchable))) {
            return true;
        }
        if (!(searchable instanceof Map)) {
            return false;
        }
        Object only = ((Map) searchable).get(ONLY);
        if (only != null) {
            return isOrContains(propertyName, only);
        }
        return !isOrContains(propertyName, ((Map) searchable).get(EXCEPT));
    }

    private static boolean isOrContains(String thing, final Object value) {
        Collection values = null;
        if (value instanceof Collection) {
            values = (Collection) value;
        } else {
            values = new HashSet();
            ((Set)values).add(value);
        }
        for (Iterator iter = values.iterator(); iter.hasNext(); ) {
            String v = (String) iter.next();
            if (!PatternUtils.hasWildcards(v)) {
                if (v.equals(thing)) {
                    return true;
                }
            } else {
                Pattern pattern = PatternUtils.makePatternFromWilcardString(v);
                if (pattern.matcher(thing).matches()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tries to resolve the Grails application name
     * @param grailsApplication the GrailsApplication instance which may be null
     * @return the app name or "app.name" if not found
     */
    public static String getAppName(GrailsApplication grailsApplication) {
        Map metadata = null;
        if (grailsApplication != null) {
            metadata = grailsApplication.getMetadata();
        }
        if (metadata == null) {
            metadata = loadMetadata();
        }
        if (metadata == null || !metadata.containsKey("app.name")) {
            return "app.name";
        }
        return (String) metadata.get("app.name");
    }

    // adapted from DefaultGrailsApplication
    private static Map loadMetadata() {
        Resource r = new ClassPathResource(PROJECT_META_FILE);
        if (r.exists()) {
            return loadMetadata(r);
        }
        String basedir = System.getProperty("base.dir");
        if (basedir != null) {
            r = new FileSystemResource(new File(basedir, PROJECT_META_FILE));
            if (r.exists()) {
                return loadMetadata(r);
            }
        }
        return null;
    }

    private static Map loadMetadata(Resource resource) {
        try {
            Properties meta = new Properties();
            meta.load(resource.getInputStream());
            return meta;
        } catch (IOException e) {
//            GrailsUtil.deepSanitize(e);
            log.warn("No application metadata file found at " + resource);
        }
        return null;
    }
}
