angular.module('Hubbub', ['restangular']).config(
    function(RestangularProvider) {
        RestangularProvider.setBaseUrl('/hubbub/api');
    }
);


function PostCtrl($scope, Restangular) {

    Restangular.setRequestInterceptor(function(elem, operation, what) {
        var retElem = elem;
        if (operation === 'post' || operation === 'put') {
            var wrapper = {};
            wrapper[what.substring(0, what.length -1)] = elem;
            retElem = wrapper;
        }
        return retElem;
    });

    $scope.isEditState = false;
    $scope.editedContent = $scope.post.content
    $scope.originalContent = $scope.post.content

    $scope.activate= function() {
        $scope.isEditState = true;
    }

    $scope.deactivate= function() {
        $scope.isEditState = false;
    }

    $scope.updatePost = function() {

        isEditState = false;

        $scope.post.content = $scope.editedContent;
        $scope.post.customPUT(null, null, null, { post: $scope.post }).then(
            function() {
                $scope.post.content = $scope.editedContent;
                $scope.isEditState = false;
            }, function(response) {
                $scope.post.content = $scope.originalContent; // reset back
                //$parent.currentError = "Error saving object:" + response.status;
                alert("Error saving object:" + response.status);
            }
        );

    }
}

function PostsCtrl($scope, Restangular) {

    $scope.maxTweets = 10;
    $scope.offset = 0;

    $scope.allPosts = [];

    $scope.postContent = "";
    $scope.currentError = "";

    $scope.fetchPosts = function() {
        var postsApi = Restangular.all("posts");
        postsApi.getList({ max: $scope.maxTweets, offset: $scope.offset }).then(function(fullList) {
            $scope.allPosts = $scope.allPosts.concat(fullList);
        }, function(response) {
            $scope.currentError = "Error fetching posts: " + response.status;
        });
    }

    $scope.newPost = function() {
        var postApi = Restangular.one("posts");
        var newPost = { post: { content: $scope.postContent }};
        var subElement;
        postApi.post(subElement, newPost).then(function() {
            $scope.offset = 0;
            $scope.allPosts = [];
            $scope.fetchPosts();
            $scope.postContent = "";
            $scope.currentError = "";
        }, function(response) {
            $scope.currentError = "Error on creating post: " + response.status;
        });
    }

    $scope.doSearch = function() {


            var filteredPosts = _.filter($scope.allPosts, function(nextPost) {
                    return nextPost.content.toLowerCase().indexOf($scope.searchTerm.toLowerCase()) > -1
                });
            $scope.allPosts = filteredPosts;
            $scope.currentError = "";

    }

    $scope.noCurrentErrors= function() {
        return $scope.currentError.length == 0;
    }

    $scope.charsRemaining = function() {
        return 140 - $scope.postContent.length;
    }

    $scope.tweetInvalidLength = function() {
        return $scope.postContent.length == 0 || $scope.postContent.length > 140
    }

    $scope.fetchNext = function() {
        $scope.offset += $scope.maxTweets;
        $scope.fetchPosts();
    }

    $scope.fetchPosts();

}
