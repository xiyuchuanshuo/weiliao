package com.b509.xiaoliao.tools;

import android.content.Context;

import com.b509.xiaoliao.R;

public class Medias {

	public static SoundPlay soundPlay;
	public static final int ID_TISHI = 0;
	
	

	public static void initSound(Context context) {
		soundPlay = new SoundPlay();
		soundPlay.initSounds(context);
		soundPlay.loadSfx(context, R.raw.tishi, ID_TISHI);
		
	}

	public static void play(int i) {
		switch (i) {
		case 0:

			soundPlay.play(ID_TISHI, 0);
			break;
		
		default:
			break;
		}
	}
}
