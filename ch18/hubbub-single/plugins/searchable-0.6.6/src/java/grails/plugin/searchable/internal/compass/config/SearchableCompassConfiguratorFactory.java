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
package grails.plugin.searchable.internal.compass.config;

import grails.plugin.searchable.internal.compass.config.mapping.SearchableGrailsDomainClassMappingConfigurator;
import grails.plugin.searchable.internal.compass.config.mapping.SearchableGrailsDomainClassMappingConfiguratorFactory;
import grails.plugin.searchable.internal.compass.mapping.SearchableCompassClassMappingXmlBuilder;

import java.util.List;
import java.util.Map;

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Maurice Nicholson
 */
public abstract class SearchableCompassConfiguratorFactory {

    public static EnvironmentSearchableCompassConfigurator getEnvironmentConfigurator(String compassConnection, Map compassSettings, GrailsApplication grailsApplication, Map beans) {
        EnvironmentSearchableCompassConfigurator environment = new EnvironmentSearchableCompassConfigurator();
        environment.setConnection(compassConnection);
        environment.setCompassSettings(compassSettings);
        environment.setGrailsApplication(grailsApplication);
        environment.setBeans(beans);
        return environment;
    }

    public static CompassXmlConfigurationSearchableCompassConfigurator getCompassXmlConfigurator(ResourceLoader resourceLoader) {
        CompassXmlConfigurationSearchableCompassConfigurator compassXml = new CompassXmlConfigurationSearchableCompassConfigurator();
        compassXml.setResourceLoader(resourceLoader);
        return compassXml;
    }

    public static DefaultGrailsDomainClassMappingSearchableCompassConfigurator getDomainClassMappingConfigurator(GrailsApplication grailsApplication, ResourceLoader resourceLoader, Map defaultFormats, List defaultExcludedProperties, SearchableCompassClassMappingXmlBuilder compassClassMappingXmlBuilder) {
        SearchableGrailsDomainClassMappingConfigurator[] classMappingConfigurators = SearchableGrailsDomainClassMappingConfiguratorFactory.getSearchableGrailsDomainClassMappingConfigurators(resourceLoader, defaultFormats, defaultExcludedProperties, compassClassMappingXmlBuilder);

        return getDomainClassMappingConfigurator(
                grailsApplication,
                classMappingConfigurators,
                defaultFormats,
                defaultExcludedProperties,
                compassClassMappingXmlBuilder);
    }

    private static DefaultGrailsDomainClassMappingSearchableCompassConfigurator getDomainClassMappingConfigurator(
            GrailsApplication grailsApplication,
            SearchableGrailsDomainClassMappingConfigurator[] classMappingConfigurators,
            Map defaultFormats,
            List defaultExcludedProperties,
            SearchableCompassClassMappingXmlBuilder classMappingXmlBuilder) {
        DefaultGrailsDomainClassMappingSearchableCompassConfigurator mappings = new DefaultGrailsDomainClassMappingSearchableCompassConfigurator();
        mappings.setGrailsApplication(grailsApplication);
        mappings.setClassMappingStrategies(classMappingConfigurators);
        mappings.setClassMappingXmlBuilder(classMappingXmlBuilder);
        mappings.setDefaultFormats(defaultFormats);
        mappings.setDefaultExcludes(defaultExcludedProperties);
        return mappings;
    }
}
