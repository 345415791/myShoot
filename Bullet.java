package shoot.day03;

import java.util.Random;

//**�ӵ����Ƿ�����*//
public class Bullet extends FlyingObject{
	private int speed=3;
	//���췽����x���ӵ���x������Ӣ�ۻ�����仯
	public Bullet(int x,int y) {
		image=ShootGame.bullet;
		width=image.getWidth();
		height=image.getHeight();
		this.x=x;//����������x��ֵ���ӵ���x,ע��this.x=x��Ҫд����
		this.y=y;
		//y=400;x=200;
	}
	public void step() {
		y-=speed;
	}
	public boolean outOfBounds() {
		return(this.y<=-this.height);//�ӵ�Խ��
	}
	

}
