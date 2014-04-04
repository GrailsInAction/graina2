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
package grails.plugin.searchable.internal;

/**
 * Creates SearchableMethod instances
 *
 * @author Maurice Nicholson
 */
public interface SearchableMethodFactory {

    /**
     * Create and return a method
     * @param clazz
     * @param methodName
     * @return
     */
    SearchableMethod getMethod(Class clazz, String methodName);

    /**
     * Create and return a method
     * @param methodName
     * @return
     */
    SearchableMethod getMethod(String methodName);
}
