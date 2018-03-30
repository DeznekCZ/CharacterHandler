package core;

import cz.deznekcz.util.xml.XMLStepper.Step;

public class Kind extends ModuleEntry<Race, Kind> implements ILoader<Kind> {

	public Kind(Race race, String id, String name) {
		super(race, id, name);
		race.addKind(id, this);
	}

	@Override
	public void loadBuild(Module module, Step node) {
		// TODO Auto-generated method stub
		
	}
}
