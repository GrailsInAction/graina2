package com.grailsinaction

class PostException extends RuntimeException {
    String message
    Post post
}

class PostService {
    static transactional = true

    Post createPost(String loginId, String content) {
        def user = User.findByLoginId(loginId)
        if (user) {
            def post = new Post(content: content)
            user.addToPosts(post)

            if (post.validate() && user.save()) {
                return post
            }
            else {
                throw new PostException(message: "Invalid or empty post", post: post)
            }
        }

        throw new PostException(message: "Invalid login ID")
    }
}
