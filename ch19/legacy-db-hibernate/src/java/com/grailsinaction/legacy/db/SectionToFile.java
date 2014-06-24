package com.grailsinaction.legacy.db;

import java.util.Date;
import java.io.Serializable;

public class SectionToFile {

	private SectionToFileId id;
    private Section section;
    private File file;
    private Date start;
    private Date end;

    public SectionToFileId getId() {
        return id;
    }

    public void setId(SectionToFileId id) {
        this.id = id;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
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
}

class SectionToFileId implements Serializable {
	private int sectionId;
	private int fileId;

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }
}