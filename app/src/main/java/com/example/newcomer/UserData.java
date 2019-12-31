package com.example.newcomer;

import android.app.Application;

public class UserData extends Application {
    String phoneNumber;
    Integer radiusDistance;
    public UserData(){
        radiusDistance = 3; //Default value for the radius of the current map that can be used to sync with other users

    }
    public void setPhoneNumber(String mobile) {
        this.phoneNumber = mobile; //Set the phone number to be the user's phone number
    }
    public void setRadiusDistance(int radius){
        this.radiusDistance = radius;
    }
    public Integer getRadiusDistance(){
        return this.radiusDistance;
    }

}
