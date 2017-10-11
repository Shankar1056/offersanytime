package bigappcompany.com.myoffers.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import bigappcompany.com.myoffers.R;
import bigappcompany.com.myoffers.network.Utility;
import io.fabric.sdk.android.Fabric;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 15 Jul 2017 at 10:34 AM
 */

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.contactus);
		mapping();
	}
	
	private void mapping() {
		TextView toolbartext = (TextView) findViewById(R.id.toolbartext);
		TextView contacttext = (TextView) findViewById(R.id.contacttext);
		TextView mobiletext = (TextView) findViewById(R.id.mobiletext);
		TextView mobilenumber = (TextView) findViewById(R.id.mobilenumber);
		TextView emailtext = (TextView) findViewById(R.id.emailtext);
		TextView emailid = (TextView) findViewById(R.id.emailid);
		
		findViewById(R.id.backimage).setOnClickListener(this);
		findViewById(R.id.mobilenumbersecond).setOnClickListener(this);
		findViewById(R.id.mobilenumber).setOnClickListener(this);
		findViewById(R.id.emaillayout).setOnClickListener(this);
		toolbartext.setText(getResources().getString(R.string.contactus));
		
		
		toolbartext.setTypeface(Utility.font(ContactUs.this, "medium"));
		contacttext.setTypeface(Utility.font(ContactUs.this, "bold"));
		mobiletext.setTypeface(Utility.font(ContactUs.this, "bold"));
		mobilenumber.setTypeface(Utility.font(ContactUs.this, "regular"));
		emailtext.setTypeface(Utility.font(ContactUs.this, "bold"));
		emailid.setTypeface(Utility.font(ContactUs.this, "regular"));
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backimage:
				finish();
				break;
			case R.id.emaillayout:
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL, new String[]{"reachus@offersanytime.com"});
				try {
					startActivity(Intent.createChooser(i, "Send mail..."));
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(ContactUs.this, "Something went wrong.", Toast
					    .LENGTH_SHORT).show();
				}
				break;
			case R.id.mobilenumber:
				try {
					if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
					    PackageManager.PERMISSION_GRANTED) {
						
						ActivityCompat.requestPermissions(this, new String[]{Manifest
						    .permission.CALL_PHONE}, 11);
					}
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + "9963111945"));
					startActivity(intent);
				} catch (Exception e) {
					
				}
				break;
			case R.id.mobilenumbersecond:
				try {
					if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
					    PackageManager.PERMISSION_GRANTED) {
						
						ActivityCompat.requestPermissions(this, new String[]{Manifest
						    .permission.CALL_PHONE}, 11);
					}
					Intent intent = new Intent(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:" + "9986077333"));
					startActivity(intent);
				} catch (Exception e) {
					
				}
				break;
		}
		
	}
}
