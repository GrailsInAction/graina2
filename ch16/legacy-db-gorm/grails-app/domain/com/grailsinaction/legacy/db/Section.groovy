package com.grailsinaction.legacy.db

class Section {

    int id
    String name
    Date start = new Date()
    Date end = new Date()

	static hasMany = [files: SectionToFile, locations: Location, branches: BranchToSection ] 
	
	def belongsTo = [ Branch ]

	static mapping = {
        table 'BK_SECTION'
		version false
	
		id column: 'SECTION_ID', generator: 'increment', type:'integer'
		name column: 'SECTION_NM'
		start column: 'START_DT', type: 'date'
		end column: 'END_DT', type: 'date'
		locations column:'SECTION_ID', joinTable:'BK_LOCATION_SECTION_MAP' // LOCATION_ID
		
    }
	
}