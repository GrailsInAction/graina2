
class FriendController {

    def index = { }

    def following = {

        def user = User.findByUserId(params.id)
        if (user) {
            def heading = "People who ${user.userId} is following"
            render(view: 'follow', model: [ users: user.following, viewuser : user, heading: heading ])
        } else {
            response.sendError(404)
        }


    }

    def followers = {
       def user = User.findByUserId(params.id)
        if (user) {
            def heading = "People who follow ${user.userId}"
            render(view: 'follow', model: [ users: user.followers, viewuser : user, heading: heading ])
        } else {
            response.sendError(404)
        }
    }
}

