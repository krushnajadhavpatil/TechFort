package com.example.techfort.Model;

public class BookmarkModel extends BookmarkId {

    String topicId;
    String topicName;
    String topicUrl;
    String user_id;
    public BookmarkModel() {
    }

    public BookmarkModel(String topicId, String topicName, String topicUrl, String user_id) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.topicUrl = topicUrl;
        this.user_id = user_id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
