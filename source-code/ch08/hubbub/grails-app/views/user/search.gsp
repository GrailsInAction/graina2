<html>
    <head>
        <title>Search Hubbub</title>
        <meta name="layout" content="main"/>
    </head>
    <body>

        <formset>
            <legend>Search for Friends</legend>

            <g:form action="results">
                <label for="userId">User Id</label>
                <g:textField name="userId" />
                
                <g:submitButton name="search" value="Search"/>
            </g:form>

        </formset>

    </body>
</html>
