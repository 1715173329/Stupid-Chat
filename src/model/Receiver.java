package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import model.message.Message;
import model.message.MessageParser;

public class Receiver{
	private DatagramSocket socket;

	public Receiver(DatagramSocket socket) {
		this.socket = socket;
	}
	
	/*
	 * ֱ���յ���Ч�İ�֮ǰ��һֱѭ��
	 * ����յ�����Ч�İ���������ѭ���������ı�����
	 * TODO: û���յ���Ϣ��һֱ�������ⲻ��һ���õ����
	 * Ӧ����ÿ�γ�ʱ��ʱ����������׳���ʱ�쳣��Ȼ���ɵ����߾���Ҫ��Ҫ����receive
	 */
	
	public Message receive() throws SocketTimeoutException {
		byte[] buf = new byte[200];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try {
			socket.receive(packet);
		} catch(SocketTimeoutException e) {
			throw e;
		} catch (IOException e) {
			//TODO ֮������������ø���
			e.printStackTrace();
		}
		SocketAddress socketAddress = packet.getSocketAddress();
		String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
		if(!"{}".equals(json)) {
			System.out.println("Message recieved: " + json);
		}	
		return MessageParser.parse(json, socketAddress);
	}
	
}

