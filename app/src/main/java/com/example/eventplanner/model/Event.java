package com.example.eventplanner.model;

public class Event {

    private String eTitle;
    private String eSubtitle;
    private String eInfo;
    //private LatLng eLocation;


    public Event(String eTitle, String eSubtitle, String eInfo) {
        this.eTitle = eTitle;
        this.eSubtitle = eSubtitle;
        this.eInfo = eInfo;
        //this.eLocation = eLocation;
    }

    public String getTitle() {
        return eTitle;
    }

    public String getSubtitle() {
        return eSubtitle;
    }

    public String getInfo() {
        return eInfo;
    }
    /*
    public LatLng getLocation() {
        return eLocation;
    }
*/
    public void setTitle(String title) {
        eTitle = title;
    }

    public void setSubtitle(String subtitle) {
        eSubtitle = subtitle;
    }

    public void setInfo(String info) {
        eInfo = info;
    }
/*
    public void setLocation(LatLng location) {
        eLocation = location;
    }

 */
}
