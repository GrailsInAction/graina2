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
package grails.plugin.searchable.internal.compass.search;

import grails.plugin.searchable.internal.compass.mapping.SearchableGrailsDomainClassCompassMappingUtils;
import grails.plugin.searchable.internal.compass.support.AbstractSearchableMethod;
import groovy.lang.IntRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassSession;
import org.compass.core.CompassTermFreqsBuilder;
import org.springframework.util.Assert;

/**
 * Implements the SearchableService and domain class termFreqs() method
 *
 * @author Maurice Nicholson
 */
public class DefaultTermFreqsMethod extends AbstractSearchableMethod {
    private GrailsApplication grailsApplication;

    public DefaultTermFreqsMethod(String methodName, Compass compass, GrailsApplication grailsApplication, Map defaultOptions) {
        super(methodName, compass, null, defaultOptions);
        this.grailsApplication = grailsApplication;
    }

    public Object invoke(Object[] args) {
        Assert.notNull(args, "args cannot be null");

        TermFreqsArgs termFreqsArgs = TermFreqsArgs.parseArgs(args, getDefaultOptions());
        if (termFreqsArgs.getClazz() != null) {
            termFreqsArgs.setAliases(SearchableGrailsDomainClassCompassMappingUtils.getPolyMappingAliases(getCompass(), termFreqsArgs.getClazz(), grailsApplication));
        }

        TermFreqsCompassCallback termFreqs = new TermFreqsCompassCallback(termFreqsArgs);
        return doInCompass(termFreqs);
    }

    /**
     * Helper to parse the method arguments
     */
    public static class TermFreqsArgs {
        private String[] properties;
        private IntRange normalizeRange;
        private Integer size;
        private CompassTermFreqsBuilder.Sort sort;
        private String[] aliases;
        private Class clazz;

        public static TermFreqsArgs parseArgs(Object[] args, Map defaultOptions) {
            TermFreqsArgs tfa = new TermFreqsArgs();

            Map options = null;
            for (int i = 0; i < args.length; i++) {
                if (Map.class.isAssignableFrom(args[i].getClass())) {
                    options = (Map) args[i];
                    break;
                }
            }
            String[] properties = getProperties(options);
            if (properties == null) {
                List propsList = new ArrayList();
                for (int i = 0; i < args.length; i++) {
                    if (String.class.isAssignableFrom(args[i].getClass())) {
                        propsList.add(args[i]);
                    }
                }
                if (!propsList.isEmpty()) {
                    properties = (String[]) propsList.toArray(new String[propsList.size()]);
                }
            }
            if (properties == null) {
                properties = getProperties(defaultOptions);
            }
            tfa.setProperties(properties);
//        Assert.notNull(properties, "Missing property/properties option");
//        Assert.notEmpty(properties, "Missing property/properties option");

            options = grails.plugin.searchable.internal.util.MapUtils.nullSafeAddMaps(defaultOptions, options);
            tfa.setNormalizeRange(getNormalizeRange(options));
            tfa.setSize(MapUtils.getInteger(options, "size"));
            if (options.containsKey("class")) {
                tfa.setClazz((Class) options.get("class"));
            } else {
                tfa.setClazz((Class) options.get("match"));
            }
            String sortName = MapUtils.getString(options, "sort");
            if (sortName != null) {
                Assert.isTrue(sortName.equalsIgnoreCase("term") || sortName.equalsIgnoreCase("freq"), "sort option must be either 'term' or 'freq' but was '" + sortName + "'");
                if (sortName.equalsIgnoreCase("term")) {
                    tfa.setSort(CompassTermFreqsBuilder.Sort.TERM);
                } else {
                    tfa.setSort(CompassTermFreqsBuilder.Sort.FREQ);
                }
            }

            return tfa;
        }

        private static IntRange getNormalizeRange(Map options) {
            IntRange norm = (IntRange) MapUtils.getObject(options, "normalize");
            if (norm == null) {
                norm = (IntRange) MapUtils.getObject(options, "normalise");
            }
            return norm;
        }

        private static String[] getProperties(Map options) {
            if (options == null) {
                return null;
            }
            Object value = null;
            if (options.containsKey("property")) {
                value = options.get("property");
            } else if (options.containsKey("properties")) {
                value = options.get("properties");
            }
            List properties = new ArrayList();
            if (value instanceof Collection) {
                properties.addAll((Collection) value);
            } else if (value instanceof String) {
                properties.add(value);
            } else if (value instanceof String[]) {
                properties.addAll(Arrays.asList((String[]) value));
            } else if (value != null) {
                throw new IllegalArgumentException("Unhandled value type for property/properties option: [" + value + "]");
            }
            if (properties.isEmpty()) {
                return null;
            }
            return (String[]) properties.toArray(new String[properties.size()]);
        }

        public String[] getProperties() {
            return properties;
        }

        public void setProperties(String[] properties) {
            this.properties = properties;
        }

        public IntRange getNormalizeRange() {
            return normalizeRange;
        }

        public void setNormalizeRange(IntRange normalizeRange) {
            this.normalizeRange = normalizeRange;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public CompassTermFreqsBuilder.Sort getSort() {
            return sort;
        }

        public void setSort(CompassTermFreqsBuilder.Sort sort) {
            this.sort = sort;
        }

        public String[] getAliases() {
            return aliases;
        }

        public void setAliases(String[] aliases) {
            this.aliases = aliases;
        }

        public Class getClazz() {
            return clazz;
        }

        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }
    }

    /**
     * A builder that builds the term freqs based on the properties set
     *
     * Note instances of this class are not thread-safe
     */
    public static class TermFreqsCompassCallback implements CompassCallback {
        private TermFreqsArgs termFreqsArgs;

        public TermFreqsCompassCallback(TermFreqsArgs termFreqsArgs) {
            super();
            this.termFreqsArgs = termFreqsArgs;
        }

        public Object doInCompass(CompassSession session) throws CompassException {
            CompassTermFreqsBuilder builder = session.termFreqsBuilder(termFreqsArgs.getProperties());
            if (termFreqsArgs.getNormalizeRange() != null) {
                builder.normalize(termFreqsArgs.getNormalizeRange().getFromInt(), termFreqsArgs.getNormalizeRange().getToInt());
            }
            if (termFreqsArgs.getSize() != null) {
                builder.setSize(termFreqsArgs.getSize().intValue());
            }
            if (termFreqsArgs.getSort() != null) {
                builder.setSort(termFreqsArgs.getSort());
            }
            if (termFreqsArgs.getAliases() != null) {
                builder.setAliases(termFreqsArgs.getAliases());
            }
            return builder.toTermFreqs();
        }
    }
}
