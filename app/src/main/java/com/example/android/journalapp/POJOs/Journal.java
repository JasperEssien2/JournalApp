package com.example.android.journalapp.POJOs;

import java.io.Serializable;

public class Journal implements Serializable{

    private String imageUrl, text, username, uploadDate, title;
    private Object timeStamp;
    private String pushId;
    private Boolean isStarred;
    public Journal(){

    }

    /**
     * This constructor is for journal with image and text
     * @param imageUrl
     * @param text
     */
    public Journal(String imageUrl, String text, String title, Object timeStamp, String pushId,
                   Boolean isStarred){
        this.imageUrl = imageUrl;
        this.text = text;
        this.timeStamp = timeStamp;
        this.title = title;
        this.pushId = pushId;
        this.isStarred = isStarred;
    }

    /**
     * This constructor is for journals with only text
     * @param text
     */
    public Journal(String text, String title, Object timeStamp, String pushId, boolean isStarred){
        this.text = text;
        this.timeStamp = timeStamp;
        this.title = title;
        this.pushId = pushId;
        this.isStarred = isStarred;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public String getUploadDate() {
        return uploadDate;
    }

//    public void setUploadDate(long timeStamp) {
//        //TODO: Do Logic to convert time stamp to time
//        this.uploadDate = "";
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public Boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }
}
