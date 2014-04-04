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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.compass.core.CompassHits;

/**
 * Collects hits within a range denoted by offset and max
 *
 * @author Maurice Nicholson
 */
public class DefaultSearchableSubsetHitCollector extends AbstractSearchableHitCollector {

    /**
     * Collect and return the subset of hits with the range offset..offset + max
     *
     * @param hits
     * @param options
     * @return
     */
    @Override
    public Object collect(CompassHits hits, boolean reload, Map options) {
        if (hits.length() == 0) {
            return Collections.EMPTY_LIST;
        }

        int offset = MapUtils.getIntValue(options, "offset");
        int max = MapUtils.getIntValue(options, "max");
        List collectedHits = new ArrayList(max);
        int low = offset;
        int high = Math.min(low + max, hits.length());
        while (low < high) {
            collectedHits.add(getObject(hits.data(low++), reload));
        }

        return collectedHits;
    }
}
