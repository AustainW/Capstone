/**
 * 
 */
package com.csce.capstone.poi;

/**
 * @author Capstone
 *
 */
public class GeoPair {

	private double longitude;
	private double latitude;
	private int radius;
	
	public GeoPair(double lati, double longi) {
		this.longitude = longi;
		this.latitude = lati;
//		this.radius = radius;
	}
	
	public GeoPair(){
		
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int compareTo(double curLati, double curLongi){
		if(this.latitude+this.longitude > curLati+curLongi){
			return 2;
		}
		else if(this.latitude+this.longitude < curLati+curLongi){
			return 1;
		}
		else {
			return 0;
		}
	}
}
