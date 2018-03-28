package drdplus2;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

import core.ILoader;
import core.Module;
import core.ModuleLoader;
import core.ModuleLoaderException;
import core.Statistic;
import core.StatisticGroup;
import cz.deznekcz.util.ITryDo;
import cz.deznekcz.util.xml.XMLLoader;
import cz.deznekcz.util.xml.XMLStepper;
import cz.deznekcz.util.xml.XMLStepper.Step;
import cz.deznekcz.util.xml.XMLStepper.StepDocument;
import cz.deznekcz.util.xml.XMLStepper.StepList;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.util.Pair;

public class DrDplus2 extends ModuleLoader<Race, Sex> implements Initializable {

	

	public static DrDplus2 INSTANCE;
	
	public DrDplus2() {
		INSTANCE = this;
	}
	
	private static final String MODULE = "DrD+2";
	
	public static Scene load() {
		return load(MODULE);
	}
	
	static {
		Race.INIT();
		Sex.INIT();
		Skill.INIT();
	}
	
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
		
		// Character definition

		Module module = new Module("DrD+2");
		Queue<Pair<ILoader<?>, Step>> lateConstruct = new LinkedList<Pair<ILoader<?>, Step>>();
		
		Exception e = ITryDo.checkValue(() -> {
			for (String file : Arrays.asList("stats")) {
				Node root = XMLLoader.load(new File("modules/" + MODULE + "/" + file + ".xml"));
				StepDocument stepDocument = XMLStepper.from(root.getOwnerDocument());

				ModuleLoader.loadStatistics(stepDocument, module, lateConstruct);
			}
		
			for (Pair<ILoader<?>, Step> iLoader : lateConstruct) {
				iLoader.getKey().loadBuild(module, iLoader.getValue());
			}
		});
		if (e != null) {
			throw new ModuleLoaderException(String.format("Module with name =\"%s\" has issues!\n%s", MODULE, e.getLocalizedMessage()));
		}
		
		groupStats(primalStats, module.getGroup("main"));
		groupStats(primalStats, module.getGroup("ext"));
		groupStats(primalStats, module.getGroup("vzl"));
		groupStats(primalStats, module.getGroup("inc"));
		groupStats(primalStats, module.getGroup("boj"));
		
		health(3, Statistic.table1(Tables::LIFE, module.getStatistic("ext.kon")));
		
		
		
		module.getStatistic("main.sil").addIncrement(1);
		module.getStatistic("main.obr").addIncrement(1);
		module.getStatistic("main.vol").addIncrement(1);
		module.getStatistic("main.int").addIncrement(1);
		
		// from skills
		module.getStatistic("main.sil").addIncrement(3);
		module.getStatistic("ext.moc").addIncrement(3);

		characterName.setText("Torwald de Tureda");
		characterHistory.setText("Rodné mìsto: Uthork"
				+ "\nRodina runových kováøù: De Tureda"
				+ "\nPøístup do:"
				+ "\n- hlavní mìsto"
				+ "\n- Holubí vrch (runová tvrz, banda kopií)");
	}
}
