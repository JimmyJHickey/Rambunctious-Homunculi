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
	
	public boolean runProcess()
	{
		boolean done = false;
		Burst currentBurst = runningProcess.bursts.peek();
		
		++runTime;
		
		currentBurst.length -= 1;
		
		// if the current CPU burst finished this tick, discard current burst, move to next one
		if(currentBurst.length <= 0)
		{
			runningProcess.bursts.remove();
			
			// ugly. If the bursts are empty, the process is done
			if(!runningProcess.bursts.isEmpty())
			{
				currentBurst = runningProcess.bursts.peek();
			
				// if the next burst is not a CPU burst, then the process in also done with the processor 
				if(!currentBurst.cpuBurst)
				{
					done = true;
				}
			}
			else done = true;
		}
		
		return done;
	}
	
	public void getNewProcess(Process proc)
	{
		runningProcess = proc;
		runTime = 0;
		
		preemptable = !runningProcess.bursts.peek().criticalSection;
	}
	
	public int getRunTime()
	{
		return runTime;
	}
}
