package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import de.javawi.jstun.attribute.MappedAddress;
import de.javawi.jstun.attribute.MessageAttributeInterface.MessageAttributeType;
import de.javawi.jstun.attribute.MessageAttributeParsingException;
import de.javawi.jstun.header.MessageHeader;
import de.javawi.jstun.header.MessageHeaderInterface.MessageHeaderType;
import de.javawi.jstun.header.MessageHeaderParsingException;
import de.javawi.jstun.util.UtilityException;

public class Stunner {
	private DatagramSocket socket = null;
	private static int STUN_RETRY_TIMES = 10;
	
	public Stunner(DatagramSocket socket) {
		this.socket = socket;
	}
	
	
	public InetSocketAddress stun() {
		InetSocketAddress publicSocketAddress = null;
		for (int i = 0; i < STUN_RETRY_TIMES; i++) {
			publicSocketAddress = tryStun();
			if(publicSocketAddress != null) {
				break;
			}
			System.out.println("����ֵΪ��");
		}
		return publicSocketAddress;
	}
	
	
	public InetSocketAddress tryStun() {
		//���� STUN ����
		try {
			MessageHeader mh = new MessageHeader(MessageHeaderType.BindingRequest);
			mh.generateTransactionID();
			byte[] data = mh.getBytes();	
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("stun.voip.aebc.com"), 3478);
			socket.send(packet);
		} catch (Exception e) {
			System.out.println("����STUN����ʧ��");
			return null;
		}
		
		//�ȴ� STUN ��Ӧ
		try {
			byte[] buf = new byte[200];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
			byte[] data = packet.getData();
			MessageHeader mh = new MessageHeader();
			mh = MessageHeader.parseHeader(data);
			mh.parseAttributes(data);
			MappedAddress ma = (MappedAddress) mh.getMessageAttribute(MessageAttributeType.MappedAddress);
			String ip = ma.getAddress().getInetAddress().toString().split("/")[1];
			int port = ma.getPort();
			System.out.println("������ַ/�˿ڣ�" + ip + ":" + port);
			return new InetSocketAddress(ip, port);
		} catch (SocketTimeoutException e) {
			System.out.println("�ȴ� STUN �ظ���ʱ");
			return null;
		} catch (IOException e) {
			System.out.println("���� STUN ���������쳣");
			return null;
		} catch (MessageHeaderParsingException e) {
			System.out.println("�޷����� STUN ����");
			return null;
		} catch (MessageAttributeParsingException e) {
			System.out.println("�޷����� STUN ����");
			return null;
		} catch (UtilityException e) {
			System.out.println("���� STUN ���������쳣");
			return null;
		}		
	}
	
//	public boolean tryStun(){
//		try {
//			sendStunRequest();
//			recieveStunResponse();
//		} catch (SocketTimeoutException e) {
//			System.out.println("STUN ����ʱ");
//			return false;
//			
//		} catch (UtilityException e) {
//			System.out.println("STUN ��������");
//		} catch (IOException e) {
//			System.out.println("STUN ��������");
//		} catch (MessageHeaderParsingException e) {
//			System.out.println("STUN ��������");
//		} catch (MessageAttributeParsingException e) {
//			System.out.println("STUN ��������");
//		}
//		System.out.println("������ַ/�˿ڣ�" + publicSocketAddress.getAddress() + ":" + publicSocketAddress.getPort());
//		return true;
//	}
	
	
	
	
//	private void sendStunRequest() throws UtilityException, IOException {
//		MessageHeader mh = new MessageHeader(MessageHeaderType.BindingRequest);
//		mh.generateTransactionID();
//		byte[] data = mh.getBytes();	
//		DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("stun.voip.aebc.com"), 3478);
//		socket.send(packet);
//	}
//	
//	private void recieveStunResponse() throws IOException, MessageHeaderParsingException, MessageAttributeParsingException, UtilityException {
//		byte[] buf = new byte[200];
//		DatagramPacket packet = new DatagramPacket(buf, buf.length);
//		socket.setSoTimeout(3000);
//		try {
//			socket.receive(packet);
//		} catch (SocketTimeoutException e) {
//			System.out.println("�ȴ� STUN �ظ���ʱ");
//			return;
//		}
//		
//		byte[] data = packet.getData();
//		MessageHeader mh = new MessageHeader();
//		mh = MessageHeader.parseHeader(data);
//		mh.parseAttributes(data);
//		MappedAddress ma = (MappedAddress) mh.getMessageAttribute(MessageAttributeType.MappedAddress);
//		String ip = ma.getAddress().getInetAddress().toString().split("/")[1];
//		int port = ma.getPort();
//		this.publicSocketAddress = new InetSocketAddress(ip, port);
//		
//	}	
	
}
