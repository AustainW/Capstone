/**
 * 
 */
package com.csce.capstone.poi;

import java.util.ArrayList;

import com.csce.capstone.poi.GeoPair;

/**
 * @author Capstone
 *
 */
public class Point {

	private GeoPair point;
	private String name;
	private int id;
	private String type;
	private ArrayList<GeoPair> points;
	
	/**
	 * Constructor with all the elements
	 * @param name Name of the Point of Interest
	 * @param id Id of the Point of Interest
	 * @param lat Latitude of the Point of Interest
	 * @param longi Longitude of the Point of Interest
	 * @param type Type of the Point of Interest
	 */
	public Point(String name, int id, double lati, double longi, String type){
		this.name = name;
		
		this.id = id;
		this.type = type;
		point = new GeoPair(lati, longi);
		points = new ArrayList<GeoPair>();
		points.add(point);
	}
	/**
	 * Empty constructor
	 */
	public Point(){
		this.name = null;
		this.type = null;
		point = null;
	}

	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return this.point.getLatitude();
	}

	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return this.point.getLongitude();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.point.setLatitude(latitude);
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.point.setLongitude(longitude);
	}

	public GeoPair getPoint(){
		return this.point;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<GeoPair> getPointsArray(){
		return points;
	}
	
	public int getPointsListSize(){
		return points.size();
	}
}
