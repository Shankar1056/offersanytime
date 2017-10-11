package bigappcompany.com.myoffers.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.fragment.HomeFragment;
import bigappcompany.com.myoffers.model.DescProdModel;
import bigappcompany.com.myoffers.network.Utility;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 06 Jul 2017 at 5:17 PM
 */

public class RecentViewAdapter extends RecyclerView.Adapter<RecentViewAdapter.MyViewHolder> {
	
	private List<DescProdModel> myCartModels;
	private Context context;
	private HomeFragment cart;
	
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView cat_name, cat_location;
		public ImageView image;
		
		public MyViewHolder(View view) {
			super(view);
			cat_name = (TextView) view.findViewById(R.id.cat_name);
			cat_location = (TextView) view.findViewById(R.id.cat_location);
			image = (ImageView) view.findViewById(R.id.image);
		}
	}
	
	
	public RecentViewAdapter(Context context, List<DescProdModel> myCartModels, HomeFragment cart) {
		this.context = context;
		this.myCartModels = myCartModels;
		this.cart = cart;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
		    .inflate(R.layout.recentfrag_item, parent, false);
		
		return new MyViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		final DescProdModel sa = myCartModels.get(position);
		holder.cat_name.setTypeface(Utility.font(context, "bold"));
		holder.cat_location.setTypeface(Utility.font(context, "regular"));
		
		Picasso.with(context).load(sa.getShop_image()).into(holder.image);
		holder.cat_name.setText(sa.getShop_name());
		
		
		holder.cat_location.append(myCartModels.get(position).getAddress_line_1());
		
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cart.senrecentdpos(position);
			}
		});
		
	}
	
	@Override
	public int getItemCount() {
		return myCartModels.size();
	}
	
}

