package shoot.day03;
/**奖励**/
public interface Award {
	public static final int DOUBLE_FIRE=0;//火力
	public int LIFE=1;	//命
	/**获取奖励类型，返回0为火力，1为命**/
	public int getType();
	
}
