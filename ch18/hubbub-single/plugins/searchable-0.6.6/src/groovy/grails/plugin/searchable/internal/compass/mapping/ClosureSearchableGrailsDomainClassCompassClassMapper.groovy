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

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.codehaus.groovy.grails.commons.GrailsDomainClassProperty
import org.compass.core.config.CompassConfiguration
import org.compass.core.converter.Converter
import org.compass.core.engine.subindex.SubIndexHash
import org.springframework.beans.BeanWrapperImpl

/**
 * @author Maurice Nicholson
 */
class ClosureSearchableGrailsDomainClassCompassClassMapper extends AbstractSearchableGrailsDomainClassCompassClassMapper {
    static final Log log = LogFactory.getLog(ClosureSearchableGrailsDomainClassCompassClassMapper)
    static final SEARCHABLE_ID_OPTIONS = ['accessor', 'converter', 'name']
    static final SEARCHABLE_PROPERTY_OPTIONS = ['accessor', 'analyzer', 'boost', 'converter', 'excludeFromAll', 'format', 'index', 'managedId', 'managedIdIndex', 'name', 'nullValue', 'propertyConverter', 'reverse', 'store', 'spellCheck', 'termVector']
    static final SEARCHABLE_CONSTANT_OPTIONS = ['analyzer', 'boost', 'excludeFromAll', 'index', 'spellCheck', 'store', 'termVector', 'converter']
    static final SEARCHABLE_REFERENCE_OPTIONS = ['accessor', 'cascade', 'converter', 'lazy', 'refAlias', 'refComponentAlias']
    static final SEARCHABLE_COMPONENT_OPTIONS = ['accessor', 'cascade', 'converter', 'maxDepth', 'override', 'prefix', 'refAlias']
    static final SEARCHABLE_REFERENCE_MAPPING_OPTIONS = SEARCHABLE_REFERENCE_OPTIONS + ["reference", "component"]
    static final SEARCHABLE_COMPONENT_MAPPING_OPTIONS = SEARCHABLE_COMPONENT_OPTIONS + ["reference", "component"]

    static final SEARCHABLE_ID_OPTION_ALIASES = [:]
    static final SEARCHABLE_PROPERTY_OPTION_ALIASES = [propertyConverter: 'converter']

    static final CLASS_MAPPING_OPTIONS = ['all', 'allName', 'allAnalyzer', 'allTermVector', 'alias', 'analyzer', 'boost', 'converter', 'enableAll', 'managedId', 'root', 'spellCheck', 'subIndex', 'supportUnmarshall']

    static final COMMON_DEPRECATED_OPTION_VALUES = [index: [
        un_tokenized: 'not_analyzed', tokenized: 'analyzed'
    ]]

    GrailsDomainClass grailsDomainClass
    Class mappedClass
    GrailsDomainClassProperty[] mappableProperties
    Collection searchableGrailsDomainClasses
    Object only
    Object except
    List mappedProperties
    Map classMappingOptions
    List constantMetadatas
    boolean hasMappingOption
    Map subIndexHash

    /**
     * No special initialisation required.
     */
    SearchableGrailsDomainClassCompassClassMapper init(
            CompassConfiguration configuration,
            Map<String, Converter> customConverters,
            List defaultExcludedProperties, Map defaultFormats) {
        return this
    }

    /**
     * Get the property mappings for the given GrailsDomainClass
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @param searchableValue the searchable value: true|false|Map|Closure
     * @param excludedProperties a List of properties NOT to map; may be ignored by impl
     * @return a List of CompassClassPropertyMapping
     */
    List getCompassClassPropertyMappings(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Object searchableValue, List excludedProperties) {
        assert searchableValue instanceof Closure
        searchableGetCompassClassPropertyMappings(grailsDomainClass, searchableGrailsDomainClasses, (Closure) searchableValue, excludedProperties, null)
        return mappedProperties
    }

