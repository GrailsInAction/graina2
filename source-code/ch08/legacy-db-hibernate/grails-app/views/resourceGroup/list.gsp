
<html>
<head>
   <title>Resource Group</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <fieldset>
        <legend>Create New Resource Group</legend>
    <g:form action="create">
        <label for="name">Name:</label> <g:textField name="name"/>
        <label for="appId">Application:</label> <g:select name="appId" from="${au.gov.centrelink.itsecurity.rd.Application.list()}" optionKey="name" optionValue="name"/>
        <label for="resourceOwnerId">Owner:</label> <g:select name="resourceOwnerId" from="${au.gov.centrelink.itsecurity.rd.ResourceOwner.list()}" optionKey="id" optionValue="ownerName"/>
        <g:submitButton name="create" value="Create"/>
    </g:form>
        </fieldset>

    <table>
        <g:each var="group" in="${resourceGroups}">
            <tr>
                <td>${group.id}</td>
                <td>${group.name}</td>
                <td>${group.apps ? group.apps*.name : ""}</td>
                <td><g:link action="delete" id="${group.id}">delete</g:link></td>
            </tr>

        </g:each>

    </table>


</body>



</html>