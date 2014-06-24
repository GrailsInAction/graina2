<%@ page import="grails.plugin.searchable.internal.util.StringQueryUtils" %>
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
            <g:if test="${session.user}">
                Just My Stuff:
                <g:checkBox name="justMine" value="${params.justMine}"/>
            </g:if>
            <g:submitButton name="search" value="Search"/>
        </g:form>
        <hr/>
        <g:if test="${searchResult?.suggestedQuery}">
        <p>Did you mean
        <g:link controller="search" action="search" params="[q: searchResult.suggestedQuery]">
                ${StringQueryUtils.highlightTermDiffs(params.q.trim(), searchResult.suggestedQuery)}
        </g:link>?
        </g:if>

        <g:if test="${searchResult}">
        <hr/>
        Displaying hits
        <b>${searchResult.offset+1}-${Math.min(searchResult.offset + searchResult.max, searchResult.total)}</b> of
        <b>${searchResult.total}</b>:
        <g:set var="totalPages" value="${Math.ceil(searchResult.total / searchResult.max)}"/>
        <g:if test="${totalPages == 1}">
            <span class="currentStep">1</span>
        </g:if>
        <g:else>
            <g:paginate controller="search" action="search" 
                params="[q: params.q]"
                total="${searchResult.total}" 
                prev="&lt; previous" next="next &gt;"/>
        </g:else>
        <p/>
        </g:if>
        
        <g:if test="${searchResult?.results}">
            <g:each var="result" in="${searchResult.results}" status="hitNum">
                <div class="searchPost">
                    <div class="searchFrom">
                        From
                        <g:link controller="users" action="${result.user.loginId}">
                            ${result.user.loginId}
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

