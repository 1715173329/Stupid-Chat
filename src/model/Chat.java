package model;

import java.net.SocketAddress;

import model.message.AcknowledgeMessage;
import model.message.TextMessage;
import view.View;

/*
 * TODO: CHAT������ά��һ�����죬��¼�յ��ĺͷ�������Ϣ�Լ�����״̬
 * �����mvc�ܹ��� model �����ã�chatwindow�е�һЩ������Ҫ����ֲ��������
 */

public class Chat {
	private Sender sender;
	private View view;
	private SocketAddress socketAddress;
	
	public void send(String text) {
		sender.send(new TextMessage(socketAddress, text));
	}
	
	public void confirm(AcknowledgeMessage acknowledgeMessage) {
		
	}

}
