package shoot.day03;
import java.util.Random;
/**�л���ʹ�����Ҳ�ǵ���**/
public class AirPlane extends FlyingObject implements Enemy{
	private int speed=2;//�л��߲��Ĳ���
	//*���췽�� *//
	public AirPlane() {
		image=ShootGame.airplane;//����ͼƬ
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width);//x�������
		y=-this.height;//thisָ����ǰ���󣬼�AIrplane��
		//y=200;
	}
	
	//**��дgetScore()*//
	public int getScore() {
		return 5;
	}	
	public void step() {//��д����step
		y+=speed;
	}
	public boolean outOfBounds() {
		return(this.y>=ShootGame.HEIGHT);//�л���y���ڴ�����������Խ��
	}
	
}
