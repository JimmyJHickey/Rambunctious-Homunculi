import java.util.ArrayList;


public class Driver 
{		
	final private static int MIN_GRANULARITY = 2;
	final private static int TARGET_LATANCY = 10;
	
	
	public static void main(String args[])
	{
		// get process list out of the csv
		ArrayList<Process> processes = CSV.parseProcessCSV("data/processes.csv");
		ArrayList<SchedEntity> waiting = new ArrayList<SchedEntity>();
		
		// set up to run
		int tick = 0;
		boolean noScheduledProcesses = false;
		CompletelyFairScheduler cfs = new CompletelyFairScheduler(MIN_GRANULARITY, TARGET_LATANCY);
		Processor processor = new Processor();
		SchedEntity schedEnt = null;
		ArrayList<SchedEntity> killList = new ArrayList<SchedEntity>();
		
		while(!noScheduledProcesses)
		{
			// find processes to that have arrived, schedule them
			for(Process proc:processes)
			{
				if(proc.arrivalTime == tick)
				{
					cfs.schedOther(new SchedEntity(proc));
				}
			}
			
			// find processes in the waiting list that are done waiting, schedule them
			for(SchedEntity se:waiting)
			{
				if(se.process.bursts.peek().length == 0)
				{
					se.process.bursts.remove();
					cfs.schedOther(se);
					killList.add(se);
				}
			}
			
			waiting.removeAll(killList);
			
			if(tick == 0)
			{
				schedEnt = cfs.pickNextTask();
				
				processor.getNewProcess(schedEnt.process);
			}
			
			// run the process that is on the processor
			if(processor.runProcess())
			{
				// process has exited
				if(schedEnt.process.bursts.isEmpty())
				{
					schedEnt = null;
				}
				// process is in an IO burst
				else 
				{
					schedEnt.virtualRuntime += processor.getRunTime();
					waiting.add(schedEnt);
					
					schedEnt = cfs.pickNextTask();
					
					if(schedEnt == null)
					{
						noScheduledProcesses = true;
					}
					else
					{
						processor.getNewProcess(schedEnt.process);
					}
				}
			}
			
			// advance the time of all the io bursts in the waiting list
			for(SchedEntity se:waiting)
			{
				System.out.printf("Waiting Burst: Name: %s, Bursts Length: %d\n", se.process.name, se.process.bursts.size());
				se.process.bursts.peek().length--;
			}
			
			if(!noScheduledProcesses)
			System.out.printf("Process Running: %s\nCS: %b\nTime Remaining: %d\n\n", schedEnt.process.name, schedEnt.process.bursts.peek().criticalSection, schedEnt.process.bursts.peek().length);
			
			++tick;
		}
		
	}
}
