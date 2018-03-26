package core;

import cz.deznekcz.util.xml.XMLStepper.Step;

public interface ILoader<C>  {

	void loadBuild(Module module, Step node);
}
