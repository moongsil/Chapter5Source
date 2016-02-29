package wiley.streaming.trident;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import backtype.storm.tuple.Values;

import storm.trident.operation.BaseAggregator;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;

public class KeyDoubleAggregator extends BaseAggregator<Map<Object, Double>> {

	private static final long serialVersionUID = 1L;

	public Map<Object, Double> init(Object batchId, TridentCollector collector) {
		return new HashMap<Object,Double>();
	}

	public void aggregate(Map<Object, Double> val, TridentTuple tuple,
			TridentCollector collector) {
		Object key = tuple.get(0);
		if(val.containsKey(key)) 
			val.put(key, val.get(key) + tuple.getDouble(1));
		else
			val.put(key, tuple.getDouble(1));
	}

	public void complete(Map<Object, Double> val, TridentCollector collector) {
		for(Entry<Object,Double> e : val.entrySet()) {
			collector.emit(new Values(e.getKey(),e.getValue()));
		}
		
	}

}
