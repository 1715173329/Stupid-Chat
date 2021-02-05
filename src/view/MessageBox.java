package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import model.message.Message;
import model.message.TextMessage;
import util.MessageStatus;
import util.MessageType;

public class MessageBox extends HBox{
	private MessageBubble messageBubble;
	private int messageId;
	private MessageType messageType;
	private MessageStatus messageStatus;
	
	//����ܲ���һ���õ����������治Ӧ�� model ��̫����ϵ����������ʱ��������
	public MessageBox(Message message, MessageType messageType) {
		this.messageType = messageType;
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			this.messageId = textMessage.getId();
			String text = textMessage.getContent();
			messageBubble = new MessageBubble(text, messageType);
			this.getChildren().add(messageBubble);
			if(messageType == MessageType.SENDED) {
				this.setAlignment(Pos.CENTER_RIGHT);
			}else {
				this.setAlignment(Pos.CENTER_LEFT);
			}
		}
		
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
	
	public void confirm() {
		messageBubble.confirm();
	}
	
	public int getMessageId() {
		return messageId;
	}
	
}
