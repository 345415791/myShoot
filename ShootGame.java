package shoot.day03;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;//框架
import javax.swing.JPanel;//镶嵌面
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;//数组扩容用
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
////主程序
	public class ShootGame extends JPanel {//当作画图面板
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
	private int state=START;//当前状态，默认启动状态
	private boolean isHitting;
	
	private Hero hero=new Hero();//英雄机对象
	private FlyingObject[] flyings= {};
	//敌人(敌机，蜜蜂)对象,没有再建一个父类，而是直接用FlyingObject代替，数组默认值为零
	private Bullet[] bullets= {};//子弹数组对象
	
	ShootGame(){//构造方法初始化，测试用	 	
		}
		
	static {//静态块，加载累的同时，初始化静态资源（图片）
		try {//检查报错
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
	
	public FlyingObject nextOne() {//随机生成敌人
		Random rand=new Random();
		int type=rand.nextInt(20);
		if(type<2) {//随机数小于4，返回蜜蜂对象
			return new Bee();
		}
		else return new AirPlane();
	}
	//启动程序的执行
	int flyEnteredIndex=0;
	public void enterAction() {///敌人入场（敌机，蜜蜂），只要装载flyings数组中，就能通过方法绘出来
		flyEnteredIndex++;
		if(flyEnteredIndex%40==0) {
			FlyingObject one =nextOne();
			flyings=Arrays.copyOf(flyings, flyings.length+1);//数组扩容
			flyings[flyings.length-1]=one;//将生成的敌人对象加到数组最后一个元素
		}		
	}
	
	/*飞行物（敌机，蜜蜂，子弹，英雄机）走步*/
	public void stepAction() {//各种物体走步
		hero.step();//英雄机
		
		for(int i=0;i<flyings.length;i++) {
			flyings[i].step();//遍历飞行物。走步
		}
		for(int i=0;i<bullets.length;i++) {
			bullets[i].step();//子弹走步
		}
	}
	int shootIndex=0;
	public void shootAction() {//创建子弹入场，由英雄机发射子弹,10ms调一次
		shootIndex++;
		if(shootIndex%20==0) {//调用20次，执行一次，即200ms发射一次子弹
			Bullet[] bs= hero.shoot();//获取子弹对象，不要加 new！已经实例化了！！！！！！！！！！！！！！！！！
		//bs数组中存放了子弹坐标
			bullets=Arrays.copyOf(bullets, bullets.length+bs.length);
		//子弹数组扩容，由于单双倍火力bs，所以扩容bs.length
		System.arraycopy(bs,0,bullets,(bullets.length-bs.length),bs.length);//将bs扩容到bullets
		//数组的追加！！！！！！！！！！！！！！！！
		}
	}
	
	public void outOfBoundsAction() {//删除越界的对象(敌机，蜜蜂，子弹)
		int index=0;//两个功能1.不越界敌人数组下标2.不越界敌人个数
		FlyingObject[] flyingLives=new FlyingObject[flyings.length];//创建一个flyingLives新数组引用
		for(int i=0;i<flyings.length;i++) {
			if(!flyings[i].outOfBounds()) {//如果原flyings数组元素(敌机或蜜蜂)没越界，则保留
				flyingLives[index]=flyings[i];//将原flyings数组中，没越界的元素，赋值到新的flyingLives中
				index++;//两个功能1.不越界敌人数组下标+1,,,2.不越界敌人个数+1
			}
		}
		//删除越界元素，，，
		flyings=Arrays.copyOf(flyingLives, index);//将flyingLives中不越界的元素，重新赋值到flyings数组中，实现删除越界元素
	///*子弹越界删除*/、、、、、、、、、、、、、、、、、、、、、
		int indexBullets=0;//两个功能1.不越界敌人数组下标2.不越界敌人个数
		Bullet[] bulletsLives=new Bullet[bullets.length];//创建一个flyingLives新数组引用
		for(int i=0;i<bullets.length;i++) {
			if(!bullets[i].outOfBounds()) {//如果原flyings数组元素(敌机或蜜蜂)没越界，则保留
				bulletsLives[indexBullets]=bullets[i];//将原flyings数组中，没越界的元素，赋值到新的flyingLives中
				indexBullets++;//两个功能1.不越界敌人数组下标+1,,,2.不越界敌人个数+1
			}
		}
		//保留不越界元素
		bullets=Arrays.copyOf(bulletsLives, indexBullets);//将flyingLives中不越界的元素，重新赋值到flyings数组中，实现删除越界元素
	}
	
	
	public void bangAction() {//子弹击中判断，从数组删除改元素，即从游戏中彻底删除改对象了
		for(int i=0;i<bullets.length;i++) {
			Bullet b=bullets[i];//遍历每一个子弹
			if(bang(b)) {//判断一个子弹撞与所有敌人碰撞//碰撞了，则和数组最后元素交换，再缩容，从而实现数组删减该子弹
				bullets[bullets.length-1]=b;
				bullets=Arrays.copyOf(bullets, bullets.length-1);
			};//判断一个子弹撞与所有敌人碰撞
		}
	}
	int score=0;
	public boolean bang (Bullet b) {
		 int index=-1;//被撞敌人下标
		 for(int i=0;i<flyings.length;i++) {
			 FlyingObject f=flyings[i];
			 if(f.shootBy(b)) {//撞上了
				 index=i;//记录被撞敌人下标
				 break;//一个子弹最多撞一个敌人，撞上就不再比较
			 }
		 }
		 if(index!=-1) {//index不是初值了，表示被撞上，此时index表示被撞敌人下标
			 
			 FlyingObject one=flyings[index];//被撞机存起来
			 if(one instanceof Enemy) {//one代表的敌机，蜜蜂分别是接口Enemy和Award的实现
				 Enemy e=(Enemy)one;//强转类型
				 score+=e.getScore();
			 }
			 if(one instanceof Award) {//若是奖励
				 Award a=(Award)one;///强转奖励类型
				  int type=a.getType();//获取奖励类型
				  switch(type) {
				  case Award.DOUBLE_FIRE:{//////注意静态常量，调用方式！！！！！！！！！！！！！！！！！！！
					  hero.addDoubleLife();
					  break;
				  }
				  case Award.LIFE:{
					  hero.addLife();
					  break;
				  }
				  }
			 }
			 //消除被撞物，，只需将被撞敌人从flings数组移出即可
			 FlyingObject t=flyings[index];
			//*把数组中被撞的元素与数组最后一个元素对调*//
			 flyings[index]=flyings[flyings.length-1];
			 flyings[flyings.length-1]=t;//其实这句可以不要
			 flyings=Arrays.copyOf(flyings, flyings.length-1);//复制，缩容:去掉最后一个元素，即被撞元素
			 return true;
		 }
		 else return false;
	}
	
	public void checkGameoverAction() {//检查游戏是否结束
		if(isGameOver()) {
			state=GAME_OVER;
		}
	}
	public boolean isGameOver() {//包含英雄机相撞
		//isHitting=false;
		for(int i=0;i<flyings.length;i++) {
			FlyingObject f=flyings[i];
			if(hero.hit(f)) {//相撞了
				isHitting=true;
				hero.clearDoubleFire();//火力值清零
				hero.subtractLife();//life-1
				flyings[i]=flyings[flyings.length-1];//撞击的敌人消失
				flyings[flyings.length-1]=f;
				flyings=Arrays.copyOf(flyings, flyings.length-1);
				
			}		
		}
		return (hero.getLife()<=0);//life<=0，游戏结束
	}
	
	
	public void action() {//启动程序的执行
		//创建侦听器对象，匿名内部类
		MouseAdapter l=new MouseAdapter() {
			/*重写鼠标移动事件*/
			public void mouseMoved(MouseEvent e) {
				//System.out.println(111);
				if(state==RUNNING) {
					int x=e.getX();//获取鼠标坐标x,y
					int y=e.getY();
					hero.moveTo(x, y);//传给英雄机鼠标的坐标参数
				}
			}
			public void mouseClicked(MouseEvent e) {//点击鼠标触发状态
				//System.out.println(111);
				switch(state) {//根据游戏状态，做不同显示
				case START:
					state=RUNNING;
					break;
				case GAME_OVER:
					score=0;
					hero=new Hero();//调用Hero无参构造，赋初值！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
					flyings=new FlyingObject[0];
					bullets=new Bullet[0];
					state=START;//改为启动状态
					break;
				}
			}
			/*重写鼠标移出事件*/
			public void mouseExited(MouseEvent e) {
				if(state==RUNNING) {//如果游戏正在运行
					state=PAUSE;	//移出鼠标时，游戏改为暂停
				}
			}
			public void mouseEntered(MouseEvent e) {/*重写鼠标移出事件*/
				if(state==PAUSE) {
					state=RUNNING;
					//System.out.println(111);
				}
			}
		};
		this.addMouseListener(l);//处理鼠标操作事件，（点击
		this.addMouseMotionListener(l);//处理鼠标滑动事件（拖拽
		Timer timer=new Timer();//创建定时器对象
		int intervel=15;//时间间隔（ms），以下所有调用方法间隔
		
		timer.schedule(new TimerTask() {//匿名内部类！！！！！！！！！！！
	public void run() {//定时干的事，每10ms一次/////state开始控制运行与否///////////////////////////////////
		if(state==RUNNING) {
				enterAction();//飞行物入场
				shootAction();//子弹入场，和英雄机相关
				stepAction();//飞行物走步
				outOfBoundsAction();//删除越界对象
				bangAction();//子弹与敌人相撞
				checkGameoverAction();
		}
				repaint();//系统重新调用paint()方法，绘图
			}
		},intervel,intervel);	//intervel不能太大，不然飞行物显示不了，步距间隔太大
	}
	
	//重写系统自带paint()  g:画笔
 	public void paint(Graphics g) {
		//画背景图，英雄及对象，子弹对象，敌人对象
		g.drawImage(background, 0, 0,null);	//背景
		paintHero(g);//英雄机对象
		paintFlyingObjects(g);//敌人对象（敌机，蜜蜂
		paintBullets(g);//子弹
		paintScoreAndLife(g);//画得分于命
		paintState(g);
		paintHit(g);
	}
	public void paintScoreAndLife(Graphics g) {//自定义字体 标题
		g.setColor(new Color(0xF000F0));//设置颜色
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,15));//设置字体
		g.drawString("SCORE:"+score, 10, 15);
		g.drawString("LIFE:"+hero.getLife(), 10, 30);
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,16));//设置字体
		g.drawString("DESIGNED BY 王攀", 10, 45);
	}
	public void paintHero(Graphics g) {//英雄机对象
		g.drawImage(hero.image, hero.x, hero.y,null);
		
	}
	public void paintFlyingObjects(Graphics g) {//敌人对象（敌机，蜜蜂）
		for(int i=0;i<flyings.length;i++) {
			FlyingObject f=flyings[i];
			g.drawImage(f.image, f.x, f.y,null);
			//System.out.println(112);/////////////////////////////////////
		}
	}
	public void paintBullets(Graphics g) {//子弹
		for(int i=0;i<bullets.length;i++) {
			Bullet b=bullets[i];
			g.drawImage(b.image, b.x, b.y,null);	
		}		
	}	

	public void paintState(Graphics g) {//画状态
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
	public void paintHit(Graphics g) {//撞击画面
		if(isHitting) {
			 qqq++;
			 g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));//设置字体
			 g.drawString("加油哦！", 150, 325);
			 if(qqq>=40) {
				 isHitting=false;
				 qqq=0;
			 }
		}
	}
	
	public static void main(String[] args) {
		JFrame frame=new JFrame("跟着攀哥打飞机");
		ShootGame game=new ShootGame();
		frame.add(game);//面板添加到窗口上
		frame.setSize(WIDTH, HEIGHT);
		frame.setAlwaysOnTop(true);//保持所有窗口前端
		//设置默认操作，关闭窗口时退出程序
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);//不设置相对位置，，即居中显示
		/*//frame.setVisible(true);1.设置窗口可见2.尽快调用paint(),,Jpanel中有一个paint()方法，但什么也没画*/
		frame.setVisible(true);//此时调用了一次paint(),仅画了框架。。想画敌机等飞行物，需要再在action()中调用
		game.action();//启动程序的执行
	}

	
}
