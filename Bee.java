package shoot.day03;
import java.util.Random;
/*��ʹ�����Ҳ�ǽ���*/
public class Bee extends FlyingObject implements Award{
	private int xSpeed=1;//xy�ٶȲ�ͬ
	private int ySpeed=2;
	private int awardType;//�������ͣ���ͬ��ֵ����������������
	//���췽��
	public Bee(){
		image=ShootGame.bee;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width);//x�������
		y=-this.height;	
		//y=100; 
		awardType=rand.nextInt(2);//0�������ֵ��1������
	}
	//��дgetType()
	public int getType() {
		return awardType;
	}	
	public void step() {
		y+=ySpeed;
		Random rand=new Random();
		x+=xSpeed;
		//int n=rand.nextInt(2);//ÿһ�ֶ�Ҫ��������������Ժ�����ң��������������Ӿ�ֱ�µ�
				//if(n>0) {x+=xSpeed;}
				//else 	{x-=xSpeed;}
		if(x>=ShootGame.WIDTH-this.width) {//���۷��ƶ��������ұ�Ե�ٵ�����(�����ƶ�)
			xSpeed=-xSpeed;
		}
		if(x<=0) {
			xSpeed=xSpeed;
		}
	}
	public boolean outOfBounds() {
		return(this.y>=ShootGame.HEIGHT);//�۷��y���ڴ�����������Խ��
	}
	
}

