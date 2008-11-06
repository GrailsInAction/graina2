
<html>
<head>
   <title>App Users</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <fieldset>
        <legend>Create New Application User</legend>
    <g:form action="create">
        <label for="userId">User Id:</label> <g:textField name="userId"/>
        <label for="id">Application:</label> <g:select name="id" from="${au.gov.centrelink.itsecurity.rd.Application.list()}" optionKey="name" optionValue="name"/>
        <label for="secRole">Security Role:</label> <g:select name="secRole" from="${au.gov.centrelink.itsecurity.rd.SecurityRole.listOrderByName()}" optionKey="id" optionValue="name"/>
        <label for="assuranceRating">Assurance Rating:</label> <g:textField name="assuranceRating"/>
        <label for="serviceName">Service Name:</label> <g:textField name="serviceName"/> 
        <g:submitButton name="create" value="Create"/>
    </g:form>
        </fieldset>

    <table>
        <g:each var="appUser" in="${appUsers}">
            <tr>
                <td>${setManager.userId}</td>
                <td>${setManager.role.name}</td>
                <td><g:link action="delete" id="${setManager.userId}">delete</g:link></td>
            </tr>

        </g:each>

    </table>


</body>



</html>