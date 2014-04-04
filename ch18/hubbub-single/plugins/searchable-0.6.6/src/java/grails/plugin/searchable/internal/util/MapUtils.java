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
package grails.plugin.searchable.internal.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Maurice Nicholson
 */
public class MapUtils {

    /**
     * Safely (without NullPointerException) add two Maps returning a new Map.
     *
     * Returns a new Map containing the entries from both Maps giving precedence to entries
     * in rhs.
     *
     * In case either but not both Maps are null, a new Map containing the non-null
     * Map's entries is returned.
     *
     * If both are null, returns null
     *
     * @param lhs Map
     * @param rhs Map
     * @return a new Map containing all entries or null
     */
    public static Map nullSafeAddMaps(Map lhs, Map rhs) {
        if (lhs == null && rhs == null) {
            return null;
        }
        Map result = new HashMap();
        if (lhs != null) {
            result.putAll(lhs);
        }
        if (rhs != null) {
            result.putAll(rhs);
        }
        return result;
    }
}
