package com.b509.xiaoliao.tools;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Intent������
 * 
 * @author way
 * 
 */
public class IntentUtils {

	public static final void sendSharedIntent(Context context,String item) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/*");

		String titleKey = "׷�����";
	

		StringBuilder sb = new StringBuilder();
		sb.append(titleKey).append(item);

		intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
		intent.putExtra(Intent.EXTRA_SUBJECT, "����");
		context.startActivity(Intent.createChooser(intent,"����" + ":" + item));
	}
}
