package wiley.streaming.storm;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import de.svenjacobs.loremipsum.LoremIpsum;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class LoremIpsumSpout implements IRichSpout {

	private static final long serialVersionUID = 1L;
	String[] fields;
	
	public LoremIpsumSpout tuple(String...fields) {
		this.fields = fields;
		return this;
	}
	
	int maxWords = 25;
	public LoremIpsumSpout maxWords(int maxWords) {
		this.maxWords = maxWords;
		return this;
	}
	
	transient LoremIpsum           ipsum;
	transient Random               rng;
	transient SpoutOutputCollector collector;
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
		ipsum = new LoremIpsum();
		rng   = new Random();
	}

	public void close() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public void nextTuple() {
		Utils.sleep(100);
		ArrayList<Object> out = new ArrayList<Object>();
		for(String s : fields)
			out.add(ipsum.getWords(rng.nextInt(maxWords)));
		collector.emit(out);
	}

	public void ack(Object msgId) {
	}

	public void fail(Object msgId) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(fields));
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
