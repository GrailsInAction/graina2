package com.grailsinaction.legacy.db

class FileType {

    Character id
    String name
    String description

	def transients = [ 'type' ]
	
	Character getType() {
		return id
	}
	
	void setType(Character type) {
		this.id = type
	}

	static mapping = {
    	table 'BK_FILE_TYPE'
		version false
		id column: 'FILE_TYPE_CD', generator: 'assigned', type: 'char'
		name column: 'FILE_TYPE_NM'
		description column: 'FILE_TYPE_DESC'
	}


}