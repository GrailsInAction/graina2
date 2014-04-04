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

import java.util.Map;

import org.compass.core.config.CompassConfiguration;

/**
 * Configures Compass
 *
 * @author Maurice Nicholson
 */
public interface SearchableCompassConfigurator {

    /**
     * Configure Compass ready for it to be built
     *
     * @param compassConfiguration runtime configuration instance
     * @param configurationContext a context allowing flexible parameter passing
     */
    void configure(CompassConfiguration compassConfiguration, Map configurationContext);
}
