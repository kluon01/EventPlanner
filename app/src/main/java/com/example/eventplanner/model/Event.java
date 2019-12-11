package com.example.eventplanner.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "event")
public class Event {

    @PrimaryKey()
    @NonNull
    private String documentId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "subtitle")
    private String subtitle;
    @ColumnInfo(name = "info")
    private String info;
    @ColumnInfo(name = "latitude")
    private float latitude; // Can convert LatLng to address, see https://youtu.be/Ut_VK92QqEQ and https://stackoverflow.com/questions/13598647/google-map-how-to-get-address-in-android
    @ColumnInfo(name = "longitude")
    private float longitude;
    @ColumnInfo(name = "dateAndTime")
    private long dateAndTime;

    public Event() {
        this.documentId = "";
    }

    @Ignore
    public Event(String eTitle, String eSubtitle, String eInfo, float eLatitude, float eLongitude, long dateAndTime, @NotNull String documentId) {
        this.title = eTitle;
        this.subtitle = eSubtitle;
        this.info = eInfo;
        this.latitude = eLatitude;
        this.longitude = eLongitude;
        this.dateAndTime = dateAndTime;
        this.documentId = documentId;
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

    @Ignore
    public Event(String eTitle, String eSubtitle, String eInfo, float eLatitude, float eLongitude, long dateAndTime) {
        this.title = eTitle;
        this.subtitle = eSubtitle;
        this.info = eInfo;
        this.latitude = eLatitude;
        this.longitude = eLongitude;
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

    public long getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(long dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @NotNull
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(@NotNull String documentId) {
        this.documentId = documentId;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
