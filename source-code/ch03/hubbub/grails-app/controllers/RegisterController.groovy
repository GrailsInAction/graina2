class RegisterController {

   def index = { redirect(action: 'newuser') }

   def newuser = { 
		
		/*
		def blankUser = new User()
		[ user : blankUser ]
		*/

	}

	def save = {

		def user = new User(params)
		user.save()
		redirect(action: 'profile', id: user.id)
		
/*		if (user.save()) {
			redirect(action: 'profile', id: user.id)
		} else {
			render(view: 'newuser', model: [ user : user ])
		}*/

	}
	
	def profile = {
		[ user: User.get(params.id) ]
	}

	def changePassword = {
		
		def user = User.get(params.id)
		[ user: user ]
	
	}
	
	def updatePassword = {
		
		def user = User.get(params.id)	
		boolean correctPassword = (params.password == user.password)	
		boolean passwordsMatch = (params.newpassword == params.confirm)
		if (correctPassword && passwordsMatch)	{		
			user.password = params.password
			user.save()
			redirect(action: 'profile', id: user.id)
		} else {
			flash.message = "Something doesn't add up, dude. Try again."
			redirect(action: 'changePassword', id: user.id)
		}
		
	}
}
