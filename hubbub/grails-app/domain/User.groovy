class User {
    String userId
    String password
    boolean enabled = true

    Date signupDate = new Date()
    Profile profile
    Map plugins
	
    static hasMany = [ posts : Post, tags : Tag, 
                       followers : User, following : User,
                       authorities: Role]
    static belongsTo = Role

    SortedSet posts
    SortedSet tags
	
    static constraints = {
	
        userId(size: 3..20, blank: false, unique: true)
		
        // Ensure password does not match userid
        password(size: 6..50, blank: false, 
            validator: { passwd, account -> 
                return passwd != account.userId
        })

        profile(nullable: true)

    }

    static transients = [ 'ipAddress' ]
    String ipAddress // never saved, just cached in session

    static mapping = {
        table 'account'
        profile lazy:false
        cache true
        userId index:'User_Id_Idx'

    }
	
	//def beforeInsert = { 
	//	signupDate = new Date() 
	//}
}
