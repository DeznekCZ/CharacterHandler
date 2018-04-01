package core;

import java.text.Collator;

import cz.deznekcz.util.xml.XMLStepper.Step;

public class Skill extends ModuleEntry<SkillGroup,Skill> implements ILoader<Skill>, Comparable<Skill> {
	
	private String description;

	public String getName() {
		return name;
	}

	public Skill(SkillGroup group, String id, String name) {
		super(group, id, name);
	}
	
	@Override
	public String toString() {
		return String.format("Skill(%s,%s)", id, name);
	}
	
	@Override
	public void loadBuild(Module module, Step node) {
		
	}


	@Override
	public Module getModule() {
		return module.getModule();
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(Skill o) {
		return Collator.getInstance().compare(name, o.name);
	}

	public static int compare(Skill o1, Skill o2)
	{
		return o1.compareTo(o2);
	}
}
