<div id="sidebar">

        <h3>Profile</h3>

            <dl>
                <dt>User</dt>
                <dd>
                  <sec:userId/>
                </dd>
            </dl>
            <sec:isLoggedIn><g:link controller="login" action="signOut">Logout</g:link></sec:isLoggedIn>

        <h3>Stats</h3>

            <!-- Follower counts -->
            <dl>
                <!--
                <dt><a href='<g:createLink action="following" controller="friend" id="${user.userId}"/>'>
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
