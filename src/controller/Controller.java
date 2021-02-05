package controller;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import model.Model;
import view.View;

public class Controller {
	private Model model;
	private View view;
	
	
	// to initialize
	public void setModel(Model model) {
		this.model = model;
	}

	public void setView(View view) {
		this.view = view;
	}
	
	//method to call
	public void send(String msg, SocketAddress socketAddress) {
		model.send(msg, socketAddress);
	}
	
	public void startChat(InetSocketAddress inetSocketAddress) {
		model.startChat(inetSocketAddress);
	}
	
	public void exit() {
		model.exit();
	}
	
	
}
