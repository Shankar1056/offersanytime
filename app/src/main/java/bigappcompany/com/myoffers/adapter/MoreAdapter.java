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
import bigappcompany.com.myoffers.fragment.MoreFragment;
import bigappcompany.com.myoffers.model.CategoryTabModel;
import bigappcompany.com.myoffers.network.Utility;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 12 Jul 2017 at 7:24 PM
 */

public class MoreAdapter extends RecyclerView.Adapter<MoreAdapter.MyViewHolder> {
	
	private List<CategoryTabModel> myCartModels;
	private Context context;
	private MoreFragment cart;
	
	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView textname;
		public ImageView image;
		
		public MyViewHolder(View view) {
			super(view);
			textname = (TextView) view.findViewById(R.id.textname);
			image = (ImageView) view.findViewById(R.id.image);
		}
	}
	
	
	public MoreAdapter(Context context, List<CategoryTabModel> myCartModels, MoreFragment cart) {
		this.context = context;
		this.myCartModels = myCartModels;
		this.cart = cart;
	}
	
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
		    .inflate(R.layout.morefrag_item, parent, false);
		
		return new MyViewHolder(itemView);
	}
	
	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		final CategoryTabModel sa = myCartModels.get(position);
		try {
			holder.textname.setTypeface(Utility.font(context, "bold"));
			
			Picasso.with(context).load(sa.getDeselect_icon()).into(holder.image);
			holder.textname.setText(sa.getB_cat_name());
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
