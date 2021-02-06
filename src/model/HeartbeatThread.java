package model;

public class HeartbeatThread extends Thread{
	private Heartbeater heartbeater;
	private boolean exit = false;
	private static final int BEAT_PERIOD = 250;//ÿ1�����һ�� punch
	public HeartbeatThread(Heartbeater heartbeater) {
		this.heartbeater = heartbeater;
	}
	
	@Override
	public void run() {
		while(!exit) {
			heartbeater.heartbeat();
			try {
				sleep(BEAT_PERIOD);
			} catch (InterruptedException e) {
				continue;
			}
		}
		
	}
	
	public void exit() {
		exit = true;
	}
	
}
