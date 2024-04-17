package com.example.techfort.Model;

public class VideoModel {

    String url,time,name;


    public  VideoModel(){

    }
    public VideoModel(String url, String time,String name) {
        this.url = url;
        this.time = time;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
