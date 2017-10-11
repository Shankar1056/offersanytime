package bigappcompany.com.myoffers.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 08 Sep 2017 at 2:33 PM
 */

public class CatBannerFilter implements Parcelable {
	
	public CatBannerFilter(String banner_id, String banner_image, String category, String valid_for,String valid_till) {
		this.banner_id = banner_id;
		this.banner_image = banner_image;
		this.category = category;
		this.valid_for = valid_for;
		this.valid_till = valid_till;
	}
	
	protected CatBannerFilter(Parcel in) {
		banner_id = in.readString();
		banner_image = in.readString();
		category = in.readString();
		valid_for = in.readString();
		valid_till = in.readString();
	}
	
	public static final Creator<CatBanner> CREATOR = new Creator<CatBanner>() {
		@Override
		public CatBanner createFromParcel(Parcel in) {
			return new CatBanner(in);
		}
		
		@Override
		public CatBanner[] newArray(int size) {
			return new CatBanner[size];
		}
	};
	
	public String getBanner_id() {
		return banner_id;
	}
	
	public String getBanner_image() {
		return banner_image;
	}
	
	public String getCategory() {
		return category;
	}
	
	String banner_id;
	String banner_image;
	String category;
	
	public String getValid_for() {
		return valid_for;
	}
	
	String valid_for;
	
	public String getValid_till() {
		return valid_till;
	}
	
	String valid_till;
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(banner_id);
		dest.writeString(banner_image);
		dest.writeString(category);
		dest.writeString(valid_for);
		dest.writeString(valid_till);
	}
}
