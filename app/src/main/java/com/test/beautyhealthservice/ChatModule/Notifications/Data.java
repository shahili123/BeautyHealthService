package com.test.beautyhealthservice.ChatModule.Notifications;

public class Data {
    private String sender_id;
    private int icon;
    private String body;
    private String title;
    private String receiver_id;
    private String sender_name;
    private String image_url;
    public Data(String sender_id,String sender_name, String body,String image_url, String title, String receiver_id) {
        this.sender_id = sender_id;
        this.sender_name=sender_name;
        this.body = body;
        this.image_url=image_url;
        this.title = title;
        this.receiver_id = receiver_id;
    }

    public Data() {
    }


    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }



}
