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
package grails.plugin.searchable.internal.compass.index;

import grails.plugin.searchable.internal.compass.DefaultSearchableMethodFactory;
import grails.plugin.searchable.internal.compass.support.AbstractSearchableMethod;

import java.util.HashMap;
import java.util.Map;

import org.compass.core.Compass;
import org.compass.gps.CompassGps;

/*
   reindexAll()

   service.reindexAll() // all searchable class instances
   service.reindexAll([class: Post]) // all Post instances
   service.reindexAll(1l, 2l, 3l) // ERROR: unknown class
   service.reindexAll(1l, 2l, 3l, [class: Post]) // id'd Post instances
   service.reindexAll(x, y, z) // given instances

   Thing.reindexAll() // all Thing instances
   Thing.reindexAll(1l, 2l, 3l) // id'd Post instances
   Thing.reindexAll(x, y, z) // given instances


*/
/*
    reindex()

    service.reindex() // all searchable class instances - same as service.reindexAll
    service.reindex([class: Post]) // all class instances
    service.reindex(x, y, z) // given object(s)
    service.reindex(1, 2, 3, [class: Post]) // id'd objects

    Thing.reindex() // all Thing instances
    Thing.reindex(1,2,3) // id'd instances
    Thing.reindex(x,y,z) // given instances

    */

/**
 * @author Maurice Nicholson
 */
public class DefaultReindexMethod extends DefaultIndexMethod {
    private AbstractSearchableMethod unindexMethod;

    public DefaultReindexMethod(String methodName, Compass compass, CompassGps compassGps, DefaultSearchableMethodFactory methodFactory, Map defaultOptions) {
        super(methodName, compass, compassGps, defaultOptions);
        unindexMethod = (AbstractSearchableMethod) methodFactory.getMethod("unindex");
        unindexMethod.getDefaultOptions().putAll(getDefaultOptions());
    }

    public DefaultReindexMethod(String methodName, Compass compass, CompassGps compassGps, DefaultSearchableMethodFactory methodFactory) {
        this(methodName, compass, compassGps, methodFactory, new HashMap());
    }

    @Override
    public Object invoke(Object[] args) {
        unindexMethod.invoke(args);
        return super.invoke(args);
    }

    @Override
    public void setDefaultOptions(Map defaultOptions) {
        super.setDefaultOptions(defaultOptions);
        unindexMethod.getDefaultOptions().putAll(getDefaultOptions());
    }
}
