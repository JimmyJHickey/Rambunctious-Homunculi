package Process;

public class Burst 
{
	public boolean cpuBurst;
	public boolean criticalSection;
	public char lock;
	public int length;
	
	public Burst(boolean cpu, boolean crit, char lck, int lngth) 
	{
		cpuBurst = cpu;
		criticalSection = crit;
		lock = lck;
		length = lngth;
	}
	
	
}