package shoot.day03;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;//���
import javax.swing.JPanel;//��Ƕ��
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;//����������
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
////������
	public class ShootGame extends JPanel {//������ͼ���
	public static final  int WIDTH=400;
	public static final int HEIGHT=654;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage airplane;
	public static final int START=0;
	public static final int RUNNING=1;
	public static final int PAUSE=2;
	public static final int GAME_OVER=3;
	private int state=START;//��ǰ״̬��Ĭ������״̬
	private boolean isHitting;
	
	private Hero hero=new Hero();//Ӣ�ۻ�����
	private FlyingObject[] flyings= {};
	//����(�л����۷�)����,û���ٽ�һ�����࣬����ֱ����FlyingObject���棬����Ĭ��ֵΪ��
	private Bullet[] bullets= {};//�ӵ��������
	
	ShootGame(){//���췽����ʼ����������	 	
		}
		
	static {//��̬�飬�����۵�ͬʱ����ʼ����̬��Դ��ͼƬ��
		try {//��鱨��
			background=ImageIO.read(ShootGame.class.getResource("background.png"));
			start=ImageIO.read(ShootGame.class.getResource("start.png"));
			pause=ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover=ImageIO.read(ShootGame.class.getResource("gameover.png"));
			bee=ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet=ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0=ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1=ImageIO.read(ShootGame.class.getResource("hero1.png"));
			airplane=ImageIO.read(ShootGame.class.getResource("airplane.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public FlyingObject nextOne() {//������ɵ���
		Random rand=new Random();
		int type=rand.nextInt(20);
		if(type<2) {//�����С��4�������۷����
			return new Bee();
		}
		else return new AirPlane();
	}
	//���������ִ��
	int flyEnteredIndex=0;
	public void enterAction() {///�����볡���л����۷䣩��ֻҪװ��flyings�����У�����ͨ�����������
		flyEnteredIndex++;
		if(flyEnteredIndex%40==0) {
			FlyingObject one =nextOne();
			flyings=Arrays.copyOf(flyings, flyings.length+1);//��������
			flyings[flyings.length-1]=one;//�����ɵĵ��˶���ӵ��������һ��Ԫ��
		}		
	}
	
	/*������л����۷䣬�ӵ���Ӣ�ۻ����߲�*/
	public void stepAction() {//���������߲�
		hero.step();//Ӣ�ۻ�
		
		for(int i=0;i<flyings.length;i++) {
			flyings[i].step();//����������߲�
		}
		for(int i=0;i<bullets.length;i++) {
			bullets[i].step();//�ӵ��߲�
		}
	}
	int shootIndex=0;
	public void shootAction() {//�����ӵ��볡����Ӣ�ۻ������ӵ�,10ms��һ��
		shootIndex++;
		if(shootIndex%20==0) {//����20�Σ�ִ��һ�Σ���200ms����һ���ӵ�
			Bullet[] bs= hero.shoot();//��ȡ�ӵ����󣬲�Ҫ�� new���Ѿ�ʵ�����ˣ���������������������������������
		//bs�����д�����ӵ�����
			bullets=Arrays.copyOf(bullets, bullets.length+bs.length);
		//�ӵ��������ݣ����ڵ�˫������bs����������bs.length
		System.arraycopy(bs,0,bullets,(bullets.length-bs.length),bs.length);//��bs���ݵ�bullets
		//�����׷�ӣ�������������������������������
		}
	}
	
	public void outOfBoundsAction() {//ɾ��Խ��Ķ���(�л����۷䣬�ӵ�)
		int index=0;//��������1.��Խ����������±�2.��Խ����˸���
		FlyingObject[] flyingLives=new FlyingObject[flyings.length];//����һ��flyingLives����������
		for(int i=0;i<flyings.length;i++) {
			if(!flyings[i].outOfBounds()) {//���ԭflyings����Ԫ��(�л����۷�)ûԽ�磬����
				flyingLives[index]=flyings[i];//��ԭflyings�����У�ûԽ���Ԫ�أ���ֵ���µ�flyingLives��
				index++;//��������1.��Խ����������±�+1,,,2.��Խ����˸���+1
			}
		}
		//ɾ��Խ��Ԫ�أ�����
		flyings=Arrays.copyOf(flyingLives, index);//��flyingLives�в�Խ���Ԫ�أ����¸�ֵ��flyings�����У�ʵ��ɾ��Խ��Ԫ��
	///*�ӵ�Խ��ɾ��*/������������������������������������������
		int indexBullets=0;//��������1.��Խ����������±�2.��Խ����˸���
		Bullet[] bulletsLives=new Bullet[bullets.length];//����һ��flyingLives����������
		for(int i=0;i<bullets.length;i++) {
			if(!bullets[i].outOfBounds()) {//���ԭflyings����Ԫ��(�л����۷�)ûԽ�磬����
				bulletsLives[indexBullets]=bullets[i];//��ԭflyings�����У�ûԽ���Ԫ�أ���ֵ���µ�flyingLives��
				indexBullets++;//��������1.��Խ����������±�+1,,,2.��Խ����˸���+1
			}
		}
		//������Խ��Ԫ��
		bullets=Arrays.copyOf(bulletsLives, indexBullets);//��flyingLives�в�Խ���Ԫ�أ����¸�ֵ��flyings�����У�ʵ��ɾ��Խ��Ԫ��
	}
	
	
	public void bangAction() {//�ӵ������жϣ�������ɾ����Ԫ�أ�������Ϸ�г���ɾ���Ķ�����
		for(int i=0;i<bullets.length;i++) {
			Bullet b=bullets[i];//����ÿһ���ӵ�
			if(bang(b)) {//�ж�һ���ӵ�ײ�����е�����ײ//��ײ�ˣ�����������Ԫ�ؽ����������ݣ��Ӷ�ʵ������ɾ�����ӵ�
				bullets[bullets.length-1]=b;
				bullets=Arrays.copyOf(bullets, bullets.length-1);
			};//�ж�һ���ӵ�ײ�����е�����ײ
		}
	}
	int score=0;
	public boolean bang (Bullet b) {
		 int index=-1;//��ײ�����±�
		 for(int i=0;i<flyings.length;i++) {
			 FlyingObject f=flyings[i];
			 if(f.shootBy(b)) {//ײ����
				 index=i;//��¼��ײ�����±�
				 break;//һ���ӵ����ײһ�����ˣ�ײ�ϾͲ��ٱȽ�
			 }
		 }
		 if(index!=-1) {//index���ǳ�ֵ�ˣ���ʾ��ײ�ϣ���ʱindex��ʾ��ײ�����±�
			 
			 FlyingObject one=flyings[index];//��ײ��������
			 if(one instanceof Enemy) {//one����ĵл����۷�ֱ��ǽӿ�Enemy��Award��ʵ��
				 Enemy e=(Enemy)one;//ǿת����
				 score+=e.getScore();
			 }
			 if(one instanceof Award) {//���ǽ���
				 Award a=(Award)one;///ǿת��������
				  int type=a.getType();//��ȡ��������
				  switch(type) {
				  case Award.DOUBLE_FIRE:{//////ע�⾲̬���������÷�ʽ��������������������������������������
					  hero.addDoubleLife();
					  break;
				  }
				  case Award.LIFE:{
					  hero.addLife();
					  break;
				  }
				  }
			 }
			 //������ײ���ֻ�轫��ײ���˴�flings�����Ƴ�����
			 FlyingObject t=flyings[index];
			//*�������б�ײ��Ԫ�����������һ��Ԫ�ضԵ�*//
			 flyings[index]=flyings[flyings.length-1];
			 flyings[flyings.length-1]=t;//��ʵ�����Բ�Ҫ
			 flyings=Arrays.copyOf(flyings, flyings.length-1);//���ƣ�����:ȥ�����һ��Ԫ�أ�����ײԪ��
			 return true;
		 }
		 else return false;
	}
	
	public void checkGameoverAction() {//�����Ϸ�Ƿ����
		if(isGameOver()) {
			state=GAME_OVER;
		}
	}
	public boolean isGameOver() {//����Ӣ�ۻ���ײ
		//isHitting=false;
		for(int i=0;i<flyings.length;i++) {
			FlyingObject f=flyings[i];
			if(hero.hit(f)) {//��ײ��
				isHitting=true;
				hero.clearDoubleFire();//����ֵ����
				hero.subtractLife();//life-1
				flyings[i]=flyings[flyings.length-1];//ײ���ĵ�����ʧ
				flyings[flyings.length-1]=f;
				flyings=Arrays.copyOf(flyings, flyings.length-1);
				
			}		
		}
		return (hero.getLife()<=0);//life<=0����Ϸ����
	}
	
	
	public void action() {//���������ִ��
		//�������������������ڲ���
		MouseAdapter l=new MouseAdapter() {
			/*��д����ƶ��¼�*/
			public void mouseMoved(MouseEvent e) {
				//System.out.println(111);
				if(state==RUNNING) {
					int x=e.getX();//��ȡ�������x,y
					int y=e.getY();
					hero.moveTo(x, y);//����Ӣ�ۻ������������
				}
			}
			public void mouseClicked(MouseEvent e) {//�����괥��״̬
				//System.out.println(111);
				switch(state) {//������Ϸ״̬������ͬ��ʾ
				case START:
					state=RUNNING;
					break;
				case GAME_OVER:
					score=0;
					hero=new Hero();//����Hero�޲ι��죬����ֵ������������������������������������������������������������������������
					flyings=new FlyingObject[0];
					bullets=new Bullet[0];
					state=START;//��Ϊ����״̬
					break;
				}
			}
			/*��д����Ƴ��¼�*/
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING) {//�����Ϸ��������
					state=PAUSE;	//�Ƴ����ʱ����Ϸ��Ϊ��ͣ
				}
			}
			public void mouseEntered(MouseEvent e) {/*��д����Ƴ��¼�*/
				if(state==PAUSE) {
					state=RUNNING;
					//System.out.println(111);
				}
			}
		};
		this.addMouseListener(l);//�����������¼��������
		this.addMouseMotionListener(l);//������껬���¼�����ק
		Timer timer=new Timer();//������ʱ������
		int intervel=15;//ʱ������ms�����������е��÷������
		
		timer.schedule(new TimerTask() {//�����ڲ��࣡��������������������
	public void run() {//��ʱ�ɵ��£�ÿ10msһ��/////state��ʼ�����������///////////////////////////////////
		if(state==RUNNING) {
				enterAction();//�������볡
				shootAction();//�ӵ��볡����Ӣ�ۻ����
				stepAction();//�������߲�
				outOfBoundsAction();//ɾ��Խ�����
				bangAction();//�ӵ��������ײ
				checkGameoverAction();
		}
				repaint();//ϵͳ���µ���paint()��������ͼ
			}
		},intervel,intervel);	//intervel����̫�󣬲�Ȼ��������ʾ���ˣ�������̫��
	}
	
	//��дϵͳ�Դ�paint()  g:����
 	public void paint(Graphics g) {
		//������ͼ��Ӣ�ۼ������ӵ����󣬵��˶���
		g.drawImage(background, 0, 0,null);	//����
		paintHero(g);//Ӣ�ۻ�����
		paintFlyingObjects(g);//���˶��󣨵л����۷�
		paintBullets(g);//�ӵ�
		paintScoreAndLife(g);//���÷�����
		paintState(g);
		paintHit(g);
	}
	public void paintScoreAndLife(Graphics g) {//�Զ������� ����
		g.setColor(new Color(0xF000F0));//������ɫ
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));//��������
		g.drawString("SCORE:"+score, 10, 15);
		g.drawString("LIFE:"+hero.getLife(), 10, 30);
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));//��������
		g.drawString("DESIGNED BY ����", 10, 45);
	}
	public void paintHero(Graphics g) {//Ӣ�ۻ�����
		g.drawImage(hero.image, hero.x, hero.y,null);
		
	}
	public void paintFlyingObjects(Graphics g) {//���˶��󣨵л����۷䣩
		for(int i=0;i<flyings.length;i++) {
			FlyingObject f=flyings[i];
			g.drawImage(f.image, f.x, f.y,null);
			//System.out.println(112);/////////////////////////////////////
		}
	}
	public void paintBullets(Graphics g) {//�ӵ�
		for(int i=0;i<bullets.length;i++) {
			Bullet b=bullets[i];
			g.drawImage(b.image, b.x, b.y,null);	
		}		
	}	

	public void paintState(Graphics g) {//��״̬
		switch(state) {
		case START:g.drawImage(start,0,0,null);
		break;
		case PAUSE:g.drawImage(pause,0,0,null);
		break;
		case GAME_OVER:g.drawImage(gameover,0,0,null);
		break;	
		}
	}
	int qqq=0;
	public void paintHit(Graphics g) {//ײ������
		if(isHitting) {
			 qqq++;
			 g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));//��������
			 g.drawString("����Ŷ��", 150, 325);
			 if(qqq>=40) {
				 isHitting=false;
				 qqq=0;
			 }
		}
	}
	
	public static void main(String[] args) {
		JFrame frame=new JFrame("�����ʸ��ɻ�");
		ShootGame game=new ShootGame();
		frame.add(game);//�����ӵ�������
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);//�������д���ǰ��
		//����Ĭ�ϲ������رմ���ʱ�˳�����
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);//���������λ�ã�����������ʾ
		/*//frame.setVisible(true);1.���ô��ڿɼ�2.�������paint(),,Jpanel����һ��paint()��������ʲôҲû��*/
		frame.setVisible(true);//��ʱ������һ��paint(),�����˿�ܡ����뻭�л��ȷ������Ҫ����action()�е���
		game.action();//���������ִ��
	}

	
}
