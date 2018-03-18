package drdplus2;

import core.Statistic;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;

class Attr {
	@SafeVarargs
	public static <L> L[] v(L...value) {
		return value;
	}
}

public enum Race {
	/*name, sil, obr, zrc, vol, int, chr, odl, sms, boj, rychlost*/
	HUMAN       ("èlovìk"         ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 0, 0, 0, 0, 0, 0, 0, 0, 0, 0), Attr.v(-1,0,0,0,0,1,0,0,0,0)),
	MOUNTAINER  ("horal"          ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 1, 0, 0, 1,-1,-1, 0, 0, 0, 0), Attr.v(-1,0,0,0,0,1,0,0,0,0)),
	ELVEN       ("elf"            ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v(-1, 1, 1,-2, 1, 1,-1, 0, 0, 0), Attr.v(-1,0,1,0,-1,1,0,0,0,0)),
	GREEN_ELVEN ("zelený elf"     ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v(-1, 1, 0,-1, 1, 1,-1, 0, 0, 0), Attr.v(-1,0,1,0,-1,1,0,0,0,0)),
	DARK_ELVEN  ("temný elf"      ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 0, 0, 0, 0, 1, 0,-1, 0, 0, 0), Attr.v(-1,0,1,0,-1,1,0,0,0,0)),
	DWARF       ("trpaslík"       ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 1,-1, 0, 2,-1,-2, 1,-1, 0, 0), Attr.v(0,0,-1,0,1,0,0,0,0,0)),
	MOUNT_DWARF ("horský trpaslík",
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 2,-1, 0, 2,-2,-2, 1,-1, 0, 0), Attr.v(-1,0,0,0,0,1,0,0,0,0)),
	HOBBIT      ("hobit"          ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v(-3, 1, 1, 0,-1, 2, 0, 0, 0,-1), Attr.v(-1,0,0,0,0,1,0,0,0,0)),
	KROL        ("krol"           ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 3,-2,-1, 1,-3,-1, 0, 0, 1, 1)),
	WILD_KROL   ("krol divoký"    ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 3,-1,-2, 2,-3,-2, 0, 0, 1, 1)), //boj a rychlost +1
	ORC         ("skøet"          ,
			Attr.v(Sex.MALE, Sex.FEMALE),
			Attr.v( 0, 2, 0,-1, 0,-2, 0, 1, 0, 0), Attr.v(-1,0,0,1,0,0,0,0,0,0)),
	SCURUT      ("skurut"         ,
			Attr.v(Sex.UNDEF),
			Attr.v( 1, 1,-1, 0, 0,-2, 0, 1, 0, 0)),
	GOBLIN      ("goblin"         ,
			Attr.v(Sex.UNDEF),
			Attr.v(-1, 2, 1,-2, 0,-1, 0, 1, 0, 0));
	
	private String rName;
	private Integer[] attributes;
	private Sex[] sexList;
	private Integer[] femaleFix;

	private Race(String name, Sex[] sexList, Integer[] attributes) {
		this(name, sexList, attributes, null);
	}

	private Race(String name, Sex[] sexList, Integer[] attributes, Integer[] femaleFix) {
		this.rName = name;
		this.sexList = sexList;
		this.attributes = attributes;
		this.femaleFix = femaleFix;
	}

	static Statistic sila = Statistic.init("síla");
	static Statistic obratnost = Statistic.init("obratnost");
	static Statistic zrucnost = Statistic.init("zruènost");
	static Statistic vule = Statistic.init("vùle");
	static Statistic inteligence = Statistic.init("inteligence");
	static Statistic charisma = Statistic.init("charisma");

	static Statistic odolnost = Statistic.init("odolnost");
	static Statistic smysly = Statistic.init("smysly");
	static Statistic iniciativa = Statistic.init("iniciativa");
	static Statistic rychlost = Statistic.init("rychlost");

	static ObjectProperty<Race> race;

	private static StringConverter<Race> converter;

	static InvalidationListener raceOrSexChange = (observable) -> {
		Race n = Race.race.get();
		Sex.list.setAll(n.sexList);
		if (n.femaleFix != null && Sex.FEMALE == Sex.sex.get())
		{
			sila.getValue().set(n.attributes[0] + n.femaleFix[0]);
			obratnost.getValue().set(n.attributes[1] + n.femaleFix[1]);
			zrucnost.getValue().set(n.attributes[2] + n.femaleFix[2]);
			vule.getValue().set(n.attributes[3] + n.femaleFix[3]);
			inteligence.getValue().set(n.attributes[4] + n.femaleFix[4]);
			charisma.getValue().set(n.attributes[5] + n.femaleFix[5]);
			
			odolnost.getValue().set(n.attributes[6] + n.femaleFix[6]);
			smysly.getValue().set(n.attributes[7] + n.femaleFix[7]);
			iniciativa.getValue().set(n.attributes[8] + n.femaleFix[8]);
			rychlost.getValue().set(n.attributes[9] + n.femaleFix[9]);
		}
		else
		{
			sila.getValue().set(n.attributes[0]);
			obratnost.getValue().set(n.attributes[1]);
			zrucnost.getValue().set(n.attributes[2]);
			vule.getValue().set(n.attributes[3]);
			inteligence.getValue().set(n.attributes[4]);
			charisma.getValue().set(n.attributes[5]);
			
			odolnost.getValue().set(n.attributes[6]);
			smysly.getValue().set(n.attributes[7]);
			iniciativa.getValue().set(n.attributes[8]);
			rychlost.getValue().set(n.attributes[9]);
		}
	};
	
	static void INIT () {
		race = new SimpleObjectProperty<Race>(Race.HUMAN);
		race.addListener(raceOrSexChange);
		converter = new StringConverter<Race>() {
			@Override
			public Race fromString(String string) {
				for (Race race : Race.values()) {
					if (race.rName.equals(string));
				}
				return Race.HUMAN;
			}
			@Override
			public String toString(Race object) {
				return object.rName;
			}
		};
	}
	
	public static StringConverter<Race> getConverter() {
		return converter;
	}
}
