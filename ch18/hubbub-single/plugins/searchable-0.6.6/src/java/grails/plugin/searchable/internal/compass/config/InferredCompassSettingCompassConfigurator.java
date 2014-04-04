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

import grails.plugin.searchable.internal.compass.mapping.CompassMappingUtils;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.core.config.CompassConfiguration;

/**
 * Sets Compas settings depending on mappings, eg, enables global spell check setting
 * if any mappings use it
 *
 * @author Maurice Nicholson
 */
public class InferredCompassSettingCompassConfigurator implements SearchableCompassConfigurator {
    private static final Log LOG = LogFactory.getLog(InferredCompassSettingCompassConfigurator.class);
    private static final String COMPASS_SPELL_CHECK_SETTING = "compass.engine.spellcheck.enable";

    public void configure(CompassConfiguration compassConfiguration, Map configurationContext) {
        boolean spellCheck = CompassMappingUtils.hasSpellCheckMapping(compassConfiguration);
        if (spellCheck) {
            if (compassConfiguration.getSettings().getSetting(COMPASS_SPELL_CHECK_SETTING) == null) {
                compassConfiguration.setSetting(COMPASS_SPELL_CHECK_SETTING, "true");
                LOG.debug("Enabled Compass global Spell Check setting \"" + COMPASS_SPELL_CHECK_SETTING + "\" since it was not already set and there are spell-check mappings");
            }
        }
    }

}