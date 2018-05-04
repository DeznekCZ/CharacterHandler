package core;

import java.text.Collator;

import cz.deznekcz.util.xml.XMLStepper.Step;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Skill extends ModuleEntry<SkillGroup,Skill> implements ILoader<Skill>, Comparable<Skill> {
	
	private String description;
	private IntegerProperty level;
	private StringProperty shortInfo;
	private int limit;
	private boolean hidden;

	public String getName() {
		return name;
	}

	public Skill(SkillGroup group, String id, String name) {
		super(group, id, name);
		group.addSkill(id, this);
		level = new SimpleIntegerProperty(0);
		shortInfo = new SimpleStringProperty("");
	}
	
	@Override
	public String toString() {
		return String.format("Skill(%s,%s,%d)", id, name, level.intValue());
	}
	
	@Override
	public void loadBuild(Module module, Step node) {
		limit = node.hasAttribute("limit") ? Integer.parseInt(node.attribute("limit")) : 3;
		hidden = node.hasAttribute("hidden") ? Boolean.parseBoolean(node.attribute("hidden")) : false;
		
		if (node.hasElement("bonus"))
		{
			for (Step bonusStep : node.getList("bonus").asList()) {
				Statistic bonusStat = Statistic
						.init(bonusStep.attribute("increase", Integer::parseInt))
						.multiplied(level);
				
				getModule().getStatistic(bonusStep.attribute("ref")).addIncrement(bonusStat);
			}
		}
		
		// TODO
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

	public Skill level(int i) {
		this.level.setValue(Math.max(0, Math.min(i, getLimit())));
		return this;
	}
	
	public int getLimit() {
		return limit; // TODO default 3
	}

	public int getLevel() {
		return level.getValue();
	}

	public IntegerProperty levelProperty() {
		return level;
	}

	public StringProperty shortInfo() {
		return shortInfo;
	}

	public boolean isHidden() {
		return hidden;
	}
}
