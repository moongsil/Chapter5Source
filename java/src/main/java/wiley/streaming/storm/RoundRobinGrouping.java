package wiley.streaming.storm;

import java.util.LinkedList;
import java.util.List;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.grouping.CustomStreamGrouping;
import backtype.storm.task.WorkerTopologyContext;

public class RoundRobinGrouping implements CustomStreamGrouping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	transient List<Integer> targetTasks;
	transient int           nextTask;
	transient int           numTasks;
	
	public void prepare(WorkerTopologyContext context, GlobalStreamId stream,
			List<Integer> targetTasks) {
		this.targetTasks = targetTasks;
		this.numTasks    = targetTasks.size();
		this.nextTask    = 0;

	}

	public List<Integer> chooseTasks(int taskId, List<Object> values) {
		LinkedList<Integer> out = new LinkedList<Integer>();
		out.add(targetTasks.get(nextTask));
		nextTask = (nextTask + 1) % numTasks;
		return out;
	}

}
