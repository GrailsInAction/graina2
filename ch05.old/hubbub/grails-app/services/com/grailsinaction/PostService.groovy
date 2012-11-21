package com.grailsinaction

class PostException extends RuntimeException {
    String message
    Post post
}

class PostService {

    boolean transactional = true

    Post createPost(String userId, String content) {
        def user = User.findByUserId(userId)
        if (user) {
            def post = new Post(content: content)
            user.addToPosts(post)
            if (post.validate() && user.save()) {
                return post
            } else {
                throw new PostException(message: "Invalid or empty post", post: post)
            }
        }
        throw new PostException(message: "Invalid User Id")

    }
}
