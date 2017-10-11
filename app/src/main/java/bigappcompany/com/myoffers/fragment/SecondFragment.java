package bigappcompany.com.myoffers.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.activity.Description;
import bigappcompany.com.myoffers.activity.ShopList;
import bigappcompany.com.myoffers.adapter.HomeBanner_Adapter;
import bigappcompany.com.myoffers.adapter.RecentViewAdapter;
import bigappcompany.com.myoffers.adapter.SecondTabAdapter;
import bigappcompany.com.myoffers.model.BannerModel;
import bigappcompany.com.myoffers.model.CatDataModel;
import bigappcompany.com.myoffers.model.CategoryModel;
import bigappcompany.com.myoffers.model.Categorydate;
import bigappcompany.com.myoffers.model.DescProdModel;
import bigappcompany.com.myoffers.model.LocationModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Download_web;
import bigappcompany.com.myoffers.network.NetworkReceiver;
import bigappcompany.com.myoffers.network.OnTaskCompleted;
import bigappcompany.com.myoffers.network.Utility;
import bigappcompany.com.myoffers.network.WebServices;

import static java.lang.Double.parseDouble;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 14 Jul 2017 at 5:20 PM
 */

@SuppressLint("ValidFragment")
public class SecondFragment extends Fragment {
	private String id, name;
	
	@SuppressLint("ValidFragment")
	public SecondFragment(String id) {
		this.id = id;
	}
	
	private RecyclerView cattab_rec;
	private SecondTabAdapter cattab_adapter;
	private RecentViewAdapter recentViewAdapter;
	private ArrayList<CatDataModel> modelArrayList = new ArrayList<>();
	private ArrayList<CatDataModel> storedArrayList = new ArrayList<>();
	private ArrayList<DescProdModel> descProdModels = new ArrayList<>();
	private ArrayList<LocationModel> locationModels = new ArrayList<>();
	private static ViewPager mPager;
	private static int currentPage = 0;
	private static int NUM_PAGES = 0;
	private ArrayList<Categorydate> catBanners = new ArrayList<>();
	private TextView nooffers;
	private LinearLayout noofferlayout;
	private NetworkReceiver networkReceiver;
	private IntentFilter intentFilter;
	ArrayList<LocationModel> locdata = new ArrayList<>();
	private View v;
	private SwipeRefreshLayout swipeRefreshLayout;
	private boolean Internetconn;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.home_fragment, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		v = view;
		
		mPager = (ViewPager) view.findViewById(R.id.pager);
		nooffers = (TextView) view.findViewById(R.id.nooffers);
		noofferlayout = (LinearLayout) view.findViewById(R.id.noofferlayout);
		
		cattab_rec = (RecyclerView) view.findViewById(R.id.homecat_rec);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		cattab_rec.setLayoutManager(mLayoutManager);
		cattab_rec.setItemAnimator(new DefaultItemAnimator());
		nooffers.setTypeface(Utility.font(getActivity(), "medium"));
		swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Refresh items
				refreshItems();
			}
		});
		
		setvalue(view);
		
		networkReceiver = new NetworkReceiver() {
			@Override
			protected void onSmsReceived(boolean s) {
				Internetconn = s;
				if (s) {
					domapping(v);
					
				} else {
					domapping(v);
				}
			}
		};
		
		intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(1000);
		
		
	}
	
	
	private void refreshItems() {
		if (Internetconn) {
			callcategoryapi();
		} else {
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	private void domapping(View view) {
		
		if (storedArrayList.size() == 0) {
			if (Utility.isNetworkAvailable(getActivity())) {
				
				callcategoryapi();
			}
		} else {
			locationfilter(storedArrayList);
			getdistance(storedArrayList);
			if (catBanners.size() > 0) {
				mPager.setAdapter(new HomeBanner_Adapter(getActivity(), catBanners));
			} else {
				getBanner();
			}
		}
	}
	
	
	private void callcategoryapi() {
		
		Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				swipeRefreshLayout.setRefreshing(false);
				
				if (response != null && response.length() > 0) {
					
					Gson gson = new Gson();
					CategoryModel categoryModel = gson.fromJson(response, CategoryModel.class);
					if (categoryModel.getStatus().equalsIgnoreCase("true")) {
						modelArrayList.clear();
						modelArrayList = categoryModel.getData();
						if (modelArrayList.size() > 0) {
							locationfilter(modelArrayList);
							getdistance(modelArrayList);
						} else {
							noofferlayout.setVisibility(View.VISIBLE);
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
			post_dict.put("category", id);
			web.setstringData(post_dict.toString());
			web.execute(WebServices.CATEGORY);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		getBanner();
	}
	
	private void locationfilter(ArrayList<CatDataModel> modelArrayList) {
		locdata.clear();
		String distance = "";
		Double latti = null, longi = null;
		try {
			latti = parseDouble(ClsGeneral.getPreferences
			    (getActivity(), ClsGeneral.LATITUTE));
			longi = parseDouble(ClsGeneral.getPreferences
			    (getActivity(), ClsGeneral.LONGITUTE));
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
	
	private void getBanner() {
		
		
		Download_web web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					
					Gson gson = new Gson();
					BannerModel categoryModel = gson.fromJson(response, BannerModel.class);
					if (categoryModel.getStatus().equalsIgnoreCase("true")) {
						catBanners.clear();
						catBanners = categoryModel.getData();
						if (catBanners.size() > 0) {
							mPager.setAdapter(new HomeBanner_Adapter(getActivity(), catBanners));
						} else {
						}
					}
					
				}
			}
		});
		web.setReqType("get");
		try {
			web.execute(WebServices.BANNERS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void getdistance(ArrayList<CatDataModel> modlArrayList) {
		ArrayList<CatDataModel> filteredlist = new ArrayList<>();
		Double latti = null, longi = null;
		try {
			latti = parseDouble(ClsGeneral.getPreferences
			    (getActivity(), ClsGeneral.LATITUTE));
			longi = parseDouble(ClsGeneral.getPreferences
			    (getActivity(), ClsGeneral.LONGITUTE));
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
		cattab_adapter = new SecondTabAdapter(getActivity(), filteredlist, locdata, SecondFragment.this);
		cattab_rec.setAdapter(cattab_adapter);
		
		if (filteredlist.size() == 0) {
			nooffers.setVisibility(View.VISIBLE);
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
	
	
	private void setvalue(View view) {
		
		try {
			CirclePageIndicator indicator = (CirclePageIndicator) view.
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
		
	}
	
	
	public void sendpos(int position) {
		ClsGeneral.setPreferences(getActivity(), ClsGeneral.CATEGORYNAME, name);
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
			
			
			startActivity(new Intent(getActivity(), ShopList.class).putParcelableArrayListExtra("list",
			    locationModels).putExtra("name", modelArrayList.get(position).getShop_name()));
			
		} else if (locationModels != null && locationModels.size() == 1) {
			
			startActivity(new Intent(getActivity(), Description.class).putExtra("id", locationModels.get
			    (0).getShop_id()).putExtra("name", locationModels.get(0).getShop_name()).putExtra("local", "no"));
		} else {
			startActivity(new Intent(getActivity(), Description.class).putExtra("id", modelArrayList.get(position).getId())
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
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (intentFilter != null) {
			getActivity().registerReceiver(networkReceiver, intentFilter);
		}
		
		
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (intentFilter != null) {
			getActivity().unregisterReceiver(networkReceiver);
		}
	}
}
