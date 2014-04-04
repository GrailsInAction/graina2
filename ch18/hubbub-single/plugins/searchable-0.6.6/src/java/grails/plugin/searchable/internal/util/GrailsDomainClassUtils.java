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
package grails.plugin.searchable.internal.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty;
import org.codehaus.groovy.grails.orm.hibernate.cfg.CompositeIdentity;
import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder;
import org.codehaus.groovy.grails.orm.hibernate.cfg.Mapping;
import org.springframework.util.Assert;

/**
 * @author Maurice Nicholson
 */
public class GrailsDomainClassUtils {

    /**
     * Is the given property an identity property? Checks the property as well as the custom domain class mapping
     * @param domainClassProperty domain clas property
     * @return true if the property is the/an identity property
     */
    public static boolean isIndentityProperty(GrailsDomainClassProperty domainClassProperty) {
        Mapping mapping = new GrailsDomainBinder().getMapping(domainClassProperty.getDomainClass().getClazz());
        if (mapping != null && mapping.getIdentity() instanceof CompositeIdentity) {
            CompositeIdentity identity = (CompositeIdentity) mapping.getIdentity();
            return Arrays.asList(identity.getPropertyNames()).contains(domainClassProperty.getName());
        }
        return domainClassProperty.isIdentity();
    }

    /**
     * Get the subclasses for the given GrailsDomainClass, filtering out any not in the given collection
     * @param grailsDomainClass the GrailsDomainClass to find subclasses for
     * @param grailsDomainClasses the collection of valid sub-classes
     * @return the collection of subclasses, if any
     */
    public static Collection getSubClasses(GrailsDomainClass grailsDomainClass, Collection grailsDomainClasses) {
        Set subClasses = new HashSet(grailsDomainClass.getSubClasses());
        subClasses.retainAll(grailsDomainClasses);
        return subClasses;
    }

    /**
     * Get the parent GrailsDomainClass for the given GrailsDomainClass, if it
     * exists in the given collection otherwise null
     *
     * @param grailsDomainClass the class whose parent to find
     * @param grailsDomainClasses the collection of possible parents
     * @return null if the given class has no parent or the parent is not in the collection
     */
    public static GrailsDomainClass getSuperClass(GrailsDomainClass grailsDomainClass, Collection grailsDomainClasses) {
        Set candidates = new HashSet();
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass gdc = (GrailsDomainClass) iter.next();
            if (gdc.getSubClasses().contains(grailsDomainClass)) {
                candidates.add(gdc);
            }
        }
        if (candidates.isEmpty()) {
            return null;
        }
        while (candidates.size() > 1) {
            Set copy = new HashSet(candidates);
            for (Iterator iter = copy.iterator(); iter.hasNext(); ) {
                GrailsDomainClass supsup = (GrailsDomainClass) iter.next();
                boolean remove = false;
                for (Iterator iter2 = candidates.iterator(); iter2.hasNext(); ) {
                    GrailsDomainClass sup = (GrailsDomainClass) iter2.next();
                    if (supsup.getSubClasses().contains(sup)) {
                        remove = true;
                        break;
                    }
                }
                if (remove) {
                    candidates.remove(supsup);
                    break;
                }
            }
        }
        return (GrailsDomainClass) candidates.iterator().next();
    }

    /**
     * Get all the super-classes in the given GrailsDomainClass's hierarchy
     * @param grailsDomainClass
     * @param grailsDomainClasses
     * @return
     */
    public static Collection getSuperClasses(GrailsDomainClass grailsDomainClass, Collection grailsDomainClasses) {
        Set superClasses = new HashSet();
        do {
            GrailsDomainClass superClass = getSuperClass(grailsDomainClass, grailsDomainClasses);
            if (superClass != null) {
                superClasses.add(superClass);
            }
            grailsDomainClass = superClass;
        } while (grailsDomainClass != null);
        return superClasses;
    }

    /**
     * Get the actual (user-defined) Classes for the given GrailsDomainClass Collection.
     * Equivalent to collecting the results of <code>grailsDomainClass.getClazz()</code> on
     * each element
     *
     * @param grailsDomainClasses
     * @return A collection of User-defined classes, which may be empty
     */
    public static Collection getClazzes(Collection grailsDomainClasses) {
        if (grailsDomainClasses == null || grailsDomainClasses.isEmpty()) {
            return Collections.EMPTY_SET;
        }
        Set clazzes = new HashSet();
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            clazzes.add(((GrailsDomainClass) iter.next()).getClazz());
        }
        return clazzes;
    }

    /**
     * Gets the GrailsDomainClass for the given user-defined clazz
     * @param clazz the user-defined domain class
     * @param grailsDomainClasses the collection of GrailsDomainClasses to look thru
     * @return the corresponding GrailsDomainClass
     */
    public static GrailsDomainClass getGrailsDomainClass(Class clazz, Collection grailsDomainClasses) {
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
            if (grailsDomainClass.getClazz().equals(clazz)) {
                return grailsDomainClass;
            }
        }
        return null;
    }

    /**
     * Returns the named property for the given domain class's clazz
     * @param grailsDomainClasses a collection GrailsDomainClass
     * @param clazz the user domain Class
     * @param propertyName the property name
     * @return the property
     */
    public static GrailsDomainClassProperty getGrailsDomainClassProperty(Collection grailsDomainClasses, Class clazz, String propertyName) {
        GrailsDomainClass grailsDomainClass = getGrailsDomainClass(clazz, grailsDomainClasses);
        Assert.notNull(grailsDomainClass, "GrailsDomainClass not found for clazz [" + clazz + "]");
        return grailsDomainClass.getPropertyByName(propertyName);
    }

    /**
     * Is the given GrailsDomainClass with an inheritance hierarchy with the given collection?
     * @param grailsDomainClass the grails domain class
     * @param grailsDomainClasses the collection of grails domain classes
     * @return true if the given class is within a hierarchy: it has super or sub-classes
     */
    public static boolean isWithinInhertitanceHierarchy(GrailsDomainClass grailsDomainClass, Collection grailsDomainClasses) {
        if (getSuperClass(grailsDomainClass, grailsDomainClasses) != null) {
            return true;
        }
        for (Iterator iter = grailsDomainClass.getSubClasses().iterator(); iter.hasNext(); ) {
            Object o = iter.next();
            if (grailsDomainClasses.contains(o)) {
                return true;
            }
        }
        return false;
    }
}
