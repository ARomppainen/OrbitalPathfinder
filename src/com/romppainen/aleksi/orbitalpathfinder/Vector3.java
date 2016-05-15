package com.romppainen.aleksi.orbitalpathfinder;

/**
 * Simple 3 dimensional vector implementation.
 * 
 * @author  Aleksi Romppainen   <aromppa@gmail.com>
 */
public class Vector3 {
    
    public double x;
    public double y;
    public double z;
    
    /**
     * Default constructor, sets all values to 0.
     */
    public Vector3() {
        x = y = z = 0;
    }
    
    /**
     * Sets desired values.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector3(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }
    
    /**
     * Copy constructor.
     * 
     * @param other 
     */
    public Vector3(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    
    /**
     * Constructs a vector from A to B.
     * 
     * @param A starting point
     * @param B end point
     */
    public Vector3(Vector3 A, Vector3 B) {  
        x = B.x - A.x;
        y = B.y - A.y;
        z = B.z - A.z;
    }
    
    // http://astro.uchicago.edu/cosmus/tech/latlong.html
    /**
     * Constructs a vector using latitude, longitude, radius and altitude.
     * 
     * @param latitude  latitude in degrees
     * @param longitude longitude in degrees
     * @param radius    circle (earth) radius
     * @param altitude  altitude from surface
     */
    public Vector3(double latitude, double longitude, double radius, double altitude) {
        double r = radius + altitude;
        double lat = Math.toRadians(latitude);
        double lon = Math.toRadians(longitude);
        
        x = -r * Math.cos(lat) * Math.cos(lon);
        y = r * Math.sin(lat);
        z = r * Math.cos(lat) * Math.sin(lon);
    }
    
    /**
     * Scales the vector v with s and performs vector addition.
     * 
     * @param v
     * @param s 
     */
    public void scaleAndAdd(Vector3 v, double s) {
        this.x += v.x * s;
        this.y += v.y * s;
        this.z += v.z * s;
    }

    /**
     * Calculates the length of the vector.
     * 
     * @return length of vector
     */
    public double length()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * Calculates the distance between 2 points.
     * 
     * @param v1    starting point
     * @param v2    end point
     * @return      the distance between v1 and v2
     */
    public static double distance(Vector3 v1, Vector3 v2) {
        double x = v2.x - v1.x;
        double y = v2.y - v1.y;
        double z = v2.z - v1.z;
        
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    /**
     * Normalizes (set the length to be equal to 1) the vector.
     * 
     */
    public void normalize() {
        double length = this.length();
        
        x /= length;
        y /= length;
        z /= length;
    }
    
    @Override
    public String toString()
    {
	return "x: " + x + " y: " + y + " z: " + z;
    }
}
