package bigappcompany.com.myoffers.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.adapter.SlidingImage_Adapter;
import bigappcompany.com.myoffers.adapter.TimingAdapter;
import bigappcompany.com.myoffers.model.CatBanner;
import bigappcompany.com.myoffers.model.CatBannerFilter;
import bigappcompany.com.myoffers.model.DescriptionModel;
import bigappcompany.com.myoffers.model.TimingModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Download_web;
import bigappcompany.com.myoffers.network.NetworkReceiver;
import bigappcompany.com.myoffers.network.OnTaskCompleted;
import bigappcompany.com.myoffers.network.Utility;
import bigappcompany.com.myoffers.network.WebServices;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 05 Jul 2017 at 6:51 PM
 */

public class Description extends AppCompatActivity implements View.OnClickListener {
	private static ViewPager mPager;
	private static int currentPage = 0;
	private static int NUM_PAGES = 0;
	private ArrayList<CatBanner> catBanners = new ArrayList<>();
	private ArrayList<CatBannerFilter> catBannersFiltered = new ArrayList<>();
	static final int REQUEST_LOCATION = 1;
	private double latti, longi;
	private String serverlat, serverlon;
	private DescriptionModel categoryModel;
	private TextView validitytext, gotomap, validontext;
	private TextView photocount, websitelink;
	private RecyclerView openclose_rec;
	int daycount = 0;
	private NetworkReceiver networkReceiver;
	private IntentFilter intentFilter;
	private RelativeLayout toprel;
	private LinearLayout transbar;
	private TextView toolbartext;
	private String catName = "";
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.description);
		
		catName = ClsGeneral.getPreferences(Description.this, ClsGeneral.CATEGORYNAME);
		
		networkReceiver = new NetworkReceiver() {
			@Override
			protected void onSmsReceived(boolean s) {
				if (s) {
					if (catBanners.size() == 0)
						initToolBar();
				} else {
					Toast.makeText(Description.this, getResources().getString(R.string.nointernetconnection),
					    Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		
		intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(1000);
		
		
		mPager = (ViewPager) findViewById(R.id.pager);
		
		toprel = (RelativeLayout) findViewById(R.id.toprel);
		transbar = (LinearLayout) findViewById(R.id.transbar);
		toprel.setOnClickListener(this);
		
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		
		switch (day) {
			case Calendar.SUNDAY:
				daycount = 6;
				break;
			
			case Calendar.MONDAY:
				daycount = 0;
				break;
			
			case Calendar.TUESDAY:
				daycount = 1;
				break;
			case Calendar.WEDNESDAY:
				daycount = 2;
				break;
			case Calendar.THURSDAY:
				daycount = 3;
				break;
			case Calendar.FRIDAY:
				daycount = 4;
				break;
			case Calendar.SATURDAY:
				daycount = 5;
				break;
		}
		
		
	}
	
	public void initToolBar() {
		toolbartext = (TextView) findViewById(R.id.toolbartext);
		validontext = (TextView) findViewById(R.id.validontext);
		toolbartext.setTypeface(Utility.font(Description.this, "medium"));
		try {
			toolbartext.setText(getIntent().getStringExtra("name"));
		} catch (Exception e) {
			e.printStackTrace();
			toolbartext.setText("Description");
		}
		findViewById(R.id.backimage).setOnClickListener(this);
		validitytext = (TextView) findViewById(R.id.validitytext);
		
		
		sendcount();
		
	}
	
	private void sendcount() {
		
		Download_web web = new Download_web(Description.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					
					callcategoryapi();
				} else {
					callcategoryapi();
				}
			}
		});
		web.setReqType("post");
		JSONObject post_dict = new JSONObject();
		try {
			post_dict.put("shop", getIntent().getStringExtra("id"));
			web.setstringData(post_dict.toString());
			web.execute(WebServices.SHOP_COUNT);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void callcategoryapi() {
		
		Download_web web = new Download_web(Description.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				try {
					if (response != null && response.length() > 0) {
						
						Gson gson = new Gson();
						categoryModel = gson.fromJson(response, DescriptionModel.class);
						if (categoryModel.getStatus().equalsIgnoreCase("true")) {
							catBanners = categoryModel.getData().getBanners();
							ImageView noofferimage = (ImageView) findViewById(R.id
							    .noofferimage);
							LinearLayout nofferlayout = (LinearLayout) findViewById(R.id
							    .nofferlayout);
							if (catBanners != null && catBanners.size() > 0) {
								transbar.setVisibility(View.VISIBLE);
								nofferlayout.setVisibility(View.GONE);
								for (int i = 0; i < catBanners.size(); i++) {
									catBannersFiltered.add(new CatBannerFilter
									    (catBanners.get(i).getBanner_id(), catBanners
										.get(i).getBanner_image(), catBanners.get
										(i).getCategory(), catBanners.get(i)
										.getValid_for(), catBanners.get(i).getValid_till()));
								}
								setbannerinAdapter(catBanners);
								
								
							} else
							
							{
								
								noofferimage.setBackgroundResource(R.mipmap.no_offer);
								transbar.setVisibility(View.GONE);
							}
							
							serverlat = categoryModel.getData()
							    .getLatitude();
							serverlon = categoryModel.getData()
							    .getLongitude();
							
							setvalue();
						}
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});
		
		web.setReqType("post");
		JSONObject post_dict = new JSONObject();
		try {
			post_dict.put("shop", getIntent().getStringExtra("id"));
			web.setstringData(post_dict.toString());
			web.execute(WebServices.DESCRIPTION);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private void setbannerinAdapter(ArrayList<CatBanner> catBanners) {
		if (catName.equals(""))
		{
			
		}
		else {
			for (int i = 0; i < catBanners.size(); i++) {
				String example = catBanners.get(i).getValid_for();
				if (example.matches("(.*)" + catName + "(.*)") || example.matches("(.*)" + catName)
				    || example.matches(catName + "(.*)")) {
					System.out.println("working");
					catBanners.set(0, new CatBanner(catBanners.get(i).getBanner_id(), catBanners.get(i)
					    .getBanner_image(), catBanners.get(i).getCategory(), catBanners.get(i).getValid_for(),
					    catBanners.get(i).getValid_till()));
					catBanners.set(i, new CatBanner(catBannersFiltered.get(0).getBanner_id(), catBannersFiltered.get(0)
					    .getBanner_image(), catBannersFiltered.get(0).getCategory(), catBannersFiltered.get(0)
					    .getValid_for(), catBannersFiltered.get(0).getValid_till()));
					
				break;
				}
				
				
			}
		}
		mPager.setAdapter(new SlidingImage_Adapter(Description
		    .this, catBanners, Description.this));
		validitytext.setText("Valid till " + catBanners.get(0).getValid_till
		    ());
		validontext.setText("Valid Only On : " + catBanners.get(0)
		    .getValid_for());
		
		
	}
	
	private void setvalue() {
		
		try {
			CirclePageIndicator indicator = (CirclePageIndicator)
			    findViewById(R.id.indicator);
			indicator.setViewPager(mPager);
			final float density = getResources().getDisplayMetrics().density;
			indicator.setRadius(5 * density);
			NUM_PAGES = catBanners.size();
			
			// Auto start of viewpager
			final Handler handler = new Handler();
			final Runnable Update = new Runnable() {
				public void run() {
					if (currentPage == NUM_PAGES) {
						currentPage = 0;
					}
					mPager.setCurrentItem(currentPage++, true);
				}
			};
			Timer swipeTimer = new Timer();
			swipeTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					handler.post(Update);
				}
			}, 10000, 10000);
			
			// Pager listener over indicator
			indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int position) {
					currentPage = position;
					validitytext.setText("Valid till" + catBanners.get(position).getValid_till());
					validontext.setText("Valid Only On : " + catBanners.get(position).getValid_for());
					if (catBanners != null && catBanners.size() == 1) {
						photocount.setText(catBanners.size() + "/1" + " Photo");
					} else if (catBanners != null && catBanners.size() > 1) {
						photocount.setText(catBanners.size() + "/" + (currentPage + 1) + " Photos");
					}
					
					
				}
				
				@Override
				public void onPageScrolled(int pos, float arg1, int arg2) {
					
				}
				
				@Override
				public void onPageScrollStateChanged(int pos) {
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		domapping();
	}
	
	private void domapping() {
		photocount = (TextView) findViewById(R.id.photocount);
		TextView shopname = (TextView) findViewById(R.id.shopname);
		TextView uploadedtime = (TextView) findViewById(R.id.uploadedtime);
		TextView location = (TextView) findViewById(R.id.location);
		TextView distanceinkm = (TextView) findViewById(R.id.distanceinkm);
		TextView daytext = (TextView) findViewById(R.id.daytext);
		gotomap = (TextView) findViewById(R.id.gotomap);
		websitelink = (TextView) findViewById(R.id.websitelink);
		findViewById(R.id.toprel).setOnClickListener(this);
		
		openclose_rec = (RecyclerView) findViewById(R.id.openclose_rec);
		openclose_rec.setHasFixedSize(true);
		openclose_rec.setNestedScrollingEnabled(false);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Description.this);
		openclose_rec.setLayoutManager(mLayoutManager);
		openclose_rec.setItemAnimator(new DefaultItemAnimator());
		
		photocount.setTypeface(Utility.font(Description.this, "medium"));
		shopname.setTypeface(Utility.font(Description.this, "bold"));
		validontext.setTypeface(Utility.font(Description.this, "bold"));
		location.setTypeface(Utility.font(Description.this, "regular"));
		distanceinkm.setTypeface(Utility.font(Description.this, "regular"));
		uploadedtime.setTypeface(Utility.font(Description.this, "medium"));
		validitytext.setTypeface(Utility.font(Description.this, "medium"));
		gotomap.setTypeface(Utility.font(Description.this, "medium"));
		websitelink.setTypeface(Utility.font(Description.this, "medium"));
		daytext.setTypeface(Utility.font(Description.this, "bold"));
		
		gotomap.setOnClickListener(this);
		location.setOnClickListener(this);
		websitelink.setOnClickListener(this);
		if (catBanners != null && catBanners.size() == 1) {
			photocount.setText(catBanners.size() + "/1" + " Photo");
		} else if (catBanners != null && catBanners.size() > 1) {
			photocount.setText(catBanners.size() + "/1" + " Photos");
		}
		
		
		latti = Double.parseDouble(ClsGeneral.getPreferences(Description.this, ClsGeneral.LATITUTE));
		longi = Double.parseDouble(ClsGeneral.getPreferences(Description.this, ClsGeneral.LONGITUTE));
		
		shopname.setText(categoryModel.getData().getShop_name());
		toolbartext.setText(categoryModel.getData().getShop_name());
		
		location.setText(categoryModel.getData().getAddress_line_1());
		distanceinkm.setText("" + Utility.calculatedistanceinkm(latti, longi, categoryModel.getData()
		    .getLatitude(), categoryModel.getData().getLongitude()) + " km");
		
		websitelink.setText(categoryModel.getData().getWebsite());
		
		
		setdaysapi(categoryModel.getData().getTimings());
	}
	
	private void setdaysapi(ArrayList<TimingModel> timings) {
		if (timings != null && timings.size() > 0) {
			for (int i = 0; i < timings.size(); i++) {
				TimingModel model = timings.get(i);
				if (i == daycount) {
					model.setTypefacetype("bold");
				} else {
					model.setTypefacetype("regular");
				}
			}
			if (timings != null && timings.size() > 0) {
				TimingAdapter timingAdapter = new TimingAdapter(Description.this, timings);
				openclose_rec.setAdapter(timingAdapter);
			}
			if (timings == null) {
				LinearLayout timinlayout = (LinearLayout) findViewById(R.id.timinlayout);
				timinlayout.setVisibility(View.GONE);
			}
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.location:
				
				break;
			case R.id.gotomap:
				LatLng start_latlong = new LatLng(latti, longi);
				LatLng end_latlng = new LatLng(Double.parseDouble(categoryModel.getData()
				    .getLatitude()), Double.parseDouble(categoryModel.getData().getLongitude()));
				Intent intent = new Intent(Intent.ACTION_VIEW,
				    Uri.parse("http://maps.google.com/maps?saddr=" + String.valueOf(start_latlong.latitude) + "," + String.valueOf(start_latlong.longitude) + "&daddr=" + String.valueOf(end_latlng.latitude) + "," + String.valueOf(end_latlng.longitude)));
				startActivity(intent);
				
				
				break;
			case R.id.backimage:
				finish();
				break;
			case R.id.websitelink:
				try {
					String url = categoryModel.getData().getWebsite();
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
		}
		
	}
	
	public void sendpos() {
		try {
			Intent intent = new Intent(Description.this, FullImageActivity.class);
			intent.putParcelableArrayListExtra("list", catBanners);
			intent.putExtra("pos", "" + currentPage);
			startActivity(intent);
		} catch (Exception e) {
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (intentFilter != null) {
			this.registerReceiver(networkReceiver, intentFilter);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (intentFilter != null) {
			this.unregisterReceiver(networkReceiver);
		}
	}
}
