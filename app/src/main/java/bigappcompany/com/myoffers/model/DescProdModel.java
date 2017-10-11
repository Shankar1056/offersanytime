package bigappcompany.com.myoffers.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 06 Jul 2017 at 4:43 PM
 */

public class DescProdModel {
	
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
	
	
	String shop_id;
	String contact_person;
	String shop_name;
	
	public String getShop_image() {
		return shop_image;
	}
	
	String shop_image;
	String phone;
	String email;
	String address_line_1;
	String address_line_2;
	String latitude;
	String longitude;
	String parent;
	String category;
	
	public DescProdModel(String shop_id,String contact_person,String shop_name,String shop_image,String phone,String
	    email,
	                     String address_line_1,String address_line_2,String latitude,String longitude,
	                     String parent, String category)
	{
		this.shop_id = shop_id;
		this.contact_person = contact_person;
		this.shop_name = shop_name;
		this.shop_image = shop_image;
		this.phone = phone;
		this.email = email;
		this.address_line_1 = address_line_1;
		this.address_line_2 = address_line_2;
		this.latitude = latitude;
		this.longitude = longitude;
		this.parent = parent;
		this.category = category;
	}
}
