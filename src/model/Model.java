package model;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

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
	private InetSocketAddress publicSocketAddress;
	
	private Stunner stunner;
	private Sender sender;
	private Receiver receiver;	
	private PortScanner portScanner;
	private Heartbeater heartbeater;
		
	private ReceiveThread receiveThread;
	private PortScanThread portScanThread;
	private HeartbeatThread heartbeatThread;
	// to initialize
	public void setView(View view) {
		this.view = view;
	}

	public Model() {
		try {
			//TODO: ���������й��ܶ���Ҫ socket �ṩ��ʱ����
			socket = new DatagramSocket(0);
			socket.setSoTimeout(1000);
		} catch (SocketException e) {
			socket = null;
		}
		stunner = new Stunner(socket);
		sender = new Sender(socket);	
		receiver = new Receiver(socket);	
		heartbeater = new Heartbeater(socket);
	}
	
	
	public void launch() {
		/*
		 * ���� STUN ��ȡ�Լ��Ĺ��� ip �Ͷ˿��Ժ�������Ϣ�����̺߳������߳�
		 * ��һ��ʼ�������̲߳�û���趨Ŀ�ĵ�
		 * ���û������˶Է��� ip ��ַ�Ͷ˿ں��Ժ󣬾ͻ��ӵ��� startChat ����
		 */
		publicSocketAddress = stunner.stun();
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
		
		view.init(publicSocketAddress);
	}
	
	public void exit() {
		if(receiveThread != null) {
			receiveThread.interrupt();
		}
		if(heartbeatThread != null) {
			heartbeatThread.interrupt();
		}
		
		
	}
		
	// ��������һ�����촰��֮�⣬����Ҫ������Ŀ�귢��һ���򶴱��ģ�������  hole puncher ������
	public void startChat(InetSocketAddress inetSocketAddress) {
		/*
		 * �������죬��һ������������������̣߳����ϵ���Ŀ�ĵط���������
		 * ��Ȼ�����ڶԳ��� NAT �Ĵ��ڣ���һĿ�ĵؿ��ܸ���������Ч��
		 * ��ʵ��Ŀ�ĵ���Ҫ�ȶԷ���֪���ǣ����ǣ��ڴ�֮ǰ��������Ҫ�Ȳ��жԷ��Ķ˿ڣ����ҷ���һ����������ȥ
		 * ֻ�з������������Ժ����ԶԷ���ʵ�˿ڵı��Ĳ��а취�������ǵĶ˿�
		 * ������Ҫ����һ��ʮ�ַ��Ķ˿�ɨ���߳�
		 */
		
		heartbeatThread = new HeartbeatThread(heartbeater);
		heartbeater.setDestination(inetSocketAddress);
		heartbeatThread.start();
		view.startChat(inetSocketAddress);
		
		//�����˿�ɨ���߳�
		portScanner = new PortScanner(socket, inetSocketAddress);
		portScanThread = new PortScanThread(portScanner);
		portScanThread.start();
	}
	
	public void send(Message message) {
		sender.send(message);
	}
	
	/*
	 * ����receiver��ȡһ����Ϣ�����ҽ���
	 * TODO: ��һ���ֵĴ���Ӧ��Ų�����ʲô�ط�ȥ��Model �౾���ô�����ô���ӵ��߼�
	 */
	public void receive() {
		Message message;
		try {
			message = receiver.receive();
		} catch (SocketTimeoutException e) {
			return;
		}
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
			//�յ�ȷ�ϱ��ĸ���ô�죿��Viewȥ����
			AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage)message;
			view.confirmMessage(acknowledgeMessage.getId());
		}else if(message instanceof HeartbeatMessage) {
			//�յ�������֮�󣬾��� view ����������һ��
			view.heartbeat();
			/*
			 * ��Ҫ������дһЩʮ�ֿ��µĶ�����
			 * ֻҪ��������ط��յ������������Ҿ͸���һ��socketAddress
			 * ������Ȼ�ǲ�����ģ���һ������һ�����Ҿ��޷�ͬʱ�����û�������
			 * �ڶ������˿��Ժ����׵�α�����
			 * ��������Σ�����ô���ɣ��������������˵
			 */
			view.updateSocketAddress(message.getSocketAddress());		
			heartbeater.setDestination(message.getSocketAddress());
			//�رն˿�ɨ���߳�
			if(portScanThread.isAlive()) {
				portScanThread.interrupt();
			}
		}
	}
	
}



