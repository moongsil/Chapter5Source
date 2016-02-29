package wiley.streaming.samza;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.samza.config.Config;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.InitableTask;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskContext;
import org.apache.samza.task.TaskCoordinator;
import org.apache.samza.task.WindowableTask;

public class WordCountTask implements StreamTask, InitableTask, WindowableTask {
	private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka","wikipedia-counts");

	HashMap<String,Integer> counts = new HashMap<String,Integer>();
	HashSet<String> changed = new HashSet<String>();
	
	public void window(MessageCollector arg0, TaskCoordinator arg1)
			throws Exception {
		
		for(String word : changed) {
			HashMap<String,Object> val = new HashMap<String,Object>();
			val.put("word", word);
			val.put("count", counts.get(word));
			arg0.send(new OutgoingMessageEnvelope(OUTPUT_STREAM,val));
		}
		
		changed.clear();
	}

	public void init(Config arg0, TaskContext arg1) throws Exception {
		counts.clear();
		changed.clear();
	}

	public void process(IncomingMessageEnvelope arg0, MessageCollector arg1,
			TaskCoordinator arg2) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String,Object> json = (Map<String,Object>)arg0.getMessage();
		String word = (String) json.get("word");
		counts.put(word, (counts.containsKey(word) ? counts.get(word) : 0) + 1);
		changed.add(word);
	}

}
