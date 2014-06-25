grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

/*
grails.project.fork = [
    // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
    //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

    // configure settings for the test-app JVM, uses the daemon by default
    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
    // configure settings for the run-app JVM
    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the run-war JVM
    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
    // configure settings for the Console UI JVM
    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]
*/

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // specify dependency exclusions here; for example, uncomment this to disable ehcache:
        // excludes 'ehcache'
    }
    log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    checksums true // Whether to verify checksums on resolve
    legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

    repositories {
        inherits true // Whether to inherit repository definitions from plugins

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()

        // For the Searchable plugin's 'compass' dependency
        mavenRepo "http://repo.grails.org/grails/core"
        mavenRepo "http://repo.spring.io/milestone/"

        // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }

    def gebVersion = "0.9.2"
    def seleniumVersion = "2.41.0"

    dependencies {
//        compile "org.apache.lucene:lucene-spellchecker:2.4.1"
        compile "org.apache.activemq:activemq-core:5.7.0", {
            exclude "spring-context"
        }

        test "org.gebish:geb-spock:$gebVersion"
        test "com.github.groovy-wslite:groovy-wslite:0.7.2"

        test "org.seleniumhq.selenium:selenium-support:$seleniumVersion"
        test "org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion"
//        test "org.seleniumhq.selenium:selenium-htmlunit-driver:$seleniumVersion", {
//            exclude "xml-apis"
//        }
//        test "org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion"

        // next four lines are required if you're using embedded/ha *and* you want the webadmin available
        /*compile(group:"org.neo4j.app", name:"neo4j-server", version:"1.8")
        runtime(group:"org.neo4j.app", name:"neo4j-server", version:"1.8", branch:"static-web")
        runtime('com.sun.jersey:jersey-server:1.9')
        runtime('com.sun.jersey:jersey-core:1.9')*/
    }

    plugins {
        // plugins for the build system only
        build ":tomcat:7.0.47"

        // plugins for the compile step
        compile ":scaffolding:2.0.1"
        compile ":mail:1.0.1"
        compile ':cache:1.1.1', ":cache-ehcache:1.0.1"
        compile ":jms:1.3"
        compile ":quartz:1.0.2"

        // Doesn't work with neo4j plugin due to conflicting Lucene dependencies.
//        compile ":searchable:0.6.6"

        compile ":platform-core:1.0.0"

        compile ":spring-security-core:2.0-RC2", ":spring-security-ui:1.0-RC1"
        compile ":spring-security-twitter:0.6.2"

        compile ":redis:1.3.3"
        compile ":mongodb:1.3.0"
        compile ":neo4j:1.0.1"

        // plugins needed at runtime but not for compilation
        runtime ":hibernate:3.6.10.6" // or ":hibernate4:4.1.11.6"
        runtime ":database-migration:1.3.8"
        runtime ":jquery:1.10.2.2"
        runtime ":resources:1.2.1"
        // Uncomment these (or add new ones) to enable additional resources capabilities
        //runtime ":zipped-resources:1.0.1"
        //runtime ":cached-resources:1.1"
        //runtime ":yui-minify-resources:0.1.5"

        runtime ":navigation:1.3.2"

        test ":geb:$gebVersion"
        test ":dumbster:0.2"
    }
}
