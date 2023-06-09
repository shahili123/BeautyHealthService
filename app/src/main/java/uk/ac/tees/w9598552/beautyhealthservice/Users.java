package uk.ac.tees.w9598552.beautyhealthservice;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {

    String user_id,full_name,email,password,address,latitude,longitude,image_url,type,token;

    public Users() {
    }

    public Users(String user_id, String full_name, String email, String password, String address, String latitude, String longitude, String image_url, String type, String token) {

        this.user_id=user_id;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.address=address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image_url = image_url;
        this.type=type;
        this.token=token;

    }




    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
