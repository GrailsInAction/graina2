package com.grailsinaction.legacy.db
/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 14/10/2008
 * Time: 12:34:28
 * To change this template use File | Settings | File Templates.
 */
class SectionToFile {

    Section section
    File file
    Date start
    Date end

	static mapping = {
		table 'BK_FILE_SECTION_MAP'
		version false
	
		id column: 'NAME'
		start column: 'START_DT'
		end column: 'END_DT'
		file column: 'FILE_ID'
		section column: 'SECTION_ID'
	}


}

