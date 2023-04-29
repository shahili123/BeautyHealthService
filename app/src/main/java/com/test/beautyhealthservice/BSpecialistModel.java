package com.test.beautyhealthservice;

import java.util.ArrayList;

public class BSpecialistModel {


    String id,name,email,address,latitude,longitude,image;
    ArrayList<String> list_services=new ArrayList<>();

    public BSpecialistModel(String id, String name, String email, String address,String image, String latitude, String longitude, ArrayList<String> list_services) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image=image;
        this.list_services = list_services;
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
