package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import util.MessageStatus;
import util.MessageType;

public class MessageBox extends HBox{
	private int id;
	private String text;
	private MessageType messageType;
	private MessageStatus messageStatus;
	
	public MessageBox(String text,String id) {
		
	}
	
	//TODO: �����Ӧ����Ψһһ�������ȥ new ���࣬����Ҫ��д���췽�����Լ�������ʲô�ĸ㶨
	public MessageBox(MessageBubble messageBubble) {
		if(messageBubble.getMessageType()==MessageType.SENDED) {
			this.getChildren().add(messageBubble);
			this.setAlignment(Pos.CENTER_RIGHT);
		}else {
			this.getChildren().add(messageBubble);
			this.setAlignment(Pos.CENTER_LEFT);
		}
		
	}
}
