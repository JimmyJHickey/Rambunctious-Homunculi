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
			
			// advance time 1 tick
			++runTime;
			currentBurst.length--;
			
			// if the current CPU burst finished this tick, discard current burst, move to next one
			if(currentBurst.length <= 0)
			{
				preemptable = true;
				
				// if the burst was a critical section with a wait queue
				if(currentBurst.criticalSection && lockman.waitQueues.containsKey(currentBurst.lock))
				{
					// pop a process out of the wait queue
					tempEntity = lockman.waitQueues.get(currentBurst.lock).poll();
					
					if(tempEntity != null) 
					{
						tempEntity.process.runnable = true;
					}
					else
					{
						// if there isn't a process waiting for the lock, remove the wait queue
						lockman.waitQueues.remove(currentBurst.lock);
					}
				}
				
				runningProcess.bursts.remove();
				
				// if there are more bursts
				if(!runningProcess.bursts.isEmpty())
				{
					currentBurst = runningProcess.bursts.peek();
					
					if(currentBurst.criticalSection)
					{
						// if no one currently owns the lock
						if(!lockman.waitQueues.containsKey(currentBurst.lock))
						{
							// if the crit section is too short to be preempted
							if(currentBurst.length <= 2)
							{
								preemptable = false;
							}
							else
							{
								// create a wait queue for this lock
								lockman.waitQueues.put(new Character(currentBurst.lock), new LinkedList<SchedEntity>());
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
		
		// if the process is a critical section and it is too short to be preempted, turn preemption off
		preemptable = !(runningProcess.bursts.peek().criticalSection && runningProcess.bursts.peek().length <= 2);
	}
	
	public boolean removeProcess()
	{
		// remove the process iff the processor can be preempted
		if(preemptable)
		{
			runningProcess = null;
		}
		
		return preemptable;
		
	}
	
	// get the time the current process spent on the processor
	public int getRunTime()
	{
		return runTime;
	}
	
	public boolean hasRunningProcess()
	{
		return runningProcess != null;
	}
}
