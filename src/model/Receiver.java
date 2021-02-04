package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class Receiver{
	private DatagramSocket socket;

	public Receiver(DatagramSocket socket) {
		this.socket = socket;
	}
	
	/*
	 * ֱ���յ���Ч�İ�֮ǰ��һֱѭ��
	 * ����յ�����Ч�İ���������ѭ���������ı�����
	 */
	public String receive() {
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
		//TODO����ʱ��ʼ������� message ����
		String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);		
		System.out.println("�Է�: " + msg);
		return msg;
	}
	
	
}

