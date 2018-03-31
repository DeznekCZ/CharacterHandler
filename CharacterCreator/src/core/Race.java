package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.StringConverter;

public class Race extends ModuleEntry<Module, Race> {

	private List<Kind> kindsList = new ArrayList<>();
	private HashMap<String, Kind> kinds = new ModuleHashMap<Race, Kind>(this);

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

	@Override
	public Module getModule() {
		return module;
	}
	
	public static StringConverter<Race> converter()
	{
		return new StringConverter<Race>() {

			@Override
			public String toString(Race object) {
				return object.getName();
			}

			@Override
			public Race fromString(String string) {
				return null;
			}
		};
	}
}
