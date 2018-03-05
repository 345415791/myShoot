package shoot.day03;
import java.awt.image.BufferedImage;
import java.util.Random;
public class Hero extends FlyingObject{
	private int life;//������57,60�е�addlife������getlife��������
	//����˽�л�����Ϊ/�������������������ࣨ�������ࣩ����ã�����½�һ��public�������ظ���ֵ
	private int doubleFire;//����ֵ
	private BufferedImage[] images;//ͼƬ����
	private int index;//ͼƬ�л�
	public Hero() {//���췽��
		image=ShootGame.hero0;
		width=image.getWidth();
		height=image.getHeight();
		x=150;//�̶�ֵ
		y=400;
		//�����ĸ���ʼ��ֵ
		life=3;
		doubleFire=0;//Ĭ��0����������
		images=new BufferedImage[] {ShootGame.hero0,ShootGame.hero1};
		index=0;
	}	
	public void step() {//10msִ��һ�Σ���ͼЧ��
		image=images[index++/10%images.length];
		/*
		 index++;
		 int a=index/10;
		 int b=a%2;//����ͼ���±�bȡ0��1
		 image=images[b];//ÿ100ms������ͼƬ�л�һ��
		 */
	}
	public Bullet[] shoot() {//Ӣ�ۻ������ӵ������ӵ������й�
		int xStep=this.width/4;
		int yStep=20;
		if(doubleFire>0) {//˫������
			Bullet[] bs=new Bullet[2];
			bs[0]=new Bullet(this.x+1*xStep,this.y-yStep); 
			bs[1]=new Bullet(this.x+3*xStep,this.y-yStep); 
			doubleFire-=2;///˫����������һֱ�������û���ֵ�ݼ�
			return bs;
		}
		else {
			Bullet[] bs=new Bullet[1];
			bs[0]=new Bullet(this.x+2*xStep,this.y-yStep); 
			return bs;
		}
	}	
	public void moveTo(int x,int y) {//Ӣ�ۻ���������ƶ�
		this.x=x-this.width/2;//Ӣ�ۻ���x����
		this.y=y-this.height/2;	
	}
	public boolean outOfBounds() {
		return(false);//Ӣ�ۻ�����Խ��
	}	
	public void subtractLife() {
		 life--;
	}
	public void addLife() {//����
		life++;
	}
	public int getLife() {//��private��score�����ù�������getlife��������������
		return life;
	}
	public void clearDoubleFire() {
		doubleFire=0;
	}
	public void addDoubleLife() {//�ӻ���ֵ
		doubleFire+=40;
	}
	/*�Ƿ���ײ*/
	public boolean hit(FlyingObject obj) {//���������˵����꣬�Եл�Ϊ��׼
		int x1=obj.x-this.width/2;//����ײʱ���������Ӣ�ۻ�������λ��
		int x2=obj.x+this.width/2+obj.width;
		int y1=obj.y-this.height/2;
		int y2=obj.y+this.height/2+obj.height;
		int x=this.width/2+this.x;//Ӣ�ۻ���������
		int y=this.height/2+this.y;
		return x>x1&&x<x2
				&&
				y>y1&&y<y2;/*�Եл�����Ϊ�ο��㣬��ײ����*/
	}
}
