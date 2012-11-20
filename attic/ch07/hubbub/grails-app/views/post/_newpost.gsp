<div id="newPost">

    <input id="charLeft" name="charLeft" size="5" value="160" readonly="true"/>

    <h3>
        What is ${user.profile.fullName} hacking on right now?
    </h3>

    <g:if test="${flash.message}">
        <div class="flash">
            ${flash.message}
        </div>
    </g:if>

    <p>
       

        <g:form action="ajaxAdd">
            <g:textArea id='postContent' name="content" rows="3" cols="50" onkeydown="updateCounter()" /><br/>
            <g:submitToRemote value="Post"
                 url="[controller: 'post', action: 'addPostAjax']"
                 update="allPosts"
                 onSuccess="clearPost(e)"
                 onLoading="showSpinner(true)"
                 onComplete="showSpinner(false)"/>

             <g:hiddenField name="timelineToReturn" value="${timelineType}"/>
             <img id="spinner" style="display: none"
                src="<g:createLinkTo dir='/images' file='spinner.gif'/>"/>

              <!-- <a href="#" id="showHideUrlOld" onClick="return toggleTinyUrl()">Show TinyURL</a>  -->
                <a href="#" id="showHideUrl" onClick="Effect.toggle('tinyUrl', 'slide', { duration: 0.5 }); return false;">TinyURL</a>

                </g:form>


                <div id="tinyUrl" style="display:none;">
					<g:form>
						TinyUrl:
							<g:textField name="fullUrl" size="68"/>
							<g:submitToRemote action="tinyurl" onSuccess="addTinyUrl(e)" value="Make Tiny"/>
						</g:form>
				</div>

        

        <g:javascript>
            function clearPost(e) {
                $('postContent').value=''
            }
            function showSpinner(visible) {
                $('spinner').style.display = visible ? "inline" : "none"
            }



                function addTinyUrl(e) {
                    var tinyUrl = e.responseJSON.urls.small
                    $("postContent").value += tinyUrl
                    updateCounter()
                    toggleTinyUrl()
                }

            function updateCounter() {
                $("charLeft").value = 160 - $F("postContent").length
            }
            updateCounter();
        </g:javascript>

    </p>
</div>