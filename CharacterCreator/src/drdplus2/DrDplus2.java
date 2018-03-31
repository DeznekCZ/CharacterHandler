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
		
		// from roots
		module.getStatistic("main.sil").addIncrement(+1);
		module.getStatistic("main.obr").addIncrement(+1);
		module.getStatistic("main.vol").addIncrement(+1);
		module.getStatistic("main.int").addIncrement(+1);
		
		// race bonus
		raceChoice.getSelectionModel().select(module.getRace("dwarf"));
		kindChoice.getSelectionModel().select(module.getKind("dwarf.dwarf"));
//		module.getStatistic("main.sil").addIncrement(+1);
//		module.getStatistic("main.obr").addIncrement(-1);
//		module.getStatistic("main.vol").addIncrement(+2);
//		module.getStatistic("main.int").addIncrement(-1);
//		module.getStatistic("main.chr").addIncrement(-2);
//		module.getStatistic("ext.odl" ).addIncrement(+1);
//		module.getStatistic("ext.sms" ).addIncrement(-1);
		
		// from skills
		module.getStatistic("main.sil").addIncrement(+3);
		module.getStatistic("ext.moc" ).addIncrement(+3);
		

		characterName.setText("Torwald de Tureda");
		characterHistory.setText(
				    "Rodn� m�sto: Uthork"
				+ "\nRodina runov�ch kov���: De Tureda");
		characterNotes.setText(
				    "P��stup do:"
				+ "\n- hlavn� m�sto"
				+ "\n- Holub� vrch (runov� tvrz, banda kopi�)");
	}
}
