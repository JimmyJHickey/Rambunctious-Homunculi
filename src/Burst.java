
public class Burst 
{
	private boolean cpuBurst;
	private boolean criticalSection;
	private char lock;
	private int length;
	
	public Burst(boolean cpu, boolean crit, char lck, int lngth) 
	{
		cpuBurst = cpu;
		criticalSection = crit;
		lock = lck;
		length = lngth;
	}
	
	
}