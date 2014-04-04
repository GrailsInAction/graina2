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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.groovy.grails.commons.GrailsDomainClass;

/**
 * @author Maurice Nicholson
 */
public abstract class AbstractSimpleSearchableGrailsDomainClassMappingConfigurator implements SearchableGrailsDomainClassMappingConfigurator {

    /**
     * Returns the collection of GrailsDomainClasses that are mapped by this instance
     * @param grailsDomainClasses ALL domain classes
     * @return the collection of domain classes mapped by this instance
     */
    public Collection getMappedBy(Collection grailsDomainClasses) {
        Set mappedBy = new HashSet();
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
            if (isMappedBy(grailsDomainClass)) {
                mappedBy.add(grailsDomainClass);
            }
        }
        return mappedBy;
    }

    /**
     * Is the given domain class mapped by this instance?
     * @param grailsDomainClass domain class
     * @return true if this mapper handles the given domain class
     */
    public abstract boolean isMappedBy(GrailsDomainClass grailsDomainClass);
}
