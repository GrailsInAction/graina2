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
package grails.plugin.searchable.internal.util;

import grails.plugin.searchable.internal.lucene.LuceneUtils;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;

/**
 * @author Maurice Nicholson
 */
public class StringQueryUtils {
    private static final Log LOG = LogFactory.getLog(StringQueryUtils.class);

    /** The default highlight pattern */
    public static final String DEFAULT_DIFFS_HIGHLIGHT_PATTERN = "<b><i>{0}</i></b>";

    /**
     * Highlights the different terms in the second query and returns a new query string.
     * Diffs are highlighted using {@link #DEFAULT_DIFFS_HIGHLIGHT_PATTERN}; for alternative
     * formats use {@link #highlightTermDiffs(String, String, String)}.
     * This method is intended to be used with suggested queries to display the suggestion
     * to the user in highlighted format, as per Google, so the queries are expected to roughly match
     * @param first the original query
     * @param second the second query, in which to highlight differences
     * zero is the highlighted term text
     * @return a new copy of second with term differences highlighted
     * @throws ParseException if either first or second query is invalid
     * @see #highlightTermDiffs(String, String, String)
     */
    public static String highlightTermDiffs(String first, String second) throws ParseException {
        return highlightTermDiffs(first, second, DEFAULT_DIFFS_HIGHLIGHT_PATTERN);
    }

    /**
     * Highlights the different terms in the second query and returns a new query string.
     * This method is intended to be used with suggested queries to display the suggestion
     * to the user in highlighted format, as per Google, so the queries are expected to roughly match
     * @param first the original query
     * @param second the second query, in which to highlight differences
     * @param highlightPattern the pattern used to highlight; should be a {@link MessageFormat} pattern where argument
     * zero is the highlighted term text
     * @return a new copy of second with term differences highlighted
     * @throws ParseException if either first or second query is invalid
     * @see #highlightTermDiffs(String, String)
     */
    public static String highlightTermDiffs(String first, String second, String highlightPattern) throws ParseException {
        final String defaultField = "$StringQueryUtils_highlightTermDiffs$";
        Term[] firstTerms = LuceneUtils.realTermsForQueryString(defaultField, first, WhitespaceAnalyzer.class);
        Term[] secondTerms = LuceneUtils.realTermsForQueryString(defaultField, second, WhitespaceAnalyzer.class);

        if (firstTerms.length != secondTerms.length) {
            LOG.warn(
                "Expected the same number of terms for first query [" + first + "] and second query [" + second + "], " +
                "but first query has [" + firstTerms.length + "] terms and second query has [" + secondTerms.length + "] terms " +
                "so unable to provide user friendly version. Returning second query as-is."
            );
            return second;
        }

        MessageFormat format = new MessageFormat(highlightPattern);
        StringBuilder diff = new StringBuilder(second);
        int offset = 0;
        for (int i = 0; i < secondTerms.length; i++) {
            Term firstTerm = firstTerms[i];
            Term secondTerm = secondTerms[i];
            boolean noField = defaultField.equals(secondTerm.field());
            String snippet = noField ? secondTerm.text() : secondTerm.field() + ":" + secondTerm.text();
            int pos = diff.indexOf(snippet, offset);
            if (!firstTerm.text().equals(secondTerm.text())) {
                if (!noField) {
                    pos += secondTerm.field().length() + 1;
                }
                diff.replace(pos, pos + secondTerm.text().length(), format.format(new Object[] { secondTerm.text() }));
            }
            offset = pos;
        }
        return diff.toString();
    }
}
