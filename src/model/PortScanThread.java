package model;

public class PortScanThread extends Thread{
	private PortScanner portScanner;
	private static final int SCAN_PERIOD = 50;//ÿ 250 ms ����һ���˿ڷ���������
	private static final int MAX_PORT_COUNT = 4000;//���ɨ�� 4000 ���˿�
	public PortScanThread(PortScanner portScanner) {
		this.portScanner = portScanner;
	}
	@Override
	public void run() {
		//����֮ǰ������ 3s �Ļ���ʱ�䣬û��ʲô�����ԭ��ֻ��Ϊ�˷��㱾�����ԣ�
		System.out.println("Port scanning thread started.");
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Port scanning thread stoped.");
			return;
		}
		long start = System.currentTimeMillis();
		long end;
		long cost;
		int portCount = 0;
		//TODO ��ôƵ���� sleep ����ʲô����ƣ�Ӧ��һ����ɨ�輸ʮ���˿ڣ�Ȼ����˯һ�£�������ɨһ��˯һ��
		while(true) {
			portScanner.scan();
			portCount++;
			try {
				sleep(SCAN_PERIOD);
			} catch (InterruptedException e) {
				System.out.println("Port scanning thread stoped.");
				return;
			}
			end = System.currentTimeMillis();
			cost = end - start;
			if(portCount >= MAX_PORT_COUNT) {
				//��ɨ��Ķ˿����������趨����ֵʱ��ǿ����ֹ
				break;
			}
		}
	}
	

	
}
