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

    dependencies {
        test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
    }

    plugins {
        compile ":mail:1.0.1", ":greenmail:1.3.4"
        compile ':cache:1.0.1'
        compile ":cache-ehcache:1.0.0"
        compile ":searchable:0.6.4"
        test ':dumbster:0.2'

        runtime ":hibernate:$grailsVersion"
        runtime ":jquery:1.7.2"
        runtime ":resources:1.2"
        runtime ":navigation:1.3.2"


        build ":tomcat:$grailsVersion"

        runtime ":database-migration:1.1"

        test ":spock:0.7", {
            exclude "spock-grails-support"
        }
    }
}
