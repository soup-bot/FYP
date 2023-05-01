//Object for a service listing

package com.example.hirehero;

import java.io.Serializable;

public class Listing implements Serializable {
    private String service;
    private String price;
    private String details;
    private String contact;
    private String listingID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    public String getUserID() {
        return userID;
    }

    private String userID;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Listing() {
    }

    public Listing(String service, String price, String details, String contact, String listingID, String userID, String userName) {
        this.service = service;
        this.price = price;
        this.details = details;
        this.contact = contact;
        this.listingID = listingID;
        this.userID = userID;
        this.userName = userName;
    }

    public void setServiceName(String service) {
        this.service = service;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getListingID() {
        return listingID;
    }
}