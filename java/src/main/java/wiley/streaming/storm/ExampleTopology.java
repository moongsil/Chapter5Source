package wiley.streaming.storm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class ExampleTopology {

	
	public static void defineTopology(TopologyBuilder builder,String[] args) {
		builder.setSpout("input", new BasicSpout());

		builder.setBolt("empty", new RichEmptyBolt());
		builder.setBolt("process", new BasicBolt())
			.fieldsGrouping("empty", "cdr", new Fields("second"));
			
		builder.setBolt("processing", new BasicBolt())
			.shuffleGrouping("input")
			.fieldsGrouping("input", new Fields("key1","key2"))
			.allGrouping("input")
		;
	
	}
	
	
	/**
	 * @param args
	 * @throws InvalidTopologyException 
	 * @throws AlreadyAliveException 
	 */
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		TopologyBuilder builder = new TopologyBuilder();
		defineTopology(builder,args);
		StormTopology topology  = builder.createTopology();
		
		Config conf = new Config();

		StormSubmitter.submitTopology("my-topology", conf, topology);





	}

}
