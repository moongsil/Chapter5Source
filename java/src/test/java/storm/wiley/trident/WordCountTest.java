package storm.wiley.trident;

import static org.junit.Assert.*;

import org.junit.Test;

import wiley.streaming.trident.WordCountTopology;

import backtype.storm.Config;
import backtype.storm.LocalCluster;

public class WordCountTest {

	@Test
	public void test() throws Exception {
		Config conf = new Config();
		//conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("example", conf, WordCountTopology.get().build());
		
		//Sleep for a minute
		Thread.sleep(30000);
	}

}
