package shoot.day03;
import java.awt.image.BufferedImage;
public abstract class FlyingObject {
	//飞行物属性
	protected BufferedImage image;
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	
	//所有飞行物都走步，不同对象飞行方法不同，所以抽象
	public abstract void step() ;
	public abstract boolean outOfBounds();
	/**敌人被子弹射击，this：敌人   bullet：子弹**/
	public boolean shootBy(Bullet bullet) {
		int x1=this.x;
		int x2=this.x+this.width;
		int y1=this.y;
		int y2=this.y+this.height;
		int x=bullet.x;
		int y=bullet.y;
		return(x>x1&&x<x2
				&&
				y>y1&&y<y2);
	}
}
