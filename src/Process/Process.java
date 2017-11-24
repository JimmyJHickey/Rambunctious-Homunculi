package Process;
import java.util.ArrayList;
import java.util.LinkedList;
import MemoryManagement.Page;
import java.util.Queue;

// Process struct

public class Process 
{
	public String name = null;
	public int number = 0;
	public int arrivalTime = 0;
	public int priority = 0; // between 1 and 99
	public int niceness = 0; // between -20 and 19
	public int memory = 0; // in MB
	
	public ArrayList<Page> pages = new ArrayList<Page>();
	
	public Queue<Burst> bursts = new LinkedList<Burst>();	
	
	public Process()
	{
		
	}
}