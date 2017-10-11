package bigappcompany.com.myoffers.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.activity.MainActivity;

import static com.google.firebase.crash.FirebaseCrash.log;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 07 Jul 2017 at 1:28 PM
 */

public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		Thread timer = new Thread() {
			public void run() {
				try {
					if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
						createshortcut();
					}
					
					EnableGPSAutoMatically();
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					
					/*startActivity(new Intent(SplashScreen.this, MainActivity.class));
					finish();*/
				}
			}
		};
		timer.start();
	}
	
	private void createshortcut() {
		Intent shortcutIntent = new Intent(getApplicationContext(),
		    SplashScreen.class);
		
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		
		Intent addIntent = new Intent();
		addIntent
		    .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
		    Intent.ShortcutIconResource.fromContext(getApplicationContext(),
			R.mipmap.logo));
		
		addIntent
		    .setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		addIntent.putExtra("duplicate", false);  //may it's already there so don't duplicate
		getApplicationContext().sendBroadcast(addIntent);
		
		
	}
	
	private void EnableGPSAutoMatically() {
		GoogleApiClient googleApiClient = null;
		if (googleApiClient == null) {
			googleApiClient = new GoogleApiClient.Builder(this)
			    .addApi(LocationServices.API).addConnectionCallbacks(this)
			    .addOnConnectionFailedListener(this).build();
			googleApiClient.connect();
			LocationRequest locationRequest = LocationRequest.create();
			locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
			locationRequest.setInterval(30 * 1000);
			locationRequest.setFastestInterval(5 * 1000);
			LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
			    .addLocationRequest(locationRequest);
			
			// **************************
			builder.setAlwaysShow(true); // this is the key ingredient
			// **************************
			
			PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
			    .checkLocationSettings(googleApiClient, builder.build());
			result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
				@Override
				public void onResult(LocationSettingsResult result) {
					final Status status = result.getStatus();
					final LocationSettingsStates state = result
					    .getLocationSettingsStates();
					switch (status.getStatusCode()) {
						case LocationSettingsStatusCodes.SUCCESS:
							//toast("Success");
							startActivity(new Intent(SplashScreen.this, MainActivity.class));
							finish();
							break;
						case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
							toast("GPS is not on");
							// Location settings are not satisfied. But could be
							// fixed by showing the user
							// a dialog.
							try {
								// Show the dialog by calling
								// startResolutionForResult(),
								// and check the result in onActivityResult().
								status.startResolutionForResult(SplashScreen.this, 1000);
								
							} catch (IntentSender.SendIntentException e) {
								// Ignore the error.
							}
							break;
						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
							toast("Setting change not allowed");
							// Location settings are not satisfied. However, we have
							// no way to fix the
							// settings so we won't show the dialog.
							break;
					}
				}
			});
		} else
		
		{
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == 1000) {
			if (resultCode == Activity.RESULT_OK) {
				startActivity(new Intent(SplashScreen.this, MainActivity.class));
				finish();
			}
			if (resultCode == Activity.RESULT_CANCELED) {
				//Write your code if there's no result
				startActivity(new Intent(SplashScreen.this, MainActivity.class));
				finish();
			}
		}
	}
	
	@Override
	public void onConnected(@Nullable Bundle bundle) {
		
	}
	
	@Override
	public void onConnectionSuspended(int i) {
		toast("Suspended");
	}
	
	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		toast("Failed");
	}
	
	private void toast(String message) {
		try {
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			log("Window has been closed");
		}
	}
	
	
}
