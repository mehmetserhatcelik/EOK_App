package com.eok.eok.Models;

public class User {
    private long hotPursuitRecord;
    private long timeRushRecord;
    private String email;
    private String name;
    private int isAdmin;

    private String userPhotoUrl;
    private Long distance;
    private String uid;

    public void setHotPursuitRecord(long hotPursuitRecord) {
        this.hotPursuitRecord = hotPursuitRecord;
    }

    public void setTimeRushRecord(long timeRushRecord) {
        this.timeRushRecord = timeRushRecord;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getWordGameRecord() {
        return wordGameRecord;
    }

    public void setWordGameRecord(long wordGameRecord) {
        this.wordGameRecord = wordGameRecord;
    }

    private String id;
    private long wordGameRecord;



    public long getHotPursuitRecord() {
        return hotPursuitRecord;
    }

    public long getTimeRushRecord() {
        return timeRushRecord;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public int getIsAdmin() {
        return isAdmin;
    }
    public String getUserPhotoUrl()
    {
        return userPhotoUrl;
    }
    public User()
    {}

    public User(String name, String userPhotoUrl) {
        this.name = name;
        this.userPhotoUrl = userPhotoUrl;
    }
    public User(String name, String userPhotoUrl,long trscore) {
        this.name = name;
        this.userPhotoUrl = userPhotoUrl;
        this.timeRushRecord = trscore;
    }
    public User(String name,long hpscore ,String userPhotoUrl) {
        this.name = name;
        this.userPhotoUrl = userPhotoUrl;
        this.hotPursuitRecord = hpscore;
    }

    public User(long hotPursuitRecord, long timeRushRecord, String email, String name, int isAdmin, String userPhotoUrl) {
        this.hotPursuitRecord = hotPursuitRecord;
        this.timeRushRecord = timeRushRecord;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
        this.userPhotoUrl = userPhotoUrl;
    }
    public Long getDistance()
    {
        return distance;
    }
    public void setDistance(Long distance)
    {
        this.distance = distance;
    }
    public String getId()
    {
        return id;
    }
    public void setID(String id)
    {
        this.id = id;
    }
    public boolean equals(String uid)
    {
        return this.getUid().equals(uid);
    }


}
