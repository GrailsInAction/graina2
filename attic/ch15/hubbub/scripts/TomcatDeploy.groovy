scriptEnv = "production"

includeTargets << grailsScript("_GrailsWar")

target(deploy: "Deploys Hubbub's WAR to Tomcat") {
    depends(war)

    Authenticator.setDefault([
            getPasswordAuthentication: {-> new PasswordAuthentication(
                    buildConfig.tomcat.mgr.username,
                    buildConfig.tomcat.mgr.password.toCharArray()) }
    ] as Authenticator)

    def url = "http://localhost:8080/manager/deploy?war="
    url += warName
    url += "&path=/hubbub&update=true"
    url = new URL(url)

    def response = url.text
    if (response.startsWith("OK")) {
        println "Application deployed successfully!"
        return 0
    }
    else {
        println "Application deployment failed: $response"
        return 1
    }
}

setDefaultTarget("deploy")
