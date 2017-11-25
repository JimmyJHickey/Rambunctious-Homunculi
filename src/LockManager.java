import java.util.ArrayList;
import java.util.Queue;
import java.util.TreeMap;

import Process.SchedEntity;

public class LockManager
{
	public TreeMap<Character, Queue<SchedEntity>> waitQueues;
	
	public LockManager()
	{
		waitQueues = new TreeMap<Character, Queue<SchedEntity>>();
	}
}
