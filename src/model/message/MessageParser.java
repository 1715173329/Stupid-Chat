package model.message;

import java.net.SocketAddress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/*
 * ������ˮ���룬����Ϣ��������ʱ��������ú��ѿ�
 * ��취��д�����������
 */
public class MessageParser {
	
	
	public static Message parse(String json, SocketAddress socketAddress) {
		JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
		if(jsonObject.get("type") == null) {
			//type��Ϊ�գ�˵��Ϊ��{}�����߱��ʲô����������������
			return HeartbeatMessage.getInstance(socketAddress);
		}
		String type = jsonObject.get("type").getAsString();
		if("text".equals(type)) {
			String content = jsonObject.get("content").getAsString();
			int id = jsonObject.get("id").getAsInt();
			return new TextMessage(socketAddress, content, id);
		}else if("ack".equals(type)) {
			int id = jsonObject.get("id").getAsInt();
			return new AcknowledgeMessage(socketAddress, id);
		}else if("heartbeat".equals(type)) {
//			������Ӧ��ʹ�õ���ģʽ����Ȼ��Ҫ new �Ķ���ʵ��̫����
			return HeartbeatMessage.getInstance(socketAddress);
//		}else if(type == null) {
//			return HeartbeatMessage.getInstance(socketAddress);
		}
		return null;
	}
	
	public static String toJson(Message message) {
		JsonObject jsonObject = new JsonObject();
		if(message instanceof TextMessage) {
			TextMessage textMessage = (TextMessage)message;
			jsonObject.addProperty("type", "text");
			jsonObject.addProperty("content", textMessage.getContent());
			jsonObject.addProperty("id", textMessage.getId());		
		}else if(message instanceof AcknowledgeMessage) {
			AcknowledgeMessage acknowledgeMessage = (AcknowledgeMessage)message;
			jsonObject.addProperty("type", "ack");
			jsonObject.addProperty("id", acknowledgeMessage.getId());
		}else if(message instanceof HeartbeatMessage) {
//			jsonObject.addProperty("type", "heartbeat");
			return "{}";
			
		}
		return jsonObject.toString();
	}
	
	
	
}
