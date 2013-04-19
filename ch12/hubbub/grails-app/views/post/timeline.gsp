<html>
    <head>
        <title>Timeline for ${user.profile ? user.profile.fullName : user.loginId}</title>
        <meta name="layout" content="main"/>
        <g:javascript library="jquery"/>
        <g:if test="${user.profile?.skin}">
            <g:external uri="/css/${user.profile.skin}.css"/>
        </g:if>
        <g:javascript>
            function clearPost(e) {
                $('#postContent').text('');
            }
            function showSpinner(visible) {
                if (visible) $('#spinner').show();
                else $('#spinner').hide();
            }
        </g:javascript>
        
    </head>
    <body>

        <div id="newPost">

            <h3>
                What is ${ user.profile ? user.profile.fullName : user.loginId } hacking on right now?
            </h3>

            <g:if test="${flash.message}">
                <div class="flash">
                    ${flash.message}
                </div>
            </g:if>

            <p>
                <g:form action="ajaxAdd">
                    <g:textArea id='postContent' name="content" rows="3" cols="50"/><br/>
                    <g:submitToRemote value="Post"
                         url="[controller: 'post', action: 'addPostAjax']"
                         update="allPosts" onSuccess="clearPost(e)"
                         onLoading="showSpinner(true)" onComplete="showSpinner(false)"/>
                    <a href="#" id="showHideUrl" onclick="toggleTinyUrl(); return false;">
                        Show TinyURL
                    </a>
                    <g:img id="spinner" style="display: none" uri="/images/spinner.gif"/>
                </g:form>

                <r:script disposition="head">
                function toggleTinyUrl() {
                    var toggleText = $('#showHideUrl');
                    if ($('#tinyUrl').is(':visible')) {
                        $('#tinyUrl').slideUp(300);
                        toggleText.innerText = 'Hide TinyURL';
                    } else {
                        $('#tinyUrl').slideDown(300);
                        toggleText.innerText = 'Show TinyURL';
                    }
                }
                </r:script>
            </p>
        </div>

        <div id="tinyUrl" style="display:none;">
           <g:formRemote name="tinyUrlForm" url="[action: 'tinyurl']" onSuccess="addTinyUrl(data);">
              TinyUrl: <g:textField name="fullUrl"/>
              <g:submitButton name="submit" value="Make Tiny"/>
           </g:formRemote>

            <g:javascript>
            function addTinyUrl(data) {
                var tinyUrl = data.urls.small;
                var postBox = $("#postContent")
                postBox.val(postBox.val() + tinyUrl);
                toggleTinyUrl();

                // Clear the URL text field once the form has been submitted and hidden.
                $("#tinyUrl input[name='fullUrl']").val('');
            }
            </g:javascript>
           
        </div>

        <div class="allPosts">
            <g:each in="${user.posts}" var="post">
                <div class="postEntry">
                    <div class="postText">${post.content}</div>
                    <div class="postDate">${post.dateCreated}</div>
                </div>
            </g:each>
        </div>

    </body>
</html>
