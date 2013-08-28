package com.grailsinaction.legacy.db;

import java.util.Date;
import java.util.Set;


public class File {

    private int id;
    private FileType resourceType;
    private String name;
    private short securityRating;
    private FileOwner owner;
    private Date start = new Date();
    private Date end = new Date();
    private String description;

    private Set sections;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FileType getResourceType() {
        return resourceType;
    }

    public void setResourceType(FileType resourceType) {
        this.resourceType = resourceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getSecurityRating() {
        return securityRating;
    }

    public void setSecurityRating(short securityRating) {
        this.securityRating = securityRating;
    }

    public FileOwner getOwner() {
        return owner;
    }

    public void setOwner(FileOwner owner) {
        this.owner = owner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set getSections() {
        return sections;
    }

    public void setSections(Set sections) {
        this.sections = sections;
    }
}