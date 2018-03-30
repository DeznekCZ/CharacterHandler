package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Race extends ModuleEntry<Module, Race> {

	private List<Kind> kindsList = new ArrayList<>();
	private HashMap<String, Kind> kinds = new HashMap<>();

	public Race(Module module, String id, String name) {
		super(module, id, name);
		module.addRace(id, this);
	}

	public HashMap<String, Kind> getKinds() {
		return kinds;
	}

	public Kind getKind(String kind) {
		return kinds.get(kind);
	}

	public List<Kind> getKindsAsList() {
		return kindsList;
	}

	public void addKind(String id, Kind kind) {
		kindsList.add(kind);
		kinds.put(id, kind);
	}
	
}
