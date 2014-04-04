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
package grails.plugin.searchable.internal.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.compass.core.util.Assert;

/**
 * Lucene utils
 *
 * @author Maurice Nicholson
 */
public class LuceneUtils {
    private static final Log LOG = LogFactory.getLog(LuceneUtils.class);
    public static final String SPECIAL_QUERY_CHARACTERS = "\\+-!():^[]\"{}~*?";

    /**
     * Returns a list of terms by analysing the given text with Lucene's StandardAnalyzer
     *
     * @param text the text to analyse
     * @return a list of text terms
     */
    public static String[] termsForText(String text) {
        return termsForText(text, (Analyzer) null);
    }

    /**
     * Returns a list of terms by analysing the given text
     *
     * @param text the text to analyse
     * @param analyzerClass the Analyzer class to use, may be null in which case Lucene's StandardAnalyzer is used
     * @return a list of text terms
     */
    public static String[] termsForText(String text, Class analyzerClass) {
        if (analyzerClass == null) {
            return termsForText(text, (Analyzer) null);
        }
        try {
            return termsForText(text, (Analyzer) analyzerClass.newInstance());
        } catch (Exception ex) {
            // Convert to unchecked
            LOG.error("Failed to create instance of Analyzer class [" + analyzerClass + "]: " + ex, ex);
            throw new IllegalStateException("Failed to create instance of Analyzer class [" + analyzerClass + "]: " + ex);
        }
    }

    /**
     * Returns a list of terms by analysing the given text
     *
     * @param text the text to analyse
     * @param analyzer the Analyzer instance to use, may be null in which case Lucene's StandardAnalyzer is used
     * @return a list of text terms
     */
    public static String[] termsForText(String text, Analyzer analyzer) {
        try {
            if (analyzer == null) {
                analyzer = new StandardAnalyzer();
            }
            TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
            ArrayList terms = new ArrayList();
            Token token = new Token();
            while (true) {
                token = stream.next(token);
                if (token == null) break;

                terms.add(new String(token.termBuffer(), 0, token.termLength()));
            }
            return (String[]) terms.toArray(new String[terms.size()]);
        } catch (IOException ex) {
            // Convert to unchecked
            LOG.error("Unable to analyze the given text: " + ex, ex);
            throw new IllegalArgumentException("Unable to analyze the given text: " + ex);
        }
    }

    /**
     * Returns a list of terms by parsing the given query string - special query characters and words (OR/AND) are
     * not included in the returned list
     *
     * @param queryString the query string to parse
     * @param analyzerClass the Analyzer Class instance to instantiate, may be null in which case Lucene's
     * StandardAnalyzer is used
     * @return a list of text terms
     */
    public static String[] termsForQueryString(String queryString, Class analyzerClass) throws ParseException {
        if (analyzerClass == null) {
            return termsForQueryString(queryString, (Analyzer) null);
        }
        try {
            return termsForQueryString(queryString, (Analyzer) analyzerClass.newInstance());
        } catch (Exception ex) {
            // Convert to unchecked
            LOG.error("Failed to create instance of Analyzer class [" + analyzerClass + "]: " + ex, ex);
            throw new IllegalStateException("Failed to create instance of Analyzer class [" + analyzerClass + "]: " + ex);
        }
    }

    /**
     * Returns a list of terms by parsing the given query string - special query characters and words (OR/AND) are
     * not included in the returned list
     *
     * @param queryString the query string to parse
     * @param analyzer the Analyzer instance, may be null in which case Lucene's StandardAnalyzer is used
     * @return a list of text terms
     * @throws org.apache.lucene.queryParser.ParseException if the query is invalid
     */
    public static String[] termsForQueryString(String queryString, Analyzer analyzer) throws ParseException {
        if (analyzer == null) {
            analyzer = new StandardAnalyzer();
        }
        final String defaultField = "$termsForQueryString_defaultField$";
        QueryParser queryParser = new QueryParser(defaultField, analyzer);
        Query query = queryParser.parse(queryString);
        Set terms = new ListNotSet();
        query.extractTerms(terms);
        String[] termsArray = new String[terms.size()];
        int i = 0;
        for (Iterator iter = terms.iterator(); iter.hasNext(); ) {
            termsArray[i++] = ((Term) iter.next()).text();
        }
        return termsArray;
    }

