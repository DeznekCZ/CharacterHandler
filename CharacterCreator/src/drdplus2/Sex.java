package drdplus2;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;

public enum Sex {
	MALE   ("muž" ),
	FEMALE ("žena"),
	UNDEF  ("jiné");
	
	private String sName;
	
	private Sex(String sName) {
		this.sName = sName;
	}

	static ObjectProperty<Sex> sex;
	static ObservableList<Sex> list;
	static StringConverter<Sex> converter;
	
	static void INIT () {
		list = FXCollections.observableArrayList();
		sex = new SimpleObjectProperty<Sex>(Sex.MALE);
		sex.addListener(Race.raceOrSexChange);
		converter = new StringConverter<Sex>() {
			@Override
			public Sex fromString(String string) {
				for (Sex race : Sex.values()) {
					if (race.sName.equals(string));
				}
				return Sex.MALE;
			}
			@Override
			public String toString(Sex object) {
				return object.sName;
			}
		};
	}

	public static StringConverter<Sex> getConverter() {
		return converter;
	}
}
