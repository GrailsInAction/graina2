class BootStrap {

     def init = { servletContext ->
		if (User.count() == 0) {
			println "Fresh Database. Creating ADMIN user."
			def user = new User(userId: "admin", password: "secret", 
				profile: new Profile(email: "admin@localhost")).save()
		}
	
     }
     def destroy = {
     }
} 