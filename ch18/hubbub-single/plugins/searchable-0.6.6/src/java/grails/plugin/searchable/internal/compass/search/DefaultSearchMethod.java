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

import grails.plugin.searchable.internal.SearchableMethod;
import grails.plugin.searchable.internal.SearchableMethodFactory;
import grails.plugin.searchable.internal.compass.support.AbstractSearchableMethod;
import grails.plugin.searchable.internal.compass.support.SearchableMethodUtils;
import groovy.lang.Closure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.compass.core.Compass;
import org.compass.core.CompassCallback;
import org.compass.core.CompassDetachedHits;
import org.compass.core.CompassException;
import org.compass.core.CompassHits;
import org.compass.core.CompassQuery;
import org.compass.core.CompassSession;
import org.springframework.util.Assert;

/**
 * The default search method implementation
 *
 * @author Maurice Nicholson
 */
public class DefaultSearchMethod extends AbstractSearchableMethod {
    private static Log LOG = LogFactory.getLog(DefaultSearchMethod.class);

    private GrailsApplication grailsApplication;
    private SearchableCompassQueryBuilder compassQueryBuilder;
    private SearchableHitCollector hitCollector;
    private SearchableSearchResultFactory searchResultFactory;

    public DefaultSearchMethod(String methodName, Compass compass, GrailsApplication grailsApplication, SearchableMethodFactory methodFactory, Map defaultOptions) {
        super(methodName, compass, methodFactory, defaultOptions);
        this.grailsApplication = grailsApplication;
    }

    public Object invoke(Object[] args) {
        Assert.notNull(args, "args cannot be null");
        Assert.notEmpty(args, "args cannot be empty");

        SearchableMethod suggestQueryMethod = getMethodFactory().getMethod("suggestQuery");

        SearchCompassCallback searchCallback = new SearchCompassCallback(getCompass(), getDefaultOptions(), args);
        searchCallback.setGrailsApplication(grailsApplication);
        searchCallback.setCompassQueryBuilder(compassQueryBuilder);
        searchCallback.setHitCollector(hitCollector);
        searchCallback.setSearchResultFactory(searchResultFactory);
        searchCallback.setSuggestQueryMethod(suggestQueryMethod);
        return doInCompass(searchCallback);
    }

    public void setCompassQueryBuilder(SearchableCompassQueryBuilder compassQueryBuilder) {
        this.compassQueryBuilder = compassQueryBuilder;
    }

    public void setHitCollector(SearchableHitCollector hitCollector) {
        this.hitCollector = hitCollector;
    }

    public void setSearchResultFactory(SearchableSearchResultFactory searchResultFactory) {
        this.searchResultFactory = searchResultFactory;
    }

    public void setGrailsApplication(GrailsApplication grailsApplication) {
        this.grailsApplication = grailsApplication;
    }

    public static class SearchCompassCallback implements CompassCallback {
        private Object[] args;
        private Map defaultOptions;
        private GrailsApplication grailsApplication;
        private SearchableCompassQueryBuilder compassQueryBuilder;
        private SearchableHitCollector hitCollector;
        private SearchableSearchResultFactory searchResultFactory;
        private SearchableMethod suggestQueryMethod;
        private static final String[] OVERRIDE_WITH_DEFAULTS_IF_NULL = {"max", "offset"};

        public SearchCompassCallback(Compass compass, Map defaultOptions, Object[] args) {
            this.args = args;
            this.defaultOptions = defaultOptions;
        }

