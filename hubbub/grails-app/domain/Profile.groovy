class Profile {

	static belongsTo = [ user: User ]

	byte[] photo

	String fullName
	String homepage
	String email
	String timezone
	String country
	String jabberAddress // jabber address

	static constraints = {
		fullName(nullable: true)
		country(nullable: true)
		homepage(url: true, nullable: true)
		email(email: true, nullable: true)
		photo(nullable: true)
		timezone(nullable: true)
		jabberAddress(nullable: true)
	}

}

