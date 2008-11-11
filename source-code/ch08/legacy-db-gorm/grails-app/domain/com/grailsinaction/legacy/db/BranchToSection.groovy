package com.grailsinaction.legacy.db
/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 14/10/2008
 * Time: 12:34:28
 * To change this template use File | Settings | File Templates.
 */
class BranchToSection implements Serializable {

    Section section
    Branch branch

	static mapping = {
		table 'BK_BRANCH_TO_SECTION'
		version false
	
		id composite:['branch', 'section']
		branch column: 'BRANCH_NM'
		section column: 'SECTION_ID'
	}


}

