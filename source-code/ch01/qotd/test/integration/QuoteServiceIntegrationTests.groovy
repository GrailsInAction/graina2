import grails.test.*

class QuoteServiceIntegrationTests extends GrailsUnitTestCase {

    def quoteService

    void testStaticQuote() {
        def staticQuote = quoteService.getStaticQuote()
        assertEquals("Anonymous", staticQuote.author)
        assertEquals("Real Programmers Don't eat Quiche", staticQuote.content)
    }
	
}
