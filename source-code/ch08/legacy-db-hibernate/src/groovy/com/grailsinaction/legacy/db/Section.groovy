package com.grailsinaction.legacy.db
/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 13/10/2008
 * Time: 17:00:18
 * To change this template use File | Settings | File Templates.
 */
class Section {

    int id
    String name
    Date start = new Date()
    Date end = new Date()
    def branches // a list of Branches

    def files  // a list of Files
    def locations      // a list of Locaitons


}