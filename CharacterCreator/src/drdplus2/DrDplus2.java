package drdplus2;

import java.net.URL;
import java.util.ResourceBundle;

import core.ModuleLoader;
import core.Statistic;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

public class DrDplus2 extends ModuleLoader<Race, Sex> implements Initializable {

	

	public static DrDplus2 INSTANCE;
	
	public DrDplus2() {
		INSTANCE = this;
	}
	
	public static final String name = "DrD+2";
	
	public static Scene load() {
		return load(name);
	}
	
	static {
		Race.INIT();
		Sex.INIT();
		Skill.INIT();
	}
	
	Statistic sila = Statistic.init("síla", Race.sila);
	Statistic obratnost = Statistic.init("obratnost", Race.obratnost);
	Statistic zrucnost = Statistic.init("zruènost", Race.zrucnost);
	Statistic vule = Statistic.init("vùle", Race.vule);
	Statistic inteligence = Statistic.init("inteligence", Race.inteligence);
	Statistic charisma = Statistic.init("charisma", Race.charisma);
	
	Statistic odolnost = Statistic.init("odolnost", sila, Race.odolnost)
			.description("Urèuje schopnost odolání vùži jedùm."
					+ "\n= síla");
	Statistic kondice = Statistic.init(5, odolnost, vule).maximal("kondice", 10)
			.description("Urèuje velikost zdraví a únavy, snižuje ji nošení zbrojí."
					+ "\n= odolnost + vùle + 5 (minimum 10)");
	Statistic moc = Statistic.init("moc", vule)
			.description("Urèuje maximální velikost kouzel, kterou mùže kouzlící postava seslat."
					+ "\n= vùle");
	Statistic smysly = Statistic.init("smysly", zrucnost, Race.smysly)
			.description("Používá se pro hledání a rychlé reakce na okolní prostøedí."
					+ "\n= zruènost");

	Statistic vzhled_nebezpecnost = Statistic.init("nebezpeènost", 
			Statistic.init(sila, vule).divideDown(2), charisma.divideDown(2)
			)
			.description("Tento bonus urèuje jak postava vypadá nebezpeènì."
					+ "\n= (sila + vùle) / 2 + charisma / 2");
	Statistic vzhled_krasa = Statistic.init("krása", 
			Statistic.init(zrucnost, obratnost).divideDown(2), charisma.divideDown(2)
			)
			.description("Tento bonus urèuje jak postava vypadá krásnì."
					+ "\n= (zruènost + obratnost) / 2 + charisma / 2");
	Statistic vzhled_dustojnost = Statistic.init("dùstojnost", 
			Statistic.init(vule, inteligence).divideDown(2), charisma.divideDown(2)
			)
			.description("Tento bonus urèuje jak postava vypadá dùstojnì."
					+ "\n= (vùle + inteligence) / 2 + charisma / 2");

	Statistic iniciativa_ftf = Statistic.init("boj z blízka", obratnost, Race.iniciativa)
			.description("Tento bonus se pøièítá k délce zbranì pro boj zblízka."
					+ "\n= obratnost");
	Statistic iniciativa_ranged = Statistic.init("støelba", Statistic.init(obratnost, zrucnost).divideDown(2), Race.iniciativa)
			.description("Tento bonus urèuje iniciativu v pøípadì støelby èi vrhu."
					+ "\n= (obratnost + zruènost) / 2 (zaokrouhleno dolu)");
	Statistic iniciativa_magic = Statistic.init("sesílání", Statistic.init(obratnost, inteligence).divideDown(2), Race.iniciativa)
			.description("Tento bonus urèuje iniciativu v pøípadì sesílání kouzel."
					+ "\n= (obratnost + inteligence) / 2 (zaokrouhleno dolu)");

	Statistic boj_ftf = obratnost.divideDown("boj z blízka", 2)
			.description("Tento bonus se pøièítá k útoènosti zbranì pro boj zblízka."
					+ "\n= obratnost / 2 (zaokrouhleno dolu)");
	Statistic boj_ranged = obratnost.divideDown("støelba", 2)
			.description("Tento bonus se pøièítá k útoènosti zbranì pro støelnu nebo vrh."
					+ "\n= zruènost / 2 (zaokrouhleno dolu)");
	Statistic boj_def = Statistic.init("obrana", 5, obratnost.divideUp("/2", 2))
			.description("Tento bonus se urèuje pasivní obranu, pøièítá se k obranì zbraní."
					+ "\n= obratnost / 2 (zaokrouhleno nahoru)");
	Statistic boj_magic = Statistic.init("sesílání", inteligence)
			.description("Urèuje velikost bonusu pro sesílání kouzel."
					+ "\n= inteligence");
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		raceChoice.setConverter(Race.getConverter());
		raceChoice.getItems().addAll(Race.values());
		raceChoice.getSelectionModel().select(Race.HUMAN);
		Race.race.bind(raceChoice.getSelectionModel().selectedItemProperty());
		
		sexChoice.setConverter(Sex.getConverter());
		Sex.list.addListener((Change<? extends Sex> change) -> {
			if (Sex.list.contains(sexChoice.getSelectionModel().getSelectedItem()))
				sexChoice.getItems().setAll(Sex.list);});
		sexChoice.getSelectionModel().select(Sex.MALE);
		Sex.sex.bind(sexChoice.getSelectionModel().selectedItemProperty());
		
		health(3, Life.init(kondice));
		
		// Character definition
		
		groupStats(primalStats, "Hlavní vlastnosti", sila, obratnost, zrucnost, vule, inteligence, charisma);
		groupStats(primalStats, "Odvozené vlastnosti", odolnost, kondice, moc, smysly);
		groupStats(primalStats, "Vzhled", vzhled_krasa, vzhled_dustojnost, vzhled_nebezpecnost);
		groupStats(primalStats, "Iniciativa", iniciativa_ftf, iniciativa_ranged, iniciativa_magic);
		groupStats(primalStats, "Boj", boj_ftf, boj_ranged, boj_def, boj_magic);

		raceChoice.getSelectionModel().select(Race.DWARF);
		sila.addIncrement(1);
		sila.addIncrement(3);
		obratnost.addIncrement(1);
		vule.addIncrement(1);
		inteligence.addIncrement(1);
		moc.addIncrement(3);

		characterName.setText("Torwald de Tureda");
		characterHistory.setText("Rodné mìsto: Uthork"
				+ "\nRodina runových kováøù: De Tureda"
				+ "\nPøístup do:"
				+ "\n- hlavní mìsto"
				+ "\n- Holubí vrch (runová tvrz, banda kopií)");
	}
}
