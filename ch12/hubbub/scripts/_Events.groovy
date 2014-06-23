import org.codehaus.groovy.grails.test.spock.GrailsSpecTestType

eventAllTestsStart = {
    functionalTests << new GrailsSpecTestType("spock", "functional")
}
