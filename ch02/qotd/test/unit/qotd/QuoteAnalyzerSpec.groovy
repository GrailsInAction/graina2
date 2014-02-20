package qotd

import qotd.Quote
import spock.lang.*

class QuoteAnalyzerSpec extends Specification {
    @Shared quotes = [
            new Quote(author: "Peter Ledbrook", content: "Time waits for no man"),
            new Quote(author: "Glen Smith", content: "Groovy solves all problems"),
            new Quote(author: "Chuck Norris", content: "The roundhouse kick is my preferred debugging tool"),
            new Quote(author: "Peter Ledbrook", content: "Grails == fast web app development"),
            new Quote(author: "Glen Smith", content: "The Ashes belong to Oz"),
            new Quote(author: "Chuck Norris", content: "Know your enemy and you will fear nothing"),
            new Quote(author: "Anonymous", content: "Real Programmers Don't eat quiche"),
            new Quote(author: "Peter Ledbrook", content: "Internet cat pictures rule!") ]

    def "Total number of quotes"() {
        given: "An analyzer initialised with known quotes"
        def analyzer = new QuoteAnalyzer(quoteList)

        expect: "The analyzed quote count matches the number of input quotes"
        expected == analyzer.quoteCount

        where:
        quoteList    | expected
        []           | 0
        quotes       | 8 //quotes.size()
    }

    def "Number of quotes per author"() {
        given: "An analyzer initialised with known quotes"
        def analyzer = new QuoteAnalyzer(quoteList)

        expect: "The per-author quote count is correct"
        expected == analyzer.quoteCountPerAuthor

        where:
        quoteList    | expected
        []           | [:]
        quotes       | ["Peter Ledbrook": 3, "Glen Smith": 2, "Chuck Norris": 2, Anonymous: 1]
    }

    def "Average quote length"() {
        given: "An analyzer initialised with known quotes"
        def analyzer = new QuoteAnalyzer(quoteList)

        when: "I ask for the average quote length"
        def result = analyzer.averageQuoteLength

        then: "The average quote length is correctly reported by the analyzer"
        result == expected

        and: "The result is of type BigDecimal"
        result instanceof BigDecimal

        where:
        quoteList    | expected
        []           | 0
        quotes       | 31.75 //(21 + 26 + 50 + 34 + 22 + 41 + 33 + 27) / 8
    }

    def "Average word count per quote"() {
        given: "An analyzer initialised with known quotes"
        def analyzer = new QuoteAnalyzer(quoteList)

        expect: "The average quote length is correctly reported by the analyzer"
        expected == analyzer.averageQuoteWordCount

        where:
        quoteList    | expected
        []           | 0
        quotes       | 5.625 //(5 + 4 + 8 + 6 + 5 + 8 + 5 + 4) / 8
    }
}
