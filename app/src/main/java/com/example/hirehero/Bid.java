package com.example.hirehero;
import java.io.Serializable;

public class Bid implements Serializable {
    private String bidderName;
    private String bidderContact;
    private int bidAmount;
    private String uid;

    public Bid() {

    }

    public Bid(String bidderName, String bidderContact, int bidAmount, String uid) {
        this.bidderName = bidderName;
        this.bidderContact = bidderContact;
        this.bidAmount = bidAmount;
        this.uid = uid;
    }
    public String getBidderName() {
        return bidderName;
    }

    public String getBidderContact() {
        return bidderContact;
    }

    public int getBidAmount() {
        return bidAmount;
    }
    public String getUid() {
        return uid;
    }
}

