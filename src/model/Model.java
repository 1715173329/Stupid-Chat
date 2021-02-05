package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
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
//		receiveThread = new ReceiveThread(view, receiver);
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
		punchThread = new PunchThread(holePuncher);
		punchThread.start();
		view.init(publicSocketAddress);
	}
	
	public void exit() {
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
		sender.sendMessage(msg, socketAddress);
	}
	
	//����receiver��ȡһ����Ϣ�����ҽ���
//	public void receive() {
//		String jsonString = receiver.receive();
//		JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
//		String type = jsonObject.get("type").getAsString();
//		if("message".equals(type)) {
//			//TODO �ظ�ȷ�ϱ���
//			String msg = jsonObject.get("content").getAsString();
//			//ί��ͼ���̸߳������
//			Platform.runLater(()->{			
//				view.displayMessage(msg);
//			});	
//			int id = jsonObject.get("id").getAsInt();
//			jsonObject = new JsonObject();
//			jsonObject.addProperty("type", "ack");
//			jsonObject.addProperty("id", id);
//			//��ȷ�ϱ��ķ���ȥ
//		}else if("heartbeat".equals(type)) {
//			//kinda do nothing, would add something here later, something like update the online/offline status of the person who send the packet
//		}else if("ack".equals(type)) {
//			//TODO �յ�ȷ�ϱ��ĸ���ô�죿��Viewȥ����
//		}
//	}		
	
	
	
	public void receive() {
		Pack pack = receiver.receive();
		String type = pack.getType();
		if("text".equals(type)) {
			//TODO �ظ�ȷ�ϱ���
			String msg = pack.getString("content");
			//ί��ͼ���̸߳������
			Platform.runLater(()->{			
				view.displayMessage(msg);
			});	
			//��ȷ�ϱ��ķ���ȥ
			sender.acknowledge(pack.getInt("id"), pack.getSourceSocketAddress());		
		}else if("heartbeat".equals(type)) {
			//kinda do nothing, would add something here later, something like update the online/offline status of the person who send the packet
		}else if("ack".equals(type)) {
			//TODO �յ�ȷ�ϱ��ĸ���ô�죿��Viewȥ����
			view.confirmMessage(pack.getInt("id"));
			
		}
	}
}



