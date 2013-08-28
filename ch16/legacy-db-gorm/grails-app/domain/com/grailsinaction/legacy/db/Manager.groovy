package com.grailsinaction.legacy.db

class Manager {

    String id
    Location location
    String name
    short managementRating
	
	static transients = ['branch']
	
	Branch getBranch() {
		return Branch.get(id)
	}
	
	void setBranch(Branch branch) {
		id = branch.id
	}
	
	static mapping = {
        table 'BK_MANAGER'
		version false
		
		id column: 'BRANCH_NM', generator: 'assigned'
		name column: 'NAME'
		managementRating column: 'MGMT_RATING_VAL'
		location column: 'LOCATION_ID'
    }

}