    private List searchableGetCompassClassPropertyMappings(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Closure closure, List excludedProperties, List inheritedPropertyMappings) {
        internalInit(grailsDomainClass, searchableGrailsDomainClasses)

        // Build user-defined specific mappings
        closure = closure.clone()
        closure.delegate = this
        closure.call()

        // Merge inherited parent mappings?
        if (inheritedPropertyMappings) {
            SearchableGrailsDomainClassCompassMappingUtils.mergePropertyMappings(mappedProperties, inheritedPropertyMappings)
        }

        // Default any remaining mappable properties
        if (only && except) throw new IllegalArgumentException("Both 'only' and 'except' were used in '${mappedClass.getName()}#searchable': provide one or neither but not both")
        def mapValue = only ? [only: only] : except ? [except: except] : true
        mappableProperties = SearchableGrailsDomainClassCompassMappingUtils.getMappableProperties(grailsDomainClass, mapValue, searchableGrailsDomainClasses, excludedProperties, getDomainClassPropertyMappingStrategyFactory())

        for (property in mappableProperties) {
            if (!mappedProperties.any { it.propertyName == property.name }) {
                mappedProperties << super.getDefaultPropertyMapping(property, searchableGrailsDomainClasses)
            }
        }
        return mappedProperties
    }

    /**
     * Get the CompassMappingDescription  for the given GrailsDomainClass and "searchable" value
     * @param grailsDomainClass the Grails domain class
     * @param searchableGrailsDomainClasses a collection of searchable GrailsDomainClass instances
     * @param searchableValue the searchable value: Closure in this case
     * @param excludedProperties a List of excluded properties NOT to map; ignored in this impl
     * @return the CompassMappingDescription
     */
    CompassClassMapping getCompassClassMapping(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses, Object closure, List excludedProperties) {
        // Get inherited parent mappings
        List parentMappedProperties = getInheritedPropertyMappings(grailsDomainClass, searchableGrailsDomainClasses, excludedProperties)
        // Get property mapping for this class
        mappedProperties = searchableGetCompassClassPropertyMappings(grailsDomainClass, searchableGrailsDomainClasses, (Closure) closure, excludedProperties, parentMappedProperties)
        def classMapping = SearchableGrailsDomainClassCompassMappingUtils.buildCompassClassMapping(grailsDomainClass, searchableGrailsDomainClasses, mappedProperties)
        def bw = new BeanWrapperImpl(classMapping)
        bw.setPropertyValues(classMappingOptions)
        classMapping.setSubIndexHash(subIndexHash)
        classMapping.getConstantMetaData().addAll(constantMetadatas)
        return classMapping
    }

