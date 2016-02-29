package wiley.streaming.trident;

import storm.trident.operation.CombinerAggregator;
import storm.trident.tuple.TridentTuple;

public class DoubleSumCombiner implements CombinerAggregator<Double> {
	private static final long serialVersionUID = 1L;

	public Double init(TridentTuple tuple) {
		return 1.0;
	}

	public Double combine(Double val1, Double val2) {
		return val1+val2;
	}

	public Double zero() {
		return 0.0;
	}

}
