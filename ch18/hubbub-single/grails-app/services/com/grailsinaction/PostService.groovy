package com.grailsinaction

import grails.transaction.Transactional

class PostException extends RuntimeException {
    String message
    Post post
}

@Transactional
class PostService {

    Post createPost(String loginId, String content) {
        def user = User.findByLoginId(loginId)
        if (user) {
            def post = new Post(content: content)
            user.addToPosts(post)
            if (!post.validate() || !user.save(flush: true)) {
                throw new PostException(message: "Invalid or empty post", post: post)
            }

            def m = content =~ /@(\w+)/
            if (m) {
                def targetUser = User.findByLoginId(m[0][1])
                if (targetUser) {
                    new Reply(post: post, inReplyTo: targetUser).save()
                }
                else {
                    throw new PostException(message: "Reply-to user not found", post: post)
//                    throw new Exception("Reply-to user not found")
                }
            }

            return post
        }

        throw new PostException(message: "Invalid User Id")
    }

}
