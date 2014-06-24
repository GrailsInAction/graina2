<html>
    <head>
        <title>Timeline for ${user.profile ? user.profile.fullName : user.loginId}</title>
        <meta name="layout" content="main"/>
        <content tag="htmlAttrs">ng-app=Hubbub</content>
        <r:require module="core"/>
    </head>
    <body>


        <div id="newPost">

            <h3>
                What is ${ user.profile ? user.profile.fullName : user.loginId } hacking on right now?
            </h3>

      <form ng-controller="NewPostCtrl">

          <textarea id='postContent' ng-model="postContent"
                    ng-class="{'charsLow' : charsRemaining() < 12, 'charsOverflow' : charsRemaining() < 0}"></textarea>
          <button ng-click="newPost()" ng-disabled="postInvalidLength()">Post</button>
          <span id='charsRemaining'>{{charsRemaining()}}</span>

      </form>


        </div>

    <div class="allPosts" ng-controller="PostsCtrl" ng-cloak>

        <div class="postEntry" ng-repeat="post in allPosts" ng-controller="EditPostCtrl"
             ng-mouseenter="activate()"
             ng-mouseleave="deactivate()">

            <span ng-show="isEditState">
                <textarea class="inplacePostEdit" ng-model="editedContent"></textarea>
                <button ng-click="updatePost()">Update</button>
                <button ng-click="deletePost()">Delete</button>
            </span>

            <span ng-show="!isEditState">
                <div class="postText">{{post.message}}</div>
                <div class="postDate">{{post.published}} by {{post.user}}</div>
            </span>

        </div>

      </div>
    </body>
</html>
