package com.b509.xiaoliao;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

/*
 * 张川
 * 用于选择界面
 * 
 */
public class ChooseActivity extends mActivity {

	ImageButton begin;
	ImageButton search;
	ImageButton establish;
	SharedPreferences preferences;

	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		preferences = getSharedPreferences("xiaoliao", Context.MODE_PRIVATE);

		isFirst = preferences.getBoolean("isFirst", true);
		if (isFirst) {
			Intent intent = new Intent(ChooseActivity.this,
					ViewFlipperActivity.class);
			ChooseActivity.this.startActivity(intent);
			ChooseActivity.this.finish();
		} else {
			init();
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_choose);

		begin = (ImageButton) findViewById(R.id.choose_begin);
		search = (ImageButton) findViewById(R.id.choose_search);
		establish = (ImageButton) findViewById(R.id.choose_establish);

		// 进入聊天界面
		begin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(ChooseActivity.this, MainActivity.class);
				finish();
				startActivity(intent);

			}

		});

		// 创建网络
		establish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(ChooseActivity.this, WTActivity.class);
				startActivity(intent);
			}
		});

		// 搜索网络
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(
						android.provider.Settings.ACTION_WIFI_SETTINGS));
			}
		});
	}
}
