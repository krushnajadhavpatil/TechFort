package com.example.techfort.Model;


public class Post extends PostId{
    public String description;
    public String question;
    public String tag;
    public String user_id;
    public long timestamp;

    public Post(String description, String question, String tag, String user_id, long timestamp) {
        this.description = description;
        this.question = question;
        this.tag = tag;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public Post() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

