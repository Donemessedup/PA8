/**
 * This file houses the Place class to store place data from the google APIs
 * CPSC 312-01, Fall 2021
 * Programming Assignment #8
 * No sources to cite.
 *
 * @author Aaron Miller
 * @author Wesley Muehlhausen
 * @version v1.0 11/24/21
 */
package com.example.pa8;

public class Place {

    private String id;
    private String name;
    private String vicinity;
    private double rating;
    private String photoReference;

    // constructor for the place class
    public Place(String id, String name, String vicinity, double rating, String photoReference) {
        this.id = id;
        this.name = name;
        this.vicinity = vicinity;
        this.rating = rating;
        this.photoReference = photoReference;
    }

    // getter for the id variable
    // @return returns the id value
    public String getId() {
        return id;
    }

    // getter for the name variable
    // @return returns the name value
    public String getName() {
        return name;
    }

    // setter for the name variable
    // @param name: value that the name variable will be set to
    public void setName(String name) {
        this.name = name;
    }

    // getter for the vicinity variable
    // @return returns the vicinity value
    public String getVicinity() {
        return vicinity;
    }

    // getter for the rating variable
    // @return returns the rating value
    public double getRating() {
        return rating;
    }

    // getter for the photo reference variable
    // @return returns the photo referencegit  value
    public String getPhotoReference() {
        return photoReference;
    }

}
