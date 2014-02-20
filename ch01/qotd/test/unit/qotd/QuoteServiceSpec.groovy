package qotd

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(QuoteService)
class QuoteServiceSpec extends Specification {

    void "static quote service always returns quiche quote"() {

        when:
        Quote staticQuote = service.getStaticQuote()

        then:
        staticQuote.author == "Anonymous"
        staticQuote.content == "Real Programmers Don't eat quiche"

    }
}
