package com.b509.xiaoliao.inter;


public interface OnUDPReceiveMessage {
	/**�������ݱ��Ž���Ϣ���к�Ļص�����*/
	void onReceive(int type);
	void sendFailure();
}
