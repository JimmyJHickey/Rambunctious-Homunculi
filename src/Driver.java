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
		boolean onProcessor = false;
		CompletelyFairScheduler cfs = new CompletelyFairScheduler(TARGET_LATANCY, MIN_GRANULARITY);
		Processor processor = new Processor();
		SchedEntity schedEnt = null;
		ArrayList<SchedEntity> killList = new ArrayList<SchedEntity>();
		String printer = "";
		
		while(!onProcessor || !waitingIO.isEmpty() || !cfs.isEmpty())
		{
			System.out.printf("tick: %d\n", tick);
			
			// find processes to that have arrived, schedule them
			for(Process proc:processes)
			{
				if(proc.arrivalTime == tick)
				{
					cfs.schedOther(new SchedEntity(proc));
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
				}
			}
			
			waitingIO.removeAll(killList);
			killList.clear();
			
			// advance the time of all the io bursts in the waiting list
			for(SchedEntity se:waitingIO)
			{
				System.out.printf("Waiting Burst: Name: %s, IO Length: %d\n", se.process.name, se.process.bursts.peek().length);
				se.process.bursts.peek().length--;
			}
			
			if(!processor.hasRunningProcess()) 
			{
				System.out.printf("# No running process\n");
				
				schedEnt = cfs.pickNextTask();
				
				if(schedEnt != null)
				{
					processor.getNewProcess(schedEnt.process);
				}
			}
			else if(schedEnt.process.bursts.isEmpty())
			{
				System.out.printf("# Process finished\n");
				
				processor.removeProcess();
				schedEnt = null;
			}
			else if(!schedEnt.process.bursts.peek().cpuBurst)
			{
				System.out.printf("# Process on IO burst\n");
				
				processor.removeProcess();
				schedEnt.virtualRuntime += processor.getRunTime();
				schedEnt.process.bursts.peek().length--;
				waitingIO.add(schedEnt);
				schedEnt = null;
			}
			else if(processor.getRunTime() >= cfs.getTimeSlice())
			{
				System.out.printf("  Runtime: %d\n", processor.getRunTime());
				
				System.out.printf("# Process overstayed its visit\n");
				
				processor.removeProcess();
				schedEnt.virtualRuntime += processor.getRunTime();
				cfs.schedOther(schedEnt);
				schedEnt = null;				
			}
			else
			{
				System.out.printf("# Running the process\n");
				processor.runProcess();
				
				schedEnt.process.bursts.isEmpty();
			}
			
			onProcessor = schedEnt == null;
			
			printer = "";
			if(!onProcessor)
			{
				printer += String.format("Process Running: %s\n", schedEnt.process.name);
				
				if(!schedEnt.process.bursts.isEmpty())
				{
					printer += String.format("%s\n", schedEnt.process.bursts.peek().criticalSection ? "Critical Section" : "Not Critical Section");
					printer += String.format("Time Remaining: %d\n", schedEnt.process.bursts.peek().length);
				}
				
				System.out.printf("%s\n", printer);
			}
			++tick;
		} // end whil loop
		System.out.printf("We are winner\n");
	}
}
