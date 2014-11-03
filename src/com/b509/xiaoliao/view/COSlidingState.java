package com.b509.xiaoliao.view;

/**
 * 当前界面的状态
 * @author zky
 * @version 创建时间：2013年9月19日13:23:39
 */

public enum COSlidingState {
	SHOWLEFT("显示左边"),//显示左边 
	SHOWCENTER("显示中间"),//显示中间
	SHOWRIGHT("显示右边");//显示右边
	
	
	private final String desc;
	private COSlidingState(String desc) {
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}
}
