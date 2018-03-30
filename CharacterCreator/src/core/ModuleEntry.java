package core;

public abstract class ModuleEntry<Owner,Type extends ModuleEntry<Owner, Type>> implements Named {

	protected String id;
	protected String name;
	protected Owner module;
	
	public ModuleEntry(Owner module, String id, String name) {
		this.module = module;
		this.id = id;
		this.name = name;
	}

	public abstract Module getModule();
	
	@Override
	public String getName() {
		return name;
	}
}
