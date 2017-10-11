package bigappcompany.com.myoffers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.adapter.SearchAdapter;
import bigappcompany.com.myoffers.model.CatDataModel;
import bigappcompany.com.myoffers.model.CategoryModel;
import bigappcompany.com.myoffers.model.LocationModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Download_web;
import bigappcompany.com.myoffers.network.OnTaskCompleted;
import bigappcompany.com.myoffers.network.Utility;
import bigappcompany.com.myoffers.network.WebServices;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 09 Aug 2017 at 1:23 PM
 */

public class SearchActivity extends AppCompatActivity {
	private RecyclerView cattab_rec;
	private ArrayList<CatDataModel> modelArrayList = new ArrayList<>();
	private ArrayList<LocationModel> locationModels = new ArrayList<>();
	private SearchAdapter cattab_adapter;
	private TextView nooffers;
	private LinearLayout noofferlayout;
	ArrayList<LocationModel> locdata = new ArrayList<>();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.search_activity);
		mapping();
	}
	
	private void mapping() {
		cattab_rec = (RecyclerView) findViewById(R.id.homecat_rec);
		cattab_rec.setHasFixedSize(true);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(SearchActivity.this);
		cattab_rec.setLayoutManager(mLayoutManager);
		cattab_rec.setItemAnimator(new DefaultItemAnimator());
		nooffers = (TextView) findViewById(R.id.nooffers);
		noofferlayout = (LinearLayout) findViewById(R.id.noofferlayout);
		nooffers.setTypeface(Utility.font(SearchActivity.this, "medium"));
		ImageView backimage = (ImageView) findViewById(R.id.backimage);
		backimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		callcategoryapi();
		
		final EditText edittext = (EditText) findViewById(R.id.edittext);
		edittext.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String text = edittext.getText().toString().toLowerCase(Locale.getDefault());
				cattab_adapter.filter(text);
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
			                              int arg2, int arg3) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
			                          int arg3) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void callcategoryapi() {
		
		Download_web web = new Download_web(SearchActivity.this, new OnTaskCompleted() {
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
					}
				}
			}
		});
		web.setReqType("get");
		web.execute(WebServices.SHOPLIST);
		
	}
	
	private void locationfilter(ArrayList<CatDataModel> modelArrayList) {
		locdata.clear();
		String distance = "";
		Double latti = null, longi = null;
		try {
			latti = Double.parseDouble(ClsGeneral.getPreferences
			    (SearchActivity.this, ClsGeneral.LATITUTE));
			longi = Double.parseDouble(ClsGeneral.getPreferences
			    (SearchActivity.this, ClsGeneral.LONGITUTE));
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
					    .getLocation(), Double.parseDouble(distance)));
				}
			}
			
		}
		
		LocationSortComparator icc = new LocationSortComparator();
		java.util.Collections.sort(locdata, icc);
		
	}
	
	private void getdistance(ArrayList<CatDataModel> modlArrayList) {
		ArrayList<CatDataModel> filteredlist = new ArrayList<>();
		Double latti = null, longi = null;
		try {
			latti = Double.parseDouble(ClsGeneral.getPreferences
			    (SearchActivity.this, ClsGeneral.LATITUTE));
			longi = Double.parseDouble(ClsGeneral.getPreferences
			    (SearchActivity.this, ClsGeneral.LONGITUTE));
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
		cattab_adapter = new SearchAdapter(SearchActivity.this, filteredlist, locdata, SearchActivity.this);
		cattab_rec.setAdapter(cattab_adapter);
		
		if (filteredlist.size() == 0) {
			noofferlayout.setVisibility(View.VISIBLE);
		}
	}
	
	private void shortdis(ArrayList<String> dislist, CatDataModel a, ArrayList<CatDataModel> filteredlist) {
		DistanceSortComparator icc = new DistanceSortComparator();
		java.util.Collections.sort(dislist, icc);
		
		if (Double.parseDouble(dislist.get(0)) <= 99.0) {
			filteredlist.add(new CatDataModel(a.getId(), a.getShop_name(), a.getShop_image(), a.getParent_add_1(), a.getParent_add_2(),
			    a.getRegistered_date(), Double.parseDouble(dislist.get(0)), a.getParent_id(), a.getShop_location()));
		}
		
		dislist.clear();
	}
	
	
	public void sendpos(int position) {
		locationModels.clear();
		ArrayList<LocationModel> a = locdata;
		ArrayList<LocationModel> aa = new ArrayList<>();
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
			
			
			startActivity(new Intent(SearchActivity.this, ShopList.class).putParcelableArrayListExtra("list",
			    locationModels).putExtra("name", modelArrayList.get(position).getShop_name()));
			
		} else if (locationModels != null && locationModels.size() == 1) {
			
			startActivity(new Intent(SearchActivity.this, Description.class).putExtra("id", locationModels.get
			    (0).getShop_id()).putExtra("name", locationModels.get(0).getShop_name()).putExtra("local", "no"));
		} else {
			startActivity(new Intent(SearchActivity.this, Description.class).putExtra("id", modelArrayList.get
			    (position)
			    .getId())
			    .putExtra("name", modelArrayList.get(position).getShop_name()).putExtra("local", "no"));
			
		}
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
}
