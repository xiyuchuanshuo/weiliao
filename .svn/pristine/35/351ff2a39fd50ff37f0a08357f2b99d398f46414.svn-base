package com.b509.xiaoliao.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.b509.xiaoliao.MessageChat.MessageUpdateBroadcastReceiver;
/**
 * 心跳包检测，只检测通信双方
 * @author zky
 * @creation 2013年9月20日19:01:38
 */
public class HeartBeatBroaadcastReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intent2=new Intent();
		intent2.setAction(MessageUpdateBroadcastReceiver.ACTION_HEARTBEAT);
		context.sendBroadcast(intent2);
		//intent.getData().getSchemeSpecificPart();
	}

}
