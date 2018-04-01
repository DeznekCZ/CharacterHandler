package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
			throw new ModuleLoaderException(new FileNotFoundException(MODULE_PATH), module);
		loader.run();
	}

	private List<StatisticGroup> groupsList = new ArrayList<StatisticGroup>();
	private HashMap<String, StatisticGroup> groups = new HashMap<>();
	private List<Race> racesList = new ArrayList<Race>();
	private HashMap<String, Race> races = new HashMap<>();
	private List<SkillGroup> skillGroupList;
	private HashMap<String, SkillGroup> skillGroups = new HashMap<>();

	public HashMap<String, StatisticGroup> getGroups() {
		return groups;
	}
	public List<StatisticGroup> getGroupsAsList() {
		return groupsList;
	}
	
	public StatisticGroup getStatGroup(String group) {
		return getGroups().get(group);
	}
	
	public HashMap<String, Statistic> getStatistics(String group) {
		return getStatGroup(group).getStatistics();
	}

	public Statistic getStatistic(String group, String statistic) {
		return getStatistics(group).get(statistic);
	} 
	
	public Statistic getStatistic(String groupStatistic) {
		String[] path = groupStatistic.split("\\.");
		return getStatistic(path[0], path[1]);
	}

	public void addStatGroup(String id, StatisticGroup statisticGroup) {
		groupsList.add(statisticGroup);
		getGroups().put(id, statisticGroup);
	}

	public String getName() {
		return name;
	}

	public void addRace(String id, Race race) {
		racesList.add(race);
		getRaces().put(id, race);
	}

	public HashMap<String, Race> getRaces() {
		return races;
	}

	public HashMap<String, Kind> getKinds(String race) {
		return getRace(race).getKinds();
	}

	public Kind getKind(String raceKind) {
		String[] path = raceKind.split("\\.");
		return getKind(path[0], path[1]);
	}

	public Kind getKind(String race, String kind) {
		return getRace(race).getKind(kind);
	}

	public Race getRace(String id) {
		return getRaces().get(id);
	}
	
	public List<Race> getRacesAsList() {
		return racesList;
	}
	
	public List<Kind> getKinsAsList(String race) {
		return getRace(race).getKindsAsList();
	}

	public void addSkillGroup(String id, SkillGroup skillGroup) {
		skillGroupList.add(skillGroup);
		skillGroupList.sort(SkillGroup::compare);
		getSkillGroups().put(id, skillGroup);
	}

	private HashMap<String, SkillGroup> getSkillGroups() {
		return skillGroups;
	}

	public SkillGroup getSkillGroup(String group) {
		return getSkillGroups().get(group);
	}

	public Skill getSkill(String group, String skill) {
		return getSkillGroup(group).getSkill(skill);
	}

	public Skill getSkill(String groupSkill) {
		String[] path = groupSkill.split("\\.");
		return getSkill(path[0], path[1]);
	}
}
