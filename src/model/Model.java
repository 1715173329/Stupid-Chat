package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import model.message.AcknowledgeMessage;
import model.message.HeartbeatMessage;
import model.message.Message;
import model.message.TextMessage;
import view.View;

public class Model {
	private View view;
	private DatagramSocket socket;
	private Stunner stunner;
	private Sender sender;
	private Receiver receiver;
	private ReceiveThread receiveThread;
	private HolePuncher holePuncher;
	private PunchThread punchThread;	
	private InetSocketAddress publicSocketAddress;
		
	
	// to initialize
	public void setView(View view) {
		this.view = view;
	}

	public Model() {
		try {
			//TODO: ���������й��ܶ���Ҫ socket �ṩ��ʱ����
			socket = new DatagramSocket(0);
			socket.setSoTimeout(3000);
		} catch (SocketException e) {
			socket = null;
		}
		stunner = new Stunner(socket);
		sender = new Sender(socket);	
		receiver = new Receiver(socket);	
		holePuncher = new HolePuncher(socket);
	}
	
	//ͨ�� STUN ��������ȡ�Լ��Ĺ�����ַ�Ͷ˿ڣ���ʼ����Ϣ�����̺߳ʹ��߳�
	public void launch() {
		publicSocketAddress = stunner.stun();
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
		punchThread = new PunchThread(holePuncher);
		punchThread.start();
		view.init(publicSocketAddress);
	}
	
	public void exit() {
		//TODO ����ķ������Ա����ã�����֪��Ϊʲô�رմ���ʱ�޷��رճ���
		receiveThread.exit();
		punchThread.exit();
	}
		
	// ��������һ�����촰��֮�⣬����Ҫ������Ŀ�귢��һ���򶴱��ģ�������  hole puncher ������
	public void startChat(InetSocketAddress inetSocketAddress) {
		view.startChat(inetSocketAddress);
		holePuncher.punch(inetSocketAddress);
		holePuncher.addDesitination(inetSocketAddress);
	}
	
	public void send(String msg, SocketAddress socketAddress) {
		TextMessage textMessage = new TextMessage(socketAddress, msg);
		sender.send(textMessage);
	}
	
	public void send(Message message) {
		sender.send(message);
	}
	
	//����receiver��ȡһ����Ϣ�����ҽ���	
	public void receive() {
		Message message = receiver.receive();
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			String text = textMessage.getContent();
			Platform.runLater(()->{			
//				view.displayMessage(text);
				view.displayMessage(message);
			});	
			AcknowledgeMessage acknowledgeMessage = new AcknowledgeMessage(textMessage.getSocketAddress(), textMessage.getId());
			sender.send(acknowledgeMessage);
		}else if(message instanceof AcknowledgeMessage) {
			//TODO �յ�ȷ�ϱ��ĸ���ô�죿��Viewȥ����
			AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage)message;
			view.confirmMessage(acknowledgeMessage.getId());
		}else if(message instanceof HeartbeatMessage) {
			//kinda do nothing, would add something here later, something like update the online/offline status of the person who send the packet
			view.heartbeat();
		}
	}
}



