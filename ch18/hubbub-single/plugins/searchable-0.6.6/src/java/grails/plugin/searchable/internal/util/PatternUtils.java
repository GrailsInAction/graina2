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

import java.util.regex.Pattern;

/**
 * @author Maurice Nicholson
 */
public class PatternUtils {

    /**
     * Make a Pattern from the given string
     * @param string a String containing wildcards (* and/or ?)
     * @return a Pattern
     */
    public static Pattern makePatternFromWilcardString(String string) {
        String str = makePatternStringFromWildcardString(string);
        return Pattern.compile(str);
    }

    public static String makePatternStringFromWildcardString(String string) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0, count = string.length(); i < count; i++) {
            char c = string.charAt(i);
            switch (c) {
                case '?':
                    buf.append(".");
                    break;
                case '*':
                    buf.append(".*");
                    break;
                default:
                    buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Does the given String contain wildcards (* or ?)
     * @param string a String
     * @return true if it contains wildcards
     */
    public static boolean hasWildcards(String string) {
        return string.indexOf('*') > -1 || string.indexOf('?') > -1;
    }
}
