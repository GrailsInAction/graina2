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

/**
 * Utilities for dealing with time
 *
 * @author Maurice Nicholson
 */
public class TimeUtils {
    private static final long MILLIS_PER_SECOND = 1000;
    private static final long MILLIS_PER_MINUTE = MILLIS_PER_SECOND * 60;
    private static final long MILLIS_PER_HOUR = MILLIS_PER_MINUTE * 60;
    private static final long MILLIS_PER_DAY = MILLIS_PER_HOUR * 24;

    /**
     * Format the given milliseconds as a human readable time period
     * @param millis milliseconds
     * @return a formatted time period
     */
    public static String formatMillisAsShortHumanReadablePeriod(long millis) {
        return formatMillisAsShortHumanReadablePeriod(millis, false);
    }

    /**
     * Format the given milliseconds as a human readable time period
     * @param millis milliseconds
     * @param full if true include the smaller units in all output
     * @return a formatted time period
     */
    public static String formatMillisAsShortHumanReadablePeriod(long millis, boolean full) {
        StringBuilder buf = new StringBuilder();
        // days
        long days = (millis / MILLIS_PER_DAY);
        if (days > 0) {
            buf.append(days).append(" days");
            millis -= days * MILLIS_PER_DAY;
        }
        long hours = (millis / MILLIS_PER_HOUR);
        if ((full && days > 0) || hours > 0) {
            appendWithLeadingCommaIfNescessary(buf, hours).append(" hrs");
            millis -= hours * MILLIS_PER_HOUR;
        }
        long minutes = (millis / MILLIS_PER_MINUTE);
        if ((full && (days > 0 || hours > 0)) || minutes > 0) {
            appendWithLeadingCommaIfNescessary(buf, minutes).append(" mins");
            millis -= minutes * MILLIS_PER_MINUTE;
        }
        long seconds = (millis / MILLIS_PER_SECOND);
        if (full || !(days > 0)) {
            if ((full && (minutes > 0 || hours > 0 || days > 0)) || seconds > 0) {
                appendWithLeadingCommaIfNescessary(buf, seconds).append(" secs");
                millis -= seconds * MILLIS_PER_SECOND;
            }
        }
        if (full || !(minutes > 0 || hours > 0 || days > 0)) {
            if ((full && (seconds > 0 || minutes > 0 || hours > 0 || days > 0)) || millis > 0) {
                appendWithLeadingCommaIfNescessary(buf, millis).append(" ms");
            }
        }
        return buf.toString();
    }

    private static StringBuilder appendWithLeadingCommaIfNescessary(StringBuilder buf, long arg) {
        if (buf.length() > 0) {
            buf.append(", ");
        }
        return buf.append(arg);
    }
}