    Object invokeMethod(String name, Object args) {
        // special cases
        if (!hasMappingOption) {
            if ("all".equals(name)) {
                if (args.size() != 1) {
                    throw new IllegalArgumentException("'${mappedClass.getName()}.${property.name}' declares '${name}': there should be just one argument following '${name}' (the value), but the arguments were ${args}")
                }
                setAllOptions(args[0])
                return
            }
            if (CLASS_MAPPING_OPTIONS.contains(name)) {
                if (args.size() != 1) {
                    throw new IllegalArgumentException("'${mappedClass.getName()}.${property.name}' declares '${name}': there should be just one argument following '${name}' (the value), but the arguments were ${args}")
                }
                classMappingOptions[name] = args[0]
                return
            }
            if ("subIndexHash".equals(name)) {
                subIndexHash = makeSubIndexHash(args)
                return
            }
        }
        if ("constant".equals(name) && !hasMappingOption) {
            if (args.size() != 1) {
                throw new IllegalArgumentException("'${mappedClass.getName()}.${property.name}' declares an '${name}': there should be just one argument following '${name}', but the arguments were ${args}")
            }
            def cmd = args[0]
            // TODO old code alert - can probably be removed for 0.6
            if (!cmd.name || !(cmd.value || cmd.values)) {
                String message = "[${mappedClass.name}#searchable] defines a constant but this feature has been reimplented! "+
                    "You probably need to change the syntax from \"foo: 'bar'\" to \"name: 'foo', value: 'bar'\". " +
                    "Ignoring this constant for now since it doesn't provide 'name' + 'value(s)' attributes. " +
                    "See http://grails.org/Searchable+Plugin+-+Mapping+-+constant for details"
                LOG.warn(message)
                return
            }
            def values = cmd.value ? cmd.value : cmd.values
            if (!(values instanceof List)) {
                values = [values]
            }
            def attributes = new HashMap(cmd)
            attributes.remove("name")
            attributes.remove("values")
            attributes.remove("value")
            validateOptions(name, "Searchable Constant", SEARCHABLE_CONSTANT_OPTIONS, attributes)
            constantMetadatas << [name: cmd.name, attributes: attributes, values: values]
            return
        }
        if ("mapping".equals(name)) {
            def options = args.find { it instanceof Map }
            if (options) {
                if (options.subIndexHash) {
                    subIndexHash = makeSubIndexHash(options.remove("subIndexHash"))
                }
                if (options.all) {
                    setAllOptions(options.remove("all"))
                }
                classMappingOptions.putAll(options)
            }
            def closure = args.find { it instanceof Closure }
            if (closure) {
                closure = closure.clone()
                closure.setDelegate(this)
                closure.call()
            }
            hasMappingOption = true
            return
        }

        def property = grailsDomainClass.getProperties().find { it.name == name }
        if (!property) {
            throw new IllegalArgumentException(
                "Unable to find property [${name}] used in [${mappedClass.getName()}#searchable]. " +
                "The names of method invocations in the closure must match class properties (like GORM's 'constraints' closure)"
            )
        }

        if (!mappableProperties.any { it == property }) {
            throw new IllegalArgumentException(
                "Unable to map [${mappedClass.getName()}.${property.name}]. " +
                "It does not appear to a suitable 'searchable property' (normally simple types like Strings, Dates, Numbers, etc), " +
                "'searchable reference' (normally another domain class) or " +
                "'searchable component' (normally another domain class defined as a component, using the 'embedded' declaration). " +
                "Is it a derived property (a getter method with no equivalent field) defined with 'def'? Try defining it with a more specific return type"
            )
        }

        // Get default mapping
        def defaultMapping = super.getDefaultPropertyMapping(property, searchableGrailsDomainClasses)
        assert defaultMapping, "Expected property ${property} to be mappable using default rules"

        // validate args
        args = args ? args[0] : [:]
        if (defaultMapping.property) {
            validateOptions(name, "Searchable Property", SEARCHABLE_PROPERTY_OPTIONS, args, false)
            SEARCHABLE_PROPERTY_OPTION_ALIASES.each { k, v ->
                if (args.containsKey(k)) {
                    args[v] = args[k]
                    args.remove(k)
                }
            }
            mappedProperties << CompassClassPropertyMapping.getPropertyInstance(name, args)
            return
        } else if (defaultMapping.id) {
            validateOptions(name, "Searchable Id", SEARCHABLE_ID_OPTIONS, args, false)
            SEARCHABLE_ID_OPTION_ALIASES.each { k, v ->
                if (args.containsKey(k)) {
                    args[v] = args[k]
                    args.remove(k)
                }
            }
            mappedProperties << CompassClassPropertyMapping.getIdInstance(name, args)
            return
        }

        // Arg check
        def defaultTypeReference = defaultMapping.reference
        def validOptions = defaultTypeReference ? SEARCHABLE_REFERENCE_MAPPING_OPTIONS : SEARCHABLE_COMPONENT_MAPPING_OPTIONS
        def invalidOptions = args.keySet() - validOptions
        if (invalidOptions) {
            throw new IllegalArgumentException("One or more invalid options were defined in '${mappedClass.getName()}#searchable' for property '${name}'. " +
                (defaultTypeReference ?
                    "It is mapped as a 'searchable reference' by default so you can either use the options for searchable reference directly " +
                    "- eg, 'user(cascade: \"create,delete\")' - or you can define it as a 'searchable reference' or 'searchable component' (instead) explicity and use their options within nested maps " +
                    " - eg, 'user(reference: [cascade: \"create,delete\"])' or 'user(component: [maxDepth : 3])'. Supported options are [${SEARCHABLE_REFERENCE_MAPPING_OPTIONS.join(', ')}]. " :
                    "It is mapped as a 'searchable component' by default so you can either use the options for searchable component directly " +
                    "- eg, 'user(cascade: \"create,delete\")' - or you can define it as a 'searchable component' or 'searchable reference' (instead) explicity and use their options within nested maps " +
                    " - eg, 'user(reference: [cascade: \"create,delete\"])' or 'user(component: [maxDepth : 3])'. Supported options are [${SEARCHABLE_REFERENCE_MAPPING_OPTIONS.join(', ')}]. ") +
                "The invalid options are: [${invalidOptions.join(', ')}]."
            )
        }

        def referenceOptions
        def componentOptions
        boolean implicitReference = defaultTypeReference
        boolean implicitComponent = !defaultTypeReference
        def component = false
        def reference = false
        if (defaultTypeReference) {
            assert defaultMapping.reference
            if (!args.reference && !args.component) {
                // eg "comments(cascade: true)" - cascade only relationship
                referenceOptions = [*:args]
            }
            if (args.reference) {
                // eg "comments(reference: true)" - explicit reference type or
                // eg "comments(reference: [cascade: 'all'])" - explicit reference options
                referenceOptions = new HashMap(defaultMapping.attributes)
                if (args.reference != true) {
                    referenceOptions.putAll(args.reference)
                }
                implicitReference = false
                reference = true
            }
            if (args.component) {
                // eg "comments(component: true)" - explicit component type or
                // eg "comments(component: [cascade: 'all'])" - explicit component options
                componentOptions = new HashMap(defaultMapping.attributes)
                if (args.component != true) {
                    componentOptions.putAll(args.component)
                }
                component = true
            }
        } else {
            reference = false
            component = false
            implicitComponent = true
            implicitReference = false
            assert defaultMapping.component
            if (!args.reference && !args.component) {
                // eg "comments(cascade: true)" - assumed to be component
                componentOptions = new HashMap(defaultMapping.attributes)
                componentOptions.putAll(args)
                component = true
            }
            if (args.reference != null) {
                if (args.reference != false) {
                    reference = true
                    referenceOptions = new HashMap(defaultMapping.attributes)
                    if (args.reference instanceof Map) {
                        referenceOptions.putAll(args.reference)
                    }
                }
            }
            if (args.component != null) {
                if (args.component != false) {
                    component = true
                    componentOptions = new HashMap(defaultMapping.attributes)
                    if (args.component instanceof Map) {
                        componentOptions.putAll(args.component)
                    }
                }
            }
        }

        // Check for invalid options
        if (component && reference) {
            throw new IllegalArgumentException("'${mappedClass.getName()}#searchable' declares property '${name}' as both reference and component but this is not supported; it must be one or the other")
        }

        if (component) {
            validateOptions(name, "Searchable Component", SEARCHABLE_COMPONENT_OPTIONS, componentOptions, implicitComponent)
            mappedProperties << CompassClassPropertyMapping.getComponentInstance(name, defaultMapping.propertyType, componentOptions)
        }
        else if (reference) {
            validateOptions(name, "Searchable Reference", SEARCHABLE_REFERENCE_OPTIONS, referenceOptions, implicitReference)
            mappedProperties << CompassClassPropertyMapping.getReferenceInstance(name, defaultMapping.propertyType, referenceOptions)
        }
        else if (referenceOptions.cascade) {
            mappedProperties << CompassClassPropertyMapping.getCascadeInstance(name, defaultMapping.propertyType, referenceOptions.cascade)
        }
    }

