package com.grailsinaction.legacy.db

class Manager {

    String id
    Branch branch
    Location location
    String name
    short managementRating

	static mapping = {
        table 'BK_MANAGER'
		version false
		
		name column: 'NAME'
		managementRating column: 'MGMT_RATING_VAL'
		branch column: 'BRANCH_NM'
		location column: 'LOCATION_ID'
    }

}