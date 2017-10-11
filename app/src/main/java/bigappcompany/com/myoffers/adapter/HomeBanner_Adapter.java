package bigappcompany.com.myoffers.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.model.Categorydate;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 12 Jul 2017 at 4:42 PM
 */

public class HomeBanner_Adapter extends PagerAdapter {
	
	
	private ArrayList<Categorydate> imageModelArrayList;
	private LayoutInflater inflater;
	private Context context;
	
	
	public HomeBanner_Adapter(Context context, ArrayList<Categorydate> imageModelArrayList) {
		this.context = context;
		this.imageModelArrayList = imageModelArrayList;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public int getCount() {
		return imageModelArrayList.size();
	}
	
	@Override
	public Object instantiateItem(ViewGroup view, int position) {
		View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
		
		assert imageLayout != null;
		final ImageView imageView = (ImageView) imageLayout
		    .findViewById(R.id.image);
		
		
		Picasso.with(context).load(imageModelArrayList.get(position).getAdvt_banner_image()).into(imageView);
		
		view.addView(imageLayout, 0);
		
		return imageLayout;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}
	
	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
	
	
}
