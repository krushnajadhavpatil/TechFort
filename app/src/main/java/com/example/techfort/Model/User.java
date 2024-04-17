package com.example.techfort.Model;

public class User {

    public String name, image,phone;

    public User(String name, String image) {
        this.name = name;
        this.image = image;
    }
    public User(String name, String image,String phone) {
        this.name = name;
        this.image = image;
        this.phone=phone;
    }
    public User() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
