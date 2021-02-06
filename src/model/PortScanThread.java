package model;

public class PortScanThread extends Thread{
	private PortScanner portScanner;
	private boolean exit = false;
	private static final int SCAN_PERIOD = 100;//ÿ 100 ms ����һ���˿ڷ���������
	private static final int TIMEOUT = 50000;//���ɨ�� 50s
	public PortScanThread(PortScanner portScanner) {
		this.portScanner = portScanner;
	}
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		long end;
		long cost;
		while(!exit) {
			portScanner.scan();
			try {
				sleep(SCAN_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();
			cost = end - start;
			if(cost >= TIMEOUT) {
				this.exit();
			}
		}
	}
	
	public void exit() {
		exit = true;
	}
	
}
