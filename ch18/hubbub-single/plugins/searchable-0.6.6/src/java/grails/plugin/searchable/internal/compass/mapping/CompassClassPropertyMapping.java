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

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

/**
 * Describes a searchable class property mapping in Compass
 *
 * @author Maurice Nicholson
 */
public class CompassClassPropertyMapping {
    public static final String ID = "id";
    public static final String PROPERTY = "property";
    public static final String REFERENCE = "reference";
    public static final String COMPONENT = "component";
    public static final String CASCADE = "cascade";

    /**
     * The type of this mapping
     */
    private String type;

    /**
     * The property mapping attributes
     */
    private Map attributes = new HashMap();

    /**
     * The name of the class property
     */
    private String propertyName;

    /**
     * The user defined class type of the property
     */
    private Class propertyType;

    /**
     * No arg constructor
     */
    public CompassClassPropertyMapping() {
        super();
    }

    /**
     * Constructor taking type constant
     * @param type REFERENCE, COMPONENT or PROPERTY
     * @param propertyName the property name
     */
    protected CompassClassPropertyMapping(String type, String propertyName) {
        Assert.notNull(type, "type cannot be null");
        Assert.notNull(propertyName, "propertyName cannot be null");
        this.type = type;
        this.propertyName = propertyName;
    }

    protected CompassClassPropertyMapping(String type, String propertyName, Class propertyType, Map attributes) {
        this(type, propertyName, propertyType);
        Assert.notNull(attributes, "attributes cannot be null");
        this.attributes = attributes;
    }

    protected CompassClassPropertyMapping(String type, String propertyName, Class propertyType) {
        this(type, propertyName);
        Assert.notNull(propertyType, "propertyType cannot be null");
        this.propertyType = propertyType;
    }

    protected CompassClassPropertyMapping(String type, String propertyName, Map attributes) {
        this(type, propertyName);
        Assert.notNull(attributes, "attributes cannot be null");
        this.attributes = attributes;
    }

    /**
     * Factory-style constructor for type-safe id type
     * @param propertyName the name of the mapped id
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getIdInstance(String propertyName) {
        return new CompassClassPropertyMapping(ID, propertyName);
    }

    /**
     * Factory-style constructor for type-safe id type
     * @param propertyName the name of the mapped id
     * @param attributes mapping attributes
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getIdInstance(String propertyName, Map attributes) {
        return new CompassClassPropertyMapping(ID, propertyName, attributes);
    }

    /**
     * Factory-style constructor for type-safe property type
     * @param propertyName the name of the mapped property
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getPropertyInstance(String propertyName) {
        return new CompassClassPropertyMapping(PROPERTY, propertyName);
    }

    /**
     * Factory-style constructor for type-safe property type
     * @param propertyName the name of the mapped property
     * @param attributes mapping attributes
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getPropertyInstance(String propertyName, Map attributes) {
        return new CompassClassPropertyMapping(PROPERTY, propertyName, attributes);
    }

    /**
     * Factory-style constructor for type-safe reference type
     * @param propertyName the name of the mapped property
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getReferenceInstance(String propertyName, Class propertyType) {
        return new CompassClassPropertyMapping(REFERENCE, propertyName, propertyType);
    }

    /**
     * Factory-style constructor for type-safe reference type
     * @param propertyName the name of the mapped property
     * @param propertyType the user class type
     * @param attributes mapping attributes @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getReferenceInstance(String propertyName, Class propertyType, Map attributes) {
        return new CompassClassPropertyMapping(REFERENCE, propertyName, propertyType, attributes);
    }

    /**
     * Factory-style constructor for type-safe compoonent type
     * @param propertyName the name of the mapped property
     * @param propertyType the user class type
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getComponentInstance(String propertyName, Class propertyType) {
        return new CompassClassPropertyMapping(COMPONENT, propertyName, propertyType);
    }

    /**
     * Factory-style constructor for type-safe compoonent type
     * @param propertyName the name of the mapped property
     * @param attributes mapping attributes
     * @param propertyType the user class type
     * @return a new CompassClassPropertyMapping instance
     */
    public static CompassClassPropertyMapping getComponentInstance(String propertyName, Class propertyType, Map attributes) {
        return new CompassClassPropertyMapping(COMPONENT, propertyName, propertyType, attributes);
    }

    /**
     * Factory-style constructor for type-safe cascade type
     * @param propertyName the name of the mapped property
     * @param propertyType the user class type
     * @param cascadeType the cascade type: "all", "create", "save" or "delete"
     */
    public static CompassClassPropertyMapping getCascadeInstance(String propertyName, Class propertyType, String cascadeType) {
        Map attrs = new HashMap();
        attrs.put("cascade", cascadeType);
        return new CompassClassPropertyMapping(CASCADE, propertyName, propertyType, attrs);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Is the strategy a searchable id?
     * @return true if this is a searchable property strategy
     */
    public boolean isId() {
        return type.equals(ID);
    }

    /**
     * Is the strategy a searchable property?
     * @return true if this is a searchable property strategy
     */
    public boolean isProperty() {
        return type.equals(PROPERTY);
    }

    /**
     * Is the strategy a searchable reference?
     * @return true if this is a searchable reference strategy
     */
    public boolean isReference() {
        return type.equals(REFERENCE);
    }

    /**
     * Is the strategy a searchable component?
     * @return true if this is a searchable component strategy
     */
    public boolean isComponent() {
        return type.equals(COMPONENT);
    }

    /**
     * Is the strategy a cascade only relationship?
     */
    public boolean isCascade() {
        return type.equals(CASCADE);
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

    public boolean hasAttribute(String name) {
        return attributes != null && attributes.containsKey(name);
    }

    public void setAttribute(String name, String value) {
        if (attributes == null) {
            attributes = new HashMap();
        }
        attributes.put(name, value);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Class getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Class propertyType) {
        this.propertyType = propertyType;
    }

    /**
     * Provide a useful String
     * @return String
     */
    @Override
    public String toString() {
        return "CompassClassPropertyMapping: type=[" + type + "], propertyName=[" + propertyName + "], attributes=[" + attributes + "]";
    }
}
