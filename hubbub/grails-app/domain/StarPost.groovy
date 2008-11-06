class StarPost extends Post {
	
	String reason
	
	static constraints = {
		reason(nullable: false)
	}

}
