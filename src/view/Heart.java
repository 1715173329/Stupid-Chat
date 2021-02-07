package view;


import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Heart extends Circle {
	private static final Color ON = Color.LIMEGREEN;
	private static final Color OFF = Color.LIGHTGREY;
	private static final int RADIUS = 5;
	private static final int MAX_TTL = 10000;
//	private static final int MAX_TTL = 100;
	private int ttl;
	private AnimationTimer timer;
	/*
	 * ÿ�� Timer ��������TTL �ͻ���٣��� TTL ���ٵ� 0 ����ʱ���ƾͻ����
	 * ������ͨ�� beat ���������԰� TTL ����Ϊ���ֵ��Ҳ����˵��ÿ���յ��������ģ���������ͻ�������
	 * �� MAX_TTL ���õıȽ�Сʱ����������ᴦ��һ����˸��ģʽ����Ϊ�յ��������ĺ�TTL �ܿ���˥��Ϊ 0 ��
	 * �� MAX_TTL ���õñȽϴ�ʱ����������ᴦ�ڳ�����״̬����Ϊ���������û���ü�Ϩ��ʱ����һ���������İ��������״̬������
	 * 
	 */
	public Heart() {
		super(RADIUS);
		ttl = 0;
		this.setFill(OFF);
		timer = new AnimationTimer() {	
			public void handle(long now) {
				if(ttl <= 0) {
					Heart.this.setFill(OFF);
					return;
				}
				ttl -= 1000.0/60.0;
			}
		};
		timer.start();
	}
	public void beat() {
		this.setFill(ON);
		ttl = MAX_TTL;
	}
}
