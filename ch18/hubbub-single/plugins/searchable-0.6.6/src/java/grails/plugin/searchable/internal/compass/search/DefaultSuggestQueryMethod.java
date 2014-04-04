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

import grails.plugin.searchable.internal.compass.mapping.CompassMappingUtils;
import grails.plugin.searchable.internal.compass.support.AbstractSearchableMethod;
import grails.plugin.searchable.internal.compass.support.SearchableMethodUtils;
import grails.plugin.searchable.internal.lucene.LuceneUtils;
import groovy.lang.Closure;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassException;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.compass.core.engine.SearchEngineQueryParseException;
import org.springframework.util.Assert;

/**
 * @author Maurice Nicholson
 */
public class DefaultSuggestQueryMethod extends AbstractSearchableMethod {

    private SearchableCompassQueryBuilder compassQueryBuilder;
    private GrailsApplication grailsApplication;

    public DefaultSuggestQueryMethod(String methodName, Compass compass, GrailsApplication grailsApplication) {
        this(methodName, compass, grailsApplication, new HashMap());
    }

    public DefaultSuggestQueryMethod(String methodName, Compass compass, GrailsApplication grailsApplication, Map defaultOptions) {
        super(methodName, compass, null, defaultOptions);
        this.grailsApplication = grailsApplication;
    }

    public Object invoke(Object[] args) {
        if (!CompassMappingUtils.hasSpellCheckMapping(getCompass())) {
            throw new IllegalStateException(
                "Suggestions are only available when classes are mapped with \"spellCheck\" options, either at the class " +
                "or property level. The simplest way to do this is add spellCheck \"include\" to the domain class searchable mapping closure. " +
                "See the plugin/Compass documentation Mapping sections for details."
            );
        }
        if (!"true".equals(getCompass().getSettings().getSetting("compass.engine.spellcheck.enable"))) {
            throw new IllegalStateException(
                "Suggestions are only available when the Compass Spell Check feature is enabled, but currently it is not. " +
                "Please set Compass setting 'compass.engine.spellcheck.enable' to 'true'. " +
                "One way to so this is to use the SearchableConfiguration.groovy file (run \"grails install-searchable-config\") and " +
                "add \"'compass.engine.spellcheck.enable': 'true'\" to the compassSettings Map. " +
                "Also see the Spell Check section in the Compass docs for additional settings."
            );
        }

        Object query = SearchableMethodUtils.getQueryArgument(args);
        if (query instanceof Closure) {
            throw new UnsupportedOperationException("Closure queries are not support for query suggestions, only String queries.");
        }
        Assert.isInstanceOf(String.class, query, "Only String queries are supported for query suggestions");

        SuggestQueryCompassCallback suggestQueryCallback = new SuggestQueryCompassCallback(getCompass(), getDefaultOptions(), args);
        Map options = getOptions(args);
        suggestQueryCallback.applyOptions(options);
        suggestQueryCallback.setGrailsApplication(grailsApplication);
        suggestQueryCallback.setCompassQueryBuilder(compassQueryBuilder);
        return doInCompass(suggestQueryCallback);
    }

    public SearchableCompassQueryBuilder getCompassQueryBuilder() {
        return compassQueryBuilder;
    }

    public void setCompassQueryBuilder(SearchableCompassQueryBuilder compassQueryBuilder) {
        this.compassQueryBuilder = compassQueryBuilder;
    }

    public Map getOptions(Object[] args) {
        Map options = new HashMap(getDefaultOptions()); // clone to avoid corrupting original
        options.putAll(SearchableMethodUtils.getOptionsArgument(args, null));
        return options;
    }

    public static class SuggestQueryCompassCallback implements CompassCallback {
        private Map defaultOptions;
        private Object[] args;
        private SearchableCompassQueryBuilder compassQueryBuilder;
        private GrailsApplication grailsApplication;
        private boolean userFriendly;
        private boolean emulateCapitalisation;
        private boolean escape;
        private boolean allowSame;

        public SuggestQueryCompassCallback(Compass compass, Map defaultOptions, Object[] args) {
            this.defaultOptions = defaultOptions;
            this.args = args;
        }

        public Object doInCompass(CompassSession session) throws CompassException {
            Map options = SearchableMethodUtils.getOptionsArgument(args, defaultOptions);
            options.put("analyzer", "searchableplugin_simple");
            CompassQuery original = compassQueryBuilder.buildQuery(grailsApplication, session, options, args);
            String queryString = original.getSuggestedQuery().toString();
            String suggestedString = queryString;
            if (options.containsKey("class")) {
                // Strip the additional junk from around the query - +(what test) +(alias:B)
                Pattern pattern = Pattern.compile("\\+\\((.+)\\) \\+\\(alias:.+\\)");
                Matcher matcher = pattern.matcher(queryString);
                if (!matcher.matches()) {
                    return queryString;
                }
                suggestedString = matcher.group(1);
            }
            String originalString = (String) SearchableMethodUtils.getQueryArgument(args);
            try {
                return new SuggestedQueryStringBuilder(originalString, suggestedString).
                    userFriendly(userFriendly).
                    emulateCapitalisation(emulateCapitalisation).
                    escape(escape).
                    allowSame(allowSame).
                    toSuggestedQueryString();
            } catch (ParseException ex) {
                throw new SearchEngineQueryParseException(
                    "Failed to parse one of the queries: orignal [" + originalString + "], suggested: [" + suggestedString + "]",
                    ex
                );
            }
        }

