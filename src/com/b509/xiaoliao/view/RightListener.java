package com.b509.xiaoliao.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.b509.xiaoliao.About;
import com.b509.xiaoliao.R;
import com.b509.xiaoliao.ViewFlipperActivity;
import com.b509.xiaoliao.tools.IntentUtils;

public class RightListener {
	private static String rtitle[] = { "���", "����", "����", "����" };

	public static void RightListeners(final Context context, View rightView) {

		ListView lvRight = (ListView) rightView.findViewById(R.id.lvRight);
		lvRight.setAdapter(new ArrayAdapter<String>(context, R.layout.item,
				R.id.tv_item, rtitle));
		lvRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				switch (arg2) {
				case 0:
					Uri uri = Uri.parse("mailto:kuoyu1992@gmail.com");    
					Intent mailIntent = new Intent(Intent.ACTION_SENDTO, uri);  

					mailIntent.putExtra(Intent.EXTRA_SUBJECT,"�Ҷ�΢�ĵĽ���");
					context. startActivity(mailIntent);    

					break;
				case 1:
					IntentUtils.sendSharedIntent(context,
							"���ڿ�����������죬���ٴ����ļ����ͺ�����һ������Ϸ�ˡ���С�ģ��S���������ġ�");
					break;
				case 2:
					context.startActivity(new Intent(context,
							ViewFlipperActivity.class));

					break;
				case 3:
					context.startActivity(new Intent(context, About.class));
					break;

				default:
					break;
				}

			}
		});
	}
}
