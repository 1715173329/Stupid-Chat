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
		System.out.println("now scanning port: " + port);
		packet.setAddress(inetAddress);
		packet.setPort(port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			// do nothing
		}
		port = nextPort(port);
	}
	
	private int nextPort(int port) {
		port++;
		if(port >= 65535) {
			port = 2000;
		}
		return port;
	}
}
