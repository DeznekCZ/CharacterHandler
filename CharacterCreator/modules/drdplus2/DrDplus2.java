package drdplus2;

import java.net.URL;
import java.util.ResourceBundle;

import core.Module;
import core.ModuleLoader;
import core.Statistic;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DrDplus2 extends ModuleLoader implements Initializable {
	
	public DrDplus2() {
		
	}
	
	private static final String MODULE = "DrD+2";
	
	@Override
	protected String getName() { return MODULE; }
	
	static {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		ableAdd = new SimpleBooleanProperty(false);
		
		// Character definition

		module = new Module(MODULE);
		
		loadData();
		
		groupStats(module.getStatGroup("main"));
		groupStats(module.getStatGroup("ext"));
		groupStats(module.getStatGroup("vzl"));
		groupStats(module.getStatGroup("inc"));
		groupStats(module.getStatGroup("boj"));
		
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
//		module.getStatistic("main.sil").addIncrement(+3);
//		module.getStatistic("ext.moc" ).addIncrement(+3);
		
		// skills
		module.getSkill("stat.sil").level(3);
		module.getSkill("stat.moc").level(3);
		
		characterSkills.getPanes().sort(SkillEntry::compare);
		
		ableAdd.bind(module.getStatistic("skills.known").getValue()
					.add(module.getStatistic("skills.available").getValue()).greaterThan(0));
		
		module.getStatistic("skills.available").getValue().bind(characterLevel.divide(20).multiply(3).add(0));

		characterLevel.setValue(3*20+3);
		
		characterName.setText("Torwald de Tureda");
		characterHistory.setText(
				    "Rodné mìsto: Uthork"
				+ "\nRodina runových kováøù: De Tureda");
		characterNotes.setText(
				    "Pøístup do:"
				+ "\n- hlavní mìsto"
				+ "\n- Holubí vrch (runová tvrz, banda kopií)");
	}
}
