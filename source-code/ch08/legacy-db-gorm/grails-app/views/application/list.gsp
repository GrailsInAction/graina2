
<html>
<head>
   <title>Applications</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <g:form action="create">
        Create New Application: <g:textField name="appName"/>
        <g:submitButton name="create" value="Create"/>
    </g:form>

    <table>
        <g:each var="app" in="${apps}">
            <tr>
                <td>${app.name}</td>
                <td><g:link action="delete" id="${app.name}">delete</g:link></td>
            </tr>

        </g:each>
    </table>


</body>



</html>