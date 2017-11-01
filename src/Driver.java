import java.util.LinkedList;
import java.util.Queue;

public class Driver 
{
	private Queue<Process> processes = new LinkedList<Process>();
	
	public Driver() 
	{
		Process process = new Process();
		
		process.number = 0;
		process.memory = 64;
		process.bursts.add(new Burst(true, false, '~', 3));
		process.bursts.add(new Burst(false, false, '~', 14));
		process.bursts.add(new Burst(true, false, '~', 3));
		process.bursts.add(new Burst(true, true, 'd', 2));
		process.bursts.add(new Burst(true, false, '~',2));
		
		processes.add(process);
		
		// build process queue
		
	}
}
