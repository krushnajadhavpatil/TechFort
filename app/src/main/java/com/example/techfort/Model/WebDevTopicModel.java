package com.example.techfort.Model;


public class WebDevTopicModel {

    String topicName, topicId, topicUrl;


    public WebDevTopicModel() {
    }

    public WebDevTopicModel(String topicName, String topicId, String topicUrl) {
        this.topicName = topicName;
        this.topicId = topicId;
        this.topicUrl = topicUrl;
    }

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
}

