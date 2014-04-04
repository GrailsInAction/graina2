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
package grails.plugin.searchable.internal.compass.mapping

import groovy.xml.MarkupBuilder

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * Builds the Compass class mapping XML. Done with Groovy cos it's so easy compared with Java
 *
 * @author Maurice Nicholson
 */
class DefaultSearchableCompassClassMappingXmlBuilder implements SearchableCompassClassMappingXmlBuilder {
    private static final Log LOG = LogFactory.getLog(DefaultSearchableCompassClassMappingXmlBuilder)

    /** Legal attribute names for known XML elements */
    static final ID_ATTR_NAMES = ['accessor', 'name']
    static final PROPERTY_ATTR_NAMES = ['accessor', 'analyzer', 'boost', 'class', 'converter', 'exclude-from-all', 'managed-id', 'managed-id-index', 'managed-id-converter', 'name', 'override']
    static final META_DATA_ATTR_NAMES = ['analyzer', 'boost', 'converter', 'exclude-from-all', 'format', 'index', 'null-value', 'reverse', 'store', 'spell-check', 'term-vector']
    static final REFERENCE_ATTR_NAMES = ['accessor', 'cascade', 'converter', 'lazy', 'name', 'ref-alias', 'ref-comp-alias']
    static final COMPONENT_ATTR_NAMES = ['accessor', 'cascade', 'converter', 'max-depth', 'name', 'override', 'prefix', 'ref-alias']

    /** Mapping from input option names to output XML attribute names */
    static final OPTION_ATTR_MAP = [type: 'class', propertyConverter: 'converter', refComponentAlias: 'ref-comp-alias']

    /** Mapping from class mapping option to XML attribute name */
    static final CLASS_MAPPING_ATTR_MAP = [
            alias: 'alias', subIndex: 'sub-index', analyzer: 'analyzer', root: 'root', poly: 'poly', extend: 'extends',
            spellCheck: 'spell-check', supportUnmarshall: 'support-unmarshall', boost: 'boost', converter: 'converter'
    ]
    /** Mapping for all property -> attribute names */
    static final ALL_PROPERTY_ATTR_MAP = [
        enableAll: 'enable', allExcludeAlias: 'exclude-alias', allName: 'name', allTermVector: 'term-vector',
        allOmitNorms: 'omit-norms', allSpellCheck: 'spell-check'
    ]

