<html>
    <head>
        <title>Global Timeline</title>
        <meta name="layout" content="main"/>
    </head>
    <body>

	<!--	
        <h3>
            Global Timeline
        </h3>
	-->
	
        <g:if test="${session.user}">
            <g:render template="newpost" model="[user: session.user, timelineType: 'global']"/>
        </g:if>

        <div id="allPosts">
            <g:render template="postentries" collection="${posts}" />
        </div>

        <g:paginate total="${postCount}"/>

    </body>
</html>
