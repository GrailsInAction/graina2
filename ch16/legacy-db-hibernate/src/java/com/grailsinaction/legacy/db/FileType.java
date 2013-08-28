package com.grailsinaction.legacy.db;


public class FileType {

    private Character type;
    private String name;
    private String description;

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}