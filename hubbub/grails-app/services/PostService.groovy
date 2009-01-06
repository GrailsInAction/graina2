import com.manning.graina.hubbub.RemotePostService

class PostService implements RemotePostService {
    static expose = [ "rmi" ]

    long createPost(String username, String content) {
        def user = User.findByUserId(username)
        if (!user) {
            throw new RuntimeException("User not found.")
        }

        def post = createPost(user?.id, content)
        if (!post) {
            throw new RuntimeException("Create failed: ${post.errors}")
        }
        else {
            return post.id
        }
    }

    Post createPost(long userId, String content) {
        def user = User.get(userId)
        def post = null
        if (user) {
            post = new Post(content: content)
            user.addToPosts(post)
            user.save(flush: true)

            def m = content =~ /@(\w+)/
            if (m) {
                def targetUser = User.findByUserId(m[0][1])
                if (targetUser) {
                    new Reply(post: post, inReplyTo: targetUser).save()
                }
                else {
                    throw new RuntimeException("Reply-to user not found: ${m[0][1]}")
                }
            }
        }

        return post
    }
}
