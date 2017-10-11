package bigappcompany.com.myoffers.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.adapter.FullImagesAdapter;
import bigappcompany.com.myoffers.model.CatBanner;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 14 Jul 2017 at 7:39 PM
 */

public class FullImageActivity extends AppCompatActivity{
	private ViewPager mViewPager;
	private ArrayList<CatBanner> catBanners = new ArrayList<>();
	private FullImagesAdapter fullImagesAdapter;
	private String position;
	
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.fullscreen_activity);
		catBanners = getIntent().getParcelableArrayListExtra("list");
		position = getIntent().getStringExtra("pos");
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		fullImagesAdapter = new FullImagesAdapter(getSupportFragmentManager(),
		    catBanners);
		mViewPager.setAdapter(fullImagesAdapter);
		try {
			mViewPager.setCurrentItem(Integer.parseInt(position));
		}
		catch (NumberFormatException e)
		{mViewPager.setCurrentItem(0);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
