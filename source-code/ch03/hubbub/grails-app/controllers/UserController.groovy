class UserController {

	
    def scaffold = true

	def search = {
		
	}
	
	def results = {
		
		def users = User.findAllByUserIdLike(params.userId)
		return [ users: users, term : params.userId ]
		
	}
	
}
