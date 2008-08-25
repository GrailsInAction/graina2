
<div id="sidebar">

        <h3>Profile</h3>

            <dl>
                <dt>User</dt>
                <dd>${session.user.userId}</dd>
            </dl>


        <h3>Stats</h3>

            <!-- Follower counts -->
            <dl>
                <dt><a href="<g:createLink action="following" controller="friend" id="${session.user.userId}"/>">
                    Following
                    </a>
                </dt>
                <dd>${followingCount}</dd>
                <dt><a href="<g:createLink action="followers" controller="friend" id="${session.user.userId}"/>">
                    Followers
                    </a>
                </dt>
                <dd>${followersCount}</dd>
                <dt>Posts</dt>
                <dd>${postsCount}</dd>
            </dl>

        <h3>Mates</h3>
        
            <!-- People I am following -->
            <div id="friendsThumbnails">
                <g:each var="followUser" in="${following}">
                    <img src="<g:createLink action="tiny" controller="image" id="${followUser.userId}"/>" alt="${followUser.userId}"/>
                </g:each>
            </div>


</div>
