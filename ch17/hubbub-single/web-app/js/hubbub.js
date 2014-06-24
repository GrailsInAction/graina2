angular.module('Hubbub', ['restangular']).config(
    function(RestangularProvider) {
        RestangularProvider.setBaseUrl('/hubbub/api');
//        RestangularProvider.setRequestInterceptor(function(elem, operation, what) {
//            var retElem = elem;
//            if (operation === 'put') {
//                var wrapper = {};
//                wrapper[what.substring(0, what.length -1)] = elem;
//                retElem = wrapper;
//            }
//            return retElem;
//        });
    }
);

function PostsCtrl($scope, Restangular) {

    var postsApi = Restangular.all("posts");

    $scope.allPosts = [];

    $scope.refreshPosts = function() {
        postsApi.getList().then(function(newPostList) {
            $scope.allPosts = newPostList;
        }, function(errorResponse) {
            alert("Error on refreshing posts: " + errorResponse.status);
        });
    }

    $scope.$on('newPost', function() {
         $scope.refreshPosts();
    });

    $scope.$on('deletePost', function(event, postToDelete) {
        $scope.allPosts = _.filter($scope.allPosts, function(nextPost) {
            return nextPost.id != postToDelete.id
        });
    });

    $scope.refreshPosts();

}

function NewPostCtrl($scope, $rootScope, Restangular) {

    $scope.postContent = "";

    $scope.charsRemaining = function() {
        return 140 - $scope.postContent.length;
    }

    $scope.postInvalidLength = function() {
        return $scope.postContent.length == 0 || $scope.postContent.length > 140
    }

    $scope.newPost = function() {
        var postApi = Restangular.one("posts");
        var newPost = { message: $scope.postContent };
        postApi.post(null, newPost).then(function(response) {
            $rootScope.$broadcast("newPost", newPost);
            $scope.postContent = "";
        }, function(errorResponse) {
            alert("Error on creating post: " + errorResponse.status);
        });
    }
}

function EditPostCtrl($scope, $rootScope, Restangular) {

    $scope.isEditState = false;

    $scope.activate= function() {
        $scope.isEditState = true;
    }

    $scope.deactivate= function() {
        $scope.isEditState = false;
    }

    $scope.originalContent = $scope.post.message
    $scope.editedContent = $scope.post.message

    $scope.updatePost = function() {

        isEditState = false;

        $scope.post.message = $scope.editedContent;
        $scope.post.put().then(
            function() {
                $scope.isEditState = false;
            }, function(errorResponse) {
                $scope.post.message = $scope.originalContent; // reset back content
                alert("Error saving object:" + errorResponse.status);
            }
        );

    }

    $scope.deletePost = function() {

        isEditState = false;

        $scope.post.message = $scope.editedContent;
        $scope.post.remove().then(
            function() {
                $rootScope.$broadcast("deletePost", $scope.post);
            }, function(errorResponse) {
                alert("Error saving object:" + errorResponse.status);
            }
        );

    }
}

