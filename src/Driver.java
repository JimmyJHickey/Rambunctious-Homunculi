import java.util.ArrayList;

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
		ArrayList<Process> processes = CSV.parseProcessCSV("data/processes.csv");
		ArrayList<SchedEntity> waitingIO = new ArrayList<SchedEntity>();
		
		// set up to run
		int tick = 0;
		CompletelyFairScheduler cfs = new CompletelyFairScheduler(TARGET_LATANCY, MIN_GRANULARITY);
		Processor processor = new Processor();
		SchedEntity schedEnt = null;
		ArrayList<SchedEntity> killList = new ArrayList<SchedEntity>();
		String printer = "";
		
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
				
				schedEnt = cfs.pickNextTask();
				
				if(schedEnt != null)
				{
					processor.getNewProcess(schedEnt.process);
					
					System.out.printf("put %s on the processor\n", schedEnt.process.name);
				}
				else System.out.printf("no process ready to be put on the processor\n");
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
				
				processor.removeProcess();
				schedEnt.virtualRuntime += processor.getRunTime();
				cfs.schedOther(schedEnt);
				schedEnt = null;			
				
				System.out.printf("put back in scheduling tree\n");
			}
			else
			{
				processor.runProcess();
				
				schedEnt.process.bursts.isEmpty();
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
		} while(processor.hasRunningProcess() || !waitingIO.isEmpty() || !cfs.isEmpty());
		
		System.out.printf("We are winner\n");
	}
}
