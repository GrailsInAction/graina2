package com.grailsinaction.legacy.db

class SectionToFile implements Serializable {

    Section section
    File file
    Date start
    Date end

	static mapping = {
		table 'BK_FILE_SECTION_MAP'
		version false
	
		id composite:['file', 'section']
		start column: 'START_DT', type: 'date'
		end column: 'END_DT', type: 'date'
		file column: 'FILE_ID'
		section column: 'SECTION_ID'
	}

}

