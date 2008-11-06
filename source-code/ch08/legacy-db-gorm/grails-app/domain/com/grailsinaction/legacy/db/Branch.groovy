package com.grailsinaction.legacy.db

class Branch {

    String name
    Manager manager

    static hasMany = [ files: File, sections: Section ]

    static mapping = {
        table 'BK_BRANCH'
		version false
		
		name column: 'BRANCH_NM'
		sections column: 'SECTION_ID', joinTable: 'BK_BRANCH_TO_SECTION'
		files column: 'FILE_ID', joinTable: 'BK_BRANCH_TO_FILE'
    }

}