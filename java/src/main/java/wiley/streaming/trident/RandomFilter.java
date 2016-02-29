package wiley.streaming.trident;

import java.util.Random;

import storm.trident.operation.BaseFilter;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class RandomFilter extends BaseFilter {
	private static final long serialVersionUID = 1L;
	Random rng = new Random();
	
	public boolean isKeep(TridentTuple tuple) {
		return rng.nextFloat() > 0.5;
	}
}
