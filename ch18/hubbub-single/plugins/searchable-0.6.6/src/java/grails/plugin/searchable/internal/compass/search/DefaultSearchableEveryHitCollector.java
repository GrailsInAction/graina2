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
import java.util.List;
import java.util.Map;

import org.compass.core.CompassHits;

/**
 * Collects every hit in the given hits
 *
 * @author Maurice Nicholson
 */
public class DefaultSearchableEveryHitCollector extends AbstractSearchableHitCollector {

    @Override
    public Object collect(CompassHits hits, boolean reload, Map options) {
        List collectedHits = new ArrayList();
        for (int i = 0, max = hits.length(); i < max; i++) {
            collectedHits.add(getObject(hits.data(i), reload));
        }
        return collectedHits;
    }
}
