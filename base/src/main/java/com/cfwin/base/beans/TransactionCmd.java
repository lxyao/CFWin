package com.cfwin.base.beans;

/**
 * Created by Administrator on 2018/10/10.
 */

public class TransactionCmd {
    public ContentInfo Content;
    public CommissionInfo Commission;
    public SignatureInfo SignContent;
    public SignatureInfo SignCommision;
    public DiscriptionInfo Discription;

    public TransactionCmd() {
        Content = new ContentInfo();
        Commission = new CommissionInfo();
        SignContent = new SignatureInfo();
        SignCommision = new SignatureInfo();
        Discription = new DiscriptionInfo();
    }

    public TransactionCmd(ContentInfo content, CommissionInfo commission, SignatureInfo signature,SignatureInfo signCommision,DiscriptionInfo discription) {
        Content = content;
        Commission = commission;
        SignContent = signature;
        SignCommision =signCommision;
        Discription = discription;
    }


    public SignatureInfo getSignContent() {
        return SignContent;
    }

    public void setSignContent(SignatureInfo signContent) {
        SignContent = signContent;
    }
    public ContentInfo getContent() {
        return Content;
    }

    public void setContent(ContentInfo content) {
        Content = content;
    }

    public CommissionInfo getCommission() {
        return Commission;
    }

    public void setCommission(CommissionInfo commission) {
        Commission = commission;
    }

    public DiscriptionInfo getDiscription() {
        return Discription;
    }

    public void setDiscription(DiscriptionInfo discription) {
        Discription = discription;
    }

    public SignatureInfo getSignCommision() {
        return SignCommision;
    }

    public void setSignCommision(SignatureInfo signCommision) {
        SignCommision = signCommision;
    }

    @Override
    public String toString() {
        return "TransactionCmd{" +
                "Content=" + Content +
                ", Commission=" + Commission +
                ", SignContent=" + SignContent +
                ", SignCommision=" + SignCommision +
                ", Discription=" + Discription +
                '}';
    }
}