    /**
     * Validate the options for a searchable-xxx mapping type
     */
    def validateOptions(String propertyName, String type, Collection validOptionNames, Map options, boolean implicit = false) {
        // check option names
        def invalidOptions = options.keySet() - validOptionNames
        if (invalidOptions) {
            throw new IllegalArgumentException(
                "One or more invalid options were defined in '${mappedClass.getName()}#searchable' for property '${propertyName}'. " +
                "'${mappedClass.getName()}.${propertyName}' is ${implicit ? 'implicitly' : ''} a [${type}], meaning you can only define the options allowed " +
                "for searchable references. The invalid options are: [${invalidOptions.join(', ')}]. " +
                "Supported options for [${type}] are [${validOptionNames.join(', ')}]"
            )
        }

        // warn about deprecated option values
        for (optionName in COMMON_DEPRECATED_OPTION_VALUES.keySet()) {
            if (options.containsKey(optionName)) {
                def deprecation = COMMON_DEPRECATED_OPTION_VALUES[optionName]
                Object optionValue = options[optionName]
                if (deprecation.containsKey(optionValue)) {
                    String message = "The Searchable Plugin Mapping option [${optionName}] used in " +
                        "[${mappedClass.getName()}#searchable] for property [${propertyName}] has a deprecated value [${optionValue}]. " +
                        "Please use [${deprecation[optionValue]}] instead."
                    log.warn(message)
                }
            }
        }
    }

