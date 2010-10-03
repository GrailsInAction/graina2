tomcat.mgr.username = "overlord"
tomcat.mgr.password = "Kr@kat04"

grails.project.dependency.resolution = {
    inherits "global"
    log "warn"
    
    repositories {
        grailsHome()
        grailsPlugins()
        grailsCentral()
        mavenCentral()
    }
    dependencies {
        runtime "postgresql:postgresql:8.3-603.jdbc3", "org.apache.activemq:activemq-all:5.4.1"
    }
}