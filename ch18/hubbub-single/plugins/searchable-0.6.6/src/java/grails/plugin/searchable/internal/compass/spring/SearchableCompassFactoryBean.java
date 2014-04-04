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
package grails.plugin.searchable.internal.compass.spring;

import grails.plugin.searchable.internal.compass.config.SearchableCompassConfigurator;
import grails.plugin.searchable.internal.compass.converter.StringMapConverter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.Compass;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassConfigurationFactory;
import org.compass.core.converter.Converter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A pluggable Spring factory bean for Compass
 *
 * @author Maurice Nicholson
 */
public class SearchableCompassFactoryBean implements FactoryBean<Compass>, DisposableBean, ApplicationContextAware {
    private static final Log LOG = LogFactory.getLog(SearchableCompassFactoryBean.class);

    private ApplicationContext applicationContext;
    private SearchableCompassConfigurator searchableCompassConfigurator;
    private Compass compass;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Compass getObject() throws Exception {
        if (compass == null) {
            buildCompass();
        }
        return compass;
    }

    public Class<Compass> getObjectType() {
        return Compass.class;
    }

    public boolean isSingleton() {
        return true;
    }

    private void buildCompass() {
        LOG.debug("Building new Compass");

        CompassConfiguration configuration = CompassConfigurationFactory.newConfiguration();

        // TODO find a better place for this
        // register custom converters
        Map converters = new HashMap();
        Map context = new HashMap();
        context.put("customConverters", converters);

        Converter converter = new StringMapConverter();
        configuration.registerConverter(StringMapConverter.CONVERTER_NAME, converter);
        converters.put(StringMapConverter.CONVERTER_NAME, converter);

        // register analyzers used internally
        configuration.getSettings().setSetting("compass.engine.analyzer.searchableplugin_whitespace.type", "whitespace");
        configuration.getSettings().setSetting("compass.engine.analyzer.searchableplugin_simple.type", "simple");

        // add reference to Spring in Compass
        configuration.getSettings().setObjectSetting(ApplicationContext.class.getName() + ".INSTANCE", applicationContext);

        searchableCompassConfigurator.configure(configuration, context);

        compass = configuration.buildCompass();

        LOG.debug("Done building Compass");
    }

    public SearchableCompassConfigurator getSearchableCompassConfigurator() {
        return searchableCompassConfigurator;
    }

    public void setSearchableCompassConfigurator(SearchableCompassConfigurator searchableCompassConfigurator) {
        this.searchableCompassConfigurator = searchableCompassConfigurator;
    }

    /**
     * Destroy the Compass instance (if created), typically called when shutting down the Spring
     * application context.
     *
     * Just calls {@link org.compass.core.Compass#close()}
     *
     * @throws Exception
     */
    public void destroy() throws Exception {
        if (compass != null) {
            compass.close();
        }
    }
}
