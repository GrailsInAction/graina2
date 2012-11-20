package com.grailsinaction

class PostException extends RuntimeException {
    String message
    Post post
}

class PostService {

    boolean transactional = true
    
    def int MAX_ENTRIES_PER_PAGE = 10

    Post createPost(String userId, String content) {
        def user = User.findByUserId(userId)
        if (user) {
            def post = new Post(content: content)
            user.addToPosts(post)
            if (user.save()) {
                return post
            } else {
                throw new PostException(message: "Invalid or empty post", post: post)
            }
        }
        throw new PostException(message: "Invalid User Id")

    }
    
    def getGlobalTimelineAndCount(params) {

        if (!params.max)
            params.max = MAX_ENTRIES_PER_PAGE

        def posts = Post.list(params)
        def postCount = Post.count()
        [ posts, postCount ]
    }
    
    def getUserTimelineAndCount(userId, params) {

        if (!params.max) 
            params.max = MAX_ENTRIES_PER_PAGE
        
        if (!params.offset)
            params.offset = 0

        
        def user = User.findByUserId(userId)
        def idsToInclude = user.following.collect { u -> u.id }
        idsToInclude.add(user.id)
        def query = "from Post as p where p.user.id in (" + idsToInclude.join(",") + ")"
        println "Query is ${query}"
        def posts = Post.findAll( query + " order by p.dateCreated desc", 
                [ max: params.max, offset: params.offset ])
        def postCount = Post.findAll(query).size() // TODO use count criteria
        println "Post count is ${posts?.size()}"
        return [ posts, postCount ]
        
    }


    def getUserPosts(userId, params) {

        if (!params.max)
            params.max = MAX_ENTRIES_PER_PAGE

        def user = User.findByUserId(userId)
        def postCount = Post.countByUser(user)
        def posts = Post.findAllByUser(user, params)
        return [ posts, postCount ]
        
    }





}
