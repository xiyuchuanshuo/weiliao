package com.b509.xiaoliao.view;

/**
 * ��ǰ�����״̬
 * @author zky
 * @version ����ʱ�䣺2013��9��19��13:23:39
 */

public enum COSlidingState {
	SHOWLEFT("��ʾ���"),//��ʾ��� 
	SHOWCENTER("��ʾ�м�"),//��ʾ�м�
	SHOWRIGHT("��ʾ�ұ�");//��ʾ�ұ�
	
	
	private final String desc;
	private COSlidingState(String desc) {
		this.desc = desc;
	}

	public String getDesc(){
		return desc;
	}
}
