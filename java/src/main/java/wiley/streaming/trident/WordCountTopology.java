package wiley.streaming.trident;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import storm.kafka.trident.OpaqueTridentKafkaSpout;
import storm.kafka.trident.TransactionalTridentKafkaSpout;
import storm.kafka.trident.TridentKafkaConfig;
import storm.trident.TridentTopology;
import storm.trident.operation.Function;
import storm.trident.operation.TridentCollector;
import storm.trident.operation.TridentOperationContext;
import storm.trident.operation.builtin.Count;
import storm.trident.operation.builtin.Debug;
import storm.trident.testing.MemoryMapState;
import storm.trident.tuple.TridentTuple;

public class WordCountTopology {
	public static TridentTopology get() {
		TridentTopology topology = new TridentTopology();
		
		//TridentKafkaConfig     config   = new TridentKafkaConfig(new ZkHosts("localhost"), "wikipedia-raw","storm");
		TridentKafkaConfig     config   = new TridentKafkaConfig(new ZkHosts("kf-broker-1"), "sqltag","storm");
		config.scheme = new SchemeAsMultiScheme(new StringScheme());
		
		
		topology.newStream("kafka", new TransactionalTridentKafkaSpout(config)).shuffle()
		.each(new Fields("str"),new Function() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			transient JSONParser parser;
			public void prepare(Map conf, TridentOperationContext context) {
				parser = new JSONParser();
			}

			public void cleanup() {

			}

			public void execute(TridentTuple tuple, TridentCollector collector) {
				if(tuple.size() == 0) return;
				try {
					Object obj = parser.parse(tuple.getString(0));
					if(obj instanceof JSONObject) {
						JSONObject json = (JSONObject)obj;
						String raw = (String)json.get("raw");
						raw = raw.substring(2,raw.indexOf("]]"));
						for(String word : raw.split("\\s+")) {
							collector.emit(new Values(word));
						}
					}
					
				} catch (ParseException e) {
					collector.reportError(e);
				}
			}
			
		}, new Fields("word")).groupBy(new Fields("word"))
		.persistentAggregate(new MemoryMapState.Factory(), new Count(), new Fields("count"))
		.newValuesStream()
		.each(new Fields("word","count"), new Debug("written"));
		
		
		
		return topology;
	}
}
