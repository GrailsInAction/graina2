class StarPost extends Post {
	
	String reason
	
	static belongsTo = [ user : User ]
	
	static hasMany = [ tags : Tag ]
	
	static constraints = {
		reason(nullable: false)
	}

}
