grails.servlet.version = "2.5" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6

grails.project.dependency.resolution = {
    inherits "global"
    log "error"
    checksums true

    repositories {
        inherits true

        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
    }

    def gebVersion = "0.9.0"
    def seleniumVersion = "2.32.0"

    dependencies {
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
        test "org.gebish:geb-spock:$gebVersion"

        test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
//        test "org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion", {
//            exclude "xml-apis"
//        }
//        test "org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion"
        test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.7.2"
        runtime ":resources:1.2"
        runtime ":navigation:1.3.2"

        build ":tomcat:$grailsVersion"
        test ":functional-test-development:0.9.4", {
            exclude "hibernate"
        }

        runtime ":database-migration:1.1"

        compile ':cache:1.0.1'

        test ":spock:0.7", ":geb:$gebVersion", {
            exclude "spock-grails-support"
        }
    }
}
