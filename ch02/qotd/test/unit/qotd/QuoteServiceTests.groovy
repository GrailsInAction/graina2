package qotd

import grails.test.mixin.*

@TestFor(QuoteService)
class QuoteServiceTests {

    void testStaticQuoteReturnsQuicheQuote() {

        Quote staticQuote = service.getStaticQuote()
        assertEquals("Anonymous", staticQuote.author)
        assertEquals("Real Programmers Don't eat quiche", staticQuote.content)

    }

}
