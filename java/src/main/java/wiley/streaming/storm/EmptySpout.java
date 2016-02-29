package wiley.streaming.storm;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

public class EmptySpout implements IRichSpout {

	transient SpoutOutputCollector collector;
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void close() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	public void nextTuple() {
		Utils.sleep(100);
		collector.emit(new Values("one","two","three"));
	}

	public void ack(Object msgId) {
	}

	public void fail(Object msgId) {
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("first","second","third"));
		declarer.declareStream("errors",new Fields("error"));
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
