import java.util.Map.Entry;
import java.util.TreeMap;

public class CompletelyFairScheduler
{
	private int targetLat;
	private int minGran;
	
	private TreeMap<String, SchedEntity> rbTree;
	
	public CompletelyFairScheduler(int tl, int mg)
	{
		rbTree  = new TreeMap<String, SchedEntity>();
		
		targetLat = tl;
		minGran = mg;
	}
	
	public void schedOther(Process proc)
	{
		rbTree.put(proc.name, new SchedEntity(proc));
	}
	
	public SchedEntity pickNextTask()
	{
		SchedEntity nextTask = null;
		
		if(!rbTree.isEmpty())
		{
			 nextTask = rbTree.remove(rbTree.firstKey());
		}
		
		return nextTask;
	}
	
}
