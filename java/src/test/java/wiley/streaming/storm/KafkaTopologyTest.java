package wiley.streaming.storm;

import static org.junit.Assert.*;

import org.junit.Test;

import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

public class KafkaTopologyTest {

	@Test
	public void test() throws Exception {
		//BrokerHosts     hosts   = new ZkHosts("localhost");
		//SpoutConfig     config  = new SpoutConfig(hosts, "wikipedia-raw", "", "storm");

		BrokerHosts     hosts   = new ZkHosts("kf-broker-1");
		SpoutConfig     config  = new SpoutConfig(hosts, "sqltag", "", "storm");

		config.scheme = new SchemeAsMultiScheme(new StringScheme());
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka", new KafkaSpout(config), 10);
		builder.setBolt("print", new LoggerBolt())
		.shuffleGrouping("kafka");
		
		Config conf = new Config();
		//conf.setDebug(true);
		
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("example", conf, builder.createTopology());
		
		//Sleep for a minute
		Thread.sleep(20000);

	}

}
