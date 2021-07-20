package com.mdevs.naivas.customer.classes;

public class FeedBackData {

    String sender, receiver, title, message, dateTime,replyMessage,replyDate;
    String id;


    public FeedBackData(String title, String message, String dateTime) {
        this.title = title;
        this.message = message;
        this.dateTime = dateTime;
    }

    public FeedBackData(String title, String message, String dateTime, String replyMessage, String id) {
        this.title = title;
        this.message = message;
        this.dateTime = dateTime;
        this.replyMessage = replyMessage;
        this.id = id;
    }

    public FeedBackData(String receiver, String title, String message, String dateTime, String replyMessage, String replyDate) {
        this.receiver = receiver;
        this.title = title;
        this.message = message;
        this.dateTime = dateTime;
        this.replyMessage = replyMessage;
        this.replyDate = replyDate;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
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

    public String getId() {
        return id;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public String getReplyDate() {
        return replyDate;
    }
}
