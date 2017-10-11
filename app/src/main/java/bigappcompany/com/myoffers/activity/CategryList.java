package bigappcompany.com.myoffers.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.adapter.CategoryListAdater;
import bigappcompany.com.myoffers.model.CatDataModel;
import bigappcompany.com.myoffers.model.CategoryModel;
import bigappcompany.com.myoffers.model.LocationModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Download_web;
import bigappcompany.com.myoffers.network.NetworkReceiver;
import bigappcompany.com.myoffers.network.OnTaskCompleted;
import bigappcompany.com.myoffers.network.Utility;
import bigappcompany.com.myoffers.network.WebServices;
import io.fabric.sdk.android.Fabric;

import static java.lang.Double.parseDouble;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Jul 2017 at 4:43 PM
 */

public class CategryList extends AppCompatActivity implements View.OnClickListener {
	private RecyclerView cattab_rec;
	private ArrayList<CatDataModel> modelArrayList = new ArrayList<>();
	private ArrayList<LocationModel> locationModels = new ArrayList<>();
	private TextView nooffers;
	private LinearLayout noofferlayout;
	private NetworkReceiver networkReceiver;
	private IntentFilter intentFilter;
	ArrayList<LocationModel> locdata = new ArrayList<>();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.category_list);
		domapping();
		networkReceiver = new NetworkReceiver() {
			@Override
			protected void onSmsReceived(boolean s) {
				if (s) {
					callcategoryapi();
				} else {
					Toast.makeText(CategryList.this, getResources().getString(R.string
					    .nointernetconnection), Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(1000);
		
	}
	
	private void domapping() {
		
		findViewById(R.id.backimage).setOnClickListener(this);
		TextView toolbartext = (TextView) findViewById(R.id.toolbartext);
		toolbartext.setText(getIntent().getStringExtra("name"));
		toolbartext.setTypeface(Utility.font(CategryList.this, "medium"));
		
		cattab_rec = (RecyclerView) findViewById(R.id.homecat_rec);
		cattab_rec.setHasFixedSize(true);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CategryList.this);
		cattab_rec.setLayoutManager(mLayoutManager);
		cattab_rec.setItemAnimator(new DefaultItemAnimator());
		
		nooffers = (TextView) findViewById(R.id.nooffers);
		noofferlayout = (LinearLayout) findViewById(R.id.noofferlayout);
		nooffers.setTypeface(Utility.font(CategryList.this, "medium"));
		
		//callcategoryapi();
	}
	
	private void callcategoryapi() {
		
		Download_web web = new Download_web(CategryList.this, new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					
					Gson gson = new Gson();
					CategoryModel categoryModel = gson.fromJson(response, CategoryModel.class);
					if (categoryModel.getStatus().equalsIgnoreCase("true")) {
						modelArrayList.clear();
						modelArrayList = categoryModel.getData();
						if (modelArrayList.size() > 0) {
							locationfilter(modelArrayList);
							getdistance(modelArrayList);
						}
					} else {
						noofferlayout.setVisibility(View.VISIBLE);
					}
					
				}
			}
		});
		web.setReqType("post");
		JSONObject post_dict = new JSONObject();
		try {
			post_dict.put("category", getIntent().getStringExtra("id"));
			web.setstringData(post_dict.toString());
			web.execute(WebServices.CATEGORY);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void sendpos(int position) {
		locationModels.clear();
		ArrayList<LocationModel> a = locdata;
		for (int j = 0; j < a.size(); j++) {
			if (modelArrayList.get(position).getParent_id() != null) {
				if (modelArrayList.get(position).getParent_id().equalsIgnoreCase(a.get(j).getParent_id())) {
					if (a.get(j).getDistance() <= 99.0) {
						locationModels.add(new LocationModel(a.get(j).getShop_id(), a.get(j)
						    .getContact_person(),
						    a.get(j).getShop_name(), a.get(j).getShop_slug(), a.get(j)
						    .getShop_category(), a.get(j).getShop_image(), a.get(j).getPh_no(),
						    a.get(j).getEmail(), a.get(j).getPassword(), a.get(j).getAssociated_with(),
						    a.get(j).getParent_id(), a.get(j).getCountry(), a.get(j).getState(), a.get
						    (j).getCity(), a.get(j).getAddress_line_1(), a.get(j)
						    .getAddress_line_2(), a.get(j).getLat(), a.get(j).getLng(), a.get(j)
						    .getStatus(), a.get(j).getShop_is_active(), a.get(j)
						    .getRegistered_on(), a.get(j).getLocation(), a.get(j).getDistance()));
					}
				}
			}
		}
		if (locationModels != null && locationModels.size() > 1) {
			
			
			startActivity(new Intent(CategryList.this, ShopList.class).putParcelableArrayListExtra("list",
			    locationModels).putExtra("name", modelArrayList.get(position).getShop_name()));
			
		} else if (locationModels != null && locationModels.size() == 1) {
			
			startActivity(new Intent(CategryList.this, Description.class).putExtra("id", locationModels.get
			    (0).getShop_id()).putExtra("name", locationModels.get(0).getShop_name()).putExtra("local", "no"));
		} else {
			startActivity(new Intent(CategryList.this, Description.class).putExtra("id", modelArrayList.get
			    (position)
			    .getId())
			    .putExtra("name", modelArrayList.get(position).getShop_name()).putExtra("local", "no"));
			
		}
	}
	
	private void locationfilter(ArrayList<CatDataModel> modelArrayList) {
		locdata.clear();
		String distance = "";
		Double latti = null, longi = null;
		try {
			latti = parseDouble(ClsGeneral.getPreferences
			    (CategryList.this, ClsGeneral.LATITUTE));
			longi = parseDouble(ClsGeneral.getPreferences
			    (CategryList.this, ClsGeneral.LONGITUTE));
		} catch (Exception e) {
			latti = 0.0;
			longi = 0.0;
			
		}
		
		for (int i = 0; i < modelArrayList.size(); i++) {
			ArrayList<LocationModel> a = modelArrayList.get(i).getLocations();
			if (a != null && a.size() > 0) {
				for (int j = 0; j < a.size(); j++) {
					if (a.get(j).getLocation() != null && a.get(j).getLocation().length() > 0) {
						distance = "" + (Utility
						    .calculatedistanceinkm(latti, longi, a.get(j).getLat(), a.get(j)
							.getLng()));
						
					}
					
					locdata.add(new LocationModel(a.get(j).getShop_id(), a.get(j)
					    .getContact_person(),
					    a.get(j).getShop_name(), a.get(j).getShop_slug(), a.get(j)
					    .getShop_category(), a.get(j).getShop_image(), a.get(j).getPh_no(),
					    a.get(j).getEmail(), a.get(j).getPassword(), a.get(j).getAssociated_with(),
					    a.get(j).getParent_id(), a.get(j).getCountry(), a.get(j).getState(), a.get
					    (j).getCity(), a.get(j).getAddress_line_1(), a.get(j)
					    .getAddress_line_2(), a.get(j).getLat(), a.get(j).getLng(), a.get(j)
					    .getStatus(), a.get(j).getShop_is_active(), a.get(j)
					    .getRegistered_on(), a.get(j)
					    .getLocation(), parseDouble(distance)));
				}
			}
			
		}
		
		LocationSortComparator icc = new LocationSortComparator();
		java.util.Collections.sort(locdata, icc);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backimage:
				finish();
				break;
		}
		
	}
	
	private void getdistance(ArrayList<CatDataModel> modlArrayList) {
		ArrayList<CatDataModel> filteredlist = new ArrayList<>();
		Double latti = null, longi = null;
		try {
			latti = parseDouble(ClsGeneral.getPreferences
			    (CategryList.this, ClsGeneral.LATITUTE));
			longi = parseDouble(ClsGeneral.getPreferences
			    (CategryList.this, ClsGeneral.LONGITUTE));
		} catch (Exception e) {
			latti = 0.0;
			longi = 0.0;
			
		}
		
		for (int i = 0; i < modlArrayList.size(); i++) {
			ArrayList<String> dislist = new ArrayList<>();
			String distance = "";
			if (modlArrayList.get(i).getLocations() != null && modlArrayList.get(i).getLocations().size() > 0) {
				for (int j = 0; j < modlArrayList.get(i).getLocations().size(); j++) {
					distance = "" + (Utility
					    .calculatedistanceinkm(latti, longi, modlArrayList.get(i).getLocations().get(j).getLat(),
						modlArrayList.get(i).getLocations().get(j).getLng()));
					dislist.add(distance);
					
				}
				shortdis(dislist, modlArrayList.get(i), filteredlist);
				
			} else {
				distance = "" + (Utility
				    .calculatedistanceinkm(latti, longi, modlArrayList.get(i).getParent_lat(), modlArrayList.get(i).getParent_long()));
				
				
				dislist.add(distance);
				shortdis(dislist, modlArrayList.get(i), filteredlist);
			}
			
			
		}
		
		IgnoreCaseComparator icc = new IgnoreCaseComparator();
		java.util.Collections.sort(filteredlist, icc);
		modelArrayList = filteredlist;
		CategoryListAdater cattab_adapter = new CategoryListAdater(CategryList.this,
		    filteredlist, locdata, CategryList.this);
		cattab_rec.setAdapter(cattab_adapter);
		
		if (filteredlist.size() == 0) {
			noofferlayout.setVisibility(View.VISIBLE);
		}
	}
	
	private void shortdis(ArrayList<String> dislist, CatDataModel a, ArrayList<CatDataModel> filteredlist) {
		/*DistanceSortComparator icc = new DistanceSortComparator();
		java.util.Collections.sort(dislist, icc);
		
		if (Double.parseDouble(dislist.get(0)) <= 99.0) {
			filteredlist.add(new CatDataModel(a.getId(), a.getShop_name(), a.getShop_image(), a.getParent_add_1(), a.getParent_add_2(),
			    a.getRegistered_date(), Double.parseDouble(dislist.get(0)), a.getParent_id(), a.getShop_location()));
		}
		
		dislist.clear();*/
		
		ArrayList<Double> list = new ArrayList();
		for (int k = 0; k < dislist.size(); k++) {
			list.add(parseDouble(dislist.get(k)));
		}
		int indexOfMinimum = list.indexOf(Collections.min(list));
		if (parseDouble(dislist.get(indexOfMinimum)) <= 99.0) {
			filteredlist.add(new CatDataModel(a.getId(), a.getShop_name(), a.getShop_image(), a.getParent_add_1(), a.getParent_add_2(),
			    a.getRegistered_date(), parseDouble(dislist.get(indexOfMinimum)), a.getParent_id(), a.getShop_location()));
		}
		
		
		dislist.clear();
	}
	
	class IgnoreCaseComparator implements Comparator<CatDataModel> {
		public int compare(CatDataModel strA, CatDataModel strB) {
			return strA.getDistance().compareTo(strB.getDistance());
		}
	}
	
	class LocationSortComparator implements Comparator<LocationModel> {
		public int compare(LocationModel strA, LocationModel strB) {
			return strA.getDistance().compareTo(strB.getDistance());
		}
	}
	
	class DistanceSortComparator implements Comparator<String> {
		public int compare(String strA, String strB) {
			return strA.compareTo(strB);
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
