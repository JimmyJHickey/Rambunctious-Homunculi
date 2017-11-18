import Process.Process;
import Process.Burst;
import Process.SchedEntity;


public class Processor
{	
	private Process runningProcess;
	private boolean processDone;
	private boolean preemptable;
	private int runTime;
	
	public Processor()
	{
		runningProcess = null;
		processDone = false;
		preemptable = true;
	}
	
	public void runProcess()
	{
		boolean done = false;
		
		if(runningProcess != null)
		{
			Burst currentBurst = runningProcess.bursts.peek();
			
			if(!currentBurst.cpuBurst) 
				System.err.printf("if you're reading this very bad things have happened\n");
			
			++runTime;
			
			currentBurst.length--;
			
			// if the current CPU burst finished this tick, discard current burst, move to next one
			if(currentBurst.length <= 0)
			{
				runningProcess.bursts.remove();
				
				// ugly. If the bursts are empty, the process is done
				if(!runningProcess.bursts.isEmpty())
				{
					currentBurst = runningProcess.bursts.peek();
				}
				else
				{
					currentBurst = null;
				}
			}
		}
	}
	
	public void getNewProcess(Process proc)
	{
		runningProcess = proc;
		runTime = 0;
		
		preemptable = !runningProcess.bursts.peek().criticalSection;
	}
	
	public Process removeProcess()
	{
		Process returnProcess = runningProcess;
		runningProcess = null;
		
		return returnProcess;
	}
	
	public int getRunTime()
	{
		return runTime;
	}
	
	public boolean hasRunningProcess()
	{
		return runningProcess != null;
	}
}
