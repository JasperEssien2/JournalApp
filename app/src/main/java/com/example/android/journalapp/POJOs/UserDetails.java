package com.example.android.journalapp.POJOs;

public class UserDetails {
    private String mUsername;
    private String mEmailAddress;
    private String mProfilePicUrl;

    public UserDetails(){

    }

    public UserDetails(String username, String emailAddress, String profilePicUrl){
        mUsername = username;
        mEmailAddress = emailAddress;
        mProfilePicUrl = profilePicUrl;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmEmailAddress() {
        return mEmailAddress;
    }

    public void setmEmailAddress(String mEmailAddress) {
        this.mEmailAddress = mEmailAddress;
    }

    public String getmProfilePicUrl() {
        return mProfilePicUrl;
    }

    public void setmProfilePicUrl(String mProfilePicUrl) {
        this.mProfilePicUrl = mProfilePicUrl;
    }
}
