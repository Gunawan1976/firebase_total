package com.example.form_firebase.model;

public class User {
    private String id,name,email,uang;

    public User(){

    }

    public User(String name, String email, String uang) {
        this.name = name;
        this.email = email;
        this.uang = uang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUang() {
        return uang;
    }

    public void setUang(String uang) {
        this.uang = uang;
    }
}
