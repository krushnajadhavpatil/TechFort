package com.example.techfort.Model;

public class ProgrammingModel {

    private String progName, progId, progImage;

    public ProgrammingModel(String progId, String progName, String progImage) {
        this.progName = progName;
        this.progImage = progImage;
        this.progId = progId;
    }

    public ProgrammingModel() {
    }

    public String getProgImage() {
        return progImage;
    }

    public void setProgImage(String progImage) {
        this.progImage = progImage;
    }

    public String getProgId() {
        return progId;
    }

    public void setProgId(String progId) {
        this.progId = progId;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }
}
