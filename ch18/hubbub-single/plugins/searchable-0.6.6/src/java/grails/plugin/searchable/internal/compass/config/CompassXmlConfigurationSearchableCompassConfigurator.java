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

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.config.CompassConfiguration;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

/**
 * A SearchableCompassConfigurator that configures Compass with a native compass.cfg.xml at the
 * root of the classpath, if available
 *
 * @author Maurice Nicholson
 */
public class CompassXmlConfigurationSearchableCompassConfigurator implements SearchableCompassConfigurator, ResourceLoaderAware {
    private static Log LOG = LogFactory.getLog(CompassXmlConfigurationSearchableCompassConfigurator.class);
    public static final String CONFIGURED = CompassXmlConfigurationSearchableCompassConfigurator.class.getName() + ".CONFIGURED";

    private ResourceLoader resourceLoader;

    /**
     * Configure Compass ready for it to be built
     *
     * @param compassConfiguration runtime configuration instance
     * @param configurationContext a context allowing flexible parameter passing
     */
    public void configure(CompassConfiguration compassConfiguration, Map configurationContext) {
        Assert.notNull(compassConfiguration, "compassConfiguration cannot be null");
        Assert.notNull(resourceLoader, "resourceLoader cannot be null");

        Resource resource = resourceLoader.getResource("classpath:/compass.cfg.xml");
        if (resource.exists()) {
            Assert.notNull(configurationContext, "configurationContext cannot be null");
            try {
                LOG.debug("Configuring Compass with compass config file [" + resource.getURL() + "]");
                compassConfiguration.configure(resource.getURL());
            } catch (IOException ex) {
                LOG.error("Failed to configure Compass with classpath resource, even though it apparently exists: " + ex, ex);
                throw new IllegalStateException("Failed to configure Compass with classpath resource, even though it apparently exists: " + ex);
            }
            configurationContext.put(CONFIGURED, true);
        }
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
