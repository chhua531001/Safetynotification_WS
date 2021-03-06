package com.example.chhua.safetynotification;

/**
 * Created by chhua on 2017/8/4.
 */

public class Transaction {
    String priority;
    String type;
    String subject;
    String detail;
    Long time_stamp;

    public Transaction() {
    }

    public Transaction(String priority, String type, String subject, String detail, Long time_stamp) {
        this.priority = priority;
        this.type = type;
        this.subject = subject;
        this.detail = detail;
        this.time_stamp = time_stamp;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getTimestamp() {
        return time_stamp;
    }

    public void setTimestamp(Long timestamp) {
        this.time_stamp = timestamp;
    }
}