    /**
     * Returns an array of {@link Term}s by parsing the given query string. Since Lucene's query parser is used,
     * special query characters and words (OR / AND) are not included in the returned terms
     *
     * @param defaultField The default term field, cannot be null
     * @param queryString the query string to parse, cannot be null
     * @param analyzerClass the Class of Analyzer, may be null in which case Lucene's StandardAnalyzer is used
     * @return the Term array (field + term pairs)
     * @throws org.apache.lucene.queryParser.ParseException if the the query has invalid syntax
     */
    public static Term[] realTermsForQueryString(String defaultField, String queryString, Class analyzerClass) throws ParseException {
        if (analyzerClass == null) {
            return realTermsForQueryString(defaultField, queryString, (Analyzer) null);
        }
        try {
            return realTermsForQueryString(defaultField, queryString, (Analyzer) analyzerClass.newInstance());
        } catch (Exception ex) {
            // Convert to unchecked
            LOG.error("Failed to create instance of Analyzer class [" + analyzerClass + "]: " + ex, ex);
            throw new IllegalStateException("Failed to create instance of Analyzer class [" + analyzerClass + "]: " + ex);
        }
    }

    /**
     * Returns an array of {@link Term}s by parsing the given query string. Since Lucene's query parser is used,
     * special query characters and words (OR / AND) are not included in the returned terms
     *
     * @param defaultField The default term field, cannot be null
     * @param queryString the query string to parse, cannot be null
     * @param analyzer the Analyzer instance, may be null in which case Lucene's StandardAnalyzer is used
     * @return the Term array (field + term pairs)
     * @throws org.apache.lucene.queryParser.ParseException if the the query has invalid syntax
     */
    public static Term[] realTermsForQueryString(String defaultField, String queryString, Analyzer analyzer) throws ParseException {
        Assert.notNull(defaultField, "defaultField cannot be null");
        Assert.notNull(queryString, "queryString cannot be null");
        if (analyzer == null) {
            analyzer = new StandardAnalyzer();
        }
        QueryParser queryParser = new QueryParser(defaultField, analyzer);
        Query query = queryParser.parse(queryString);
        Set terms = new ListNotSet();
        query.extractTerms(terms);
        Term[] termsArray = new Term[terms.size()];
        int i = 0;
        for (Iterator iter = terms.iterator(); iter.hasNext(); ) {
            Term term = (Term) iter.next();
            termsArray[i++] = term;
        }
        return termsArray;
    }

    // A Set that allows dupes and maintains insertion order, so not really a set :-)
    private static class ListNotSet extends ArrayList implements Set {
        private static final long serialVersionUID = 1;
    }

    /**
     * Escape special characters in the given string that would otherwise cause a parse exception
     *
     * @param query the query to escape
     * @return the escaped query
     */
    public static String escapeQuery(String query) {
        // Note we use the Lucene QueryParser instead of the Compass subclass
        // because Groovy does not inherit static methods (?)
        if (query == null) return null;
        return QueryParser.escape(query);
    }

    /**
     * Returns the query string with special characters removed
     *
     * @param query the query to clean
     * @return the cleaned query
     */
    public static String cleanQuery(String query) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, count = query.length(); i < count; i++) {
            char c = query.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (isSpecialQueryCharacter(c)) continue;
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Does the given query string contain special characters, ie, those with
     * special meaning to Lucene's query parser
     * @param query the query
     * @return true if it contains special characters
     */
    public static boolean queryHasSpecialCharacters(String query) {
        for (int i = 0, count = query.length(); i < count; i++) {
            char c = query.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (isSpecialQueryCharacter(c)) return true;
        }
        return false;
    }

    private static boolean isSpecialQueryCharacter(char c) {
        return SPECIAL_QUERY_CHARACTERS.indexOf(c) > -1;
    }
}
