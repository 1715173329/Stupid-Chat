package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
	 */
//	public Pack receive() {
//		byte[] buf = new byte[200];
//		DatagramPacket packet = new DatagramPacket(buf, buf.length);
//		while(true) {
//			try {
//				socket.receive(packet);
//			} catch (SocketTimeoutException e) {
//				continue;
//			} catch (IOException e) {
//				continue;
//			}
//			break;
//		}
//		//TODO����ʱ��ʼ������� message ����
//		InetSocketAddress sourceSocketAddress = (InetSocketAddress) packet.getSocketAddress();
//		String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
//		Pack pack = new Pack(sourceSocketAddress, json);
//		System.out.println("Message recieved: " + json);
//		return pack;
//	}
	
	
	public Message receive() {
		byte[] buf = new byte[200];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		while(true) {
			try {
				socket.receive(packet);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				continue;
			}
			break;
		}
		SocketAddress socketAddress = packet.getSocketAddress();
		String json = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
		if(!"{}".equals(json)) {
			System.out.println("Message recieved: " + json);
		}	
		return MessageParser.parse(json, socketAddress);
	}
	
	
	
}

