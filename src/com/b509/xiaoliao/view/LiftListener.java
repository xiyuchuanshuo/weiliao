package com.b509.xiaoliao.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.b509.xiaoliao.ChooseActivity;
import com.b509.xiaoliao.R;
import com.b509.xiaoliao.WTActivity;
import com.b509.xiaoliao.util.LocalMemoryCache;
import com.b509.xiaoliao.util.Util;

public class LiftListener {
	private static String Ltitle[] = { "�޸�����", "��������", "�˳�" };
	private static EditText nickNameEdt;
	private static Button nickNameBtn;
	private static WifiManager wifiManager;
	private static boolean flag = false;

	public static void LiftListeners(final Context context, View leftView) {
		ListView lvLeft = (ListView) leftView.findViewById(R.id.lvLeft);
		nickNameEdt = (EditText) leftView.findViewById(R.id.et_name);
		nickNameBtn = (Button) leftView.findViewById(R.id.bt_name);
		wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		nickNameEdt.setText(context.getSharedPreferences("me", 0).getString(
				"name", "����"));
		nickNameBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nickName = nickNameEdt.getText().toString().trim();
				if ("".equals(nickName)) {
					Toast.makeText(context, "�������ǳ�", Toast.LENGTH_SHORT).show();
					return;
				}
				if (nickName.length() > 6) {
					Toast.makeText(context, "���ܳ���6���ַ�", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Editor editor = context.getSharedPreferences("me", 0).edit();
				editor.putString("name", nickName);
				editor.commit();
				Toast.makeText(context, "����ɹ�", Toast.LENGTH_SHORT).show();
			}
		});

		lvLeft.setAdapter(new ArrayAdapter<String>(context, R.layout.item,
				R.id.tv_item, Ltitle));
		lvLeft.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(MainActivity.this, "" + arg2 + "",
				// Toast.LENGTH_SHORT).show();
				switch (arg2) {
				case 0:
					context.startActivity(new Intent(
							android.provider.Settings.ACTION_WIFI_SETTINGS));
					break;
				case 1:
					
					Intent intent = new Intent();
					intent.setClass(context, WTActivity.class);
					context.startActivity(intent);
//					flag = !flag;
//					setWifiApEnabled(flag);
//					Toast.makeText(context, "���ڼ����ȵ㣬���Ժ󡣡���", 1).show();
					break;
				case 2:
					System.exit(-1);
					break;
				default:
					break;
				}
			}
		});

	}

	public static boolean setWifiApEnabled(boolean enabled) {
		if (enabled) { // disable WiFi in any case
			// wifi���ȵ㲻��ͬʱ�򿪣����Դ��ȵ��ʱ����Ҫ�ر�wifi
			wifiManager.setWifiEnabled(false);
		}
		try {
			// �ȵ��������
			WifiConfiguration apConfig = new WifiConfiguration();
			// �����ȵ������(���������ֺ���ӵ������ʲô��)
			// apConfig.SSID = "Little Chat";
			// �����ȵ������
			// apConfig.preSharedKey="12122112";
			// ͨ��������������ȵ�
			Method method = wifiManager.getClass().getMethod(
					"setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);
			// �����ȵ��״̬
			return (Boolean) method.invoke(wifiManager, apConfig, enabled);
		} catch (Exception e) {
			return false;
		}
	}

}