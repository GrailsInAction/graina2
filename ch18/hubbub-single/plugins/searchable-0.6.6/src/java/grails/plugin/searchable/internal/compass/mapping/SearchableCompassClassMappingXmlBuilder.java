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
package grails.plugin.searchable.internal.compass.mapping;

import java.io.InputStream;

/**
 * @author Maurice Nicholson
 */
public interface SearchableCompassClassMappingXmlBuilder {

    /**
     * Returns an InputStream for the given mapping classMapping
     *
     * @param classMapping describes the class mapping
     * @return an InputStream for the Compass class mapping XML
     */
    InputStream buildClassMappingXml(CompassClassMapping classMapping);
}