        public Object doInCompass(CompassSession session) throws CompassException {
            Map options = SearchableMethodUtils.getOptionsArgument(args, defaultOptions, OVERRIDE_WITH_DEFAULTS_IF_NULL);
            CompassQuery compassQuery = compassQueryBuilder.buildQuery(grailsApplication, session, options, args);
            long start = System.currentTimeMillis();
            CompassHits hits = compassQuery.hits();
            if (LOG.isDebugEnabled()) {
                long time = System.currentTimeMillis() - start;
                LOG.debug("query: [" + compassQuery + "], [" + hits.length() + "] hits, took [" + time + "] millis");
            }
            if (hitCollector == null && searchResultFactory == null) {
                Assert.notNull(options.get("result"), "Missing 'result' option for search/query method: this should be provided if hitCollector/searchResultFactory are null to determine the type of result to return");
                String result = (String) options.get("result");
                if (result.equals("top")) {
                    hitCollector = new DefaultSearchableTopHitCollector();
                    searchResultFactory = new SearchableHitsOnlySearchResultFactory();
                } else if (result.equals("every")) {
                    hitCollector = new DefaultSearchableEveryHitCollector();
                    searchResultFactory = new SearchableHitsOnlySearchResultFactory();
                } else if (result.equals("searchResult")) {
                    hitCollector = new DefaultSearchableSubsetHitCollector();
                    searchResultFactory = new SearchableSubsetSearchResultFactory();
                } else if (result.equals("count")) {
                    hitCollector = new CountOnlyHitCollector();
                    searchResultFactory = new SearchableHitsOnlySearchResultFactory();
                } else {
                    throw new IllegalArgumentException("Invalid 'result' option for search/query method [" + result + "]. Supported values are ['searchResult', 'every', 'top']");
                }
            }
            int max = MapUtils.getIntValue(options, "max");
            int offset = MapUtils.getIntValue(options, "offset");
            int low = offset;

            Object collectedHits =  hitCollector.collect(hits,options);
            CompassDetachedHits compassDetachedHits = hits.detach(low,max);
            Object searchResult = searchResultFactory.buildSearchResult(hits, collectedHits, compassDetachedHits, options);

            doWithHighlighter(collectedHits, hits, searchResult, options);

            Object suggestOption = options.get("suggestQuery");
            if (searchResult instanceof Map && suggestOption != null) {
                addSuggestedQuery((Map) searchResult, suggestOption);
            }
            return searchResult;
        }

        private void addSuggestedQuery(Map searchResult, Object suggestOption) {
            if (suggestOption instanceof Boolean && !(Boolean)suggestOption) {
                return;
            }
            if (suggestOption instanceof String && !Boolean.valueOf((String) suggestOption)) {
                return;
            }
            Object[] suggestArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Map searchOptions = (Map) args[i];
                    Map suggestOptions = new HashMap(searchOptions);
                    if (suggestOption instanceof Map) {
                        suggestOptions.putAll((Map) suggestOption);
                    }
                    suggestOptions.remove("suggestQuery"); // remove the option itself
                    // add other defaults for use from search method
                    if (!suggestOptions.containsKey("allowSame")) {
                        suggestOptions.put("allowSame", false);
                    }
                    suggestArgs[i] = suggestOptions;
                } else {
                    suggestArgs[i] = args[i];
                }
            }
            searchResult.put("suggestedQuery", suggestQueryMethod.invoke(suggestArgs));
        }

        public void doWithHighlighter(Object collectedHits, CompassHits hits, Object searchResult, Map options) {
            if (!(collectedHits instanceof Collection)) {
                return;
            }
            Closure withHighlighter = (Closure) options.get("withHighlighter");
            if (withHighlighter == null) {
                return;
            }
            withHighlighter = (Closure) withHighlighter.clone();
            int offset = org.apache.commons.collections.MapUtils.getIntValue(options, "offset");
            for (int i = 0, length = ((Collection) collectedHits).size(); i < length; i++) {
                withHighlighter.call(new Object[] {
                    hits.highlighter(offset + i), i, searchResult
                });
            }
        }

        public void setGrailsApplication(GrailsApplication grailsApplication) {
            this.grailsApplication = grailsApplication;
        }

        public void setCompassQueryBuilder(SearchableCompassQueryBuilder compassQueryBuilder) {
            this.compassQueryBuilder = compassQueryBuilder;
        }

        public void setHitCollector(SearchableHitCollector hitCollector) {
            this.hitCollector = hitCollector;
        }

        public void setSearchResultFactory(SearchableSearchResultFactory searchResultFactory) {
            this.searchResultFactory = searchResultFactory;
        }

        public void setSuggestQueryMethod(SearchableMethod suggestQueryMethod) {
            this.suggestQueryMethod = suggestQueryMethod;
        }
    }
}
