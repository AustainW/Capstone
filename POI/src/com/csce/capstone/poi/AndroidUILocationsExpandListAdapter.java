/**
 * 
 */
package com.csce.capstone.poi;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/**
 * @author Capstone
 *
 */
public class AndroidUILocationsExpandListAdapter extends BaseExpandableListAdapter implements Filterable {

	
	private ArrayList<POI> pointOfInterestData;
	private ArrayList<POI> originalPOIData;
	private Context appContext;
	
	private LocationsFilter filter;
	
	
	public AndroidUILocationsExpandListAdapter(Context context, ArrayList<POI> poiData){
		this.appContext = context;
		this.pointOfInterestData = poiData;
		this.originalPOIData = poiData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = convertView;
		LocationHolder holder = new LocationHolder();
		
		if(v == null){
			LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row, parent, false);	
			TextView lt = (TextView) v.findViewById(R.id.itemName);
			holder.LocationTag = lt;
			v.setTag(holder);
		}
		else{
			v.getTag();
		}
		
		
		String tag = pointOfInterestData.get(groupPosition).getMetaTagsList().get(childPosition);
		
		holder.LocationTag.setText(tag);
		
		return v;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v = convertView;
		LocationHolder holder = new LocationHolder();
		
		if(v == null){
			LayoutInflater inflater = (LayoutInflater) appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.row, parent, false);	
			TextView ln = (TextView) v.findViewById(R.id.rowTextView);
			holder.LocationName = ln;
			v.setTag(holder);
		}else{
			holder = (LocationHolder) v.getTag();
		}
		
		POI pointOfInterest = pointOfInterestData.get(groupPosition);
		
		holder.LocationName.setText(pointOfInterest.getName());
		
		return v;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void notifyDataSetInvalidated(){
		super.notifyDataSetInvalidated();
		this.resetData();
	}

	@Override
	public Filter getFilter() {
		if(filter == null){
			filter = new LocationsFilter();
		}
		return filter;
	}
	
	public void resetData(){
		pointOfInterestData = originalPOIData;
	}
	
	
	private static class LocationHolder{
		public TextView LocationName;
		public TextView LocationTag;
	}
	
	private class LocationsFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			
			FilterResults result = new FilterResults();
			if(constraint != null && constraint.toString().length() > 0){
				result.values = originalPOIData;
				result.count = originalPOIData.size();
				
			}else{
				ArrayList<POI> nPOIList = new ArrayList<POI>();
				for(POI p : pointOfInterestData){
					ArrayList<String> tags = p.getMetaTagsList();
					for(String s : tags){
						if(s.toUpperCase().startsWith(constraint.toString().toUpperCase())){
							nPOIList.add(p);
							break;
						}
					}
				}
				result.values = nPOIList;
				result.count = nPOIList.size();
			}
			return result;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if(results.count == 0){
				notifyDataSetInvalidated();
			}
			else{
				pointOfInterestData = (ArrayList<POI>) results.values;
				notifyDataSetChanged();
			}
		}
		
		
	}
}
