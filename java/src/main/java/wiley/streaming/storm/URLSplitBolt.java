package wiley.streaming.storm;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class URLSplitBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1661178681279589681L;

	public void execute(Tuple input) {
		try {
			URL url = new URL(input.getString(0));
			collector.emit(new Values(url.getAuthority(),url.getHost(),url.getPath()));
		} catch (MalformedURLException e) {
			collector.reportError(e);
			collector.fail(input);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("authority","host","path"));

	}
	
	transient OutputCollector collector;
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		
	}

}
