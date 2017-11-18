import java.util.Map.Entry;
import java.util.TreeSet;

import Process.*;


public class CompletelyFairScheduler
{
	private int targetLat;
	private int minGran;
	private int timeSliceButNotReally;
	
	private TreeSet<SchedEntity> rbTree;
	
	public CompletelyFairScheduler(int tl, int mg)
	{
		rbTree  = new TreeSet<SchedEntity>(new SchedEntity(null));
		
		targetLat = tl;
		minGran = mg;
	}
	
	public void schedOther(SchedEntity sched)
	{
		System.err.printf("added a sched\n");
		rbTree.add(sched);
		System.out.printf("   size: %d\n", rbTree.size());
	}
	
	public SchedEntity pickNextTask()
	{
		SchedEntity nextTask = null;
		
		System.out.printf("   size: %d\n", rbTree.size());
		
		if(!rbTree.isEmpty())
		{
			 nextTask = rbTree.pollFirst();
		}
		
		return nextTask;
	}
	
	public int getTimeSlice()
	{
		timeSliceButNotReally = targetLat / (rbTree.size() +1);
		
		return timeSliceButNotReally > minGran ? timeSliceButNotReally : minGran;
	}
	
	public boolean isEmpty()
	{
		return rbTree.isEmpty();
	}
	
}
