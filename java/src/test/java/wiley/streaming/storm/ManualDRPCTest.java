package wiley.streaming.storm;

import static org.junit.Assert.*;

import org.junit.Test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.LocalDRPC;
import backtype.storm.drpc.DRPCSpout;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.drpc.ReturnResults;
import backtype.storm.topology.TopologyBuilder;

public class ManualDRPCTest {

	@Test
	public void test() {
		TopologyBuilder builder = new TopologyBuilder();
		LocalDRPC drpc = new LocalDRPC();
		
		//Omit the drpc argument when submitting to the cluster
		builder.setSpout("drpc", new DRPCSpout("power", drpc));
		builder.setBolt("split", new SplitBolt()).shuffleGrouping("drpc");
		builder.setBolt("power", new PowerBolt()).shuffleGrouping("split");
		builder.setBolt("return", new ReturnResults()).shuffleGrouping("power");
		
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("power", conf, builder.createTopology());
		
		System.out.println(drpc.execute("power", "2,2"));
		
	}

}
