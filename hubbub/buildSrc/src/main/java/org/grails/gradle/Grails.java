package org.grails.gradle;

import org.gradle.api.*;
import org.gradle.api.internal.DefaultTask;

public class Grails extends DefaultTask {
    private String command;

    public Grails(Project project, String name) {
        super(project, name);

        doFirst(new TaskAction() {
            public void execute(Task task) {
                callGrails();
            }
        });
    }

    public String getCommand() {
        return this.command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    protected void callGrails() {
        
    }
}