        public void setCompassQueryBuilder(SearchableCompassQueryBuilder compassQueryBuilder) {
            this.compassQueryBuilder = compassQueryBuilder;
        }

        public void setGrailsApplication(GrailsApplication grailsApplication) {
            this.grailsApplication = grailsApplication;
        }

        public void applyOptions(Map options) {
            if (options == null) {
                return;
            }
            userFriendly = SearchableMethodUtils.getBool(options, "userFriendly", true);
            emulateCapitalisation = SearchableMethodUtils.getBool(options, "emulateCapitalisation", true);
            escape = SearchableMethodUtils.getBool(options, "escape", false);
            allowSame = SearchableMethodUtils.getBool(options, "allowSame", true);
        }
    }

    public static class SuggestedQueryStringBuilder {
        private static final Log LOG = LogFactory.getLog(SuggestedQueryStringBuilder.class);
        private static final String defaultField = "$SuggestedQueryStringUtils_defaultField$";

        private String original;
        private String suggested;
        private boolean userFriendly = true;
        private boolean emulateCapitalisation = true;
        private boolean escape = false;
        private boolean allowSame = true;

        /**
         * Create a suggested query string builder with the given original and suggested query strings
         * @param original the original query - probably from a user
         * @param suggested the suggested query - probably from the Compass suggestion engine
         */
        public SuggestedQueryStringBuilder(String original, String suggested) {
            this.original = original;
            this.suggested = suggested;
        }

        /**
         * Enable/disable whether queries suggested by {@link #toSuggestedQueryString} are user-frienly,
         * ie, look like the user's original query.
         * This feature is enabled by default
         * If you disable this feature, the emulateCapitalisation setting is ignored and the suggested query
         * is returned by {@link # toSuggestedQueryString}  as-is
         * @param userFriendly true or false to enable or disable
         * @return this
         */
        public SuggestedQueryStringBuilder userFriendly(boolean userFriendly) {
            this.userFriendly = userFriendly;
            return this;
        }

        /**
         * Enable/disable the emulation of capitalised words.
         * This feature is enabled by default
         * @param emulateCapitalisation true or false to enable or disable
         * @return this
         */
        public SuggestedQueryStringBuilder emulateCapitalisation(boolean emulateCapitalisation) {
            this.emulateCapitalisation = emulateCapitalisation;
            return this;
        }

        /**
         * Enable/disable whether to allow the same query to be suggested as the original
         * This is enabled by default
         * @param allowSame true or false to enable or disable
         * @return this
         */
        public SuggestedQueryStringBuilder allowSame(boolean allowSame) {
            this.allowSame = allowSame;
            return this;
        }

        /**
         * Get the suggested query based on the options set
         * @return the suggested query string or null if allowSame is false and the queries match
         * @throws ParseException if either original or suggested query is invalid
         */
        public String toSuggestedQueryString() throws ParseException {
            if (!userFriendly) {
                return suggested;
            }

            Term[] originalTerms = LuceneUtils.realTermsForQueryString(defaultField, escape ? LuceneUtils.cleanQuery(original) : original, WhitespaceAnalyzer.class);
            Term[] suggestedTerms = LuceneUtils.realTermsForQueryString(defaultField, suggested, WhitespaceAnalyzer.class);

            if (originalTerms.length != suggestedTerms.length) {
                LOG.warn(
                    "Expected the same number of terms for original query [" + original + "] and suggested query [" + suggested + "], " +
                    "but original query has [" + originalTerms.length + "] terms and suggested query has [" + suggestedTerms.length + "] terms " +
                    "so unable to provide user friendly version. Returning suggested query as-is."
                );
                return suggested;
            }

            StringBuilder userFriendly = new StringBuilder(original);
            int offset = 0;
            for (int i = 0; i < originalTerms.length; i++) {
                Term originalTerm = originalTerms[i];
                boolean noField = originalTerm.field().equals(defaultField);
                String snippet = noField ? originalTerm.text() : originalTerm.field() + ":" + originalTerm.text();
                int pos = userFriendly.indexOf(snippet, offset);
                Term suggestedTerm = suggestedTerms[i];
                String replacement = getReplacement(originalTerm, noField, suggestedTerm);
                userFriendly.replace(pos, pos + snippet.length(), replacement);
                offset = pos;
            }
            String suggestion = userFriendly.toString();
            if (!allowSame && suggestion.equals(original)) {
                return null;
            }
            return suggestion;
        }

        public SuggestedQueryStringBuilder escape(boolean escape) {
            this.escape = escape;
            return this;
        }

        private String getReplacement(Term originalTerm, boolean noField, Term suggestedTerm) {
            String replacement = noField ? suggestedTerm.text() : originalTerm.field() + ":" + suggestedTerm.text();
            if (emulateCapitalisation) {
                boolean upperCase = true;
                boolean firstUpperCase = false;
                final String original = originalTerm.text();
                for (int i = 0; i < original.length(); i++) {
                    if (!Character.isUpperCase(original.charAt(i))) {
                        upperCase = false;
                        break;
                    }
                    if (i == 0) {
                        firstUpperCase = true;
                    }
                }
                if (upperCase) {
                    return replacement.toUpperCase();
                }
                if (firstUpperCase) {
                    return replacement.substring(0, 1).toUpperCase() + (replacement.length() > 1 ? replacement.substring(1) : "");
                }
            }
            return replacement;
        }
    }
}
