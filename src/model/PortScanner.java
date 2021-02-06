package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

import model.message.HeartbeatMessage;
import model.message.MessageParser;

public class PortScanner {
	
	private DatagramSocket socket;
	private DatagramPacket packet;
	private InetAddress inetAddress;
	private int port;
	private int count = 0;//���ڼ�¼��ǰ�Ѿ�ɨ���˶��ٸ��˿�
	
	//��ÿ���˿��ظ����Ͷ��ٴ������������� UDP �������еĿɿ�����������Ϊ 1
	private static final int SEND_TIME = 1;
	
	
	public PortScanner(DatagramSocket socket, SocketAddress dest) {
		this.socket = socket;
		this.inetAddress = ((InetSocketAddress)dest).getAddress();
		this.port = ((InetSocketAddress)dest).getPort();
		//��ʼ��������
		String json = MessageParser.toJson(HeartbeatMessage.getInstance(dest));	
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		packet = new DatagramPacket(bytes, bytes.length);
		
	}
	
	//��Ŀ�� IP �ĵ�ǰ�˿ڷ���һ����������Ȼ����µ�ǰ�˿�
	public void scan() {	
		packet.setAddress(inetAddress);
		packet.setPort(port);
		for (int i = 0; i < SEND_TIME; i++) {
			try {
				socket.send(packet);
			} catch (IOException e) {
				// do nothing
			}
			System.out.println("now scanning port: " + port);
		}
		count++;
		port = nextPort(port);
	}
	
	/*
	 * һ���������һ�ζ˿� = ���ζ˿� + 1
	 * ����һЩ���⣬����˵���������˳��� 250 ���˿�ʱ����ֱ�ӻص� 1024 (�������¼���)
	 * ���� 65535 ʱҲ�ص� 1024
	 */
	
	/*
	 * �ڸ����˿ڵ����ҷ�����̽
	 * ���ԣ���һ���� +1 �ڶ����� -2 �������� +3 ��4���� -4
	 */
	private int nextPort(int port) {
//		port++;
//		if(count >= 250 ||port >= 65535) {
//			port = 1024;
//			count = 0;
//		}
//		return port;
		if(count%2 == 1) {
			port+=count;
		}else {
			port-=count;
		}
		if(port > 65535 || port <= 0) {
			port = 1024;
		}
		return port;
	}
}
