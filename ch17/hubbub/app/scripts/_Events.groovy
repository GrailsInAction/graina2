import org.codehaus.groovy.grails.test.spock.GrailsSpecTestType

eventCreateWarStart = { String warName, File stagingDir ->
    println "About to package the WAR file from ${stagingDir}"

    def exclusions = ["commons-logging",
                          "h2",
                          "hsqldb",
                          "spring-test",
                          "junit"]

    ant.delete {
        fileset(dir: new File(stagingDir, "WEB-INF/lib").canonicalPath) {
            for (basename in exclusions) {
                include name: "${basename}-*.jar"
            }
        }
    }

    /* Uncomment to get thin WAR with shared libs
    if (grailsEnv == "production") {
        def sharedLibsDir = "${grailsSettings.projectWorkDir}/sharedLibs"

        ant.mkdir dir: sharedLibsDir
        ant.move todir: sharedLibsDir, {
            fileset dir: "${stagingDir}/WEB-INF/lib", {
                include name: "*.jar"
                exclude name: "grails-*"
            }
        }

        println "Shared JARs put into ${sharedLibsDir}"
    } 
    */
    
}

eventAllTestsStart = {
    functionalTests << new GrailsSpecTestType("spock", "functional")
}
