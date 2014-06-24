package com.grailsinaction.legacy.db;

import java.util.Date;
import java.util.Set;


public class Section {

    private int id;
    private String name;
    private Date start = new Date();
    private Date end = new Date();
    private Set branches; // a list of Branches

    private Set files;  // a list of Files
    private Set locations;      // a list of Locaitons


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Set getBranches() {
        return branches;
    }

    public void setBranches(Set branches) {
        this.branches = branches;
    }

    public Set getFiles() {
        return files;
    }

    public void setFiles(Set files) {
        this.files = files;
    }

    public Set getLocations() {
        return locations;
    }

    public void setLocations(Set locations) {
        this.locations = locations;
    }
}