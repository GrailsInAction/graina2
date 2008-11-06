package com.grailsinaction.legacy.db

/**
 * Created by IntelliJ IDEA.
 * User: CGS750
 * Date: 13/10/2008
 * Time: 16:34:06
 * To change this template use File | Settings | File Templates.
 */
class Branch {

    String name
    Manager manager

    def files
    def sections

    static constraints = {
        manager(nullable: true)
    }

}