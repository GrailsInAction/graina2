<html>
    <head>
        <title>Posts for ${targetUser.profile.fullName}</title>
        <meta name="layout" content="main"/>
        <g:if test="${user.profile.skin}">
            <link rel="stylesheet" href="<g:createLinkTo dir='css' file='${user.profile.skin}.css'/>"/>
        </g:if>
    </head>
    <body>

	<!--
        <h3>
            Personal Posts for ${targetUser.profile.fullName}
        </h3>
	-->

        <g:if test="${ targetUser == user }">
            <g:render template="newpost" model="[user: user, timelineType: 'myposts']"/>
        </g:if>

        <div id="allPosts">
            <g:render template="postentries" collection="${posts}" />
        </div>

        
        <g:paginate total="${postCount}"/>
        
    </body>
</html>
