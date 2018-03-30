package core;

public abstract class ModuleEntry<ModuleName,Type extends ModuleEntry<ModuleName, Type>> {

	protected String id;
	protected String name;
	protected ModuleName module;
	
	public ModuleEntry(ModuleName module, String id, String name) {
		this.module = module;
		this.id = id;
		this.name = name;
	}

}
