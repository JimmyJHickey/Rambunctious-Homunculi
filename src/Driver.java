import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import Process.Process;
import Process.Burst;
import Process.SchedEntity;



public class Driver 
{		
	final private static int MIN_GRANULARITY = 2;
	final private static int TARGET_LATANCY = 10;
	
	
	public static void main(String args[])
	{
		// get process list out of the csv
		ArrayList<Process> processes = CSV.parseProcessCSV("data/processes1.csv");
		ArrayList<SchedEntity> waitingIO = new ArrayList<SchedEntity>();
		
		// set up to run
		int tick = 0;
		
		LockManager lckmgr = new LockManager();
		CompletelyFairScheduler cfs = new CompletelyFairScheduler(TARGET_LATANCY, MIN_GRANULARITY);
		Processor processor = new Processor(lckmgr);
		
		SchedEntity schedEnt = null;
		ArrayList<SchedEntity> killList = new ArrayList<SchedEntity>();
		
		boolean lockedCriticalSection = false;
		boolean runnableProcess = false;
		
		String printer = "";
		
		int i = 0;
		
		// the main event
		do 
		{
			System.out.printf("tick: %d\n", tick);
			
			// find processes to that have arrived, schedule them
			for(Process proc:processes)
			{
				if(proc.arrivalTime == tick)
				{
					cfs.schedOther(new SchedEntity(proc));
					
					System.out.printf("Adding %s to the schedule tree\n", proc.name);
				}
			}
			
			// find processes in the waiting list that are done waiting, schedule them
			for(SchedEntity se:waitingIO)
			{
				if(se.process.bursts.peek().length == 0)
				{
					se.process.bursts.remove();
					cfs.schedOther(se);
					killList.add(se);
					
					System.out.printf("Moving %s out of the waiting queue\n", se.process.name);
				}
			}
			
			waitingIO.removeAll(killList);
			killList.clear();
			
			// advance the time of all the io bursts in the waiting list
			for(SchedEntity se:waitingIO)
			{
				se.process.bursts.peek().length--;
				
				System.out.printf("%s waiting for %d more ticks\n", se.process.name, se.process.bursts.peek().length);
			}
			
			
			if(!processor.hasRunningProcess()) 
			{
				System.out.printf("No process running...");
				
				runnableProcess = false;
				
				// find a the next runnable process on the scheduling tree
				while(!runnableProcess)
				{
					schedEnt = cfs.pickNextTask();
					
					// if the tree is empty, schedEnt will be null
					if(schedEnt != null)
					{
						if((runnableProcess = schedEnt.process.runnable))
						{
							processor.getNewProcess(schedEnt.process);
							System.out.printf("put %s on the processor\n", schedEnt.process.name);
						}
						else
						{
							killList.add(schedEnt);
							
							System.out.printf("\nprocess %s not runnable...", schedEnt.process.name);
						}
					}
					else 
					{
						System.out.printf("no process ready to be put on the processor\n");
						break;
					}
				}
				
				// add all of the not runnable processes back into the schedule tree
				for(SchedEntity se:killList)
				{
					cfs.schedOther(se);
				}
				killList.clear();
				
			}
			else if(lockedCriticalSection)
			{
				System.out.printf("Process %s tried to enter a locked critical section...", schedEnt.process.name);
				processor.removeProcess();
				
				// if the wait queue for this lock does not exist
				if(!lckmgr.waitQueues.containsKey(schedEnt.process.bursts.peek().lock))
				{
					// create it
					lckmgr.waitQueues.put(new Character(schedEnt.process.bursts.peek().lock), new LinkedList<SchedEntity>());
				}
				
				// add the schedentity to the appropriate wait queue
				lckmgr.waitQueues.get(schedEnt.process.bursts.peek().lock).add(schedEnt);
				
				// set the process to not runnable
				schedEnt.process.runnable = false;
				
				// add the schedentity back into the schedule tree
				cfs.schedOther(schedEnt);
				
				// don't move this print statement...
				System.out.printf("put in waiting queue '%c'\n", schedEnt.process.bursts.peek().lock);
				
				lockedCriticalSection = false;
				schedEnt = null;
			}
			else if(schedEnt.process.bursts.isEmpty())
			{
				System.out.printf("%s finished processing...", schedEnt.process.name);
				
				processor.removeProcess();
				schedEnt = null;
				
				System.out.printf("process reaped\n");
			}
			else if(!schedEnt.process.bursts.peek().cpuBurst)
			{
				System.out.printf("%s is waiting for IO...", schedEnt.process.name);
				
				processor.removeProcess();
				schedEnt.virtualRuntime += processor.getRunTime();
				schedEnt.process.bursts.peek().length--;
				waitingIO.add(schedEnt);
				schedEnt = null;
				
				System.out.printf("placed in wait list\n");
			}
			else if(processor.getRunTime() >= cfs.getTimeSlice())
			{
				System.out.printf("%s overstayed its visit...", schedEnt.process.name);
				
				if(cfs.hasRunnableProcess())
				{
					if(processor.removeProcess())
					{
						schedEnt.virtualRuntime += processor.getRunTime();
						cfs.schedOther(schedEnt);
						schedEnt = null;
						
						System.out.printf("put back in scheduling tree\n");
					}
					else 
					{
						System.out.printf("CPU currently unpreemptable...running process\n");		
						lockedCriticalSection = processor.runProcess();
					}
				}
				else
				{
					System.out.printf("no runnable processes in the tree...running process\n");
					lockedCriticalSection = processor.runProcess();
				}
			}
			else
			{
				lockedCriticalSection = processor.runProcess();
			}
			
			
			printer = "";
			if(processor.hasRunningProcess())
			{
				printer += String.format("Process Running: %s\n", schedEnt.process.name);
				
				if(!schedEnt.process.bursts.isEmpty())
				{
					printer += String.format("Burst Time Remaining: %d\n", schedEnt.process.bursts.peek().length);
					printer += String.format("%s\n", schedEnt.process.bursts.peek().criticalSection ? "Critical Section" : "Not Critical Section");
					printer += String.format("Lock: %c\n", schedEnt.process.bursts.peek().lock);
				}
				else
				{
					printer += String.format("Process Time Remaining: 0\n");
				}
			}
			else printer += String.format("No process running\n");
			
			System.out.printf("%s\n", printer);
			
			++tick;			
		} while(  (processor.hasRunningProcess() || !waitingIO.isEmpty() || !cfs.isEmpty()) /*&& i++ < 200*/);
		// while the processor is running a process, there are processes waiting for I/O, or there are processes in the schedule tree
		
		System.out.printf("We are winner\n");
	}
}
