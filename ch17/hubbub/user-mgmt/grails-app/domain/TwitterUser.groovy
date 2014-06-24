import com.grailsinaction.User

class TwitterUser {

    Long twitterId
    String username

    String tokenSecret
    String token

    static belongsTo = [user: User]

    static constraints = {
        twitterId(unique: true, nullable: false)
        username(nullable: false, blank: false)
    }
}
