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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.compass.core.CompassHits;

/**
 * Returns a "paged" search result object for the given hits
 *
 * NOT thread-safe
 *
 * @author Maurice Nicholson  PCR edit
 *
 * added the collected compasshits
 */
public class SearchableSubsetSearchResultFactory implements SearchableSearchResultFactory {

    /*
     * collectedHits are NOT objects of type compassHits, they are the businessobjects that are returned from compassHit.getValue()
     * The collectedCompassHits are the the compassHit equivalent of the collectedHits
     */
    public Object buildSearchResult(final CompassHits hits, final Object collectedHits,final Object collectedCompassHits, Map options) {
        final int offset = MapUtils.getIntValue(options, "offset");
        final int max = MapUtils.getIntValue(options, "max");
        final List scores = new ArrayList(max);
        for (int i = offset; i < Math.min(offset + max, hits.length()); i++) {
            scores.add(i - offset, Float.valueOf(hits.score(i)));
        }

        Map map = new HashMap();
        map.put("offset", offset);
        map.put("max", max);
        map.put("results", collectedHits);

        map.put("scores", scores);
        map.put("total", hits.length());
        //need to return the compass hits also!
        map.put("hits", collectedCompassHits);
        return map;
    }
}
