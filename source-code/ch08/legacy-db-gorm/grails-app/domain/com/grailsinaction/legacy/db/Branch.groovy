package com.grailsinaction.legacy.db

class Branch {

    String id
	
	static transients = ['name']

	void setName(String name) {
		id = name
	}
		   
	String getName() {
		return id
	}
		
    // Manager manager

    static hasMany = [sections:BranchToSection] // sections:Section]

    static mapping = {
        table 'BK_BRANCH'
		version false

		id generator:'assigned', column: "BRANCH_NM", type:'string'
		// sections column:'SECTION_ID',joinTable:'BK_BRANCH_TO_SECTION'
		//manager column: 'BRANCH_NM'
		
    }

}