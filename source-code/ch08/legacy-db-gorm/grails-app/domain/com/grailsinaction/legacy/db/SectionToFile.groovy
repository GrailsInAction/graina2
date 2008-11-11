package com.grailsinaction.legacy.db
/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 14/10/2008
 * Time: 12:34:28
 * To change this template use File | Settings | File Templates.
 */
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

