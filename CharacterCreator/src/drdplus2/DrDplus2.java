package drdplus2;

import java.net.URL;
import java.util.ResourceBundle;

import core.Module;
import core.ModuleLoader;
import core.Statistic;
import javafx.fxml.Initializable;
import javafx.scene.Scene;

public class DrDplus2 extends ModuleLoader implements Initializable {

	

	public static DrDplus2 INSTANCE;
	
	public DrDplus2() {
		INSTANCE = this;
	}
	
	private static final String MODULE = "DrD+2";
	
	public static Scene load() {
		return load(MODULE);
	}
	
	static {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		// Character definition

		Module module = new Module("DrD+2");
		
		loadData(module);
		
		groupStats(module.getGroup("main"));
		groupStats(module.getGroup("ext"));
		groupStats(module.getGroup("vzl"));
		groupStats(module.getGroup("inc"));
		groupStats(module.getGroup("boj"));
		
		primalStats.setExpandedPane(primalStats.getPanes().get(0));
		
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
