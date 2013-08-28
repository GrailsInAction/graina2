package com.grailsinaction.legacy.db

class Branch {

    String id
	
	static transients = ['name', 'manager']

	void setName(String name) {
		id = name
	}
		   
	String getName() {
		return id
	}
	
	Manager getManager() {
		return Manager.get(id)
	}
	
	void setManager(Manager manager) {
		manager.id = id
	}
		
    static hasMany = [sections:BranchToSection] 

    static mapping = {
        table 'BK_BRANCH'
		version false

		id generator:'assigned', column: "BRANCH_NM", type:'string'
		
    }

}