package bigappcompany.com.myoffers.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 16 Aug 2017 at 11:33 AM
 */

public abstract class NetworkReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			if (isOnline(context)) {
				onSmsReceived(true);
				Log.e("keshav", "Online Connect Intenet ");
			} else {
				onSmsReceived(false);
				Log.e("keshav", "Conectivity Failure !!! ");
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean isOnline(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			//should check null because in airplane mode it will be null
			return (netInfo != null && netInfo.isConnected());
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	protected abstract void onSmsReceived(boolean s);
}
