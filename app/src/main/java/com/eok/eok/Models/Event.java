package com.eok.eok.Models;

public class Event {
    private String EventDescription;
    private String ShortDescription;
    private String Title;
    private String Place;
    private String Date;
    private String Time;

    public Event() {
    }

    public Event(String eventDescription, String shortDescription, String title, String place, String date, String time, String eventImageURL) {
        EventDescription = eventDescription;
        ShortDescription = shortDescription;
        Title = title;
        Place = place;
        Date = date;
        Time = time;
        EventImageURL = eventImageURL;
    }

    private String EventImageURL;

    public String getEventDescription() {
        return EventDescription;
    }

    public void setEventDescription(String eventDescription) {
        EventDescription = eventDescription;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getEventImageURL() {
        return EventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        EventImageURL = eventImageURL;
    }
}
