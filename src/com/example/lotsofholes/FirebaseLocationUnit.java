package com.example.lotsofholes;


public class FirebaseLocationUnit {

    private String lon, lat,frequency;

    public String getLon() {
	return lon;
    }

    public void setLon(String lon) {
	this.lon = lon;
    }

    public String getLat() {
	return lat;
    }

    public void setLat(String lat) {
	this.lat = lat;
    }

    public FirebaseLocationUnit(String lon, String lat) {
	this.lon = lon;
	this.lat = lat;
    }

    
    public FirebaseLocationUnit(String lon, String lat, String frequency) {
	super();
	this.lon = lon;
	this.lat = lat;
	this.frequency = frequency;
    }

    public String getFrequency() {
	return frequency;
    }

    public void setFrequency(String frequency) {
	this.frequency = frequency;
    }



}
