
<html>
<head>
   <title>Resource Type</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <fieldset>
        <legend>Create New Resource Type</legend>
    <g:form action="create">
        <label for="type">Resource Type:</label> <g:textField name="type"/>
        <label for="name">Name:</label> <g:textField name="name"/>
        <label for="description">Description:</label> <g:textField name="description"/>
        <g:submitButton name="create" value="Create"/>
    </g:form>
        </fieldset>

    <table>
        <g:each var="type" in="${resourceTypes}">
            <tr>
                <td>${type.type}</td>
                <td>${type.name}</td>
                <td>${type.description}</td>
                <td><g:link action="delete" id="${type.type}">delete</g:link></td>
            </tr>

        </g:each>

    </table>


</body>



</html>