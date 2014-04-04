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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describes a Compass searchable class mapping
 *
 * Note: a runtime Compass Mapping API is coming soon, so I'll replace this with that then
 *
 * @author Maurice Nicholson
 */
public class CompassClassMapping {
    private Class mappedClass;
    private Class mappedClassSuperClass;

    private String alias;
    private String analyzer;
    private BigDecimal boost;
    private String converter;
    private Boolean enableAll;
    private String allName;
    private String allAnalyzer;
    private String allTermVector;
    private Boolean allExcludeAlias;
    private Boolean allOmitNorms;
    private String allSpellCheck;
    private String extend;
    private String managedId;
    private boolean poly = false;
    private Boolean root;
    private String spellCheck;
    private String subIndex;
    private boolean supportUnmarshall = true;

    private Map subIndexHash;

    private List propertyMappings = new ArrayList();
    private List constantMetaData = new ArrayList();

    public Class getMappedClass() {
        return mappedClass;
    }

    public void setMappedClass(Class mappedClass) {
        this.mappedClass = mappedClass;
    }

    public void setMappedClassSuperClass(Class clazz) {
        mappedClassSuperClass = clazz;
    }

    public Class getMappedClassSuperClass() {
        return mappedClassSuperClass;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSubIndex() {
        return subIndex;
    }

    public void setSubIndex(String subIndex) {
        this.subIndex = subIndex;
    }

    public Boolean getRoot() {
        return root;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public boolean isPoly() {
        return poly;
    }

    public void setPoly(boolean poly) {
        this.poly = poly;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }

    public BigDecimal getBoost() {
        return boost;
    }

    public void setBoost(BigDecimal boost) {
        this.boost = boost;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(String converter) {
        this.converter = converter;
    }

    public Boolean getEnableAll() {
        return enableAll;
    }

    public void setEnableAll(Boolean enableAll) {
        this.enableAll = enableAll;
    }

    public Boolean getAll() {
        return enableAll;
    }

    public void setAll(Boolean enableAll) {
        this.enableAll = enableAll;
    }

    public String getAllName() {
        return allName;
    }

    public void setAllName(String allName) {
        this.allName = allName;
    }

    public String getAllAnalyzer() {
        return allAnalyzer;
    }

    public void setAllAnalyzer(String allAnalyzer) {
        this.allAnalyzer = allAnalyzer;
    }

    public String getAllTermVector() {
        return allTermVector;
    }

    public void setAllTermVector(String allTermVector) {
        this.allTermVector = allTermVector;
    }

    public Boolean getAllExcludeAlias() {
        return allExcludeAlias;
    }

    public void setAllExcludeAlias(Boolean allExcludeAlias) {
        this.allExcludeAlias = allExcludeAlias;
    }

    public Boolean getAllOmitNorms() {
        return allOmitNorms;
    }

    public void setAllOmitNorms(Boolean allOmitNorms) {
        this.allOmitNorms = allOmitNorms;
    }

    public String getAllSpellCheck() {
        return allSpellCheck;
    }

    public void setAllSpellCheck(String allSpellCheck) {
        this.allSpellCheck = allSpellCheck;
    }

    public String getManagedId() {
        return managedId;
    }

    public void setManagedId(String managedId) {
        this.managedId = managedId;
    }

    public String getSpellCheck() {
        return spellCheck;
    }

    public void setSpellCheck(String spellCheck) {
        this.spellCheck = spellCheck;
    }

    public boolean isSupportUnmarshall() {
        return supportUnmarshall;
    }

    public void setSupportUnmarshall(boolean supportUnmarshall) {
        this.supportUnmarshall = supportUnmarshall;
    }

    public Map getSubIndexHash() {
        return subIndexHash;
    }

    public void setSubIndexHash(Map subIndexHash) {
        this.subIndexHash = subIndexHash;
    }

    public List getConstantMetaData() {
        return constantMetaData;
    }

    public void setConstantMetaData(List constantMetaData) {
        this.constantMetaData = constantMetaData;
    }

    public void addConstantMetaData(final String name, final Map attributes, final List values) {
        Map map = new HashMap();
        map.put("name", name);
        map.put("attributes", attributes);
        map.put("values", values);
        this.constantMetaData.add(map);
    }

    public List getPropertyMappings() {
        return propertyMappings;
    }

    public void setPropertyMappings(List propertyMappings) {
        this.propertyMappings = propertyMappings;
    }

    public void addPropertyMapping(CompassClassPropertyMapping propertyMapping) {
        this.propertyMappings.add(propertyMapping);
    }

    /**
     * Provide a useful String
     * @return String
     */
    @Override
    public String toString() {
        return "CompassClassMapping: mappedClass=[" + mappedClass + "],  mappedClassSuperClass=[" + mappedClassSuperClass + "], alias=[" + alias + "], spellCheck=[" + spellCheck + "], subIndex=[" + subIndex + "], root=[" + root + "], poly=[" + poly + "], extend=[" + extend + "], propertyMappings=[" + propertyMappings + "]";
    }
}
