package bigappcompany.com.myoffers.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Jul 2017 at 3:08 PM
 */

public class CategoryTabModel {
	
	public String getB_cat_id() {
		return b_cat_id;
	}
	
	public String getB_cat_name() {
		return b_cat_name;
	}
	
	String b_cat_id;
	String b_cat_name;
	
	public String getDeselect_icon() {
		return deselect_icon;
	}
	
	String deselect_icon;
	
	public String getIcon() {
		return icon;
	}
	
	String icon;
	
	public CategoryTabModel(String b_cat_id, String b_cat_name,String icon,String deselect_icon)
	{
		this.b_cat_id= b_cat_id;
		this.b_cat_name= b_cat_name;
		this.icon= icon;
		this.deselect_icon= deselect_icon;
	}
}
