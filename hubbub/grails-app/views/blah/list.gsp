

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Blah List</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create">New Blah</g:link></span>
        </div>
        <div class="body">
            <h1>Blah List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="userId" title="User Id" />
                        
                   	        <g:sortableColumn property="password" title="Password" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${blahList}" status="i" var="blah">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${blah.id}">${fieldValue(bean:blah, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:blah, field:'userId')}</td>
                        
                            <td>${fieldValue(bean:blah, field:'password')}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Blah.count()}" />
            </div>
        </div>
    </body>
</html>
