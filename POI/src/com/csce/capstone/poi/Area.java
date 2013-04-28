/**
 * 
 */
package com.csce.capstone.poi;

import java.util.ArrayList;

/**
 * @author Capstone
 *
 */
public class Area implements POI {

	private String name;
	private int id;
	private String type;
	private ArrayList<GeoPair> points;
	private ArrayList<String> metaTags;
	
	public Area(String name, int id, ArrayList<GeoPair> listCoords,
			String type) {
		this.name = name;
		this.type = type;
		this.id = id;
		this.points = listCoords;
	}
	
	public Area(String name, int id, String type, ArrayList<String> tags) {
		this.name = name;
		this.type = type;
		this.id = id;
		this.points = null;
		this.metaTags = tags;
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
	
	public ArrayList<GeoPair> getPointsArray(){
		return points;
	}
	
	public int getPointsListSize(){
		return points.size();
	}

	@Override
	public ArrayList<String> getMetaTagsList() {
		
		return this.metaTags;
	}
	@Override
	public int getMetaTagsSize() {
		// TODO Auto-generated method stub
		return this.metaTags.size();
	}
}
