/**
 * 
 */
package com.cscapstone.poiapp;

import java.util.ArrayList;

/**
 * @author Capstone
 *
 */
public class AreaOfInterest extends POI {

	private String name;
	private int id;
	private String type;
	private ArrayList<GeoPair> points;
	
	public AreaOfInterest(String name, int id,
			ArrayList<GeoPair> listCoords, String type) {
		this.name = name;
		this.type = type;
		this.id = id;
		this.points = listCoords;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	
	/**
	 * @param points the points to set
	 */
	public void setPoints(ArrayList<GeoPair> points) {
		this.points = points;
	}

	public ArrayList<GeoPair> getPointsArray(){
		return points;
	}
	
	public int getPointsListSize(){
		return points.size();
	}

}
