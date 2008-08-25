
class UserTests extends GroovyTestCase {

		void testFirstSaveEver() {

			def user = new User(userId: 'joe', password: 'secret')
			assertNotNull(user.save())

			assertEquals "joe", user.userId 
			println User.findByUserIdAndPassword('joe', 'secret')

		}

		void testEvilSave() {
			def user = new User(userId: 'joe', password: 'pass')
			assertFalse(user.validate())

			user.errors.each { err -> println err }

			user.password = "password"
			assertTrue(user.validate())

		}

		void testCustomValidator() {

			def user = new User(userId: 'password', password: 'password')
			assertFalse(user.validate())	
			user.userId = "joe"
			assertTrue(user.validate())

		}


	    void testAddingPosts() {

			def user = new User(userId: 'joe', password: 'secret').save()
			def post1 = new Post(content: "First post... W00t!")
			user.addToPosts(post1)	
			def post2 = new Post(content: "Second post...")
			user.addToPosts(post2)
			def post3 = new Post(content: "Third post...")
			user.addToPosts(post3)

			assertEquals 3, user.posts.size()

	    }

		void OFFtestPrintingPosts() {

			def user = User.findByUserId("joe")

			user.posts.each { post ->
			    println "${post.content} created on ${post.created}"
			}

		}

        void testListing() {

            new User(userId: 'glen', password: 'secret').save()
			new User(userId: 'peter', password: 'sesame').save()
            new User(userId: 'cynthia', password: 'squirrel').save()

            def users = User.list()
            assertEquals 3, users.size()

            def usersOrdered = User.listOrderByUserId()
            assertEquals "cynthia", usersOrdered[0].userId
            assertEquals "glen", usersOrdered[1].userId
            assertEquals "peter", usersOrdered[2].userId


            assertEquals(["cynthia", "glen", "peter"], usersOrdered*.userId )
            //assertEquals(["cythia", "glen", "peter"], usersOrdered.collect { it.userId } )


        }

        void testBasicListQueries() {


            1.upto(10) {newId ->
                println "Creating user ${newId}"
                def user = new User(userId: "userNum${newId}", password: "password").save()
            }
            def users = User.list()
            assertEquals 10, users.size()

            // you can order things too
            users = User.listOrderBySignupDate(order: 'desc')
            assertEquals "userNum10", users[0].userId
    

        }

        void testBasicDynamicFinders() {

			new User(userId: 'glen', password: 'secret').save()
			new User(userId: 'peter', password: 'sesame').save()

			def user = User.findByUserId('glen')
			assertEquals "secret", user.password

			user = User.findByPassword("sesame")
			assertEquals "peter", user.userId

			user = User.findByUserIdAndPassword('glen', 'secret')
			assertEquals "glen", user.userId


		}

		void testSave() {
			new User(userId: 'cynthia', password: 'secret').save()
			def user = User.findByUserId('cynthia')

			assertEquals "secret", user.password
			user.password = "abc"
			assertEquals "abc", user.password
			println user.validate()

			user = User.findByUserId('cynthia')
			assertEquals "abc", user.password

		}
		
		void testFollows() {
			
			def u1 = new User(userId: 'first', password: 'secret').save()
			def u2 = new User(userId: 'second', password: 'secret').save()
			def u3 = new User(userId: 'third', password: 'secret').save()
			
			u1.addToFollowers(u2) // but also need to addToFollowing on the other guy
			u2.addToFollowing(u1)
			
			u1.addToFollowers(u3)
			u3.addToFollowing(u1)
			
			def c = User.createCriteria()
			def l = c.list {
				following {
					eq('userId', u1.userId)
				}
			}
			assertEquals 2, l.size()
			
			
		}
	
}

