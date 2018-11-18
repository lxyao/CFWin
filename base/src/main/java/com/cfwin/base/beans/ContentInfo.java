package com.cfwin.base.beans;

/**
 * Created by Administrator on 2018/10/10.
 */

public class ContentInfo {
    private String from;
    private String to;
    private String balance;
    private String serial;
    private String timestamp;

    public ContentInfo() {
    }

    public ContentInfo(String from, String to, String balance, String serial, String timestamp) {
        this.from = from;
        this.to = to;
        this.balance = balance;
        this.serial = serial;
        this.timestamp = timestamp;
    }

    public void Init(String from, String to, String balance, String serial, String timestamp) {
        this.from = from;
        this.to = to;
        this.balance = balance;
        this.serial = serial;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        to = to;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        balance = balance;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        serial = serial;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ContentInfo{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", balance=" + balance +
                ", serial=" + serial +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
