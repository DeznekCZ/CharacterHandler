package core;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SkillGroup extends ModuleEntry<Module,SkillGroup> implements Comparable<SkillGroup> {

	public static final SkillGroup GLOBAL = new SkillGroup(Module.GLOBAL, "GLOBAL_GROUP", "Generated Skills");
	
	
	private List<Skill> list = new ArrayList<Skill>();
	private HashMap<String, Skill> Skills = new ModuleHashMap<SkillGroup, Skill>(this);

	public SkillGroup(Module module, String id, String name) {
		super(module, id, name);
		module.addSkillGroup(id, this);
	}

	public String getName() {
		return name;
	}

	public HashMap<String, Skill> getSkills() {
		return Skills;
	}

	public Skill getSkill(String group, String Skill) {
		return getSkills().get(Skill);
	} 
	
	public Skill getSkill(String groupSkill) {
		String[] path = groupSkill.split("\\.");
		return getSkill(path[0], path[1]);
	}

	public List<Skill> getSkillsAsList() {
		return list;
	}

	public void addSkill(String id, Skill skill) {
		list.add(skill);
		getSkills().put(id, skill);
	}

	@Override
	public Module getModule() {
		return module;
	}

	@Override
	public int compareTo(SkillGroup o) {
		return Collator.getInstance().compare(name, o.name);
	}

	public static int compare(SkillGroup o1, SkillGroup o2)
	{
		return o1.compareTo(o2);
	}
}
