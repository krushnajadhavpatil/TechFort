package com.example.techfort.Model;

public class BookmarkModel extends BookmarkId {

    String topicId;
    String topicName;
    String topicUrl;
    String user_id;
    String category;
    public BookmarkModel() {
    }

//    public BookmarkModel(String topicId, String topicName, String topicUrl, String user_id) {
//        this.topicId = topicId;
//        this.topicName = topicName;
//        this.topicUrl = topicUrl;
//        this.user_id = user_id;
//    }
    public BookmarkModel(String topicId, String topicName, String user_id,String category) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.user_id = user_id;
        this.category=category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
