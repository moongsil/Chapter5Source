package wiley.streaming.samza;

import java.util.HashMap;
import java.util.Map;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;

public class WordSplitTask implements StreamTask {
	private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka","wikipedia-words");

	public void process(IncomingMessageEnvelope envelope, MessageCollector collector,
			TaskCoordinator coordinator) throws Exception {
		try {
		@SuppressWarnings("unchecked")
		Map<String,Object> json = (Map<String,Object>)envelope.getMessage();
		String raw = (String)json.get("raw");
		raw = raw.substring(2,raw.indexOf("]]"));
		for(String word : raw.split("\\s+")) {
			HashMap<String,Object> val = new HashMap<String,Object>();
			val.put("word", word);
			collector.send(new OutgoingMessageEnvelope(OUTPUT_STREAM,val));
		}
		} catch(Exception e) {
			System.err.println(e);
		}
		
	}
}
