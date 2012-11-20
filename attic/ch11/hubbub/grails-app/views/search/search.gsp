<html>
    <head>
        <title>Find A Post</title>
        <meta name="layout" content="main"/>
    </head>
    <body>

        <h1>Search</h1>

        <g:form>
            <g:textField name="q" value="${params.q}"/>
            <g:select name="max" from="${[1, 5, 10, 50]}" value="${params.max ?: 10}" />
            <g:submitButton name="search" value="Search"/>
        </g:form>

        <hr/>

        <g:if test="${searchResult?.results}">
            <g:each var="result" in="${searchResult.results}" status="hitNum">

                <div class="searchPost">
                    <div class="searchFrom">
                        From
                        <g:link controller="users" action="${result.user.userId}">
                            ${result.user.userId}
                        </g:link>
                        ...
                    </div>
                    <div class="searchContent">
                        ${searchResult.highlights[hitNum]}
                    </div>
                </div>

            </g:each>

        </g:if>

    </body>
</html>
