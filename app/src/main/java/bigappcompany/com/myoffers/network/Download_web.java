package bigappcompany.com.myoffers.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.NameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Download_web extends AsyncTask<String, String, String> {
	private final Context context;
	private String response = "";
	private OnTaskCompleted listener;
	private String responsetype = "post";
	private ArrayList<NameValuePair> nameValuePairs;
	String data;
	
	public Download_web(Context context, OnTaskCompleted listener) {
		this.context = context;
		this.listener = listener;
	}
	
	public void setReqType(String responsetype) {
		this.responsetype = responsetype;
	}
	
	public void setData(ArrayList<NameValuePair> nameValuePairs) {
		this.nameValuePairs = nameValuePairs;
	}
	
	public void setstringData(String data) {
		this.data = data;
	}
	
	@Override
	protected String doInBackground(String... params) {
		
		for (String url : params) {
			
			try {
				if (responsetype.equalsIgnoreCase("get")) {
					response = Utility.executeHttpGet(url);
				}
				if (responsetype.equalsIgnoreCase("post")) {
					response = doPost(url);
				}
				if (responsetype.equalsIgnoreCase("put")) {
					response = Utility.executeHttpPut(url, nameValuePairs);
				}
				if (responsetype.equalsIgnoreCase("post")) {
					response = Utility.executeHttpPost(url, nameValuePairs);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		return response;
	}
	
	@Override
	protected void onPreExecute() {
		
		super.onPreExecute();
		showDailog("Please wait...");
	}
	
	@Override
	protected void onPostExecute(String result) {
		
		if (!result.equals("")) {
			listener.onTaskCompleted(result);
			closeDialog();
		} else {
			closeDialog();
			//Toast.makeText(context, "check your internet connection", Toast.LENGTH_LONG).show();
		}
		
	}
	
	private String doPost(String url) {
		try {
			
			HttpURLConnection c = (HttpURLConnection) new URL(url).openConnection();
			c.setRequestProperty("Authorization", "Basic " +
			    Base64.encodeToString(new ApiUrl().getUnp().getBytes(), Base64.DEFAULT));
			c.setUseCaches(false);
			c.setDoOutput(true);
			c.connect();
			
			OutputStreamWriter Out_wr = new OutputStreamWriter(c.getOutputStream());
			Log.e("sending_data", data);
			Out_wr.write(data.toString());
			Out_wr.flush();
			BufferedReader buf = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String sr = "";
			while ((sr = buf.readLine()) != null) {
				response += sr;
			}
			Log.e("json", response);
			c.disconnect();
			
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", e.getMessage());
			response = "";
			return response;
		}
		return response;
	}
	
	ProgressDialog dialog;
	
	public void showDailog(String msg) {
		dialog = new ProgressDialog(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setMessage(msg);
		dialog.show();
	}
	
	public void closeDialog() {
		if (dialog != null && dialog.isShowing())
			dialog.cancel();
	}
	
}