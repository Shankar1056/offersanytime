package bigappcompany.com.myoffers.model;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Jul 2017 at 4:45 PM
 */

public class CatDataModel {
	public String getId() {
		return id;
	}
	
	public String getShop_name() {
		return shop_name;
	}
	
	public String getShop_image() {
		return shop_image;
	}
	
	public ArrayList<LocationModel> getLocations() {
		return locations;
	}
	
	String id;
	String shop_name;
	String shop_image;
	
	public String getParent_add_1() {
		return parent_add_1;
	}
	
	public String getParent_add_2() {
		return parent_add_2;
	}
	
	public String getParent_lat() {
		return parent_lat;
	}
	
	public String getParent_long() {
		return parent_long;
	}
	
	public String getRegistered_date() {
		return registered_date;
	}
	
	String parent_add_1;
	String parent_add_2;
	String parent_lat;
	String parent_long;
	String registered_date;
	
	public String getParent_id() {
		return parent_id;
	}
	
	String parent_id;
	
	public Double getDistance() {
		return distance;
	}
	
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	Double distance;
	
	public String getShop_location() {
		return shop_location;
	}
	
	String shop_location;
	ArrayList<LocationModel> locations;
	public CatDataModel(String id,String shop_name, String shop_image, String parent_add_1,String
	    parent_add_2, String registered_date, Double distance, String parent_id,String shop_location)
	{
		this.id = id;
		this.shop_name = shop_name;
		this.shop_image = shop_image;
		this.parent_add_1 = parent_add_1;
		this.parent_add_2 = parent_add_2;
		this.registered_date = registered_date;
		this.distance = distance;
		this.parent_id = parent_id;
		this.shop_location = shop_location;
	}
	
}
