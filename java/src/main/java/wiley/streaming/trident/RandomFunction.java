package wiley.streaming.trident;

import java.util.Random;

import backtype.storm.tuple.Values;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class RandomFunction extends BaseFunction {

	private static final long serialVersionUID = 1L;
	Random rng = new Random();
	
	public void execute(TridentTuple tuple, TridentCollector collector) {
		collector.emit(new Values(rng.nextDouble()));
	}

}
