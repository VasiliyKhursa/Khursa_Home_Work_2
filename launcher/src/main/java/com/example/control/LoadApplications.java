package com.example.control;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.example.event.MessageEvent;
import com.example.ui.MainActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Home on 02.02.2017.
 */
public class LoadApplications extends AsyncTask<Void, Void, Void> {


	public static final int NOT_CREATE = 0;
	public static final int START_FIND = 1;
	public static final int END_FIND = 2;

	public static List<ApplicationInfo> applist = null;
	public static int flag = NOT_CREATE;                   // флаг состояния

	private PackageManager packageManager = null;
	Context context;

	public LoadApplications(Context context) {
		packageManager = context.getPackageManager();
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		flag = START_FIND;
	}

	@Override
	protected Void doInBackground(Void... params) {
		applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		flag = END_FIND;

		// сообщаем об окончании задачи
		EventBus.getDefault().post(new MessageEvent().field = END_FIND);
		context.sendBroadcast(new Intent(MainActivity.BROADCAST_ACTION));
	}


	private List checkForLaunchIntent(List<ApplicationInfo> list) {

		ArrayList appList = new ArrayList();

		for (ApplicationInfo info : list) {
			try {
				if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
					appList.add(info);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return appList;
	}
}
