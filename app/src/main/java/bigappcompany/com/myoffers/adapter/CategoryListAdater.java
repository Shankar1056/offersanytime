package bigappcompany.com.myoffers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.activity.CategryList;
import bigappcompany.com.myoffers.model.CatDataModel;
import bigappcompany.com.myoffers.model.LocationModel;
import bigappcompany.com.myoffers.network.ClsGeneral;
import bigappcompany.com.myoffers.network.Utility;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 13 Jul 2017 at 4:52 PM
 */

public class CategoryListAdater extends RecyclerView.Adapter<CategoryListAdater.MyViewHolder> {
	
	private List<CatDataModel> myCartModels;
	private Context context;
	private CategryList cart;
	SimpleDateFormat simpleDateFormat =
	    new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
	ArrayList<LocationModel> locdata;
	
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView cat_name, cat_location, distanceinkm, daydate;
		public ImageView image;
		
		public MyViewHolder(View view) {
			super(view);
			cat_name = (TextView) view.findViewById(R.id.cat_name);
			cat_location = (TextView) view.findViewById(R.id.cat_location);
			image = (ImageView) view.findViewById(R.id.image);
			distanceinkm = (TextView) view.findViewById(R.id.distanceinkm);
			daydate = (TextView) view.findViewById(R.id.daydate);
		}
	}
	
	
	public CategoryListAdater(Context context, List<CatDataModel> myCartModels, ArrayList<LocationModel> locdata, CategryList cart) {
		this.context = context;
		this.myCartModels = myCartModels;
		this.cart = cart;
		this.locdata = locdata;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
		    .inflate(R.layout.homefrag_item, parent, false);
		
		return new MyViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		final CatDataModel sa = myCartModels.get(position);
		try {
			Double latti = Double.parseDouble(ClsGeneral.getPreferences(context, ClsGeneral.LATITUTE));
			Double longi = Double.parseDouble(ClsGeneral.getPreferences(context, ClsGeneral.LONGITUTE));
			
			holder.cat_name.setTypeface(Utility.font(context, "bold"));
			holder.cat_location.setTypeface(Utility.font(context, "regular"));
			
			Picasso.with(context).load(sa.getShop_image()).into(holder.image);
			holder.cat_name.setText(sa.getShop_name());
			holder.distanceinkm.setText("" + sa.getDistance() + " km");
			String text = "";
			
			for (int i = 0; i < locdata.size(); i++) {
				try {
					if (myCartModels.get(position).getParent_id() != null && myCartModels.get(position).getParent_id().length() > 0) {
						if (myCartModels.get(position).getParent_id().equalsIgnoreCase(locdata.get(i).getParent_id())) {
							text = locdata.get(i).getLocation();
							holder.cat_location.setText(text);
							break;
						}
						
					} else {
						text = myCartModels.get(position).getShop_location();
						holder.cat_location.setText(text);
					}
				} catch (Exception e) {
					e.printStackTrace();
					text = myCartModels.get(position).getShop_location();
					holder.cat_location.setText(text);
				}
				
			}
			
			
			try {
				
				Date date1 = simpleDateFormat.parse(sa.getRegistered_date());
				Date date2 = simpleDateFormat.parse(Utility.GetTodaysDate());
				
				holder.daydate.setText(Utility.GetDateTimeDifference(date1, date2));
				
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			holder.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					cart.sendpos(position);
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int getItemCount() {
		return myCartModels.size();
	}
	
}
