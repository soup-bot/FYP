package com.example.hirehero;
import java.io.Serializable;

public class Bid implements Serializable {
    private String bidderName;
    private String bidderContact;
    private int bidAmount;
    private String uid;

    public String getBidid() {
        return bidid;
    }

    public String getListingid() {
        return listingid;
    }

    private String bidid;
    private String listingid;

    public Bid() {

    }

    public Bid(String bidderName, String bidderContact, int bidAmount, String uid, String listingid, String bidid) {
        this.bidderName = bidderName;
        this.bidderContact = bidderContact;
        this.bidAmount = bidAmount;
        this.uid = uid;
        this.listingid = listingid;
        this.bidid = bidid;
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

