package com.cfwin.base.beans;

/**
 * Created by Administrator on 2018/10/10.
 */

public class DiscriptionInfo {
    private String Body;
    private String AuthFrom;
    private String AuthTo;

    public DiscriptionInfo() {
    }

    public DiscriptionInfo(String body, String authFrom, String authTo) {
        Body = body;
        AuthFrom = authFrom;
        AuthTo = authTo;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getAuthFrom() {
        return AuthFrom;
    }

    public void setAuthFrom(String authFrom) {
        AuthFrom = authFrom;
    }

    public String getAuthTo() {
        return AuthTo;
    }

    public void setAuthTo(String authTo) {
        AuthTo = authTo;
    }

    @Override
    public String toString() {
        return "DiscriptionInfo{" +
                "Body='" + Body + '\'' +
                ", AuthFrom='" + AuthFrom + '\'' +
                ", AuthTo='" + AuthTo + '\'' +
                '}';
    }
}
