package com.eok.eok.Models;

public class Announcement {

        private String description;

        private String Title;



    public Announcement() {
    }

    public Announcement( String title,String description) {

        Title = title;
        this.description = description;

    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return Title;
    }

        public void setTitle(String title) {
        Title = title;
    }







}
