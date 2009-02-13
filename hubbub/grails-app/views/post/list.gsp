<html>
    <head>
        <title>New Post</title>
		<g:javascript library="prototype" />
    </head>
    <body>
        <div id="newPost">

            <input id="charLeft" name="charLeft" size="5" value="160" readonly="true"/>

            <h3>
                What are you hacking on right now?
            </h3>


            <p>
                <g:form name="postMessage" action="add">
                    <textarea id='postContent' name="postContent" onkeydown="updateCounter()" rows="3" cols="50"></textarea>
                <g:submitButton name="post" value="Post"/>
                </g:form>
            </p>
        </div>

        <g:javascript>

		function updateCounter() {
			$("charLeft").value = 160 - $F("postContent").length
		}
		updateCounter();

		</g:javascript>

        <div id="allPosts">

            <g:each var="post" in="${allPosts}">

                <div class="postImage">
                   <img src="<g:createLink action="show" controller="image" id="${post.user.userId}"/>" alt="${post.user.userId}"/>
                </div>
                <div class="postEntry">
                    <div class="postText">

                        <a href="<g:createLink action="show" id="${post.user.userId}"/>">${post.user.userId}</a>
                        ${post.content}

                    </div>
                    <div class="postDate">
                        <g:dateFromNow date="${post.created}"/>
                    </div>
                </div>

            </g:each>

        </div>

    </body>
</html>
