package com.grailsinaction.legacy.db
/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 13/10/2008
 * Time: 16:45:38
 * To change this template use File | Settings | File Templates.
 */
class File {

    int id
    FileType resourceType
    String name
    short securityRating
    FileOwner owner
    Date start = new Date()
    Date end = new Date()
    String description

	static hasMany = [sections: SectionToFile]
	
	static mapping = {
    	table 'BK_FILE'
		version false
		
		id column: 'FILE_ID', generator: 'increment'
		name column: 'FILE_NM'
		start column: 'START_DT', type: 'date'
		end column: 'END_DT', type: 'date'
		description column: 'FILE_DESC'
		securityRating column: 'SECURITY_RATING_VAL'
		owner column: 'FILE_OWNER_ID'
		resourceType column: 'FILE_TYPE_CD'
		//branches column: 'BRANCH_NM', joinTable: 'BK_BRANCH_TO_FILE'
		sections column: 'SECTION_ID', joinTable: 'BK_FILE_SECTION_MAP'
	}

    
}