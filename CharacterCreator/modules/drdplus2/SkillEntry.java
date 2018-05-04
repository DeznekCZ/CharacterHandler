package drdplus2;

import java.util.List;

import core.Module;
import core.Skill;
import cz.deznekcz.reference.OutBoolean;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class SkillEntry extends TitledPane {
	
	private static final double B_SIZE = 30;
	private static final String LEARNED = "Kompletnì nauèeno";

	public static TitledPane listEntry(Skill skill, BooleanProperty ableAdd, List<TitledPane> skills, BooleanProperty edit) {
		return new SkillEntry(skill, ableAdd, skills, edit);
	}

	private Skill skill;
	private TextArea textArea;
	private BooleanExpression disableAdd;
	private HBox editBox;
	private BooleanExpression disableRemove;
	private BooleanExpression always;
	private BooleanExpression maxed;
	private BooleanExpression overZero;
	private Button buttonPlus;
	private Button buttonMinus;
	private Button buttonRemove;
	private TitledPane entry;
	
	public SkillEntry(Skill skill, BooleanProperty ableAdd, BooleanProperty edit) {
		this.skill = skill;
		always = BooleanExpression.booleanExpression(OutBoolean.FALSE());
		maxed = skill.levelProperty().lessThan(skill.getLimit());
		overZero = skill.levelProperty().greaterThan(0);
		disableAdd = ableAdd.not();
		disableRemove = overZero.not();
		
		buttonPlus = buttonPlus();
		buttonMinus = buttonMinus();
		buttonRemove = buttonRemove();
		
		editBox = new HBox(buttonRemove, buttonMinus, buttonPlus);
		editBox.visibleProperty().bind(skill.isHidden() ? OutBoolean.FALSE() : edit);
		
		setGraphic(editBox);
		textProperty().bind(
				Bindings.concat(
						skill.getName(),
						": ",
						IGen.init(skill.levelProperty()),
						skill.shortInfo()
				)
		);
		textArea = new TextArea(skill.getDescription());
		textArea.setEditable(false);
		setContent(textArea);
	}

	public SkillEntry(Skill skill, BooleanProperty ableAdd, List<TitledPane> skills, BooleanProperty edit) {
		this.skill = skill;
		always = BooleanExpression.booleanExpression(OutBoolean.FALSE());
		maxed = skill.levelProperty().lessThan(skill.getLimit());
		overZero = skill.levelProperty().greaterThan(0);
		disableAdd = ableAdd.not();
		disableRemove = overZero.not();
		
		buttonPlus = buttonPlus();
		buttonMinus = buttonMinus();
		
		editBox = new HBox(buttonMinus, buttonPlus);
		editBox.visibleProperty().bind(skill.isHidden() ? OutBoolean.FALSE() : edit);
		
		setGraphic(editBox);
		textProperty().bind(
				Bindings.concat(
						skill.getName(),
						": ",
						IGen.init(skill.levelProperty()),
						skill.shortInfo()
				)
		);
		textArea = new TextArea(skill.getDescription());
		textArea.setEditable(false);
		setContent(textArea);
		
		entry = new SkillEntry(skill, ableAdd, edit);
		overZero.addListener((o,l,n) -> {
			if (n) {
				skills.add(entry);
				skills.sort(SkillEntry::compare);
			} else {
				skills.remove(entry);
			}
		});
	}

	private Button button(String name, EventHandler<ActionEvent> object, BooleanExpression disabler, BooleanExpression maxed) {
		Button b = new Button(name);
		b.setStyle("-fx-font-family: Consolas;");
		b.setOnAction(object);
		b.disableProperty().bind(disabler);
		b.visibleProperty().bind(maxed);
		b.setMaxWidth(B_SIZE);
		b.setMinWidth(B_SIZE);
		b.setMaxHeight(B_SIZE);
		b.setMinHeight(B_SIZE);
		return b;
	}

	private Button buttonPlus() {
		return button("+", (e) -> skill.level(skill.getLevel() + 1), disableAdd, maxed);
	}

	private Button buttonMinus() {
		return button("-", (e) -> skill.level(skill.getLevel() - 1), disableRemove, overZero);
	}

	private Button buttonRemove() {
		return button("x", (e) -> { 
				skill.level(0);
			}
		, always, always.not());
	}

	public static int compare(TitledPane o1, TitledPane o2) {
		return ((SkillEntry) o1).skill.compareTo(((SkillEntry) o2).skill);
	}
}
