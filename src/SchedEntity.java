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

	@Override
	public int compare(SchedEntity arg0, SchedEntity arg1)
	{
		return arg0.virtualRuntime - arg1.virtualRuntime;
	}	
	
}
