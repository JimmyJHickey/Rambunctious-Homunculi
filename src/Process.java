import java.util.LinkedList;
import java.util.Queue;

// Process struct

public class Process 
{
	public int number = 0;
	int arrivalTime = 0;
	int priority = 0; // between 1 and 99
	public int memory = 0;
	public Queue<Burst> bursts = new LinkedList<Burst>();	
	
	public Process()
	{
		
	}
}