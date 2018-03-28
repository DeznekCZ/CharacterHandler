package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cz.deznekcz.util.xml.XMLLoader;

public class Module {
	
	private static List<Module> list = new ArrayList<Module>();
	private static HashMap<String, Module> modules = new HashMap<>();
	
	public static final Module GLOBAL = new Module("GLOBAL_MODULE");
	
	private String name;
	
	public Module(String name) {
		this.name = name;
		list.add(this);
		modules.put(name, this);
	}
	
	public static HashMap<String, Module> getModules() {
		return modules;
	}

	/**
	 * 
	 * @param moduleName
	 * @return
	 * @throws ModuleLoaderException
	 */
	public static Module get(String moduleName) throws ModuleLoaderException {
		return modules.get(moduleName);
	}

	/**
	 * 
	 * @param module
	 * @param files
	 * @throws ModuleLoaderException
	 */
	public static void load(String module, Runnable loader) throws ModuleLoaderException {
		String MODULE_PATH = "modules/" + module;
		if (!new File(MODULE_PATH).exists())
			throw new ModuleLoaderException(String.format("Module with name =\"%s\" does not exist!", module));
		loader.run();
	}

	private List<StatisticGroup> groupsList = new ArrayList<StatisticGroup>();
	private HashMap<String, StatisticGroup> groups = new HashMap<>();

	public HashMap<String, StatisticGroup> getGroups() {
		return groups;
	}
	public List<StatisticGroup> getGroupsAsList() {
		return groupsList;
	}
	
	public StatisticGroup getGroup(String group) {
		return getGroups().get(group);
	}
	
	public HashMap<String, Statistic> getStatistics(String group) {
		return getGroup(group).getStatistics();
	}

	public Statistic getStatistic(String group, String statistic) {
		return getStatistics(group).get(statistic);
	} 
	
	public Statistic getStatistic(String groupStatistic) {
		String[] path = groupStatistic.split("\\.");
		return getStatistic(path[0], path[1]);
	}

	public void addGroup(String id, StatisticGroup statisticGroup) {
		groupsList.add(statisticGroup);
		getGroups().put(id, statisticGroup);
	}
}
