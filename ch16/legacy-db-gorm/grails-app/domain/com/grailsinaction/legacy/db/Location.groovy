package com.grailsinaction.legacy.db

class Location {

    int id
    String name

	static mapping = {
    	table 'BK_LOCATION'
		version false
		
		id column: 'LOCATION_ID', generator: 'increment'
		name column: 'LOCATION_NM'
	}


}