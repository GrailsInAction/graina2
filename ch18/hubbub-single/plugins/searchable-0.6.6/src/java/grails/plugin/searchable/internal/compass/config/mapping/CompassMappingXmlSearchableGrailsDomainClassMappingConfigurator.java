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

import grails.plugin.searchable.internal.compass.config.CompassXmlConfigurationSearchableCompassConfigurator;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.compass.core.config.CompassConfiguration;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * Configures Compass with searchable domain classes that have a corresponding Compass mapping XML classpath resource.
 *
 * @author Maurice Nicholson
 */
public class CompassMappingXmlSearchableGrailsDomainClassMappingConfigurator extends AbstractSimpleSearchableGrailsDomainClassMappingConfigurator implements ResourceLoaderAware, Ordered {
    private static final Log LOG = LogFactory.getLog(CompassMappingXmlSearchableGrailsDomainClassMappingConfigurator.class);

    private ResourceLoader resourceLoader;

    /**
     * Does this strategy handle the given domain class (and it's respective mapping type)
     *
     * @param grailsDomainClass the Grails domain class
     * @return true if the mapping of the class can be handled by this strategy
     */
    @Override
    public boolean isMappedBy(GrailsDomainClass grailsDomainClass) {
        Assert.notNull(resourceLoader, "resourceLoader cannot be null");
        Resource resource = getMappingResource(grailsDomainClass);
        return resource.exists();
    }

    /**
     * Configure the Mapping in the CompassConfiguration for the given domain class
     *
     * @param compassConfiguration          the CompassConfiguration instance
     * @param configurationContext          a configuration context, for flexible parameter passing
     * @param searchableGrailsDomainClasses searchable domain classes to map
     * @param allSearchableGrailsDomainClasses all searchable domain classes, whether configured here or elsewhere
     */
    public void configureMappings(CompassConfiguration compassConfiguration, Map configurationContext, Collection searchableGrailsDomainClasses, Collection allSearchableGrailsDomainClasses) {
        Assert.notNull(resourceLoader, "resourceLoader cannot be null");
        if (configurationContext.containsKey(CompassXmlConfigurationSearchableCompassConfigurator.CONFIGURED)) {
            return;
        }

        for (Iterator iter = searchableGrailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
            Resource resource = getMappingResource(grailsDomainClass);
            Assert.isTrue(resource.exists(), "expected mapping resource [" + resource + "] to exist but it does not");
            try {
                compassConfiguration.addURL(resource.getURL());
            } catch (IOException ex) {
                String message = "Failed to configure Compass with mapping resource for class [" + grailsDomainClass.getClazz().getName() + "] and resource [" + getMappingResourceName(grailsDomainClass) + "]";
                LOG.error(message, ex);
                throw new IllegalStateException(message + ": " + ex);
            }
        }
    }

    /**
     * Get this strategy's name
     *
     * @return name
     */
    public String getName() {
        return "Compass Mapping XML";
    }

    /**
     * Determines the order of this mapping configurator in relation to others
     * @return the order
     */
    public int getOrder() {
        return 0;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    private Resource getMappingResource(GrailsDomainClass grailsDomainClass) {
        return resourceLoader.getResource(getMappingResourceName(grailsDomainClass));
    }

    private String getMappingResourceName(GrailsDomainClass grailsDomainClass) {
        String className = grailsDomainClass.getClazz().getName();
        Assert.notNull(grailsDomainClass, "grailsDomainClass cannot be null");
        return "classpath:/" + className.replaceAll("\\.", "/") + ".cpm.xml";
    }
}
