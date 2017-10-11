package bigappcompany.com.myoffers.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.adapter.ViewPagerAdapter;
import bigappcompany.com.myoffers.fragment.HomeFragment;
import bigappcompany.com.myoffers.fragment.MoreFragment;
import bigappcompany.com.myoffers.fragment.SecondFragment;
import bigappcompany.com.myoffers.model.CategoryTabModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Download_web;
import bigappcompany.com.myoffers.network.NetworkReceiver;
import bigappcompany.com.myoffers.network.OnTaskCompleted;
import bigappcompany.com.myoffers.network.Utility;
import bigappcompany.com.myoffers.network.WebServices;
import io.fabric.sdk.android.Fabric;

import static bigappcompany.com.myoffers.R.id.simpleSwitch;
import static bigappcompany.com.myoffers.R.mipmap.store;
import static bigappcompany.com.myoffers.activity.Description.REQUEST_LOCATION;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {
	private TabLayout tabLayout;
	private ViewPager viewPager;
	TextView tabOne, tabTwo, tabFour;
	ImageView tabimageone, tabimageTwo, tabimageFour;
	ArrayList<CategoryTabModel> modelArrayList = new ArrayList<>();
	private LocationManager locationManager;
	private double latti, longi;
	private View v1, v2, v4;
	private TextView home, sharetheapp, ratetheapp, help, contactus, homeback, sharetheappback, ratetheappback,
	    helpback, contactusback, toolbartext;
	private DrawerLayout drawer;
	private int tabcatsize;
	private static final String TAG = "BroadcastTest";
	private Intent intent;
	private boolean isGPSEnabled = false, isNetworkEnabled = false, canGetLocation = false;
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	Location location;
	private String refreshedToken;
	private boolean doubleBackToExitPressedOnce = false;
	private NetworkReceiver networkReceiver;
	private IntentFilter intentFilter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_main);
		
		
		networkReceiver = new NetworkReceiver() {
			@Override
			protected void onSmsReceived(boolean s) {
				if (s) {
					if (modelArrayList.size() == 0)
						drawermapping();
				} else {
					Toast.makeText(MainActivity.this, getResources().getString(R.string.nointernetconnection), Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(1000);
		
		
		if (Utility.isNetworkAvailable(MainActivity.this)) {
			String token = ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.DEVICE_TOKEN);
			if (token != null && token.length() > 0) {
				senddeviceaccesskey(token);
			}
		}
		
	}
	
	
	private void drawermapping() {
		
		if (ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.DEVICE_TOKEN).equalsIgnoreCase("")) {
			refreshedToken = FirebaseInstanceId.getInstance().getToken();
		} else {
			refreshedToken = ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.DEVICE_TOKEN);
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
		    this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();
		
		findViewById(R.id.drawerimage).setOnClickListener(this);
		domapping();
	}
	
	private void domapping() {
		
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		home = (TextView) findViewById(R.id.home);
		sharetheapp = (TextView) findViewById(R.id.sharetheapp);
		ratetheapp = (TextView) findViewById(R.id.ratetheapp);
		help = (TextView) findViewById(R.id.help);
		contactus = (TextView) findViewById(R.id.contactus);
		
		homeback = (TextView) findViewById(R.id.homeback);
		sharetheappback = (TextView) findViewById(R.id.sharetheappback);
		ratetheappback = (TextView) findViewById(R.id.ratetheappback);
		helpback = (TextView) findViewById(R.id.helpback);
		contactusback = (TextView) findViewById(R.id.contactusback);
		toolbartext = (TextView) findViewById(R.id.toolbartext);
		toolbartext.setOnClickListener(this);
		toolbartext.setTypeface(Utility.font(MainActivity.this, "medium"));
		
		Switch notificationSwitch = (Switch) findViewById(simpleSwitch);
		String message = ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.NOTIFICATION_SETTING);
		if (message.equalsIgnoreCase("Disabled")) {
			notificationSwitch.setChecked(false);
		} else {
			notificationSwitch.setChecked(true);
		}
		
		notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					sendnotificationstatus("1");
				} else {
					sendnotificationstatus("0");
				}
			}
		});
		
		home.setOnClickListener(this);
		sharetheapp.setOnClickListener(this);
		ratetheapp.setOnClickListener(this);
		help.setOnClickListener(this);
		contactus.setOnClickListener(this);
		findViewById(R.id.searchlayout).setOnClickListener(this);
		
		home.setTypeface(Utility.font(MainActivity.this, "bold"));
		sharetheapp.setTypeface(Utility.font(MainActivity.this, "bold"));
		ratetheapp.setTypeface(Utility.font(MainActivity.this, "bold"));
		help.setTypeface(Utility.font(MainActivity.this, "bold"));
		contactus.setTypeface(Utility.font(MainActivity.this, "bold"));
		notificationSwitch.setTypeface(Utility.font(MainActivity.this, "bold"));
		if (Utility.isNetworkAvailable(MainActivity.this)) {
			if (ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.LATITUTE).equalsIgnoreCase("")) {
				getLocation();
			} else {
				Intent msgIntent = new Intent(MainActivity.this, BroadcastService.class);
				msgIntent.putExtra("lat", ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.LATITUTE));
				msgIntent.putExtra("lon", ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.LONGITUTE));
				startService(msgIntent);
				callcategoryapi();
				
			}
			
			
		} else {
			Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
		}
		
		final Handler ha = new Handler();
		ha.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (toolbartext.getText().toString().trim().equalsIgnoreCase("")) {
					if (ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.LATITUTE).equalsIgnoreCase("")) {
						getLocation();
					} else {
						Intent msgIntent = new Intent(MainActivity.this, BroadcastService.class);
						msgIntent.putExtra("lat", ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.LATITUTE));
						msgIntent.putExtra("lon", ClsGeneral.getPreferences(MainActivity.this, ClsGeneral.LONGITUTE));
						startService(msgIntent);
						
					}
				}
				
				ha.postDelayed(this, 5000);
			}
		}, 1000);
	}
	
	private void sendnotificationstatus(String isChecked) {
		
		Download_web download_web = new Download_web(MainActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						String message = jsonObject.optString("message");
						Toast.makeText(MainActivity.this, "" + message, Toast.LENGTH_SHORT)
						    .show();
						ClsGeneral.setPreferences(MainActivity.this, ClsGeneral.NOTIFICATION_SETTING, message);
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
		});
		download_web.setReqType("post");
		JSONObject post_dict = new JSONObject();
		try {
			post_dict.put("token", refreshedToken);
			post_dict.put("status", isChecked);
			download_web.setstringData(post_dict.toString());
			download_web.execute(WebServices.NOTIFICATION_STATUS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	
	private void callcategoryapi() {
		
		Download_web download_web = new Download_web(MainActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.optString("status").equalsIgnoreCase("true")) {
							modelArrayList.clear();
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jo = jsonArray.getJSONObject(i);
								String b_cat_id = jo.optString("b_cat_id");
								String b_cat_name = jo.optString("b_cat_name");
								String icon = jo.optString("icon");
								String deselect_icon = jo.optString("deselect_icon");
								CategoryTabModel model = new CategoryTabModel(b_cat_id,
								    b_cat_name, icon, deselect_icon);
								modelArrayList.add(model);
								
								
							}
							tabcatsize = modelArrayList.size();
							setuptab();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		download_web.setReqType("get");
		download_web.execute(WebServices.CATEGORYLIST);
		
		
	}
	
	private void setuptab() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		createViewPager(viewPager);
		
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				
				int position = tab.getPosition();
				
				if (position == 0) {
					
					
					tabOne.setText(getResources().getString(R.string.home));
					tabOne.setTextColor(getResources().getColor(R.color.white));
					tabimageone.setBackgroundResource(store);
					tabLayout.getTabAt(0).setCustomView(v1);
					
					
					tabTwo.setText(modelArrayList.get(0).getB_cat_name());
					tabTwo.setTextColor(getResources().getColor(R.color.light_white));
					//tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_menu_camera, 0,0);
					Picasso.with(MainActivity.this).load(modelArrayList.get(0).getDeselect_icon()).into(tabimageTwo);
					tabLayout.getTabAt(1).setCustomView(v2);
					
					
					tabFour.setText(getResources().getString(R.string.more));
					tabFour.setTextColor(getResources().getColor(R.color.light_white));
					tabimageFour.setBackgroundResource(R.mipmap.deselect_more);
					tabLayout.getTabAt(2).setCustomView(v4);
					
					
				} else if (position == 1) {
					
					
					tabOne.setText(getResources().getString(R.string.home));
					tabOne.setTextColor(getResources().getColor(R.color.light_white));
					tabimageone.setBackgroundResource(R.mipmap.select_store);
					tabLayout.getTabAt(0).setCustomView(v1);
					
					
					tabTwo.setText(modelArrayList.get(0).getB_cat_name());
					tabTwo.setTextColor(getResources().getColor(R.color.white));
					Picasso.with(MainActivity.this).load(modelArrayList.get(0).getIcon()).into(tabimageTwo);
					tabLayout.getTabAt(1).setCustomView(v2);
					
					
					tabFour.setText(getResources().getString(R.string.more));
					tabFour.setTextColor(getResources().getColor(R.color.light_white));
					tabimageFour.setBackgroundResource(R.mipmap.deselect_more);
					tabLayout.getTabAt(2).setCustomView(v4);
					
					
				} else if (position == 2) {
					
					tabOne.setText(getResources().getString(R.string.home));
					tabOne.setTextColor(getResources().getColor(R.color.light_white));
					tabimageone.setBackgroundResource(R.mipmap.select_store);
					tabLayout.getTabAt(0).setCustomView(v1);
					
					
					tabTwo.setText(modelArrayList.get(0).getB_cat_name());
					tabTwo.setTextColor(getResources().getColor(R.color.light_white));
					Picasso.with(MainActivity.this).load(modelArrayList.get(0).getDeselect_icon()).into(tabimageTwo);
					tabLayout.getTabAt(1).setCustomView(v2);
					
					
					tabFour.setText(getResources().getString(R.string.more));
					tabFour.setTextColor(getResources().getColor(R.color.white));
					tabimageFour.setBackgroundResource(R.mipmap.select_more);
					tabLayout.getTabAt(2).setCustomView(v4);
					
					
				}
				
				//also you can use tab.setCustomView() too
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
				// tab.setIcon(R.drawable.oldicon);
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				
			}
		});
		
		createTabIcons();
	}
	
	private void createViewPager(ViewPager viewPager) {
		ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
		adapter.addFrag(new HomeFragment("all"), modelArrayList.get(0).getB_cat_name());
		adapter.addFrag(new SecondFragment(modelArrayList.get(0).getB_cat_id()), modelArrayList.get(0)
		    .getB_cat_name());
		
		if (tabcatsize > 1) {
			adapter.addFrag(new MoreFragment("more"), modelArrayList.get(3).getB_cat_name());
		}
		viewPager.setAdapter(adapter);
	}
	
	private void createTabIcons() {
		v1 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
		v2 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
		v4 = LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
		//tablayout
		tabOne = (TextView) v1.findViewById(R.id.tab);
		tabimageone = (ImageView) v1.findViewById(R.id.tabimage);
		tabOne.setTypeface(Utility.font(MainActivity.this, "medium"));
		tabOne.setText(getResources().getString(R.string.home));
		tabOne.setTextSize(14);
		tabOne.setTextColor(getResources().getColor(R.color.white));
		tabimageone.setBackgroundResource(store);
		tabLayout.getTabAt(0).setCustomView(v1);
		
		tabTwo = (TextView) v2.findViewById(R.id.tab);
		tabimageTwo = (ImageView) v2.findViewById(R.id.tabimage);
		tabTwo.setTypeface(Utility.font(MainActivity.this, "medium"));
		tabTwo.setText(modelArrayList.get(0).getB_cat_name());
		tabTwo.setTextSize(14);
		tabTwo.setTextColor(getResources().getColor(R.color.light_white));
		Picasso.with(MainActivity.this).load(modelArrayList.get(0).getDeselect_icon()).into(tabimageTwo);
		tabLayout.getTabAt(1).setCustomView(v2);
		
		
		tabFour = (TextView) v4.findViewById(R.id.tab);
		tabimageFour = (ImageView) v4.findViewById(R.id.tabimage);
		tabFour.setTypeface(Utility.font(MainActivity.this, "medium"));
		tabFour.setText(getResources().getString(R.string.more));
		tabFour.setTextSize(14);
		tabFour.setTextColor(getResources().getColor(R.color.light_white));
		tabimageFour.setBackgroundResource(R.mipmap.deselect_more);
		tabLayout.getTabAt(2).setCustomView(v4);
	}
	
	
	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			if (doubleBackToExitPressedOnce) {
				super.onBackPressed();
			}
			
			this.doubleBackToExitPressedOnce = true;
			Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					doubleBackToExitPressedOnce = false;
				}
			}, 3000);
		}
	}
	
	
	void getLocation() {
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
		    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
		    (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
			
		} else {
			//Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			
			locationManager = (LocationManager) MainActivity.this
			    .getSystemService(LOCATION_SERVICE);
			
			// getting GPS status
			isGPSEnabled = locationManager
			    .isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			// getting network status
			isNetworkEnabled = locationManager
			    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			if (!isGPSEnabled && !isNetworkEnabled) {
				// no network provider is enabled
			} else {
				this.canGetLocation = true;
				if (isNetworkEnabled) {
					locationManager.requestLocationUpdates(
					    LocationManager.NETWORK_PROVIDER,
					    MIN_TIME_BW_UPDATES,
					    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("Network", "Network Enabled");
					if (locationManager != null) {
						location = locationManager
						    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latti = location.getLatitude();
							longi = location.getLongitude();
							ClsGeneral.setPreferences(MainActivity.this, ClsGeneral.LATITUTE, "" + latti);
							ClsGeneral.setPreferences(MainActivity.this, ClsGeneral.LONGITUTE, "" + longi);
							Intent msgIntent = new Intent(MainActivity.this, BroadcastService.class);
							
							msgIntent.putExtra("lat", "" + latti);
							msgIntent.putExtra("lon", "" + longi);
							startService(msgIntent);
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
						    LocationManager.GPS_PROVIDER,
						    MIN_TIME_BW_UPDATES,
						    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
							    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latti = location.getLatitude();
								longi = location.getLongitude();
								ClsGeneral.setPreferences(MainActivity.this, ClsGeneral.LATITUTE, "" + latti);
								ClsGeneral.setPreferences(MainActivity.this, ClsGeneral.LONGITUTE, "" + longi);
								Intent msgIntent = new Intent(MainActivity.this, BroadcastService.class);
								
								msgIntent.putExtra("lat", "" + latti);
								msgIntent.putExtra("lon", "" + longi);
								startService(msgIntent);
							}
						}
					}
				}
			}
			
			
		}
		
		if (latti == 0.0 && longi == 0.0) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
			    .setCancelable(false)
			    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
					    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					    
					    finish();
					    startActivity(getIntent());
				    }
			    })
			    .setNegativeButton("No", new DialogInterface.OnClickListener() {
				    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
					    // callcategoryapi();
					    dialog.cancel();
				    }
			    });
			final AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		
		switch (requestCode) {
			case REQUEST_LOCATION:
				getLocation();
				break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.drawerimage:
				if (drawer.isDrawerOpen(GravityCompat.START)) {
					drawer.closeDrawer(GravityCompat.START);
				} else {
					drawer.openDrawer(GravityCompat.START);
				}
				break;
			case R.id.home:
				changetextback(home, homeback);
				break;
			case R.id.sharetheapp:
				String shareText = "https://play.google.com/store/apps/details?id=bigappcompany.com.myoffers&hl=en";
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, (shareText));
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
				startActivity(Intent.createChooser(shareIntent, "Share this story"));
				changetextback(sharetheapp, sharetheappback);
				break;
			case R.id.ratetheapp:
				try {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=bigappcompany.com.myoffers&hl=en")));
				} catch (Exception e) {
					
				}
				changetextback(ratetheapp, ratetheappback);
				break;
			case R.id.help:
				changetextback(help, helpback);
				break;
			case R.id.contactus:
				changetextback(contactus, contactusback);
				startActivity(new Intent(MainActivity.this, ContactUs.class));
				break;
			case R.id.searchlayout:
				startActivity(new Intent(MainActivity.this, SearchActivity.class));
				break;
			case R.id.toolbartext:
				startActivity(new Intent(MainActivity.this, bigappcompany.com.myoffers.mmap.MapActivity.class));
				break;
		}
		
	}
	
	private void senddeviceaccesskey(String token) {
		Download_web download_web = new Download_web(MainActivity.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						
					} catch (JSONException e) {
						e.printStackTrace();
					} finally {
					}
				}
				
				
			}
		});
		download_web.setReqType("post");
		JSONObject post_dict = new JSONObject();
		try {
			post_dict.put("token", token);
			download_web.setstringData(post_dict.toString());
			download_web.execute(WebServices.FCM_ACCESS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void changetextback(TextView hometext, TextView homebck) {
		
		home.setTextColor(getResources().getColor(R.color.text_black));
		sharetheapp.setTextColor(getResources().getColor(R.color.text_black));
		ratetheapp.setTextColor(getResources().getColor(R.color.text_black));
		help.setTextColor(getResources().getColor(R.color.text_black));
		contactus.setTextColor(getResources().getColor(R.color.text_black));
		
		homeback.setBackgroundColor(getResources().getColor(R.color.white));
		sharetheappback.setBackgroundColor(getResources().getColor(R.color.white));
		ratetheappback.setBackgroundColor(getResources().getColor(R.color.white));
		helpback.setBackgroundColor(getResources().getColor(R.color.white));
		contactusback.setBackgroundColor(getResources().getColor(R.color.white));
		
		hometext.setTextColor(getResources().getColor(R.color.colorPrimary));
		homebck.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
		
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			
		}
		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateUI(intent);
		}
	};
	
	private void updateUI(Intent intent) {
		
		String addres = intent.getStringExtra("name");
		toolbartext.setText(addres);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		IntentFilter intentFlter = new IntentFilter(BroadcastService.BROADCAST_ACTION);
		intentFlter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(broadcastReceiver, intentFlter);
		
		if (intentFilter != null) {
			this.registerReceiver(networkReceiver, intentFilter);
		}
		
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		if (intentFilter != null) {
			this.unregisterReceiver(networkReceiver);
		}
	}
	
	
	@Override
	public void onLocationChanged(Location location) {
		
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		
	}
	
	
}
