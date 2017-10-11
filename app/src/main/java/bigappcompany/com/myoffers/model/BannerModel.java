package bigappcompany.com.myoffers.model;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 14 Jul 2017 at 5:05 PM
 */

public class BannerModel {
	
	public String getStatus() {
		return status;
	}
	
	public ArrayList<Categorydate> getData() {
		return data;
	}
	
	String status;
	ArrayList<Categorydate> data;
}
