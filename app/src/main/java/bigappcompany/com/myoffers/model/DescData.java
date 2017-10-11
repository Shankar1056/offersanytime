package bigappcompany.com.myoffers.model;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Jul 2017 at 8:03 PM
 */

public class DescData {
	public String getShop_id() {
		return shop_id;
	}
	
	public String getContact_person() {
		return contact_person;
	}
	
	public String getShop_name() {
		return shop_name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getAddress_line_1() {
		return address_line_1;
	}
	
	public String getAddress_line_2() {
		return address_line_2;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getParent() {
		return parent;
	}
	
	public String getCategory() {
		return category;
	}
	
	public ArrayList<CatBanner> getBanners() {
		return banners;
	}
	
	String shop_id;
	String contact_person;
	String shop_name;
	String phone;
	String email;
	String address_line_1;
	String address_line_2;
	String latitude;
	String longitude;
	String parent;
	String category;
	
	public String getWebsite() {
		return website;
	}
	
	String website;
	
	public String getShop_image() {
		return shop_image;
	}
	
	String shop_image;
	ArrayList<CatBanner> banners;
	
	public ArrayList<TimingModel> getTimings() {
		return timings;
	}
	
	ArrayList<TimingModel> timings;
}
