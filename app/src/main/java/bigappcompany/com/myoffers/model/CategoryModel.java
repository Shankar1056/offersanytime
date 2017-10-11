package bigappcompany.com.myoffers.model;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Jul 2017 at 4:18 PM
 */

public class CategoryModel {
	public String getStatus() {
		return status;
	}
	
	String status;
	
	public ArrayList<CatDataModel> getData() {
		return data;
	}
	
	ArrayList<CatDataModel> data;
	
	}
