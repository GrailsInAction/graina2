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

import grails.plugin.searchable.internal.compass.CompassGpsUtils;
import grails.plugin.searchable.internal.compass.support.SearchableMethodUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassSession;
import org.compass.gps.CompassGps;
import org.springframework.util.Assert;

/*
   indexAll()

   service.indexAll() // all searchable class instances
   service.indexAll([class: Post]) // all Post instances
   service.indexAll(1l, 2l, 3l) // ERROR: unknown class
   service.indexAll(1l, 2l, 3l, [class: Post]) // id'd Post instances
   service.indexAll(x, y, z) // given instances

   Thing.indexAll() // all Thing instances
   Thing.indexAll(1l, 2l, 3l) // id'd Thing instances
   Thing.indexAll(x, y, z) // given instances


*/
/*
    index()

    Same as indexAll

    service.index() // all searchable class instances
    service.index([class: Post]) // all Post instances (same as service.indexAll(class: Thing))
    service.index(x, y, z) // given object(s)
    service.index(1, 2, 3, [class: Post]) // id'd objects

    Thing.index() // all thing instances (same as Thing.indexAll())
    Thing.index(1,2,3) // id'd instances
    Thing.index(x,y,z) // given instances

    */

/**
 * @author Maurice Nicholson
 */
public class DefaultIndexMethod extends AbstractDefaultIndexMethod {

    private CompassGps compassGps;

    public DefaultIndexMethod(String methodName, Compass compass, CompassGps compassGps, Map defaultOptions) {
        super(methodName, compass, defaultOptions);
        this.compassGps = compassGps;
    }

    public DefaultIndexMethod(String methodName, Compass compass, CompassGps compassGps) {
        this(methodName, compass, compassGps, new HashMap());
    }

    public Object invoke(Object[] args) {
        Map options = SearchableMethodUtils.getOptionsArgument(args, getDefaultOptions());
        final Class clazz = (Class) (options.containsKey("match") ? options.remove("match") : options.remove("class"));
        final List ids = getIds(args);
        final List objects = getObjects(args);

        validateArguments(args, clazz, ids, objects, options);

        if (args.length == 0 || (args.length == 1 && args[0] instanceof Map && clazz != null)) {
            CompassGpsUtils.index(compassGps, clazz);
            return null;
        }

        return doInCompass(new CompassCallback() {
            public Object doInCompass(CompassSession session) throws CompassException {
                List objectsToSave = objects;
                if (clazz != null && !ids.isEmpty()) {
                    Assert.isTrue(objects.isEmpty(), "Either provide ids or objects, not both");
                    objectsToSave = (List) InvokerHelper.invokeStaticMethod(clazz, "getAll", ids);
                }
                Assert.notEmpty(objectsToSave);
                for (Iterator iter = objectsToSave.iterator(); iter.hasNext(); ) {
                    Object o = iter.next();
                    if (o != null) {
                        session.save(o);
                    }
                }
                return ids.isEmpty() ? (objectsToSave.size() == 1 ? objectsToSave.get(0) : objectsToSave)
                                     : (ids.size() == 1 ? objectsToSave.get(0) : objectsToSave);
            }
        });
    }

    public CompassGps getCompassGps() {
        return compassGps;
    }

    public void setCompassGps(CompassGps compassGps) {
        this.compassGps = compassGps;
    }
}
