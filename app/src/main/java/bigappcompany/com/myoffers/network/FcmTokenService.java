package bigappcompany.com.myoffers.network;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 28 Aug 2017 at 3:38 PM
 */

public class FcmTokenService extends IntentService {
	private static final String TAG = "FcmTokenService";
	
	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public FcmTokenService() {
		super("FcmTokenService");
	}
	
	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		
		try {
			String token = intent.getStringExtra("token");
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
			nameValuePairs.add(new BasicNameValuePair("token", token));
			Utility.executeHttpPost(WebServices.FCM_ACCESS, nameValuePairs);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		
		
	}
}
