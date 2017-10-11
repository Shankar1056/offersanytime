package bigappcompany.com.myoffers.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 09 Aug 2017 at 1:30 PM
 */

public class SearchModel {
	public String getId() {
		return id;
	}
	
	public String getShop_name() {
		return shop_name;
	}
	
	public String getCat_location() {
		return cat_location;
	}
	
	public String getLat() {
		return lat;
	}
	
	public String getLon() {
		return lon;
	}
	
	public String getDaydate() {
		return daydate;
	}
	
	public String getImage() {
		return image;
	}
	
	String id;
	String shop_name;
	String cat_location;
	String lat;
	String lon;
	String daydate;
	String image;
	
	
	public Double getDistancee() {
		return distancee;
	}
	
	Double distancee;
	
	public SearchModel(String id, String shop_name, String cat_location, String lat, String lon, String daydate, String
	    image, Double distancee)
	{
	this.id = id;
	this.shop_name = shop_name;
	this.cat_location = cat_location;
	this.lat = lat;
	this.lon = lon;
	this.daydate = daydate;
	this.image = image;
	this.distancee = distancee;
		
	}
}
