package com.test.beautyhealthservice;

import java.util.ArrayList;

public class BSpecialistModel {


    String user_id,name,email,address,latitude,longitude,image,password,type;
    ArrayList<String> list_services=new ArrayList<>();

    public BSpecialistModel(String user_id, String name, String email, String address, String latitude, String longitude,String password,String image, ArrayList<String> list_services,String type) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image=image;
        this.password=password;
        this.list_services = list_services;
        this.type=type;
    }

    public String getId() {
        return user_id;
    }

    public void setId(String id) {
        this.user_id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getList_services() {
        return list_services;
    }

    public void setList_services(ArrayList<String> list_services) {
        this.list_services = list_services;
    }
}
