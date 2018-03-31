package core;

import java.util.HashMap;

import cz.deznekcz.util.xml.XMLStepper.Step;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.StringConverter;

public class Kind extends ModuleEntry<Race, Kind> implements ILoader<Kind> {

	public HashMap<String, Statistic> statBonuses;
	private BooleanProperty selected;
	private String path;
	
	public Kind(Race race, String id, String name) {
		super(race, id, name);
		race.addKind(id, this);
		selected = new SimpleBooleanProperty(false);
		this.path = race.id + "." + id;
	}

	@Override
	public void loadBuild(Module module, Step node) {
		if (node.hasElement("bonus"))
		{
			for (Step bonusStep : node.getList("bonus").asStepList()) {
				Statistic bonusStat = Statistic
						.constant(bonusStep.attribute("increase", Integer::parseInt))
						.predicted(selected);
				getModule().getStatistic(bonusStep.attribute("ref")).addIncrement(bonusStat);
			}
		}
	}

	@Override
	public Module getModule() {
		return module.getModule();
	}
	
	public String getPath() {
		return path;
	}
	
	public void setSelected(boolean selected) {
		this.selected.set(selected);;
	}
	
	public boolean getSelected() {
		return this.selected.get();
	}
	
	public static StringConverter<Kind> converter()
	{
		return new StringConverter<Kind>() {

			@Override
			public String toString(Kind object) {
				return object.getName();
			}

			@Override
			public Kind fromString(String string) {
				return null;
			}
		};
	}
}
