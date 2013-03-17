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
	
	public GeoPair(double lati, double longi) {
		this.longitude = longi;
		this.latitude = lati;
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
