package shoot.day03;
import java.util.Random;
/**敌机即使飞行物，也是敌人**/
public class AirPlane extends FlyingObject implements Enemy{
	private int speed=2;//敌机走步的步数
	//*构造方法 *//
	public AirPlane() {
		image=ShootGame.airplane;//导入图片
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width);//x随机产生
		y=-this.height;//this指代当前对象，即AIrplane的
		//y=200;
	}
	
	//**重写getScore()*//
	public int getScore() {
		return 5;
	}	
	public void step() {//重写父类step
		y+=speed;
	}
	public boolean outOfBounds() {
		return(this.y>=ShootGame.HEIGHT);//敌机的y大于窗口竖长，则越界
	}
	
}