    /**
     * Returns an InputStream for the given mapping description
     *
     * @param description describes the class mapping
     * @return an InputStream for the Compass class mapping XML
     */
    InputStream buildClassMappingXml(CompassClassMapping description) {
        def writer = new StringWriter()
        def mkp = new MarkupBuilder(writer)

        def className = description.mappedClass.name
        LOG.debug("Building Compass mapping XML for [${className}] from description [${description}]")
        def self = this
        mkp."compass-core-mapping" {
            def classAttrs = [name: className, alias: description.alias, root: description.root]
            CLASS_MAPPING_ATTR_MAP.each { propertyName, attrName ->
                if (description[propertyName] != null) {
                    classAttrs[attrName] = description[propertyName]
                }
            }
            "class"(classAttrs) {
                def allAttrs = [:]
                for (key in ALL_PROPERTY_ATTR_MAP.keySet()) {
                    Map properties = description.properties
                    def value = properties[key]
                    if (value != null) {
                        allAttrs[ALL_PROPERTY_ATTR_MAP[key]] = value
                    }
                }
                if (!allAttrs.isEmpty()) {
                    all(allAttrs)
                }

                if (description.subIndexHash) {
                    "sub-index-hash"(type: description.subIndexHash.type.getName()) {
                        if (description.subIndexHash.settings) {
                            for (name in description.subIndexHash.settings.keySet()) {
                                "setting"(name: name, value: description.subIndexHash.settings[name])
                            }
                        }
                    }
                }

                def idPropertyMappings = description.propertyMappings.findAll { it.id }
                if (!idPropertyMappings) {
                    throw new IllegalArgumentException("No searchable id mapping(s) found - there must be (at least) one")
                }
                for (idPropertyMapping in idPropertyMappings) {
                    def idAttrs = [:]
                    def idMetaDataAttrs = [:]
                    def idTmp = new HashMap(self.transformAttrNames(idPropertyMapping.attributes))
                    def idName = idTmp.remove("name")
                    validateAttributes("id", idTmp, ID_ATTR_NAMES + META_DATA_ATTR_NAMES)
                    idTmp.each { k, v ->
                        if (META_DATA_ATTR_NAMES.contains(k)) {
                            idMetaDataAttrs[k] = v
                        } else {
                            assert ID_ATTR_NAMES.contains(k)
                            idAttrs[k] = v
                        }
                    }
                    idAttrs.name = idPropertyMapping.propertyName
                    id(idAttrs) {
                        if (idMetaDataAttrs.size() || idName) {
                            "meta-data"(idMetaDataAttrs, idName ? idName : idPropertyMapping.propertyName)
                        }
                    }
                }

                for (constantMetaData in description.constantMetaData) {
                    def metaData = new HashMap(constantMetaData) // clone to avoid corruption
                    def name = metaData.name
                    def attributes = self.transformAttrNames(metaData.attributes)
                    validateAttributes("meta-data", attributes, META_DATA_ATTR_NAMES)
                    constant {
                        'meta-data'(attributes, name)
                        for (value in metaData.values) {
                            'meta-data-value'(value)
                        }
                    }
                }

                def donePropertyMappings = [] + idPropertyMappings
                for (propertyMapping in description.propertyMappings) {
                    if (donePropertyMappings.contains(propertyMapping)) {
                        continue
                    }

                    def propertyName = propertyMapping.propertyName

                    def attrs = [name: propertyName]
                    if (propertyMapping.reference) {
                        def refAttrs = new HashMap(attrs)
                        def attributes = propertyMapping.attributes
                        LOG.debug("Mapping Searchable Reference '${className}.${propertyName}' with attributes ${attributes}")
                        refAttrs.putAll(self.transformAttrNames(attributes))
                        validateAttributes("reference", refAttrs, REFERENCE_ATTR_NAMES)
                        reference(refAttrs)
                    }
                    if (propertyMapping.component) {
                        def compAttrs = new HashMap(attrs)
                        def attributes = propertyMapping.attributes
                        LOG.debug("Mapping Searchable Component '${className}.${propertyName}' with attributes ${attributes}")
                        compAttrs.putAll(self.transformAttrNames(attributes))
                        validateAttributes("component", compAttrs, COMPONENT_ATTR_NAMES)
                        component(compAttrs)
                    }
                    if (propertyMapping.cascade) {
                        def attributes = propertyMapping.attributes
                        LOG.debug("Mapping cascade-only relationship '${className}.${propertyName}' with attributes ${attributes}")
                        def cascadeAttrs = attrs + self.transformAttrNames(attributes)
                        cascade(cascadeAttrs)
                    }
                    if (propertyMapping.property) {
                        def propertyMappings = description.propertyMappings.findAll { it.propertyName == propertyName }
                        donePropertyMappings.addAll(propertyMappings)
                        def propAttrs = new HashMap()
                        def metaDatas = [:]
                        for (mapping in propertyMappings) {
                            def metaDataAttrs = [:]
                            def attributes = mapping.attributes
                            def tmp = self.transformAttrNames(attributes)
                            def name = tmp.remove("name")
                            validateAttributes("property", tmp, PROPERTY_ATTR_NAMES + META_DATA_ATTR_NAMES)
                            tmp.each { k, v ->
                                if (META_DATA_ATTR_NAMES.contains(k)) {
                                    metaDataAttrs[k] = v
                                } else if (PROPERTY_ATTR_NAMES.contains(k)) {
                                    propAttrs[k] = v
                                }
                            }
                            metaDatas[name ?: propertyName] = metaDataAttrs
                        }
                        propAttrs.putAll(attrs)
                        LOG.debug("Mapping Searchable Property '${className}.${propertyName}' with property attributes ${propAttrs} and meta-data ${metaDatas}")
                        property(propAttrs) {
                            metaDatas.each { metaDataName, metaDataAttrs ->
                                "meta-data"(metaDataAttrs, metaDataName)
                            }
                        }
                    }
                }
           }
       }

       String xml = """<?xml version="1.0"?>
<!DOCTYPE compass-core-mapping PUBLIC
    "-//Compass/Compass Core Mapping DTD 2.0//EN"
    "http://www.compass-project.org/dtd/compass-core-mapping-2.2.dtd">
""" + writer

       LOG.debug("${className} xml [${xml}]")
       return new ByteArrayInputStream(xml.getBytes())
    }

    private validateAttributes(elementName, attributeMap, validAttrNames) {
        def invalidAttrs = attributeMap.keySet() - validAttrNames
        if (invalidAttrs) {
            throw new IllegalArgumentException("Invalid attribute(s) for $elementName element: ${invalidAttrs}. Valid attributes are ${validAttrNames.unique().sort()}. Given attributes are ${attributeMap}")
        }
    }

    private transformAttrNames(value) {
        if (value == true) {
            return [:]
        }
        assert value instanceof Map, "attrs should be value of Map"
        def attrs = [:]
        value.each { k, v ->
            if (OPTION_ATTR_MAP[k]) {
                k = OPTION_ATTR_MAP[k]
            } else {
                k = convertCamelCaseToLowerCaseDashed(k)
            }
            attrs[k] = v
        }
        return attrs
    }

    // TODO extract to utils class if needed elsewhere
    String convertCamelCaseToLowerCaseDashed(String string) {
        def buf = new StringBuilder()
        for (i in 0..<string.size()) {
            def ch = string[i]
            if (Character.isUpperCase(ch as char)) {
                if (i != 0) {
                    buf.append("-")
                }
                ch = ch.toLowerCase()
            }
            buf.append(ch)
        }
        return buf.toString()
    }
}
