class User {
	
	String userId
	String password
	Date signupDate = new Date()
	Profile profile = new Profile()
	
	static hasMany = [ posts : Post, tags : Tag, 
				followers : User, following : User ]
				
				
	
	SortedSet posts
	SortedSet tags
	
	static constraints = {
	
		userId(size: 3..20, blank: false, unique: true)
		
		// Ensure password does not match userid
		password(size: 6..50, blank: false, 
				 validator: { passwd, account -> 
					return passwd != account.userId
				}) 
	 
	}
	

	static transients = [ 'ipAddress' ]
	String ipAddress // never saved, just cached in session

	
	static mapping = {
		table 'account'
		profile lazy:false
	}
	
	//def beforeInsert = { 
	//	signupDate = new Date() 
	//}

}
