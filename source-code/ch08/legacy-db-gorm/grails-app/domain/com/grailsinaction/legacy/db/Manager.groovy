package com.grailsinaction.legacy.db

class Manager {

    String id
    Location location
    String name
    short managementRating
	Branch branch
	/*
	Branch getBranch() {
		return Branch.get(id)
	}
	
	void setBranch(Branch branch) {
		id = branch.id
	}
	*/
	static mapping = {
        table 'BK_MANAGER'
		version false
		
		id generator: 'foreign', params:[property: 'branch'], column: 'BRANCH_NM'
		branch column: 'BRANCH_NM'
		name column: 'NAME'
		managementRating column: 'MGMT_RATING_VAL'
		// branch column: 'BRANCH_NM'
		location column: 'LOCATION_ID'
    }

}