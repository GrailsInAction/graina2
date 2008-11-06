package com.grailsinaction.legacy.db

class Section {

    int id
    String name
    Date start = new Date()
    Date end = new Date()

	static hasMany = [ branches: Branch, files: SectionToFile, locations: SectionToLocation ]
	
	def belongsTo = [ Branch ]

	static mapping = {
        table 'BK_SECTION'
		version false
		
		id column: 'SECTION_ID'
		start column: 'START_DT'
		end column: 'END_DT'
		branch column: 'BRANCH_NM', joinTable: 'BK_BRANCH_TO_SECTION'
		
    }
	


}