
<html>
<head>
   <title>Resource Owner</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <fieldset>
        <legend>Create New Resource Owner</legend>
    <g:form action="create">
        <label for="adminUserId">Admin User Id:</label> <g:textField name="adminUserId"/>
        <label for="ownerName">Owner Name:</label> <g:textField name="ownerName"/>
        <label for="description">Description:</label> <g:textField name="description"/>
        <g:submitButton name="create" value="Create"/>
    </g:form>
        </fieldset>

    <table>
        <g:each var="owner" in="${resourceOwners}">
            <tr>
                <td>${owner.id}</td>
                <td>${owner.ownerName}</td>
                <td><g:link action="delete" id="${owner.id}">delete</g:link></td>
            </tr>

        </g:each>

    </table>


</body>



</html>