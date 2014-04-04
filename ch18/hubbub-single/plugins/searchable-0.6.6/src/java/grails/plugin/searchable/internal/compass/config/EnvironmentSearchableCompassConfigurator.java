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

import grails.plugin.searchable.internal.compass.SearchableCompassUtils;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.config.CompassConfiguration;
import org.compass.core.config.CompassEnvironment;
import org.compass.core.converter.Converter;

/**
 * Responsible for setting the Compass environment, most importantly the "connection" (ie, Lucene index dir),
 * as well as any other general properties
 *
 * @author Maurice Nicholson
 */
public class EnvironmentSearchableCompassConfigurator implements SearchableCompassConfigurator {
    private static final Log LOG = LogFactory.getLog(EnvironmentSearchableCompassConfigurator.class);

    private String connection;
    private Map compassSettings;
    private GrailsApplication grailsApplication;
    private Map beans;

    /**
     * Configure the Compass environment
     *
     * @param compassConfiguration the runtime config instance
     * @param configurationContext a context allowing flexible parameter passing
     */
    public void configure(CompassConfiguration compassConfiguration, Map configurationContext) {
        // Configure connection?
        if (compassConfiguration.getSettings().getSetting(CompassEnvironment.CONNECTION) == null) {
            String conn = connection;
            if (conn == null) {
                LOG.debug("No connection specified, using default");
                conn = SearchableCompassUtils.getDefaultConnection(grailsApplication);
            }
            LOG.info("Setting Compass connection to [" + conn + "]");
            compassConfiguration.setConnection(conn);
        }

        if (compassSettings != null) {
            for (Iterator iter = compassSettings.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next().toString();
                Object value = compassSettings.get(name);
                LOG.debug("Setting Compass setting [" + name + "] = [" + value + "]");
                compassConfiguration.setSetting(name, value);
            }
        }

        if (beans != null) {
            Map converters = (Map) configurationContext.get("customConverters");
            for (Iterator iter = beans.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                String name = (String) entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Converter) {
                    LOG.debug("Registering Converter bean [" + name + "] with value [" + value + "]");
                    compassConfiguration.registerConverter(name, (Converter) value);
                    converters.put(name, value);
                    //compassConfiguration.setSetting("compass.engine.converter." + name + ".type", value);
                } else if (value instanceof Analyzer) {
                    LOG.debug("Registering Analyzer bean [" + name + "] with value [" + value + "]");
                    compassConfiguration.setSetting("compass.engine.analyzer." + name + ".type", value);
                } else {
                    LOG.warn("Bean [" + name + "] value is null or not a recognised type [" + (value != null ? value.getClass().getName() : "null") + "] - ignoring");
                }
            }
        }
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public Map getCompassSettings() {
        return compassSettings;
    }

    public void setCompassSettings(Map compassSettings) {
        this.compassSettings = compassSettings;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    public void setBeans(Map beans) {
        this.beans = beans;
    }
}
