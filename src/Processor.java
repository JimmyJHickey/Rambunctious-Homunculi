import Process.Process;

import java.util.LinkedList;
import java.util.Queue;

import Process.Burst;
import Process.SchedEntity;


public class Processor
{	
	private LockManager lockman;
	private Process runningProcess;
	private boolean processDone;
	private boolean preemptable;
	private int runTime;
	
	private SchedEntity tempEntity;
	
	public Processor(LockManager lm)
	{
		lockman = lm;
		runningProcess = null;
		processDone = false;
		preemptable = true;
	}
	
	public boolean runProcess()
	{
		boolean lockedCritSection = false;
		
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
				preemptable = true;
				
				// if the burst was a critical section with a wait queue
				if(currentBurst.criticalSection && lockman.waitQueues.containsKey(currentBurst.lock))
				{
					tempEntity = lockman.waitQueues.get(currentBurst.lock).poll();
					
					if(tempEntity != null) tempEntity.process.runnable = true;
				}
				
				runningProcess.bursts.remove();
				
				// if the bursts are empty, the process is done
				if(!runningProcess.bursts.isEmpty())
				{
					currentBurst = runningProcess.bursts.peek();
					
					if(currentBurst.criticalSection)
					{
						// if no one currently owns the lock
						if(!lockman.waitQueues.containsKey(currentBurst.lock))
						{
							if(currentBurst.length <= 2)
							{
								preemptable = false;
							}
						}
						else
						{
							lockedCritSection = true;
						}
					}
				}
				else
				{
					currentBurst = null;
				}
			}
		}
		
		return lockedCritSection;
	}
	
	public void getNewProcess(Process proc)
	{
		runningProcess = proc;
		runTime = 0;
		
		preemptable = !runningProcess.bursts.peek().criticalSection;
	}
	
	public boolean removeProcess()
	{
		if(preemptable)
		{
			runningProcess = null;
		}
		
		return preemptable;
		
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
