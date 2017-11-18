package Process;
import java.util.Comparator;

public class SchedEntity implements Comparator<SchedEntity>
{
	public Process process;
	public int virtualRuntime;
	
	public SchedEntity(Process proc)
	{
		process = proc;
		virtualRuntime = 0;
	}

	// nothing to see here, no reason to avert eyes
	// though warning, not for the faint of heart
	// FCFS if virtual runtimes are the same
	@Override
	public int compare(SchedEntity arg0, SchedEntity arg1)
	{
		int returnValue;
		
		if(arg0.virtualRuntime == arg1.virtualRuntime)
		{
			returnValue = 1;
		}
		else
		{
			returnValue = arg0.virtualRuntime - arg1.virtualRuntime;
		}
		
		return returnValue;
	}	
	
}
