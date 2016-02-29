package wiley.streaming.storm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import backtype.storm.generated.GlobalStreamId;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class FilterBolt extends BaseRichBolt {
	private static final Logger LOG = Logger.getLogger(FilterBolt.class);
	private static final long serialVersionUID = -7739856267277627178L;

	public class FilterDefinition implements Serializable {
		public String  stream;
		public String  fieldName;
		public Pattern regexp;
		public Fields  fields;
		private static final long serialVersionUID = 1L;
	}
	
	ArrayList<FilterDefinition> filters = new ArrayList<FilterDefinition>();
	
	public FilterBolt filter(String stream,String field,String regexp,String... fields) {
		FilterDefinition def = new FilterDefinition();
		def.stream = stream;
		def.fieldName = field;
		def.regexp = Pattern.compile(regexp);
		def.fields = new Fields(fields);
		filters.add(def);
		return this;
	}
	
	public class FilterBinding {
		public FilterDefinition filter;
		public int              fieldNdx;
	}
	
	
	transient OutputCollector                         collector;
	transient Map<GlobalStreamId,List<FilterBinding>> bindings;

	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		
		bindings = new HashMap<GlobalStreamId,List<FilterBinding>>();
		for(GlobalStreamId id : context.getThisSources().keySet()) {
			Fields fromId = context.getComponentOutputFields(id);
			ArrayList<FilterBinding> bounds = new ArrayList<FilterBinding>();
			for(FilterDefinition def : filters) {
				if(fromId.contains(def.fieldName)) {
					FilterBinding bind = new FilterBinding();
					bind.filter = def;
					bind.fieldNdx = fromId.fieldIndex(def.fieldName);
					bounds.add(bind);
				}
			}
			if(bounds.size() > 0) bindings.put(id, bounds);
		}
		
		
	}

	public void execute(Tuple input) {
		List<FilterBinding> bound = bindings.get(input.getSourceGlobalStreamid());
		if(bound != null && bound.size() > 0) {
			for(FilterBinding b : bound) {
				String item = input.getString(b.fieldNdx);
				if(b.filter.regexp.matcher(item).matches()) {
					collector.emit(b.filter.stream,input.select(b.filter.fields));
				}
			}
		}
		collector.ack(input);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		for(FilterDefinition def : filters)
			declarer.declareStream(def.stream, def.fields);
	}

}
