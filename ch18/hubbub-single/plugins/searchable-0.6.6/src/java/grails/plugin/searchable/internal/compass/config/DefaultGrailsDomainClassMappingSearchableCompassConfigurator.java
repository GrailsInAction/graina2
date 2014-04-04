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

import grails.plugin.searchable.internal.SearchableUtils;
import grails.plugin.searchable.internal.compass.config.mapping.AppConfigMappingConfigurator;
import grails.plugin.searchable.internal.compass.config.mapping.SearchableClassPropertySearchableGrailsDomainClassMappingConfigurator;
import grails.plugin.searchable.internal.compass.config.mapping.SearchableGrailsDomainClassMappingConfigurator;
import grails.plugin.searchable.internal.compass.mapping.AppConfigClassMapper;
import grails.plugin.searchable.internal.compass.mapping.CompositeSearchableGrailsDomainClassCompassClassMapper;
import grails.plugin.searchable.internal.compass.mapping.SearchableCompassClassMappingXmlBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.compass.core.config.CompassConfiguration;
import org.springframework.core.OrderComparator;
import org.springframework.util.Assert;

/**
 * A Compass configurator that configures Compass with the Grails domain class mappings.
 *
 * An appropriate mapping strategy is identified for each searchable domain class from the
 * {@link #classMappingConfigurators}
 *
 * @author Maurice Nicholson
 */
public class DefaultGrailsDomainClassMappingSearchableCompassConfigurator implements SearchableCompassConfigurator {
    private static final Log LOG = LogFactory.getLog(DefaultGrailsDomainClassMappingSearchableCompassConfigurator.class);

    private GrailsApplication grailsApplication;
    private SearchableGrailsDomainClassMappingConfigurator[] classMappingConfigurators;

    private Map defaultFormats;
    private List defaultExcludes;
    private SearchableCompassClassMappingXmlBuilder classMappingXmlBuilder;

    /**
     * Configure Compass ready for it to be built
     *
     * @param compassConfiguration runtime configuration instance
     * @param configurationContext a context allowing flexible parameter passing
     */
    public void configure(CompassConfiguration compassConfiguration, Map configurationContext) {
        Assert.notNull(grailsApplication, "grailsApplication cannot be null");
        Assert.notNull(classMappingConfigurators, "classMappingConfigurators cannot be null");

        CompositeSearchableGrailsDomainClassCompassClassMapper classMapper = null;

        // determine which classes are mapped by which strategy
        Map classesByStrategy = new HashMap();
        Collection grailsDomainClasses = SearchableUtils.getGrailsDomainClasses(grailsApplication);
        Set mappableClasses = new HashSet();
        Set notMapped = new HashSet(grailsDomainClasses);
        for (int i = 0; i < classMappingConfigurators.length; i++) {
            SearchableGrailsDomainClassMappingConfigurator configurator = classMappingConfigurators[i];

            // Total hack. This seems to be the easiest way to initialise this
            // particular property mapping configurator.
            if (configurator instanceof SearchableClassPropertySearchableGrailsDomainClassMappingConfigurator) {
                classMapper = ((SearchableClassPropertySearchableGrailsDomainClassMappingConfigurator) configurator).
                        getMappingDescriptionProviderManager();
                classMapper.init(
                        compassConfiguration,
                        (Map) configurationContext.get("customConverters"),
                        defaultExcludes,
                        defaultFormats);
            }

            Collection classes = configurator.getMappedBy(notMapped);
            if (classes != null) {
                notMapped.removeAll(classes);
                if (LOG.isDebugEnabled()) {
                    for (Iterator iter = classes.iterator(); iter.hasNext(); ) {
                        GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
                        LOG.debug("Mapping class [" + grailsDomainClass.getClazz().getName() + "] with strategy [" + configurator.getName() + "]");
                    }
                }
                classesByStrategy.put(classMappingConfigurators[i], classes);
                mappableClasses.addAll(classes);
            }
        }

        // Deal with any domain classes configured through the application's runtime
        // config. This is treated differently to the other configuration options
        // because it can override existing mapping information. Also, it requires
        // access to the application config object.
        AppConfigClassMapper overrideClassMapper = new AppConfigClassMapper(grailsApplication.getConfig());
        overrideClassMapper.init(compassConfiguration, (Map) configurationContext.get("customConverters"), defaultExcludes, defaultFormats);

        AppConfigMappingConfigurator appConfigConfigurator = new AppConfigMappingConfigurator(grailsApplication.getConfig());
        appConfigConfigurator.setMappingDescriptionProviderManager(overrideClassMapper);
        appConfigConfigurator.setCompassClassMappingXmlBuilder(classMappingXmlBuilder);

        Collection appConfigMapped = appConfigConfigurator.getMappedBy(grailsDomainClasses);
        mappableClasses.addAll(appConfigMapped);

        // Check whether search has been explicitly removed from any domain classes.
        Collection unmapped = appConfigConfigurator.getUnmapped(grailsDomainClasses);
        mappableClasses.removeAll(unmapped);
        notMapped.addAll(unmapped);

        if (LOG.isDebugEnabled() && !notMapped.isEmpty()) {
            for (Iterator iter = notMapped.iterator(); iter.hasNext(); ) {
                GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
                LOG.debug("No mapping strategy found for class [" + grailsDomainClass.getClazz().getName() + "]: assuming this class is not searchable");
            }
        }

        // map classes in the order defined by the classMappingConfigurators
        for (int i = 0; i < classMappingConfigurators.length; i++) {
            SearchableGrailsDomainClassMappingConfigurator classMappingConfigurator = classMappingConfigurators[i];
            Collection classes = (Collection) classesByStrategy.get(classMappingConfigurator);
            if (classes != null && !classes.isEmpty()) {
                classMappingConfigurator.configureMappings(compassConfiguration, configurationContext, classes, mappableClasses);
            }
        }

        // Finally, execute the config-based configurator so that it can add and
        // override mappings.
        if (appConfigMapped != null && !appConfigMapped.isEmpty()) {
            appConfigConfigurator.configureMappings(compassConfiguration, configurationContext, appConfigMapped, mappableClasses);
        }
    }

    public GrailsApplication getGrailsApplication() {
        return grailsApplication;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    public void setClassMappingStrategies(SearchableGrailsDomainClassMappingConfigurator[] classMappingConfigurators) {
        Arrays.sort(classMappingConfigurators, new OrderComparator());
        this.classMappingConfigurators = classMappingConfigurators;
    }

    public void setDefaultFormats(Map defaultFormats) {
        this.defaultFormats = defaultFormats == null ? new LinkedHashMap() : new LinkedHashMap(defaultFormats);
    }

    public void setDefaultExcludes(List defaultExcludedProperties) {
        this.defaultExcludes = defaultExcludedProperties == null ? new ArrayList() : new ArrayList(defaultExcludedProperties);
    }

    public void setClassMappingXmlBuilder(SearchableCompassClassMappingXmlBuilder classMappingXmlBuilder) {
        this.classMappingXmlBuilder = classMappingXmlBuilder;
    }
}
