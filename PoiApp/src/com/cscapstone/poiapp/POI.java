/**
 * 
 */
package com.cscapstone.poiapp;

import java.util.ArrayList;

/**
 * @author Capstone
 *
 */
public abstract class POI {

	private String name;
	private int id;
	private String type;
	
	private ArrayList<GeoPair> points;
	
	public abstract ArrayList getPointsArray();
	public abstract int getPointsListSize();
	public abstract String getName();
}
