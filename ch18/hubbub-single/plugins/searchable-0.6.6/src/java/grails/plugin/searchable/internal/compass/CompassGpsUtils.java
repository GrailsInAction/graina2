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
package grails.plugin.searchable.internal.compass;

import grails.plugin.searchable.internal.util.TimeUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.compass.gps.CompassGps;
import org.springframework.util.Assert;

/**
 * @author Maurice Nicholson
 */
public class CompassGpsUtils {
    private static final Log LOG = LogFactory.getLog(CompassGpsUtils.class);

    /**
     * Calls CompassGps's index method, starting and stopping it if required
     * @param compassGps aCompassGps instance, cannot be null
     * @param clazz the Class to index instances of, may be null
     */
    public static void index(CompassGps compassGps, Class clazz) {
        Assert.notNull(compassGps, "compassGps cannot be null");

        long start = System.currentTimeMillis();
        LOG.info("Starting Searchable Plugin bulk index");
        boolean gpsRunning = compassGps.isRunning();
        try {
            if (!gpsRunning) {
                compassGps.start();
            }
            if (clazz != null) {
                compassGps.index(new Class[] {clazz});
            } else {
                compassGps.index();
            }
        } finally {
            if (!gpsRunning) {
                compassGps.stop();
            }
        }
        LOG.info("Finished Searchable Plugin bulk index, " + TimeUtils.formatMillisAsShortHumanReadablePeriod(System.currentTimeMillis() - start));
    }
}
