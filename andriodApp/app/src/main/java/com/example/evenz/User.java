package com.example.evenz;

import java.util.ArrayList;

public class User
{
    private String name;
    private String profilePicID;
    private String phone;
    private String email;

    private String userId;

    private String userType;
    private ArrayList<String> eventsSignedUpFor;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }


    public User(String name, String profilePicID, String phone, String email, String userId, String userType)
    {
        this.name = name;
        this.profilePicID = profilePicID;
        this.phone = phone;
        this.email = email;
        this.userId = userId;
        this.userType = userType;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicID() {
        return profilePicID;
    }

    public void setProfilePicID(String profilePicID) {
        this.profilePicID = profilePicID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }


    public ArrayList<String> getEventsSignedUpFor() {
        return eventsSignedUpFor;
    }

    public void setEventsSignedUpFor(ArrayList<String> eventsSignedUpFor) {
        this.eventsSignedUpFor = eventsSignedUpFor;
    }




    public void setUserType(String userType) {
        this.userType = userType;
    }
}