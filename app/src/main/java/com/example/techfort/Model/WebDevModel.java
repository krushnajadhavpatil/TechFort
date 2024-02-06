package com.example.techfort.Model;

public class WebDevModel {

    private String webName, webImage, webId;

    public WebDevModel(String webName, String webImage, String webId) {
        this.webName = webName;
        this.webImage = webImage;
        this.webId = webId;

    }

    public WebDevModel() {
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getWebImage() {
        return webImage;
    }

    public void setWebImage(String webImage) {
        this.webImage = webImage;
    }

    public String getWebId() {
        return webId;
    }

    public void setWebId(String webId) {
        this.webId = webId;
    }


}
