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
package grails.plugin.searchable.internal.compass.support;

import grails.plugin.searchable.internal.SearchableMethod;
import grails.plugin.searchable.internal.SearchableMethodFactory;

import java.util.Map;

import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassTemplate;
import org.springframework.util.Assert;

/**
 * @author Maurice Nicholson
 */
public abstract class AbstractSearchableMethod implements SearchableMethod {
    private String methodName;
    private Compass compass;
    private SearchableMethodFactory methodFactory;
    private Map defaultOptions;

    public AbstractSearchableMethod(String methodName, Compass compass, SearchableMethodFactory methodFactory, Map defaultOptions) {
        Assert.notNull(methodName, "methodName cannot be null");
        Assert.notNull(compass, "compass cannot be null");
        this.methodName = methodName;
        this.compass = compass;
        this.methodFactory = methodFactory;
        this.defaultOptions = defaultOptions;
    }

    public Object doCall(Object[] args) {
        return invoke(args);
    }

    // allows groovy code to call instances of this object as a method, ie, method = AbstractSearchableMethod(); method()
    public Object call(Object[] args) {
        return invoke(args);
    }

    protected Object doInCompass(CompassCallback compassCallback) {
        return new CompassTemplate(compass).execute(compassCallback);
    }

    public Map getDefaultOptions() {
        return defaultOptions;
    }

    public void setDefaultOptions(Map defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public Compass getCompass() {
        return compass;
    }

    public String getMethodName() {
        return methodName;
    }

    public SearchableMethodFactory getMethodFactory() {
        return methodFactory;
    }

    public void setMethodFactory(SearchableMethodFactory methodFactory) {
        this.methodFactory = methodFactory;
    }
}
