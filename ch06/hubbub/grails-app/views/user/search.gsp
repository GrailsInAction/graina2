<html>
<head>
    <title>Search Hubbub</title>
    <meta name="layout" content="main"/>
</head>
<body>
    <formset>
        <legend>Search for Friends</legend>
        <g:form action="results">
            <label for="loginId">User Id</label>
            <g:textField name="loginId" />
            <g:submitButton name="search" value="Search"/>
        </g:form>
    </formset>
    <g:textField name="loginId"/>
</body>
</html>
