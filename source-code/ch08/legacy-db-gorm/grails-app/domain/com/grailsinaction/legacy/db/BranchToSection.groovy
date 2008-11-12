package com.grailsinaction.legacy.db

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

