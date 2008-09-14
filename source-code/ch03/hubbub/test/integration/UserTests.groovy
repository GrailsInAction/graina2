class UserTests extends GroovyTestCase {

   	void testFirstSaveEver() {

			def user = new User(userId: 'joe', password: 'secret', 
				homepage: 'http://www.grailsinaction.com')
			assertNotNull user.save()
			assertNotNull user.id
			
			def foundUser = User.get(user.id)
			assertEquals 'joe', foundUser.userId 

	}
	
	
	void testSaveAndUpdate() {

			def user = new User(userId: 'joe', password: 'secret', 
				homepage: 'http://www.grailsinaction.com')
			assertNotNull user.save()
			
			def foundUser = User.get(user.id)
			foundUser.password = 'sesame' 
			foundUser.save()
			
			def editedUser = User.get(user.id)
			assertEquals 'sesame', editedUser.password 

	}
	
	void testSaveThenDelete() {
		
		def user = new User(userId: 'joe', password: 'secret', 
			homepage: 'http://www.grailsinaction.com')
		assertNotNull user.save()
		
		def foundUser = User.get(user.id)
		foundUser.delete()
		
		assertFalse User.exists(user.id)
				
	}
	
	void testManyPosts() {
		
		def user = new User(userId: 'joe', password: 'secret').save()
		def post1 = new Post(content: "First post... W00t!")
		
		user.addToPosts(post1)
		def post2 = new Post(content: "Second post...")
		user.addToPosts(post2)
		def post3 = new Post(content: "Third post...")
		user.addToPosts(post3)
		assertEquals 3, User.get(user.id).posts.size()
	}
	
	void testPostWithTags() {
		
		def user = new User(userId: 'joe', password: 'secret').save()
		def post1 = new Post(content: "First post... W00t!")
		
		user.addToPosts(post1)
		
		def tag1 = new Tag(name: 'groovy')
		def tag2 = new Tag(name: 'grails')
		user.addToTags(tag1)
		user.addToTags(tag2)
		post1.addToTags(tag1)
		post1.addToTags(tag2)
		
		assertEquals([ 'grails', 'groovy'], User.get(user.id).tags*.name)
		
	}
	
	
	void testBasicDynamicFinders() {

			new User(userId: 'glen', password: 'secret', 
				profile: new Profile(email: 'glen@glensmith.com')).save()
	        new User(userId: 'peter', password: 'sesame', 
				profile: new Profile(homepage: 'http://www.peter.com/')).save()

	        def user = User.findByPassword('sesame')
	        assertEquals 'peter', user.userId

	        user = User.findByUserIdAndPassword('glen', 'secret')
	        assertEquals 'glen', user.userId

	        def now = new Date()
	        def users = User.findAllBySignupDateBetween(now-1, now)
	        assertEquals 2, users.size()

	        def profiles = Profile.findAllByEmailIsNotNull()
	        assertEquals 1, profiles.size()

		}

	
	
	
	void testQueryByExample() {
		
		new User(userId: 'glen', password: 'password').save()
		new User(userId: 'peter', password: 'password').save()
		new User(userId: 'cynthia', password: 'sesame').save()
		
		def userToFind = new User(userId: 'glen', signupDate: null)
		def u1 = User.find(userToFind)
		assertEquals('password', u1.password)

		userToFind = new User(userId: 'cynthia', signupDate: null)
		def u2 = User.find(userToFind)
		assertEquals('cynthia', u2.userId)

		userToFind = new User(password: 'password', signupDate: null)
		def u3 = User.findAll(userToFind)
		assert [ 'glen', 'peter'] == u3*.userId 
		
		
	}
	

}
