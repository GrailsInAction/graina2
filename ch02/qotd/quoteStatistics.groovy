import com.grailsinaction.QuoteAnalyzer
import qotd.Quote

new Quote(
    author: "Peter Ledbrook",
    content: "Time waits for no man").save()
new Quote(
    author: "Glen Smith",
    content: "Groovy solves all problems").save(flush: true)

def analyzer = new QuoteAnalyzer(Quote.list())
try {
    def reportFile = new File("report.txt")
    reportFile.withPrintWriter { w ->
        w.println """\
Quote report
------------

Total: ${analyzer.quoteCount}

Number of quotes by author:
"""
        analyzer.quoteCountPerAuthor.each { String author, int count ->
            w.println "  " + author.padRight(20) + count
        }

    }

    println reportFile.text
}
catch (IOException ex) {
    println "Unable to write to the 'report.txt' file!"
}


