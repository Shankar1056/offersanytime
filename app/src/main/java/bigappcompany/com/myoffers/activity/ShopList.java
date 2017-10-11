package bigappcompany.com.myoffers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.adapter.ShopListAdapter;
import bigappcompany.com.myoffers.model.LocationModel;
import bigappcompany.com.myoffers.network.Utility;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 10 Jul 2017 at 11:32 AM
 */

public class ShopList extends AppCompatActivity implements View.OnClickListener {
	private RecyclerView recycler_list;
	private ArrayList<LocationModel> locationModels = new ArrayList<>();
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.shop_list);
		domapping();
		
		
	}
	
	private void domapping() {
		
		findViewById(R.id.backbutton).setOnClickListener(this);
		TextView toolbartext = (TextView) findViewById(R.id.toolbartext);
		toolbartext.setTypeface(Utility.font(ShopList.this, "medium"));
		recycler_list = (RecyclerView) findViewById(R.id.recycler_list);
		recycler_list.setHasFixedSize(true);
		recycler_list.setNestedScrollingEnabled(false);
		recycler_list.setLayoutManager(new LinearLayoutManager(ShopList.this, LinearLayoutManager.VERTICAL, false));
		recycler_list.setNestedScrollingEnabled(true);
		
		toolbartext.setText(getIntent().getStringExtra("name"));
		locationModels.clear();
		locationModels = getIntent().getParcelableArrayListExtra("list");
		
		for (int i = 0; i < locationModels.size(); i++) {
			if (locationModels.get(i).getDistance() <= 99.0) {
				
				
			} else {
				locationModels.remove(i);
			}
			
		}
		
		
		ShopListAdapter shopListAdapter = new ShopListAdapter(ShopList.this, locationModels, ShopList.this);
		recycler_list.setAdapter(shopListAdapter);
	}
	
	public void sendpos(int position) {
		startActivity(new Intent(ShopList.this, Description.class).putExtra("id", locationModels.get(position).getShop_id())
		    .putExtra("name", locationModels.get(position).getShop_name()).putExtra("local", "no"));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backbutton:
				finish();
				break;
		}
		
	}
	
	
}
