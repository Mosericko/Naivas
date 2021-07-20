package com.mdevs.naivas.customer.classes;

public class Notifications {
    String user_id,title,message,dateTime;

    public Notifications(String user_id, String title, String message, String dateTime) {
        this.user_id = user_id;
        this.title = title;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDateTime() {
        return dateTime;
    }
}
