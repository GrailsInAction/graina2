import org.codehaus.groovy.grails.commons.ApplicationHolder

class BootStrap {


    def init = {servletContext ->

        def samples = ['glen', 'peter', 'phil', 'jason']
        def userRole = new Role(authority: "ROLE_USER", description: "Registered user")
        def authenticateService = ApplicationHolder.application.mainContext.getBean("authenticateService")

        if (!User.list()) {
            samples.each { userId ->
                def user = new User(userId: userId, password: authenticateService.encodePassword("password"), profile: new Profile(fullName: userId, homepage: "http://www.${userId}.com/${userId}", email: "${userId}@${userId}.com"))
                userRole.addToPeople(user)
                if (user.validate()) {
                    println "Creating user ${userId}..."
                    user.profile.jabberAddress = "${userId}@decaf.local"
                    def url = this.class.getResource("/${userId}.jpg")
                    if (url) {
                        def image = new File(url.toURI()).readBytes()
                        println "Creating With custom photo"
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

        userRole.save()
    }

    def destroy = {
    }

} 