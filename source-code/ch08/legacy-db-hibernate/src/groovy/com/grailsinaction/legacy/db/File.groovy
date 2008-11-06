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

    def branches
    def sections
    
}