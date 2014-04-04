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
 * A composite implememtation of SearchableCompassConfigurator that simply applies all
 * the composed configurators in turn
 *
 * @author Maurice Nicholson
 */
public class CompositeSearchableCompassConfigurator implements SearchableCompassConfigurator {
    private SearchableCompassConfigurator[] searchableCompassConfigurators;

    /**
     * Configures Compass by applying all searchableCompassConfigurators
     *
     * @param compassConfiguration the Compass runtime config instance
     * @param configurationContext a context allowing flexible parameter passing
     */
    public void configure(CompassConfiguration compassConfiguration, Map configurationContext) {
        for (int i = 0, max = searchableCompassConfigurators.length; i < max; i++) {
            searchableCompassConfigurators[i].configure(compassConfiguration, configurationContext);
        }
    }

    public SearchableCompassConfigurator[] getSearchableCompassConfigurators() {
        return searchableCompassConfigurators;
    }

    public void setSearchableCompassConfigurators(SearchableCompassConfigurator[] searchableCompassConfigurators) {
        this.searchableCompassConfigurators = searchableCompassConfigurators;
    }
}
