<html>
    <head>
        <title>Timeline for ${user.profile ? user.profile.fullName : user.loginId}</title>
        <meta name="layout" content="main"/>
        <content tag="htmlAttrs">ng-app="Hubbub"</content>
        <r:require module="core"/>
        <style>
            .lowChars { background-color: yellow !important; }
            .overflowChars { background-color: red !important; }
        </style>
    </head>
    <body>

      <div ng-controller="PostsCtrl">


        <div id="newPost">

            <h3>
                What is ${ user.profile ? user.profile.fullName : user.loginId } hacking on right now?
            </h3>



  <div class="flash" ng-hide="noCurrentErrors()">
      {{currentError}}
  </div>


  <p>

      <form>

          <textarea id='postContent' ng-model="postContent" ng-class="{'lowChars' : charsRemaining() < 12, 'overflowChars' : charsRemaining() < 0}"></textarea>
          <span>{{charsRemaining()}}</span>
          <button ng-click="newPost()" ng-disabled="tweetInvalidLength()">Post</button>

      </form>

  </p>
</div>


<div class="searchSection">
  <form>
      <input type="text" ng-model="searchTerm" ng-change="doSearch()"/>
      <button ng-click="doSearch()">Search</button>
  </form>

</div>

        <div class="allPosts" ng-cloak>


            <div class="postEntry" ng-repeat="post in allPosts" ng-controller="PostCtrl"
                 ng-mouseenter="activate()"
                 ng-mouseleave="deactivate()">
                <span ng-show="isEditState">

                    <div class="postText"><input type="text" ng-model="editedContent"></div>
                    <div class="postDate">{{post.published}} by {{post.user}}</div>
                    <button ng-click="updatePost()">Update</button>
                </span>
                <span ng-show="!isEditState">
                    <div class="postText">{{post.content}}</div>
                    <div class="postDate">{{post.published}} by {{post.user}}</div>
                </span>
            </div>

            <span ng-click="fetchNext()" style="color: blue; cursor: pointer;">Fetch next {{maxTweets}} posts</span>
            <%--
            <g:each in="${user.posts}" var="post">
                <div class="postEntry">
                    <div class="postText">${post.content}</div>
                    <div class="postDate">${post.dateCreated}</div>
                </div>
            </g:each>
            --%>
        </div>

      </div>
    </body>
</html>
