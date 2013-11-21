<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Environment information</title>
    <style>
    pre {
        white-space: pre-wrap;
    }
    </style>
  </head>
  <body>
    <h2><g:link controller="quote" action="random">Main App</g:link></h2>
    <p>Running with Grails ${grails.util.Metadata.current.getGrailsVersion()}</p>
    <h2>Environment variables</h2>
    <h3>VCAP_APPLICATION</h3>
    <pre>${System.getenv("VCAP_APPLICATION")}</pre>
    <h3>VCAP_SERVICES</h3>
    <pre>${System.getenv("VCAP_SERVICES")}</pre>
    <h3>VCAP_APP_HOST</h3>
    <pre>${System.getenv("VCAP_APP_HOST")}</pre>
    <h3>VCAP_APP_PORT</h3>
    <pre>${System.getenv("VCAP_APP_PORT")}</pre>
  </body>
</html>
