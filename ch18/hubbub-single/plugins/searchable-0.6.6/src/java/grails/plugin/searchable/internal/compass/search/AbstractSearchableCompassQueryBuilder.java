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

import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;

/**
 * @author Maurice Nicholson
 */
public abstract class AbstractSearchableCompassQueryBuilder implements SearchableCompassQueryBuilder {
    private SearchableCompassQueryBuilderOptionsHelper[] optionHelpers = new SearchableCompassQueryBuilderOptionsHelper[] {
            new SearchableCompassQueryBuilderClassOptionHelper(),
            new SearchableCompassQueryBuilderSortOptionHelper(),
            new SearchableCompassQueryBuilderFilterOptionHelper()
        };
    private Compass compass;

    public AbstractSearchableCompassQueryBuilder(Compass compass) {
        this.compass = compass;
    }

    public Compass getCompass() {
        return compass;
    }

    public void setCompass(Compass compass) {
        this.compass = compass;
    }

    protected CompassQuery applyOptions(GrailsApplication grailsApplication, Compass compass, CompassSession compassSession, CompassQuery compassQuery, Map options) {
        for (int i = 0, max = optionHelpers.length; i < max; i++) {
            compassQuery = optionHelpers[i].applyOptions(grailsApplication, compass, compassSession, compassQuery, options);
        }
        return compassQuery;
    }
}
