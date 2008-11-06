
<html>
<head>
   <title>Resource</title>
</head>
<body>
    <div>${flash.message}</div>
    <g:hasErrors>
        <g:eachError><p>${it.defaultMessage}</p></g:eachError>
    </g:hasErrors>

    <fieldset>
        <legend>Create New Resource</legend>
    <g:form action="create">
        <label for="name">Name:</label> <g:textField name="name"/>
        <label for="description">Application:</label> <g:textField name="description"/>
        <label for="appId">Application:</label> <g:select name="appId" from="${au.gov.centrelink.itsecurity.rd.Application.list()}" optionKey="name" optionValue="name"/>
        <label for="resourceGroupId">Resource Group:</label> <g:select name="resourceGroupId" from="${au.gov.centrelink.itsecurity.rd.ResourceGroup.list()}" optionKey="id" optionValue="name"/>
        <label for="resourceRating">Resource Rating:</label> <g:textField name="resourceRating"/>
        <label for="credRating">Credential Rating:</label> <g:textField name="credRating"/>
        <label for="resourceOwnerId">Owner:</label> <g:select name="resourceOwnerId" from="${au.gov.centrelink.itsecurity.rd.ResourceOwner.list()}" optionKey="id" optionValue="ownerName"/>
        <label for="resourceTypeId">Type:</label> <g:select name="resourceTypeId" from="${au.gov.centrelink.itsecurity.rd.ResourceType.list()}" optionKey="type" optionValue="name"/>
        <g:submitButton name="create" value="Create"/>
    </g:form>
        </fieldset>

    <table>
        <g:each var="resource" in="${resources}">
            <tr>
                <td>${setFile.id}</td>
                <td>${setFile.name}</td>
                <td>${setFile.apps ? setFile.apps*.name : ""}</td>
                <td>${setFile.apps ? setFile.apps*.name : ""}</td>
                <td><g:link action="delete" id="${setFile.id}">delete</g:link></td>
            </tr>

        </g:each>

    </table>


</body>



</html>