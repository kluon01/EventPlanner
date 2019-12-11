package com.example.eventplanner.model;


public class Event {

    private String eTitle;
    private String eSubtitle;
    private String eInfo;
    private LatLng eLocation; // Can convert LatLng to address, see https://youtu.be/Ut_VK92QqEQ and https://stackoverflow.com/questions/13598647/google-map-how-to-get-address-in-android
    private long dateAndTime;
    private String uid;

    public Event(){

    }

    public Event(String eTitle, String eSubtitle, String eInfo){
        this.eTitle = eTitle;
        this.eSubtitle = eSubtitle;
        this.eInfo = eInfo;
        eLocation = new LatLng();
        dateAndTime = 0;

    }

    public Event(String eTitle, String eSubtitle, String eInfo, LatLng eLocation, long dateAndTime) {
        this.eTitle = eTitle;
        this.eSubtitle = eSubtitle;
        this.eInfo = eInfo;
        this.eLocation = eLocation;
        this.dateAndTime = dateAndTime;

        /*
        // For future reference
        private SimpleDateFormat dateFormat;
        private Calendar calendar;
        Calendar calendar;
        SimpleDateFormat dateFormat;

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dateAndTime);
        dateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");

        tvDateAndTime.setText(dateFormat.format(calendar.getTime()));
        */
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

    public LatLng getLocation() {
        return eLocation;
    }

    public void setTitle(String title) {
        eTitle = title;
    }

    public void setSubtitle(String subtitle) {
        eSubtitle = subtitle;
    }

    public void setInfo(String info) {
        eInfo = info;
    }

    public void setLocation(LatLng location) {
        eLocation = location;
    }

    public long getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(long dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
