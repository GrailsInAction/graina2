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
package grails.plugin.searchable.internal.support;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.springframework.beans.BeanUtils;

/**
 * @author Maurice Nicholson
 */
public class DynamicMethodUtils {

    /**
     * Extract the property names from the given method name suffix
     *
     * @param methodSuffix a method name suffix like 'NameAndAddress'
     * @return a Collection of property names like ['name', 'address']
     */
    public static Collection extractPropertyNames(String methodSuffix) {
        String[] splited = methodSuffix.split("And");
        Set propertyNames = new HashSet();
        for (int i = 0; i < splited.length; i++) {
            if (splited[i].length() > 0) {
                propertyNames.add(Introspector.decapitalize(splited[i]));
            }
        }
        return propertyNames;
    }

    /**
     * Extract the property names from the given method name suffix accoring
     * to the given Class's properties
     *
     * @param clazz the Class to resolve property names against
     * @param methodSuffix a method name suffix like 'NameAndAddressCity'
     * @return a Collection of property names like ['name', 'address']
     */
    public static Collection extractPropertyNames(Class clazz, String methodSuffix) {
        String joinedNames = methodSuffix;
        Set propertyNames = new HashSet();
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(clazz);
        for (int i = 0; i < propertyDescriptors.length; i++) {
            String name = propertyDescriptors[i].getName();
            String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (joinedNames.indexOf(capitalized) > -1) { // uses indexOf instead of contains for Java 1.4 compatibility
                propertyNames.add(name);
                joinedNames = DefaultGroovyMethods.minus(joinedNames, capitalized);
            }
        }
        if (joinedNames.length() > 0) {
            propertyNames.addAll(extractPropertyNames(joinedNames));
        }
        return propertyNames;
    }
}
