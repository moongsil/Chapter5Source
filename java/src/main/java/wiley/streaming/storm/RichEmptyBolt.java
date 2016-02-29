package wiley.streaming.storm;

import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class RichEmptyBolt extends BaseRichBolt {

	private static final long serialVersionUID = -4021848362136264967L;

	private transient OutputCollector collector;
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	public void execute(Tuple input) {
		List<Object> objs = input.select(new Fields("first","second","third"));
		collector.emit(objs);
		collector.emit("car", new Values(objs.get(0)));
		collector.emit("cdr", new Values(objs.get(1),objs.get(2)));
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("first","second","third"));
		declarer.declareStream("car", new Fields("first"));
		declarer.declareStream("cdr", new Fields("second","third"));
	}

}
