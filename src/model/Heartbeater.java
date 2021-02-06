package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;


import model.message.HeartbeatMessage;
import model.message.MessageParser;

/*
 * ��Ȼ�ⶫ������ puncher �����Ĺ�����Ҫ�����������ϵ������ͷ�ֹ�˿��ϻ�
 * ֮��������ĳ�ʲô���ְɡ���
 */
public class Heartbeater{
	private DatagramSocket socket;
	private DatagramPacket packet;
	private SocketAddress dest = null;
	
	
	
	public Heartbeater(DatagramSocket socket) {
		this.socket = socket;
		//��ʼ��������
		String json = MessageParser.toJson(HeartbeatMessage.getInstance(dest));	
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		packet = new DatagramPacket(bytes, bytes.length);
	}


	//��ָ��Ŀ��˿ڷ���������
	public void heartbeat() {
		if(dest == null) {
			return;
		}
		packet.setSocketAddress(dest);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("Failed to send hole-punching packet.");
		}
	}
	
	//����Ŀ��
	public void setDestination(SocketAddress dest) {
		this.dest = dest;
	}
}
