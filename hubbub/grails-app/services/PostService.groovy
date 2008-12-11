/**
 * Created by IntelliJ IDEA.
 * User: pal20
 * Date: 09-Dec-2008
 * Time: 16:40:52
 * To change this template use File | Settings | File Templates.
 */

class PostService {
    static transactional = false

    Post createPost(long userId, String content) {
        def user = User.get(userId)
        def post = null
        if (user) {
            post = new Post(content: content)
            user.addToPosts(post)
            user.save()

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
