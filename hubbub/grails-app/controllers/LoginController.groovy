class LoginController {

    def index = {

        def userId = params.userId
        def password = params.password

        def user = User.findByUserIdAndPassword(userId, password.encodeAsSha1())
        if (user) {
            session.user = user
            flash.message = "Welcome " + userId
        } else {
            flash.message = "User Not Found"
        }
        redirect(uri: "/post/list")

    }

    def logout = {
        session.user = null
        flash.message = "Logout successful"
        redirect(uri: "/")
    }
}
