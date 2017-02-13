package com.example.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.control.LoadApplications;
import com.example.control.StorePreference;
import com.example.event.MessageEvent;
import com.example.launcher.IListeners;
import com.example.launcher.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.example.launcher.R.layout.gridlayout_cell;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {

	GridView gridView;
	ArrayAdapter<String> adapter;
	String[] dataString = new String[12];

	ImageButton btnCall;
	ImageButton btnSms;
	Button btnApp;
	GridLayout gridLayout;

	StorePreference storePreference;

	BroadcastReceiver br;

	final String SAVED_TEXT = "saved_text";
	public final static String BROADCAST_ACTION = "khursa.launcher.br";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnCall = (ImageButton) findViewById(R.id.btnCall);
		btnSms = (ImageButton) findViewById(R.id.btnSms);
		btnApp = (Button) findViewById(R.id.btnApp);

		btnCall.setOnClickListener(this);
		btnApp.setOnClickListener(this);
		btnSms.setOnClickListener(this);


		gridLayout = (GridLayout) findViewById(R.id.mainGrid);
		gridLayout.setColumnCount(3);
		gridLayout.setRowCount(4);

		storePreference = new StorePreference(this);

		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Log.i("myTag", "MainActivity Broadcast receive");
				onLoadApplication();
			}
		};
		registerReceiver(br, new IntentFilter(BROADCAST_ACTION));

		//onLoadApplication();


	}

	@Override
	protected void onResume() {
		super.onResume();
		new LoadApplications(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
			case R.id.itemSettings:
				break;

			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();

		switch (v.getId()) {
			case R.id.btnCall:
				intent = new Intent(Intent.ACTION_DIAL);
				break;
			case R.id.btnApp:
				intent = new Intent(this, ActivitySoftware.class);
				break;
			case R.id.btnSms:
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("sms:"));
				break;
		}
		startActivity(intent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onMessageEvent(MessageEvent event) {
		/* Do something */
		Log.i("myTag", "MainActivity Receive EventBus message, data = " + event.field);
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.i("myTag", "MainActivity position = " + position);
	}

	public void onLoadApplication() {

		gridLayout.removeAllViews();
		for (int i = 0; i < 12; i++) {
			View view = LayoutInflater.from(this).inflate(gridlayout_cell, gridLayout, false);
			ImageView img = (ImageView) view.findViewById(R.id.gridlayout_iv);
			TextView textView = (TextView) view.findViewById(R.id.grid_text);
			if (!storePreference.getItem(i).equals("")) {
				try {
					ApplicationInfo info = getPackageManager().getApplicationInfo(storePreference.getItem(i), 0);
					CharSequence label = getPackageManager().getApplicationLabel(info);
					img.setImageDrawable(getPackageManager().getApplicationIcon(storePreference.getItem(i)));
					Log.i("myTag", "onLoadApplication() i = " + i + " package - " + storePreference.getItem(i));
					img.setTag(storePreference.getItem(i));
					textView.setText(label);
				} catch (PackageManager.NameNotFoundException e) {
					e.printStackTrace();
				}
			}
			else {
				img.setTag("");
			}


			IListeners listenrs = new IListeners(this, view);
			gridLayout.addView(view);

		}
	}
}