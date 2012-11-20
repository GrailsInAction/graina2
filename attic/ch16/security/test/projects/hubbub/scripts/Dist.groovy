includeTargets << grailsScript("_GrailsWar")
includeTargets << grailsScript("_GrailsDocs")

target(createDist: "Creates a distribution of Hubbub") {
    depends(docs, war)

    zipFile = "${basedir}/hubbub.zip"

    createZip()
}

target(createZip: "Packages the required files into a zip") {
    def warFile = new File(warName)
    ant.zip(destfile: zipFile) {
        zipfileset(dir: "${basedir}/src/templates/bin", prefix: "bin")
        zipfileset(dir: "${grailsHome}/lib", prefix: "lib") {
            include(name: "jetty*.jar")
        }
        zipfileset(dir: "${basedir}/docs", prefix: "docs")
        fileset(dir: warFile.parent, includes: warFile.name)
    }
}

setDefaultTarget("createDist")
