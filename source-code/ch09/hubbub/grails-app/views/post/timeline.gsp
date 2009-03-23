<html>
    <head>
        <title>MyTimeline for ${user.profile.fullName}</title>
        <%-- <meta name="layout" content="main"/> --%>
        <g:if test="${user.profile.skin}">
            <link rel="stylesheet" href="<g:createLinkTo dir='css' file='${user.profile.skin}.css'/>"/>
        </g:if>
    </head>
    <body>

	<!--
        <h3>
            Personal Timeline for ${user.profile.fullName}
        </h3>
	-->

        <g:if test="${session.user}">
            <g:render template="newpost" model="[user: user, timelineType: 'mytimeline']"/>
        </g:if>

        <div id="allPosts">
            <g:render template="postentries" collection="${posts}" />
        </div>

        <g:paginate total="${postCount}"/>

    </body>
</html>
