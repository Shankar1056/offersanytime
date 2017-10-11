package bigappcompany.com.myoffers.activity;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BroadcastService extends IntentService {
	private static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.websmithing.broadcasttest.displayevent";
	private final Handler handler = new Handler();
	
	public BroadcastService() {
		super("BroadcastService");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		String addres = "";
		Double latti = null, longi = null;
			latti = Double.parseDouble(intent.getStringExtra("lat"));
			longi = Double.parseDouble(intent.getStringExtra("lon"));
			
			Geocoder geocoder;
			List<Address> addresses = null;
			geocoder = new Geocoder(this, Locale.getDefault());
		
		try {
			addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
				addres = addresses.get(0).getAddressLine(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		
		
		Intent broadcastIntent = new Intent();
		broadcastIntent.setAction(BroadcastService.BROADCAST_ACTION);
		broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
		broadcastIntent.putExtra("name", addres);
		sendBroadcast(broadcastIntent);
	}
}
