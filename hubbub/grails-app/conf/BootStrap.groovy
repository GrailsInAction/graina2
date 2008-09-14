class BootStrap {


    def init = {servletContext ->

        def samples = ['glen', 'peter', 'phil', 'jason']

        if (!User.list()) {
            samples.each { userId ->
                def user = new User(userId: userId, password: "password".encodeAsSha1(), profile: new Profile(fullName: userId, homepage: "http://www.${userId}.com/${userId}", email: "${userId}@${userId}.com"))
                if (user.validate()) {
                    println "Creating user ${userId}..."
                    def url = this.class.getResource("/${userId}.jpg")
                    if (url) {
                        def image = new File(url.toURI()).readBytes()
                        println "Creatig With custom photo"
                        user.profile.photo = image
                    }
                    user.save(flush:true)
					def tag = new Tag(name: "grails", user: user).save()
                    def post = new Post(content: "A post from ${userId}", user: user, tag: tag).save()
                    
                    post.addToTags(tag)
                    user.addToPosts(post)
                } else {
                    println("\n\n\nError in account bootstrap for ${userId}!\n\n\n")
                    user.errors.each {err ->
                        println err
                    }
                }
            }
            samples.each { userId ->
                println "Searching for user ${userId}"
                def user = User.findByUserId(userId)
                println "User is ${user}"
                def others = samples.findAll { it != userId }
                others.each { otherId ->
                    def other = User.findByUserId(otherId)
                    user.addToFollowers(other)
                    other.addToFollowing(user)
                }

            }
            def loner = new User(userId: 'loner', password: "password".encodeAsSha1()).save()
        }
    }

    def destroy = {
    }

} 