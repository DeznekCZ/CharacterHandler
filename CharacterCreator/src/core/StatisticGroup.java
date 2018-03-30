package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticGroup extends ModuleEntry<Module,StatisticGroup> {

	public static final StatisticGroup GLOBAL = new StatisticGroup(Module.GLOBAL, "GLOBAL_GROUP", "Generated Statistics");
	
	
	private List<Statistic> list = new ArrayList<Statistic>();
	private HashMap<String, Statistic> statistics = new HashMap<>();

	public StatisticGroup(Module module, String id, String name) {
		super(module, id, name);
		module.addGroup(id, this);
	}

	public String getName() {
		return name;
	}

	public HashMap<String, Statistic> getStatistics() {
		return statistics;
	}

	public Statistic getStatistic(String group, String statistic) {
		return getStatistics().get(statistic);
	} 
	
	public Statistic getStatistic(String groupStatistic) {
		String[] path = groupStatistic.split("\\.");
		return getStatistic(path[0], path[1]);
	}

	public List<Statistic> getStatisticsAsList() {
		return list;
	}

	public void addStatistic(String id, Statistic statistic) {
		list.add(statistic);
		getStatistics().put(id, statistic);
	}

}
