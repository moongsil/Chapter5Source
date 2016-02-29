package wiley.streaming.storm;

import static org.junit.Assert.*;

import org.junit.Test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.clojure.RichShellSpout;
import backtype.storm.topology.TopologyBuilder;


public class FilterBoltTest {

	@Test
	public void test() throws Exception {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout1", new LoremIpsumSpout().tuple("first","second","third"));
		builder.setSpout("spout2", new LoremIpsumSpout().tuple("second","third","forth"));		
		builder.setBolt("filter", new FilterBolt()
		.filter("ipsum", "first", ".*ipsum.*","first")
		.filter("amet","second",".*amet.*","second")
		).shuffleGrouping("spout1")
		 .shuffleGrouping("spout2")
		;
		
		builder.setBolt("print", new LoggerBolt())
		.shuffleGrouping("filter","amet")
		.shuffleGrouping("filter","ipsum");
		
		Config conf = new Config();
		//conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("example", conf, builder.createTopology());
		
		//Sleep for a minute
		Thread.sleep(10000);
		
	}

}
