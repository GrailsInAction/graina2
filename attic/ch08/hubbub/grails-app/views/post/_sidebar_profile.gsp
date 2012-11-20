<div id="sidebar">

        <h3>Profile</h3>

            <dl>
                <dt>User</dt>
                <dd>
                  ${session.user.userId}
                  <%--
                  <g:loggedInUserInfo field="userId">Guest</g:loggedInUserInfo><g:isLoggedIn>
                  (<g:link controller="logout" action="index">sign out</g:link>)</g:isLoggedIn>
                  --%>
                </dd>
            </dl>
			<g:link controller="login" action="logout">Logout</g:link>


        <h3>Stats</h3>

            <!-- Follower counts -->
            <dl>
                <!--
                <dt><a href='<g:createLink action="following" controller="friend" id="${session.user.userId}"/>'>
                    Following
                    </a>
                </dt>
                -->
                <dt>Posts</dt>
                <dd>${postCount}</dd>
            </dl>

        <h3>Mates</h3>
        
            <!-- People I am following -->
            
            <div id="friendsThumbnails">
                <g:each var="followUser" in="${user.following}">
                    <img src="<g:createLink action="tiny" controller="image" id="${followUser.userId}"/>" alt="${followUser.userId}"/>
                </g:each>
            </div>
            
            
<%--
    <div id="friendsThumbnails2">
                <g:each var="followUser" in="${following}">
                    <gui:toolTip text="${followUser.userId}">
                        <h:tinyThumbnail userId="${followUser.userId}"/>
                    </gui:toolTip>   
                </g:each>
            </div>

--%>


</div>
