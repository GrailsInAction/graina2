import com.grailsinaction.*
import grails.util.Environment

class BootStrap { 
    def springSecurityService

    def init = { servletContext ->

        switch (Environment.current) {

            case Environment.DEVELOPMENT:
                createAdminUserIfRequired()
                createTestingUsers()
                break;

            case Environment.PRODUCTION:
                createAdminUserIfRequired()
                if (!Role.findByAuthority("ROLE_USER")) {
                    def userRole = new Role(
                            authority: "ROLE_USER",
                            description: "Registered user").save()
                }
                break;

        }
        
    }


    def destroy = {
    }

    void createAdminUserIfRequired() {
        if (!User.findByUserId("admin")) {
            println "Fresh Database. Creating ADMIN user."
            def profile = new Profile(email: "admin@yourhost.com")
            def user = new User(
                    userId: "admin",
                    password: springSecurityService.encodePassword("secret"), profile: profile).save()
            
            def role = Role.findByAuthority("ROLE_ADMIN")
            if (!role) {
                role =  new Role(authority: "ROLE_ADMIN", description: "Administrator").save()
            }
            
            UserRole.create user, role
        } else {
            println "Existing admin user, skipping creation"
        }
    }


    void createTestingUsers()  {

        def samples = [
                       'chuck_norris' : [ fullName: 'Chuck Norris', bio: 'Can kill two stones with one bird' ],
                       'glen' : [ fullName: 'Glen Smith', jabberAddress: 'glen@decaf.local' ],
                       'peter' : [ fullName: 'Peter Ledbrook' ],
                       'sven' : [ fullName: 'Sven Haiges' ],
                       'burt' : [fullName : 'Burt Beckwith']
        ]

        def userRole = new Role(authority: "ROLE_USER", description: "Registered user").save()
 
        def now = new Date()

        if (!User.list()) {
            samples.each { userId, profileAttrs ->
                def user = new User(userId: userId, password: springSecurityService.encodePassword("password"))

                if (user.validate()) {
                    println "Creating user ${userId}..."
                    user.profile = new Profile(profileAttrs)
                    def url = this.class.getResource("/${userId}.jpg")
                    if (url) {
                        def image = new File(url.toURI()).readBytes()
                        println "Creating With custom photo"
                        user.profile.photo = image
                    }
                    user.save(flush:true)
                    
                    // Link the user to the role.
                    UserRole.create(user, userRole)

                    def tag = new Tag(name: "grails", user: user).save()
                    def tag2 = new Tag(name: "groovy", user: user).save()
                    // 10.downto(1) { postNo ->
                        def post = new Post(content: "A first post from ${userId}", user: user, tag: tag).save()
                        post.dateCreated = now--

                        post.addToTags(tag)
                        user.addToPosts(post)
                    // }
                } else {
                    println("\n\n\nError in account bootstrap for ${userId}!\n\n\n")
                    user.errors.each {err ->
                        println err
                    }
                }
            }
            samples.each { userId, profileAttrs ->
                println "Searching for user ${userId}"
                def user = User.findByUserId(userId)
                println "User is ${user}"
                def others = samples.keySet().findAll { it != userId }
                def skip = true // to mix followers
                others.each { otherId ->
                    skip = !skip
                    if (!skip) {
                      def other = User.findByUserId(otherId)
                      user.addToFollowing(other)
                      println "${user.userId} is following ${otherId}"
                    }
                }

            }
            
            // User that isn't following anyone and that no one else is following.
            new User(userId: 'loner', password: springSecurityService.encodePassword("password")).save()
        }
    }
}

