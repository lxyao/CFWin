package com.cfwin.base.beans;

/**
 * Created by Administrator on 2018/10/10.
 */

public class SignatureInfo {
    private String sign;

    public SignatureInfo() {
    }

    public SignatureInfo(String sign) {
        sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "SignatureInfo{" +
                "sign='" + sign + '\'' +
                '}';
    }
}
