package com.rajaryan.eatit;

public class ChatClass {
    String Message,Reply;

    public ChatClass() {
    }

    public ChatClass(String message, String reply) {
        Message = message;
        Reply = reply;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }
}
