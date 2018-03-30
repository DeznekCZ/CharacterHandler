package core;

import java.util.HashMap;

public class ModuleHashMap<O extends ModuleEntry<?, O>,V extends ModuleEntry<O, V>> extends HashMap<String, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9083146194461568974L;
	private O owner;

	public ModuleHashMap(O owner) {
		this.owner = owner;
	}
	
	@Override
	public V get(Object key) {
		V retV = super.get(key);
		if (retV == null)
			throw new ModuleLoaderException(
				new NullPointerException(String.format("Key: \"%s\" in \"%s.%s\" was not found",
						key, owner.getClass().getSimpleName(), owner.getName() 
						)), 
				owner.getModule().getName()
			);
		return retV;
	}
}
