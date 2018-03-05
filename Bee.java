package shoot.day03;
import java.util.Random;
/*即使飞行物，也是奖励*/
public class Bee extends FlyingObject implements Award{
	private int xSpeed=1;//xy速度不同
	private int ySpeed=2;
	private int awardType;//奖励类型，不同数值代表，命，火力奖励
	//构造方法
	public Bee(){
		image=ShootGame.bee;
		width=image.getWidth();
		height=image.getHeight();
		Random rand=new Random();
		x=rand.nextInt(ShootGame.WIDTH-this.width);//x随机产生
		y=-this.height;	
		//y=100; 
		awardType=rand.nextInt(2);//0代表火力值，1代表命
	}
	//重写getType()
	public int getType() {
		return awardType;
	}	
	public void step() {
		y+=ySpeed;
		Random rand=new Random();
		x+=xSpeed;
		//int n=rand.nextInt(2);//每一轮都要调用随机数，所以忽左忽右，当看不出来，视觉直下的
				//if(n>0) {x+=xSpeed;}
				//else 	{x-=xSpeed;}
		if(x>=ShootGame.WIDTH-this.width) {//当蜜蜂移动到窗口右边缘再弹回来(向左移动)
			xSpeed=-xSpeed;
		}
		if(x<=0) {
			xSpeed=xSpeed;
		}
	}
	public boolean outOfBounds() {
		return(this.y>=ShootGame.HEIGHT);//蜜蜂的y大于窗口竖长，则越界
	}
	
}

