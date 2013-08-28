package com.grailsinaction.legacy.db;

import java.util.Set;

public class Branch {

    private String name;
    private Manager manager;

    private Set sections;



    public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Manager getManager() {
		return manager;
	}
	
	public void setManager(Manager manager) {
		this.manager = manager;
	}

    public Set getSections() {
        return sections;
    }

    public void setSections(Set sections) {
        this.sections = sections;
    }

}