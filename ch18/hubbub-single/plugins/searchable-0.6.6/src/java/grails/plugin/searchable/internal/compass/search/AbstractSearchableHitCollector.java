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
package grails.plugin.searchable.internal.compass.search;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.compass.core.CompassHits;

/**
 * @author Maurice Nicholson
 */
public abstract class AbstractSearchableHitCollector implements SearchableHitCollector {

    /**
     * Collect hits and return something; maybe a list of results, maybe a single result, ...
     * @param hits compass hits
     * @param options
     * @return the result object
     */
    public Object collect(CompassHits hits, Map options) {
        boolean reload = MapUtils.getBooleanValue(options, "reload");
        return collect(hits, reload, options);
    }

    protected abstract Object collect(CompassHits hits, boolean reload, Map options);

    protected Object getObject(Object data, boolean reload) {
        if (!reload) return data;
        Object id = InvokerHelper.getProperty(data, "id");
        return InvokerHelper.invokeStaticMethod(data.getClass(), "get", new Object[] {id});
    }
}
