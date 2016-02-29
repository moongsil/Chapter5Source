package storm.wiley.trident;

import static org.junit.Assert.*;

import org.junit.Test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.tuple.Fields;

import storm.trident.TridentTopology;
import wiley.streaming.storm.LoremIpsumSpout;
import wiley.streaming.trident.PrintFunction;
import wiley.streaming.trident.RandomFilter;
import wiley.streaming.trident.RandomFunction;
import wiley.streaming.trident.SquareFunction;

public class TopologyTest {

	@Test
	public void test() throws Exception {
		TridentTopology topology = new TridentTopology();
		topology.newStream("lorem", new LoremIpsumSpout().tuple("first","second","third"))
		.each(new RandomFunction(),new Fields("x"))
		.each(new Fields("x"), new SquareFunction(),new Fields("y"))
		.each(new Fields("x","y"),new PrintFunction(), new Fields())
		
		;
		
		Config conf = new Config();
		//conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("example", conf, topology.build());
		
		//Sleep for a minute
		Thread.sleep(60000);
		
	}

}
