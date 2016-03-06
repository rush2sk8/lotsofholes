package com.example.lotsofholes;


public class FirebaseLocationUnit {

    private double lon, lat;
    private double frequency;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public FirebaseLocationUnit(double lon, double lat, double frequency) {
	super();
	this.lon = lon;
	this.lat = lat;
	this.frequency = frequency;
    }

    public FirebaseLocationUnit(double lon, double lat) {
	super();
	this.lon = lon;
	this.lat = lat;
    }



}
