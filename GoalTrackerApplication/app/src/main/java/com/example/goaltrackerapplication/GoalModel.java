package com.example.goaltrackerapplication;

public class GoalModel {
    private int id;
    private String title;
    private String desc;
    private String dateend;
    private String timeend;
    private int notification;
    private int completeness;

    public GoalModel(int id, String title, String desc, String dateend, String timeend, int notification, int completeness) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.dateend = dateend;
        this.timeend = timeend;
        this.notification = notification;
        this.completeness = completeness;
    }

    @Override
    public String toString() {
        return "GoalModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", dateend='" + dateend + '\'' +
                ", timeend='" + timeend + '\'' +
                ", notification=" + notification +
                ", completeness=" + completeness +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDateend() {
        return dateend;
    }

    public void setDateend(String dateend) {
        this.dateend = dateend;
    }

    public String getTimeend() {
        return timeend;
    }

    public void setTimeend(String timeend) {
        this.timeend = timeend;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getCompleteness() {
        return completeness;
    }

    public void setCompleteness(int completeness) {
        this.completeness = completeness;
    }
}