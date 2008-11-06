
<html>
<head>
   <title>Roles</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <g:form action="create">
        Create New Role: <g:textField name="name"/>
        <g:submitButton name="create" value="Create"/>
    </g:form>

    <table>
        <g:each var="role" in="${roles}">
            <tr>
                <td>${role.id}</td>
                <td>${role.name}</td>
                <td><g:link action="delete" id="${role.id}">delete</g:link></td>
            </tr>

        </g:each>
    </table>


</body>



</html>