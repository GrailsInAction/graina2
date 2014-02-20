package qotd

import qotd.Quote

class QuoteAnalyzer {
    private List<Quote> quotes

    QuoteAnalyzer(List<Quote> quotes) {
        this.quotes = new ArrayList(quotes)
    }

    int getQuoteCount() {
        return this.quotes.size()
    }

    Class<Integer> getClazz() { return int.class }

    Map<String, Integer> getQuoteCountPerAuthor() {
        def result = [:]
        for (Quote quote in quotes) {
            if (result.containsKey(quote.author)) {
                result[quote.author] = result[quote.author] + 1
            }
            else {
                result[quote.author] = 1
            }
        }
        return result
    }

    def getAverageQuoteLength() {
        if (!quotes) return 0.0

        /* This is the loop-based implementation.
        def totalSize = 0
        for (Quote q in quotes) {
            totalSize += q.content.size()
        }

        return totalSize / quotes.size()
        */
        return quotes.sum { it.content.size() } / quotes.size()
    }

    def getAverageQuoteWordCount() {
        if (!quotes) return 0
        return quotes.sum { it.content.split(/\s+/).size() } / quotes.size()
    }

    def getOccurrencesOf(ch) {
        return quotes.inject(0) { count, quote -> count + quote.count(ch) }
    }
}

class QuoteAnalyzerResult {
    Number averageQuoteLength
    Number averageQuoteWordCount
    Number quoteCount
}
