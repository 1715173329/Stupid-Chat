package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

public class HolePuncher{
	private DatagramSocket socket;
	private DatagramPacket packet;
	private List<InetSocketAddress> destList = new ArrayList<InetSocketAddress>();
	private static final int PUNCH_PERIOD = 250;//ÿ5�����һ�� punch
	
	public HolePuncher(DatagramSocket socket) {
		this.socket = socket;
		JsonObject json = new JsonObject();
		json.addProperty("type", "heartbeat");
		byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
		packet = new DatagramPacket(bytes, bytes.length);
	}


	//��ָ���˿ڷ��ʹ򶴱���
	public void punch(InetSocketAddress dest) {
		packet.setSocketAddress(dest);
		try {
			socket.send(packet);
		} catch (IOException e) {
			System.out.println("�򶴱��ķ���ʧ�ܡ�");
		}
		
	}
	
	//�����Ե���Ŀ���б������еĶ˿ڷ��ʹ򶴱���
	public void punch() {
		for (InetSocketAddress dest : destList) {
			punch(dest); 
		}
		try {
			Thread.sleep(PUNCH_PERIOD);
		} catch (InterruptedException e) {
			//do nothing
		}
	}

	
	public void addDesitination(InetSocketAddress dest) {
		destList.add(dest);
	}
}
