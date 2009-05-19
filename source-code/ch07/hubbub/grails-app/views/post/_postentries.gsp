<div class="postImage">
    <g:if test="${post.user.profile.photo}">
        <img src="<g:createLink controller='image' action='renderImage' id='${post.user.userId}'/>"/>
    </g:if>
</div>
<div class="postEntry">
    <div class="postText">

        <a href="<g:createLink controller='user'
           action='profile' id='${post.user.userId}'/>">${post.user.userId}</a>
        ${post.content}

    </div>
    <div class="postDate">
        <h:dateFromNow date="${post.dateCreated}"/>
    </div>
</div>
 