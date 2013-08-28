package com.grailsinaction.legacy.db;


public class Manager {

    private String id;
    private Branch branch;
    private Location location;
    private String name;
    private short managementRating;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getManagementRating() {
        return managementRating;
    }

    public void setManagementRating(short managementRating) {
        this.managementRating = managementRating;
    }
}