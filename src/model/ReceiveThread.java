package model;

public class ReceiveThread extends Thread{
	private Model model;
	private boolean exit = false;
	public ReceiveThread(Model model) {
		this.model = model;
	}
	public void run() {
		System.out.println("Message receiving thread started.");
		while(!exit) {
			//这里会阻塞 所以导致线程无法停止
			model.receive();
		}
		System.out.println("Message receiving thread stoped.");
	}

	public void exit() {
		exit = true;
		model.stopReceiving();
	}
}
