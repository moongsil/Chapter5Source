package wiley.streaming.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class SplitBolt extends BaseBasicBolt {

	private static final long serialVersionUID = -3681655596236684743L;

	public void execute(Tuple input, BasicOutputCollector collector) {
		String[] p = input.getString(0).split(",");
		collector.emit(new Values(Double.parseDouble(p[0]),Double.parseDouble(p[1]),input.getValue(1)));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("x","y","return-info"));
	}

}
