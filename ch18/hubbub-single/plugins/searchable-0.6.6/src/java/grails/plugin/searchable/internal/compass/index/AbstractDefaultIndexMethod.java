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

import grails.plugin.searchable.internal.compass.support.AbstractSearchableMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.compass.core.Compass;

/**
 * @author Maurice Nicholson
 */
public abstract class AbstractDefaultIndexMethod extends AbstractSearchableMethod {

    public AbstractDefaultIndexMethod(String methodName, Compass compass, Map defaultOptions) {
        super(methodName, compass, null, defaultOptions);
    }

    protected void validateArguments(Object[] args, Class clazz, List ids, List objects, Map options) {
        if (clazz == null && !ids.isEmpty()) {
            throw new IllegalArgumentException(
                "You called " + getMethodName() + "() with object ids, but no Class! " +
                "Either pass the Class as an option or call the class's own static " + getMethodName() + " method"
            );
        }
    }

    protected List getObjects(Object[] args) {
        if (args == null) return Collections.EMPTY_LIST;
        List objects = new ArrayList();
        for (int i = 0, max = args.length; i < max; i++) {
            Object arg = args[i];
            if (arg instanceof Collection) {
                arg = new ArrayList((Collection) arg);
            }
            if (arg instanceof List) {
                List list = (List) arg;
                objects.addAll(getObjects(list.toArray(new Object[list.size()])));
            } else if (arg instanceof Object[]) {
                objects.addAll(getObjects((Object[]) arg));
            } else if (arg != null && !(arg instanceof Long) && !(arg instanceof Map)) {
                objects.add(arg);
            }
        }
        return objects;
    }

    protected List getIds(Object[] args) {
        if (args == null) return Collections.EMPTY_LIST;
        List ids = new ArrayList();
        for (int i = 0, max = args.length; i < max; i++) {
            Object arg = args[i];
            if (arg instanceof Collection) {
                arg = new ArrayList((Collection) arg);
            }
            if (arg instanceof List) {
                List list = (List) arg;
                ids.addAll(getIds(list.toArray(new Object[list.size()])));
            } else if (arg instanceof Object[]) {
                ids.addAll(getIds((Object[]) arg));
            } else if (arg instanceof Long) {
                ids.add(arg);
            }
        }
        return ids;
    }
}
