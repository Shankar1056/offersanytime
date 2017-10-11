package bigappcompany.com.myoffers.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.activity.CategryList;
import bigappcompany.com.myoffers.adapter.MoreAdapter;
import bigappcompany.com.myoffers.model.CategoryTabModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Download_web;
import bigappcompany.com.myoffers.network.NetworkReceiver;
import bigappcompany.com.myoffers.network.OnTaskCompleted;
import bigappcompany.com.myoffers.network.Utility;
import bigappcompany.com.myoffers.network.WebServices;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 12 Jul 2017 at 7:02 PM
 */

@SuppressLint("ValidFragment")
public class MoreFragment extends Fragment {
	private ArrayList<CategoryTabModel> modelArrayList = new ArrayList<>();
	private RecyclerView more_rec;
	private NetworkReceiver networkReceiver;
	private IntentFilter intentFilter;
	private View v;
	private boolean isInternetconnected = false;
	
	@SuppressLint("ValidFragment")
	public MoreFragment(String name) {
	}
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.more_fragment, container, false);
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		v = view;
		domapping(v);
		networkReceiver = new NetworkReceiver() {
			@Override
			protected void onSmsReceived(boolean s) {
				isInternetconnected = s;
				
			}
		};
		
		intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		intentFilter.setPriority(1000);
		if (modelArrayList.size() == 0) {
			if (Utility.isNetworkAvailable(getActivity()) || isInternetconnected) {
				callcategoryapi();
			}
		} else {
			
			MoreAdapter moreAdapter = new MoreAdapter(getActivity(),
			    modelArrayList, MoreFragment.this);
			more_rec.setAdapter(moreAdapter);
		}
		
	}
	
	private void domapping(View view) {
		more_rec = (RecyclerView) view.findViewById(R.id.more_rec);
		RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
		more_rec.setLayoutManager(mLayoutManager);
		more_rec.setItemAnimator(new DefaultItemAnimator());
		more_rec.setHasFixedSize(true);
		
	}
	
	private void callcategoryapi() {
		
		Download_web download_web = new Download_web(getActivity(), new OnTaskCompleted() {
			@Override
			public void onTaskCompleted(String response) {
				
				if (response != null && response.length() > 0) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						if (jsonObject.optString("status").equalsIgnoreCase("true")) {
							modelArrayList.clear();
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							for (int i = 1; i < jsonArray.length(); i++) {
								JSONObject jo = jsonArray.getJSONObject(i);
								String b_cat_id = jo.optString("b_cat_id");
								String b_cat_name = jo.optString("b_cat_name");
								String icon = jo.optString("icon");
								String deselect_icon = jo.optString("deselect_icon");
								CategoryTabModel model = new CategoryTabModel(b_cat_id,
								    b_cat_name, icon, deselect_icon);
								modelArrayList.add(model);
							}
							MoreAdapter moreAdapter = new MoreAdapter(getActivity(),
							    modelArrayList, MoreFragment.this);
							more_rec.setAdapter(moreAdapter);
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
	
	public void sendpos(int position) {
		ClsGeneral.setPreferences(getActivity(), ClsGeneral.CATEGORYNAME, modelArrayList.get(position)
		    .getB_cat_name());
		startActivity(new Intent(getActivity(), CategryList.class).putExtra("id", modelArrayList.get(position)
		    .getB_cat_id()).putExtra("name", modelArrayList.get(position)
		    .getB_cat_name()));
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