    def makeSubIndexHash(args) {
        def subIndexHash = [:]
        def options = args instanceof Map ? args : args.find { it instanceof Map }
        def type = options?.type ? options.type : args.find { it instanceof Class }
        if (!SubIndexHash.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Missing or invalid SubIndexHash class supplied for 'subIndexHash' in '${mappedClass.getName()}#searchable'. It should implement org.compass.core.engine.subindex.SubIndexHash")
        }
        subIndexHash.type = type
        if (options?.settings) {
            subIndexHash.settings = new HashMap(options.settings)
        }
        return subIndexHash
    }

    def setAllOptions(arg) {
        if (arg instanceof Boolean) {
            classMappingOptions.enableAll = arg
            return
        }
        if (!(arg instanceof Map)) {
            throw new IllegalArgumentException("The argument [${arg}] provided to the 'all' mapping option is invalid. It is a ${arg?.getClass()} but should be either Boolean or Map")
        }
//        classMappingOptions.enableAll = true
        if (arg.name) {
            classMappingOptions.allName = arg.name
        }
        if (arg.analyzer) {
            classMappingOptions.allAnalyzer = arg.analyzer
        }
        if (arg.termVector) {
            classMappingOptions.allTermVector = arg.termVector
        }
    }

    /**
     * Does the implementation handle th given "searchable" value type?
     * @param searchableValue a searchable value
     * @return true for Closure for this class
     */
    boolean handlesSearchableValue(Object searchableValue) {
        searchableValue instanceof Closure
    }

    /**
     * Init
     */
    private internalInit(GrailsDomainClass grailsDomainClass, Collection searchableGrailsDomainClasses) {
        this.grailsDomainClass = grailsDomainClass
        mappedClass = grailsDomainClass.clazz
        mappableProperties = SearchableGrailsDomainClassCompassMappingUtils.getMappableProperties(grailsDomainClass, true, searchableGrailsDomainClasses, excludedProperties, getDomainClassPropertyMappingStrategyFactory())
        this.searchableGrailsDomainClasses = searchableGrailsDomainClasses
        only = null
        except = null
        mappedProperties = []
        subIndexHash
        classMappingOptions = [:]
        constantMetadatas = []
        hasMappingOption = false
    }
}
