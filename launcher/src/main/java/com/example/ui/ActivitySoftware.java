package com.example.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;

import com.example.control.LoadApplications;
import com.example.event.MessageEvent;
import com.example.launcher.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.example.ui.MainActivity.BROADCAST_ACTION;


/**
 * Created by Home on 17.12.2016.
 */
public class ActivitySoftware extends Activity implements View.OnClickListener {


	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;

	// rECYCLER
	Button btnGrid;
	Button btnList;
	RecyclerView mRecyclerView;
	RecyclerViewAdapter mAdapter;
	RecyclerView.LayoutManager layoutManager;
	private EditText editText;
	private Filter filterApp;

	BroadcastReceiver br;
	ProgressDialog progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_software);


		btnGrid = (Button) findViewById(R.id.btnGrid);
		btnList = (Button) findViewById(R.id.btnList);

		btnGrid.setOnClickListener(this);
		btnList.setOnClickListener(this);

		editText = (EditText) findViewById(R.id.edit);
		editText.addTextChangedListener(textWatcher);

		mRecyclerView = (RecyclerView) findViewById(R.id.listRecycl);
		mRecyclerView.setHasFixedSize(true);

		br = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				onLoadApplication();
			}
		};
		registerReceiver(br, new IntentFilter(BROADCAST_ACTION));

		new LoadApplications(this).execute();
		progress = ProgressDialog.show(ActivitySoftware.this, null, "Loading apps info...");
	}

	TextWatcher textWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			filterApp.filter(s);
		}
	};


	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(br);
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View view) {
		RecyclerView.LayoutManager layoutManager;
		switch (view.getId()) {
			case R.id.btnGrid:
				mAdapter.setLayoutRes(R.layout.grid_item);
				layoutManager = new GridLayoutManager(this, 3);
				mRecyclerView.setLayoutManager(layoutManager);
				break;

			case R.id.btnList:
				mAdapter.setLayoutRes(R.layout.list_item);
				layoutManager = new LinearLayoutManager(this);
				mRecyclerView.setLayoutManager(layoutManager);
				break;
		}
	}

	public void onLoadApplication() {

		progress.dismiss();

		mAdapter = new RecyclerViewAdapter(this, LoadApplications.applist);
		mAdapter.setLayoutRes(R.layout.grid_item);
		filterApp = mAdapter.getFilter();
		mRecyclerView.setAdapter(mAdapter);


		layoutManager = new GridLayoutManager(this, 3);
		mRecyclerView.setLayoutManager(layoutManager);
	}


	@Subscribe
	public void onMessageEvent(MessageEvent event) {
		/* Do something */
		onLoadApplication();
	}

}