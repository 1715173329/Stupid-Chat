package model;

public class HeartbeatThread extends Thread{
	private Heartbeater heartbeater;
	private static final int BEAT_PERIOD = 5000;//ÿ5�뷢��һ��������
	public HeartbeatThread(Heartbeater heartbeater) {
		this.heartbeater = heartbeater;
	}
	
	@Override
	public void run() {	
		System.out.println("Heartbeat thread started.");
		while(true) {			
			if(this.isInterrupted()) {
				System.out.println("Heartbeat thread stoped.");
				return;
			}
			heartbeater.heartbeat();
			try {
				sleep(BEAT_PERIOD);
			} catch (InterruptedException e) {
				System.out.println("Heartbeat thread stoped.");
				return;
			}			
		}		
	}
	
}
