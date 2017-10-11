package bigappcompany.com.myoffers.network;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import bigappcompany.com.myoffers.R;

/**
 * Created by embed on 13/5/15.
 */
public class Utility {
	private static final int HTTP_TIMEOUT = 60 * 1000; // milliseconds
	
	private static HttpClient mHttpClient;
	
	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return mHttpClient;
		
	}
	
	
	public static String executeHttpPost(String url,
	                                     ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
			    postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String executeHttpGet(String url) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static String executeHttpPut(String url,
	                                    ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPut request = new HttpPut();
			request.setURI(new URI(url));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
			    postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity()
			    .getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line).append(NL);
			}
			in.close();
			
			return sb.toString();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static boolean isNetworkAvailable(Context context) {
		
		ConnectivityManager connectivity = null;
		
		try {
			connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (connectivity != null) {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();
				
				if (info != null) {
					for (NetworkInfo anInfo : info) {
						if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
							
							return true;
						}
					}
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connectivity != null) {
			}
		}
		return false;
	}
	
	public static Typeface font(Context context, String fonttype) {
		Typeface typeface = null;
		if (fonttype.equalsIgnoreCase("black")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.black_font));
		} else if (fonttype.equalsIgnoreCase("regular")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.regular_font));
		} else if (fonttype.equalsIgnoreCase("medium")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.medium_font));
		} else if (fonttype.equalsIgnoreCase("bold")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.bold_font));
		} else if (fonttype.equalsIgnoreCase("blackitalic")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.black_italic_font));
		} else if (fonttype.equalsIgnoreCase("light")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.light_font));
		} else if (fonttype.equalsIgnoreCase("italic")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.italic_font));
		} else if (fonttype.equalsIgnoreCase("thin")) {
			typeface = Typeface.createFromAsset(context.getAssets(),
			    context.getResources().getString(R.string.thin_font));
		}
		return typeface;
	}
	
	public static String GetTodaysDate() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(c.getTime());
	}
	
	public static String GetDateTimeDifference(Date startDate, Date endDate) {
		
		//milliseconds
		long different = endDate.getTime() - startDate.getTime();
		
		System.out.println("startDate : " + startDate);
		System.out.println("endDate : " + endDate);
		System.out.println("different : " + different);
		
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long monthInMilli = daysInMilli * 30;
		
		long elapsedMonths = different / monthInMilli;
		different = different % monthInMilli;
		
		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		
		long elapsedSeconds = different / secondsInMilli;
		long totalSecond = (elapsedSeconds + (60 * elapsedMinutes) + (3600 * elapsedHours) + (86400 * elapsedDays) + (2592000 * elapsedMonths));
		
		Log.i("elapsedMonths", "" + elapsedMonths);
		Log.i("elapsedDays", "" + elapsedDays);
		Log.i("elapsedHours", "" + elapsedHours);
		Log.i("elapsedMinutes", "" + elapsedMinutes);
		Log.i("elapsedSeconds", "" + elapsedSeconds);
		if (totalSecond < 60) {
			return "now";
		}
		if ((totalSecond >= 60) && (!(totalSecond >= 120))) {
			return elapsedMinutes + " min ago";
		}
		if ((totalSecond >= 120) && (!(totalSecond >= 3600))) {
			return elapsedMinutes + " mins ago";
		}
		if ((totalSecond >= 3600) && (!(totalSecond >= 7200))) {
			return elapsedHours + " hour ago";
		}
		if ((totalSecond >= 7200) && (!(totalSecond >= 86400))) {
			return elapsedHours + " hours ago";
		}
		if ((totalSecond >= 86400) && (totalSecond < 172800)) {
			return elapsedDays + " day ago";
		}
		if ((totalSecond >= 172800) && (!(totalSecond >= 2592000))) {
			return elapsedDays + " days ago";
		}
		if ((totalSecond >= 2592000) && (!(totalSecond >= 5184000))) {
			return elapsedMonths + " month ago";
		}
		if ((totalSecond >= 5184000) && (!(totalSecond < 31104000))) {
			return elapsedMonths + " months ago";
		} else {
			return "" + startDate;
		}
		
	}
	
	public static String calculatedistanceinkm(double latti, double longi, String latitude, String longitude) {
		
		
		double dist = 0;
		try {
			
			
			double theta = longi - Double.parseDouble(longitude);
			dist = Math.sin(deg2rad(latti)) * Math.sin(deg2rad(Double.parseDouble(latitude))) + Math.cos(deg2rad(latti)) * Math.cos(deg2rad(Double.parseDouble(latitude))) * Math.cos(deg2rad(theta));
			dist = Math.acos(dist);
			dist = rad2deg(dist);
			dist = dist * 60 * 1.1515;
			dist = dist * 1.609344;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.format("%.2f", dist);
	}
	
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
	
	public static <T extends Comparable<T>> int findMinIndex(final List<T> xs) {
		int minIndex;
		if (xs.isEmpty()) {
			minIndex = -1;
		} else {
			final ListIterator<T> itr = xs.listIterator();
			T min = itr.next(); // first element as the current minimum
			minIndex = itr.previousIndex();
			while (itr.hasNext()) {
				final T curr = itr.next();
				if (curr.compareTo(min) < 0) {
					min = curr;
					minIndex = itr.previousIndex();
				}
			}
		}
		return minIndex;
	}
}
