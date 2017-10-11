package bigappcompany.com.myoffers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Jul 2017 at 4:19 PM
 */

public class LocationModel implements Parcelable{
	
	String shop_id;
	
	public Double getDistance() {
		return distance;
	}
	
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	
	Double distance;
	
	public LocationModel(String shop_id, String contact_person, String shop_name, String shop_slug,
	                     String shop_category, String shop_image, String ph_no, String email,
	                     String password, String associated_with, String parent_id, String country,
	                     String state, String city, String address_line_1, String address_line_2,
	                     String lat, String lng, String status, String shop_is_active, String registered_on,String
	                         location, Double distance)
	{
		this.shop_id = shop_id;
		this.contact_person = contact_person;
		this.shop_name = shop_name;
		this.shop_slug = shop_slug;
		this.shop_category = shop_category;
		this.shop_image = shop_image;
		this.email = email;
		this.password = password;
		this.associated_with = associated_with;
		this.parent_id = parent_id;
		this.country = country;
		this.state = state;
		this.city = city;
		this.address_line_1 = address_line_1;
		this.address_line_2 = address_line_2;
		this.lat = lat;
		this.lng = lng;
		this.status = status;
		this.shop_is_active = shop_is_active;
		this.registered_on = registered_on;
		this.location = location;
		this.distance = distance;
		
	}
	
	protected LocationModel(Parcel in) {
		shop_id = in.readString();
		contact_person = in.readString();
		shop_name = in.readString();
		shop_slug = in.readString();
		shop_category = in.readString();
		shop_image = in.readString();
		ph_no = in.readString();
		email = in.readString();
		password = in.readString();
		associated_with = in.readString();
		parent_id = in.readString();
		country = in.readString();
		state = in.readString();
		city = in.readString();
		address_line_1 = in.readString();
		address_line_2 = in.readString();
		lat = in.readString();
		lng = in.readString();
		status = in.readString();
		shop_is_active = in.readString();
		registered_on = in.readString();
		location = in.readString();
		distance = in.readDouble();
	}
	
	public static final Creator<LocationModel> CREATOR = new Creator<LocationModel>() {
		@Override
		public LocationModel createFromParcel(Parcel in) {
			return new LocationModel(in);
		}
		
		@Override
		public LocationModel[] newArray(int size) {
			return new LocationModel[size];
		}
	};
	
	public String getShop_id() {
		return shop_id;
	}
	
	public String getContact_person() {
		return contact_person;
	}
	
	public String getShop_name() {
		return shop_name;
	}
	
	public String getShop_slug() {
		return shop_slug;
	}
	
	public String getShop_category() {
		return shop_category;
	}
	
	public String getShop_image() {
		return shop_image;
	}
	
	public String getPh_no() {
		return ph_no;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getAssociated_with() {
		return associated_with;
	}
	
	public String getParent_id() {
		return parent_id;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getState() {
		return state;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getAddress_line_1() {
		return address_line_1;
	}
	
	public String getAddress_line_2() {
		return address_line_2;
	}
	
	public String getLat() {
		return lat;
	}
	
	public String getLng() {
		return lng;
	}
	
	public String getStatus() {
		return status;
	}
	
	public String getShop_is_active() {
		return shop_is_active;
	}
	
	public String getRegistered_on() {
		return registered_on;
	}
	
	String contact_person;
	String shop_name;
	String shop_slug;
	String shop_category;
	String shop_image;
	String ph_no;
	String email;
	String password;
	String associated_with;
	String parent_id;
	String country;
	String state;
	String city;
	String address_line_1;
	String address_line_2;
	String lat;
	String lng;
	String status;
	String shop_is_active;
	String registered_on;
	
	
	
	public String getLocation() {
		return location;
	}
	
	String location;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(shop_id);
		dest.writeString(contact_person);
		dest.writeString(shop_name);
		dest.writeString(shop_slug);
		dest.writeString(shop_category);
		dest.writeString(shop_image);
		dest.writeString(ph_no);
		dest.writeString(email);
		dest.writeString(password);
		dest.writeString(associated_with);
		dest.writeString(parent_id);
		dest.writeString(country);
		dest.writeString(state);
		dest.writeString(city);
		dest.writeString(address_line_1);
		dest.writeString(address_line_2);
		dest.writeString(lat);
		dest.writeString(lng);
		dest.writeString(status);
		dest.writeString(shop_is_active);
		dest.writeString(registered_on);
		dest.writeString(location);
		dest.writeDouble(distance);
	}
}
