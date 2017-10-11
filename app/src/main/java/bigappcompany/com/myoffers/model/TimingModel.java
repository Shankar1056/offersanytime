package bigappcompany.com.myoffers.model;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 09 Aug 2017 at 5:12 PM
 */

public class TimingModel {
	public String getTime_id() {
		return time_id;
	}
	
	public String getDay() {
		return day;
	}
	
	public String getOpen_time() {
		return open_time;
	}
	
	public String getClose_time() {
		return close_time;
	}
	
	String time_id;
	String day;
	String open_time;
	String close_time;
	
	public String getOpen_status() {
		return open_status;
	}
	
	String open_status;
	
	public String getTypefacetype() {
		return typefacetype;
	}
	
	public void setTypefacetype(String typefacetype) {
		this.typefacetype = typefacetype;
	}
	
	String typefacetype;
}
