package model;

public class PortScanThread extends Thread{
	private PortScanner portScanner;
	private boolean exit = false;
	private static final int SCAN_PERIOD = 50;//ÿ 250 ms ����һ���˿ڷ���������
//	private static final int TIMEOUT = 50000;//���ɨ�� 50s
	private static final int MAX_PORT_COUNT = 4000;//���ɨ�� 4000 ���˿�
	public PortScanThread(PortScanner portScanner) {
		this.portScanner = portScanner;
	}
	@Override
	public void run() {
		//����֮ǰ������ 1s �Ļ���ʱ�䣬û��ʲô�����ԭ��ֻ��Ϊ�˷��㱾�����ԣ�
		System.out.println("Port scanning thread started.");
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			//do nothing
		}
		long start = System.currentTimeMillis();
		long end;
		long cost;
		int portCount = 0;
		while(!exit) {
			portScanner.scan();
			portCount++;
			try {
				sleep(SCAN_PERIOD);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();
			cost = end - start;
			if(portCount >= MAX_PORT_COUNT) {
				//��ɨ��Ķ˿����������趨����ֵʱ��ǿ����ֹ
				this.exit();
			}
		}
		System.out.println("Port scanning thread stoped.");
	}
	
	public void exit() {
		exit = true;
	}
	
}
