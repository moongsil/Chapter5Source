package wiley.streaming.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class SimpleCountingBolt extends BaseBasicBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5980494915433100442L;

	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}


}
