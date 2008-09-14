class Profile {
	
	static belongsTo = User
	
	byte[] photo
	
	String homepage
	String email
	
	static constraints = {
		homepage(url: true, nullable: true)
		email(email: true, nullable: true)
		photo(nullable: true)
	}

}
