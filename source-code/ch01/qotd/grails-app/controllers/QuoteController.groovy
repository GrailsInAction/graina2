class QuoteController {

	def scaffold = true

	def quoteService

	def random = {
		def randomQuote = quoteService.getRandomQuote()
		[ quote : randomQuote ]
	}
	
	def ajaxRandom = {
			def randomQuote = quoteService.getRandomQuote()
			response.outputStream << "<q>${randomQuote.content}</q>" +
						"<p>${randomQuote.author}"
	}
	

}

	
