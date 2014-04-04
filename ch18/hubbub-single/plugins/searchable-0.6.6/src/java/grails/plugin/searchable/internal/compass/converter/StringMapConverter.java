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
package grails.plugin.searchable.internal.compass.converter;

import groovy.util.Eval;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.compass.core.CompassException;
import org.compass.core.Property;
import org.compass.core.Resource;
import org.compass.core.config.CompassConfigurable;
import org.compass.core.config.CompassSettings;
import org.compass.core.converter.ConversionException;
import org.compass.core.converter.Converter;
import org.compass.core.engine.SearchEngineFactory;
import org.compass.core.engine.naming.PropertyNamingStrategy;
import org.compass.core.mapping.Mapping;
import org.compass.core.mapping.ResourcePropertyMapping;
import org.compass.core.marshall.MarshallingContext;
import org.compass.core.spi.InternalCompass;
import org.compass.core.spi.InternalCompassSession;

/**
 * Based on the example from Compass's unit tests
 *
 * @author Maurice Nicholson
 */
public class StringMapConverter implements Converter, CompassConfigurable {
    public static final String CONVERTER_NAME = "stringmap";

    private boolean supportUnmarshall = true;

    public void configure(CompassSettings settings) throws CompassException {
        supportUnmarshall = settings.getSettingAsBoolean("supportUnmarshall", true);
    }

    public boolean marshall(Resource resource, Object root, Mapping mapping, MarshallingContext context) throws ConversionException {
        if (root == null) {
            return false;
        }

        ResourcePropertyMapping resourcePropertyMapping = (ResourcePropertyMapping) mapping;
        Map map = (Map) root;
        Set entries;
        try {
            entries = map.entrySet();
        } catch (NullPointerException ex) {
            // this can happen with Hibernate when cascading the delete from an owner of a Map<String, String>
            // see http://jira.codehaus.org/browse/GRAILSPLUGINS-482
            return false;
        }
        for (Iterator it = entries.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            Property p = context.getResourceFactory().createProperty(entry.getKey().toString(), entry.getValue().toString(),
                    resourcePropertyMapping.getStore(), resourcePropertyMapping.getIndex(), resourcePropertyMapping.getTermVector());
            p.setBoost(resourcePropertyMapping.getBoost());
            resource.addProperty(p);
        }

        if (supportUnmarshall) {
            String stringmap = DefaultGroovyMethods.inspect(map);
            InternalCompassSession compassSession = context.getSession();
            InternalCompass compass = compassSession.getCompass();
            SearchEngineFactory searchEngineFactory = compass.getSearchEngineFactory();
            PropertyNamingStrategy propertyNamingStrategy =
                    searchEngineFactory.getPropertyNamingStrategy();
            // save stringifiedmap map (under an internal name)
            String keyPath = propertyNamingStrategy.buildPath(resourcePropertyMapping.getPath(), "stringmap").getPath();
            Property p = context.getResourceFactory().createProperty(keyPath, stringmap, Property.Store.YES, Property.Index.NOT_ANALYZED);
            resource.addProperty(p);
        }

        return true;
    }

    public Object unmarshall(Resource resource, Mapping mapping, MarshallingContext context) throws ConversionException {
        if (!supportUnmarshall) {
            return null;
        }

        ResourcePropertyMapping resourcePropertyMapping = (ResourcePropertyMapping) mapping;
//        SearchEngine searchEngine = context.getSearchEngine();

        PropertyNamingStrategy propertyNamingStrategy = context.getSession().getCompass().getSearchEngineFactory().getPropertyNamingStrategy();
        // parse map (from an internal name)
        String path = propertyNamingStrategy.buildPath(resourcePropertyMapping.getPath(), "stringmap").getPath();
        String stringmap = resource.getValue(path);
        if (stringmap == null) {
            return null;
        }
        return Eval.me(stringmap);
    }

    public boolean isSupportUnmarshall() {
        return supportUnmarshall;
    }

    public void setSupportUnmarshall(boolean supportUnmarshall) {
        this.supportUnmarshall = supportUnmarshall;
    }
}
