package shoot.day03;

import java.util.Random;

//**子弹，是飞行物*//
public class Bullet extends FlyingObject{
	private int speed=3;
	//构造方法，x：子弹的x，随着英雄机坐标变化
	public Bullet(int x,int y) {
		image=ShootGame.bullet;
		width=image.getWidth();
		height=image.getHeight();
		this.x=x;//将传过来的x赋值给子弹的x,注意this.x=x不要写反了
		this.y=y;
		//y=400;x=200;
	}
	public void step() {
		y-=speed;
	}
	public boolean outOfBounds() {
		return(this.y<=-this.height);//子弹越界
	}
	

}
