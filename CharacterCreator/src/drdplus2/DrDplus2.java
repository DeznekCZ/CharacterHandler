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
	
	Statistic sila = Statistic.init("s�la", Race.sila);
	Statistic obratnost = Statistic.init("obratnost", Race.obratnost);
	Statistic zrucnost = Statistic.init("zru�nost", Race.zrucnost);
	Statistic vule = Statistic.init("v�le", Race.vule);
	Statistic inteligence = Statistic.init("inteligence", Race.inteligence);
	Statistic charisma = Statistic.init("charisma", Race.charisma);
	
	Statistic odolnost = Statistic.init("odolnost", sila, Race.odolnost)
			.description("Ur�uje schopnost odol�n� v��i jed�m."
					+ "\n= s�la");
	Statistic kondice = Statistic.init(5, odolnost, vule).maximal("kondice", 10)
			.description("Ur�uje velikost zdrav� a �navy, sni�uje ji no�en� zbroj�."
					+ "\n= odolnost + v�le + 5 (minimum 10)");
	Statistic moc = Statistic.init("moc", vule)
			.description("Ur�uje maxim�ln� velikost kouzel, kterou m��e kouzl�c� postava seslat."
					+ "\n= v�le");
	Statistic smysly = Statistic.init("smysly", zrucnost, Race.smysly)
			.description("Pou��v� se pro hled�n� a rychl� reakce na okoln� prost�ed�."
					+ "\n= zru�nost");

	Statistic vzhled_nebezpecnost = Statistic.init("nebezpe�nost", 
			Statistic.init(sila, vule).divideDown(2), charisma.divideDown(2)
			)
			.description("Tento bonus ur�uje jak postava vypad� nebezpe�n�."
					+ "\n= (sila + v�le) / 2 + charisma / 2");
	Statistic vzhled_krasa = Statistic.init("kr�sa", 
			Statistic.init(zrucnost, obratnost).divideDown(2), charisma.divideDown(2)
			)
			.description("Tento bonus ur�uje jak postava vypad� kr�sn�."
					+ "\n= (zru�nost + obratnost) / 2 + charisma / 2");
	Statistic vzhled_dustojnost = Statistic.init("d�stojnost", 
			Statistic.init(vule, inteligence).divideDown(2), charisma.divideDown(2)
			)
			.description("Tento bonus ur�uje jak postava vypad� d�stojn�."
					+ "\n= (v�le + inteligence) / 2 + charisma / 2");

	Statistic iniciativa_ftf = Statistic.init("boj z bl�zka", obratnost, Race.iniciativa)
			.description("Tento bonus se p�i��t� k d�lce zbran� pro boj zbl�zka."
					+ "\n= obratnost");
	Statistic iniciativa_ranged = Statistic.init("st�elba", Statistic.init(obratnost, zrucnost).divideDown(2), Race.iniciativa)
			.description("Tento bonus ur�uje iniciativu v p��pad� st�elby �i vrhu."
					+ "\n= (obratnost + zru�nost) / 2 (zaokrouhleno dolu)");
	Statistic iniciativa_magic = Statistic.init("ses�l�n�", Statistic.init(obratnost, inteligence).divideDown(2), Race.iniciativa)
			.description("Tento bonus ur�uje iniciativu v p��pad� ses�l�n� kouzel."
					+ "\n= (obratnost + inteligence) / 2 (zaokrouhleno dolu)");

	Statistic boj_ftf = obratnost.divideDown("boj z bl�zka", 2)
			.description("Tento bonus se p�i��t� k �to�nosti zbran� pro boj zbl�zka."
					+ "\n= obratnost / 2 (zaokrouhleno dolu)");
	Statistic boj_ranged = obratnost.divideDown("st�elba", 2)
			.description("Tento bonus se p�i��t� k �to�nosti zbran� pro st�elnu nebo vrh."
					+ "\n= zru�nost / 2 (zaokrouhleno dolu)");
	Statistic boj_def = Statistic.init("obrana", 5, obratnost.divideUp("/2", 2))
			.description("Tento bonus se ur�uje pasivn� obranu, p�i��t� se k obran� zbran�."
					+ "\n= obratnost / 2 (zaokrouhleno nahoru)");
	Statistic boj_magic = Statistic.init("ses�l�n�", inteligence)
			.description("Ur�uje velikost bonusu pro ses�l�n� kouzel."
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
		
		groupStats(primalStats, "Hlavn� vlastnosti", sila, obratnost, zrucnost, vule, inteligence, charisma);
		groupStats(primalStats, "Odvozen� vlastnosti", odolnost, kondice, moc, smysly);
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
		characterHistory.setText("Rodn� m�sto: Uthork"
				+ "\nRodina runov�ch kov���: De Tureda"
				+ "\nP��stup do:"
				+ "\n- hlavn� m�sto"
				+ "\n- Holub� vrch (runov� tvrz, banda kopi�)");
	}
}
