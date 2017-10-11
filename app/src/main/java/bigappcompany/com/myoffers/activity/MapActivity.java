package bigappcompany.com.myoffers.activity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.network.Utility;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 07 Jul 2017 at 6:30 PM
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
	Double lat, lon;
	private static final String TAG = "MainActivity.class";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.map_activity);
		
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
		    .findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
		findViewById(R.id.backimage).setOnClickListener(this);
		try {
			lat = Double.parseDouble(getIntent().getStringExtra("lat"));
			lon = Double.parseDouble(getIntent().getStringExtra("lon"));
			getcurrentAddress(lat, lon);
		} catch (Exception e) {
			getcurrentAddress(13.0272362, 77.5775033);
		}
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		
		GoogleMap mMap = googleMap;
		LatLng bangalore = null;
		// Add a marker in Bangalore and move the camera
		try {
			
			bangalore = new LatLng(lat, lon);
		} catch (NumberFormatException e) {
			//bangalore = new LatLng(13.0286192, 77.5791356);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mMap.addMarker(new MarkerOptions().position(bangalore).title(""));
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bangalore, 15.0f));
		
		
	}
	
	
	private void getcurrentAddress(double latti, double longi) {
		
		
		Geocoder geocoder;
		List<Address> addresses = null;
		geocoder = new Geocoder(this, Locale.getDefault());
		
		try {
			addresses = geocoder.getFromLocation(latti, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		
		String addres = "";
		
		addres = addresses.get(0).getAddressLine(1);
		
		
		TextView toolbartext = (TextView) findViewById(R.id.toolbartext);
		toolbartext.setTypeface(Utility.font(MapActivity.this, "medium"));
		toolbartext.setText(addres);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backimage:
				finish();
				break;
		}
		
	}
}
