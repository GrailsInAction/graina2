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
package grails.plugin.searchable.internal.compass;

import grails.plugin.searchable.internal.SearchableUtils;
import grails.util.Environment;

import java.io.File;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.compass.core.Compass;
import org.compass.core.spi.InternalCompass;

/**
 * Utilities for Compass and Grails Searchable Plugin
 *
 * @author Maurice Nicholson
 */
public class SearchableCompassUtils {

    /**
     * Get the default Compass connection (ie, Lucene index dir)
     *
     * @param grailsApplication the GrailsApplication - may be null
     * @return {user.home}/.grails/projects/{project-name}/searchable-index/{grails.env}
     */
    public static String getDefaultConnection(GrailsApplication grailsApplication) {
        String appName = SearchableUtils.getAppName(grailsApplication);
        return new StringBuilder(System.getProperty("user.home")).
            append(File.separator).
            append(".grails").
            append(File.separator).
            append("projects").
            append(File.separator).
            append(appName).
            append(File.separator).
            append("searchable-index").
            append(File.separator).
            append(Environment.getCurrent().getName()).
            toString();
    }

    /**
     * Does the Compass instance have mappings?
     * @param compass Compass
     * @return true if Compass has mappings
     */
    public static boolean hasMappings(Compass compass) {
        return ((InternalCompass) compass).getMapping().getRootMappings().length > 0;
    }

    /**
     * Is the given GrailsDomainClass a root mapped class in Compass
     * @param domainClass the GrailsDomainClass
     * @param compass Compass
     * @return true if there is a root mapping for the class in Compass
     */
    public static boolean isRootMappedClass(GrailsDomainClass domainClass, Compass compass) {
        return ((InternalCompass) compass).getMapping().getRootMappingByClass(domainClass.getClazz()) != null;
    }
}
