package com.grailsinaction.legacy.db

class FileType {

    Character type
    String name
    String description

	static mapping = {
    	table 'BK_FILE_TYPE'
		version false
		type column: 'FILE_TYPE_CD'
		name column: 'FILE_TYPE_NM'
		description column: 'FILE_TYPE_DESC'
	}


}