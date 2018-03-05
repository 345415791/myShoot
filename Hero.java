package shoot.day03;
import java.awt.image.BufferedImage;
import java.util.Random;
public class Hero extends FlyingObject{
	private int life;//命、、57,60行的addlife（），getlife（）函数
	//数据私有化，行为/方法公开化，若其他类（除了子类）需调用，则可新建一个public方法返回该数值
	private int doubleFire;//火力值
	private BufferedImage[] images;//图片数组
	private int index;//图片切换
	public Hero() {//构造方法
		image=ShootGame.hero0;
		width=image.getWidth();
		height=image.getHeight();
		x=150;//固定值
		y=400;
		//本类四个初始赋值
		life=3;
		doubleFire=0;//默认0代表单倍火力
		images=new BufferedImage[] {ShootGame.hero0,ShootGame.hero1};
		index=0;
	}	
	public void step() {//10ms执行一次，切图效果
		image=images[index++/10%images.length];
		/*
		 index++;
		 int a=index/10;
		 int b=a%2;//两张图，下标b取0或1
		 image=images[b];//每100ms在两张图片切换一次
		 */
	}
	public Bullet[] shoot() {//英雄机发射子弹，和子弹产生有关
		int xStep=this.width/4;
		int yStep=20;
		if(doubleFire>0) {//双倍火力
			Bullet[] bs=new Bullet[2];
			bs[0]=new Bullet(this.x+1*xStep,this.y-yStep); 
			bs[1]=new Bullet(this.x+3*xStep,this.y-yStep); 
			doubleFire-=2;///双倍火力不能一直持续，让火力值递减
			return bs;
		}
		else {
			Bullet[] bs=new Bullet[1];
			bs[0]=new Bullet(this.x+2*xStep,this.y-yStep); 
			return bs;
		}
	}	
	public void moveTo(int x,int y) {//英雄机随着鼠标移动
		this.x=x-this.width/2;//英雄机的x坐标
		this.y=y-this.height/2;	
	}
	public boolean outOfBounds() {
		return(false);//英雄机永不越界
	}	
	public void subtractLife() {
		 life--;
	}
	public void addLife() {//加命
		life++;
	}
	public int getLife() {//把private的score，利用公开方法getlife（）传给其他类
		return life;
	}
	public void clearDoubleFire() {
		doubleFire=0;
	}
	public void addDoubleLife() {//加火力值
		doubleFire+=40;
	}
	/*是否相撞*/
	public boolean hit(FlyingObject obj) {//传过来敌人的坐标，以敌机为标准
		int x1=obj.x-this.width/2;//刚碰撞时，左边区域英雄机的中心位置
		int x2=obj.x+this.width/2+obj.width;
		int y1=obj.y-this.height/2;
		int y2=obj.y+this.height/2+obj.height;
		int x=this.width/2+this.x;//英雄机中心区域
		int y=this.height/2+this.y;
		return x>x1&&x<x2
				&&
				y>y1&&y<y2;/*以敌机顶点为参考点，相撞区域*/
	}
}
