package com.example.techfort.Model;

public class ProgTopicModel {

    String progName;
    String progTopicId;
    String progUrl;
    public ProgTopicModel() {
    }

    public ProgTopicModel(String progName, String progTopicId, String progUrl) {
        this.progName = progName;
        this.progTopicId =progTopicId;
        this.progUrl = progUrl;
    }

    public String getProgName() {
        return progName;
    }

    public void setProgName(String progName) {
        this.progName = progName;
    }

    public String getProgTopicId() {
        return progTopicId;
    }

    public void setProgTopicId(String progTopicId) {
        this.progTopicId = progTopicId;
    }

    public String getProgUrl() {
        return progUrl;
    }

    public void setProgUrl(String progUrl) {
        this.progUrl = progUrl;
    }
}
