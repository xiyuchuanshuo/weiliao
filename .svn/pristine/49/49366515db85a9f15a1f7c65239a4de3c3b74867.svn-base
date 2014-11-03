package com.b509.xiaoliao;

import java.io.IOException;
import java.net.InetAddress;

import android.os.Bundle;

import com.b509.xiaoliao.listener.UDPVoiceListener;

/**
 * 语音聊天
 * @author zky
 * @creation 2013年9月20日18:57:17
 */
public class VoiceChat extends mActivity{

		private String chatterIP;//记录当前用户ip
		private UDPVoiceListener voiceListener;
		
	    @Override  
	    public void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.video_chat);  
	        chatterIP=getIntent().getStringExtra("IP");
	        findViews();
	        try {
				voiceListener=UDPVoiceListener.getInstance(InetAddress.getByName(chatterIP));
				 voiceListener.open();
			} catch (IOException e) {
				e.printStackTrace();
//				finish();
				showToast("抱歉，语音聊天器打开失败");
			}
	    }  
	    
	    private void findViews(){
	    	
	    }
	    
	    
	    @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        try {
				voiceListener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }  
	
}
