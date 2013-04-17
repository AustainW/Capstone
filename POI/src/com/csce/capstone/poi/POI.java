/**
 * 
 */
package com.csce.capstone.poi;

import java.util.ArrayList;

/**
 * @author Capstone
 *
 */
public interface POI {

	public abstract ArrayList<GeoPair> getPointsArray();
	public abstract int getPointsListSize();
	public abstract ArrayList<String> getMetaTagsList();
	public abstract String getName();
	public abstract int getId();
}
