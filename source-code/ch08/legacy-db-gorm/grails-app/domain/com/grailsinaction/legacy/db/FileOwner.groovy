package com.grailsinaction.legacy.db

class FileOwner {

    int id
    String name
    String description

	static mapping = {
    	table 'BK_MANAGER'
		version false
		
		id column: 'NAME'
		name column: 'MGMT_RATING_VAL'
		description column: 'BRANCH_NM'
	}



}