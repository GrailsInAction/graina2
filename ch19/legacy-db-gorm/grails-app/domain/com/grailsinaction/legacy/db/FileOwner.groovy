package com.grailsinaction.legacy.db

class FileOwner {

    int id
    String name
    String description

	static mapping = {
    	table 'BK_FILE_OWNER'
		version false
		
		id column: 'FILE_OWNER_ID', generator: 'increment'
		name column: 'OWNER_NM'
		description column: 'OWNER_DESC'
	}

}