package wiley.streaming.trident;

import backtype.storm.tuple.Values;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class SquareFunction extends BaseFunction {
	private static final long serialVersionUID = 1L;
	public void execute(TridentTuple tuple, TridentCollector collector) {
		collector.emit(new Values(tuple.getDouble(0)*tuple.getDouble(0)));
	}
}
