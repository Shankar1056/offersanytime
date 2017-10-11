package bigappcompany.com.myoffers.fragment;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.network.TouchImageView;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 14 Jul 2017 at 7:51 PM
 */

public class FullImageFragment extends Fragment implements View.OnClickListener {
	
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy =
			    new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		final View rootView = inflater.inflate(R.layout.fullimage_fragment, container, false);
		
		final Bundle args = getArguments();
		int total_length = Integer.parseInt(args.getString("total_length"));
		int pos = Integer.parseInt(args.getString("pos"));
		
		TouchImageView expanded_image = (TouchImageView) rootView.findViewById(R.id.expanded_image);
		TextView count = (TextView) rootView.findViewById(R.id.count);
		TextView expiry = (TextView) rootView.findViewById(R.id.expiry);
		rootView.findViewById(R.id.backimage).setOnClickListener(this);
		expanded_image.setMaxZoom(4f);
		
		count.setText(total_length + "/" + (pos + 1));
		expiry.setText("Valid till : " + args.getString("valid_till"));
		Picasso.with(getActivity()).load(args.getString("imageurl")).into(expanded_image);
		
		
		return rootView;
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backimage:
				getActivity().finish();
				break;
		}
		
	}
}
