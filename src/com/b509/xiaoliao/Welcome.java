package com.b509.xiaoliao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Welcome extends mActivity {

	private static final String SHAREDPREFERENCES_NAME = "xiaoliao";
	ProgressBar bar;
	int i = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.first);

		bar = (ProgressBar) findViewById(R.id.bar);
		message.post(handler);

	}

	Handler message = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			bar.setProgress(msg.arg1);
			message.post(handler);

		}

	};
	Runnable handler = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			i += 10;
			Message msg = message.obtainMessage();
			msg.arg1 = i;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			message.sendMessage(msg);
			if (i == bar.getMax()) {
				bar.removeCallbacks(handler);

				Intent intent = new Intent(Welcome.this, ChooseActivity.class);
				Welcome.this.startActivity(intent);
				Welcome.this.finish();

			}

		}
	};

}
