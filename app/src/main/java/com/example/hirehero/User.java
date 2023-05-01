//Class for user object

package com.example.hirehero;

public class User {
    public String name, email;
    public float rating;
    public int numOfRatings;

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }



    public float getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User(String name, String email, Float rating, Integer numOfRatings){
        this.name=name;
        this.email=email;
        this.rating=rating;
        this.numOfRatings=numOfRatings;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
