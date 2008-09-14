class User {
	
	String userId
	String password
	Date signupDate = new Date()
	Profile profile
	
	SortedSet tags
	SortedSet posts
	
	static hasMany = [ posts : Post, tags : Tag ]
		
	static constraints = {
	
		userId(size: 3..20, blank: false, unique: true)
		
		// Ensure password does not match userid
		password(size: 6..8, blank: false, 
				 validator: { passwd, account -> 
					return passwd != account.userId
				}) 
				
		profile(nullable: true)
	 
	}
	
	
	static mapping = {
		table 'users'
		profile lazy:false
	}
	
}
