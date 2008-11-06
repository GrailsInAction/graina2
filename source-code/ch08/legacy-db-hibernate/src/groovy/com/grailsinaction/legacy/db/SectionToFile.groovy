package com.grailsinaction.legacy.db
/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 14/10/2008
 * Time: 12:34:28
 * To change this template use File | Settings | File Templates.
 */
class SectionToFile {

	SectionToFileId id
    Section section
    File file
    Date start
    Date end

}

class SectionToFileId implements Serializable {
	int sectionId
	int fileId
}