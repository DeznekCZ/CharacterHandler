package core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.acl.LastOwnerException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.ResourceBundle;

import org.w3c.dom.Node;

import core.javafx.Binder;
import cz.deznekcz.util.ITryDo;
import cz.deznekcz.util.xml.XMLLoader;
import cz.deznekcz.util.xml.XMLStepper;
import cz.deznekcz.util.xml.XMLStepper.Step;
import cz.deznekcz.util.xml.XMLStepper.StepDocument;
import cz.deznekcz.util.xml.XMLStepper.StepList;
import drdplus2.SkillEntry;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

public abstract class ModuleLoader {

	public static Scene load (String character){
		Parent root =  new BorderPane();
		try { // , ResourceBundle.getBundle(name)
			StepDocument doc = XMLStepper.fromFile("characters/"+character+".xml");
			String moduleName = doc.getNode("module").attribute("id");
			
			FXMLLoader loader = new FXMLLoader();
			root = loader.load(new File("modules/"+moduleName+"/window.fxml").toURI().toURL().openStream());
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Scene(root);
	}

	@FXML
	protected VBox healthBar;

	@FXML
	protected Accordion primalStats;
	protected int statCounter = 1; 
	
	@FXML
	protected TextField characterName;
	
	@FXML
	protected TextField characterLevelText;
	
	protected IntegerProperty characterLevel;
	
	@FXML
	protected Label characterNameLabel;
	
	@FXML
	protected TextArea characterHistory;
	
	@FXML
	protected TextArea characterNotes;
	
	@FXML
	protected ChoiceBox<Race> raceChoice;

	@FXML
	protected ChoiceBox<Kind> kindChoice;

	@FXML
	protected Accordion characterSkills;

	@FXML
	protected CheckBox editSkills;
	
	@FXML
	protected Button addSkills;

	protected Module module;
	
	public void initialize(URL location, ResourceBundle resources) {
		characterLevel = new SimpleIntegerProperty(0);
		Binder.bidirectional(
				characterLevel,
				characterLevelText.textProperty(), 
				Number::toString, 
				Integer::parseInt);
		characterLevel.setValue(0);
		
		characterNameLabel.textProperty().bind(
				characterName
				.textProperty()
				.concat("\n")
				.concat(characterLevelText.textProperty())
				);
		
		addSkills.disableProperty().bind(editSkills.selectedProperty().not());
	}

	protected void groupStats(StatisticGroup statGroup) {
		GridPane grid = new GridPane();
		TitledPane pane = new TitledPane(statGroup.getName(), grid);
		primalStats.getPanes().add(pane);
		int i = 0;
		for (Statistic statistic : statGroup.getStatisticsAsList()) {
			stat(grid, statistic, i++);
		}
	}

	protected void stat(GridPane pane, Statistic statistic, int index) {
		Label name = new Label(statistic.getName());
		name.setMaxWidth(Double.MAX_VALUE);
		GridPane.setHgrow(name, Priority.ALWAYS);
		Label value = new Label();
		value.setTextAlignment(TextAlignment.RIGHT);
		value.setAlignment(Pos.CENTER_RIGHT);
		value.setPrefWidth(20);
		value.textProperty().bind(statistic.getValue().asString());
		pane.addRow(index, name, value);
		if (statistic.getDescription() != null)
		{
			Tooltip tooltip = new Tooltip(statistic.getDescription());
			name.setTooltip(tooltip);
			value.setTooltip(tooltip);
		}
	}

	protected void health(int lines, Statistic value) {
		// TODO
	}
	
	@FXML
	protected void addLevel(ActionEvent event)
	{
		characterLevel.setValue(characterLevel.getValue() + 1);
	}

	@FXML
	protected void removeLevel(ActionEvent event)
	{
		characterLevel.setValue(characterLevel.getValue() - 1);
	}

	public static void loadStatistics(StepDocument stepDocument, Module module, Queue<Pair<ILoader<?>, Step>> lateConstruct) {
		StepList stepList = stepDocument.getList("module/stats/stats_group");
		stepList.foreach(XMLStepper::hasAttribute, (groupStep) -> {
			StatisticGroup group = new StatisticGroup(module, groupStep.attribute("id"), groupStep.attribute("name"));
			
			groupStep.getList("stat").foreach(XMLStepper::hasAttribute, (statStep) -> {
				Statistic stat = new Statistic(group, statStep.attribute("id"), statStep.attribute("name"));
				stat.description(statStep.attribute("desc"));
				lateConstruct.add(new Pair<ILoader<?>, XMLStepper.Step>(stat, statStep));
			});
		});
	}

	public static void loadRaces(StepDocument stepDocument, Module module, Queue<Pair<ILoader<?>, Step>> lateConstruct) {
		StepList stepList = stepDocument.getList("module/races/race");
		stepList.foreach(XMLStepper::hasAttribute, (raceStep) -> {
			Race group = new Race(module, raceStep.attribute("id"), raceStep.attribute("name"));
			
			raceStep.getList("kind").foreach(XMLStepper::hasAttribute, (kindStep) -> {
				Kind kind = new Kind(group, kindStep.attribute("id"), kindStep.attribute("name"));
				lateConstruct.add(new Pair<ILoader<?>, XMLStepper.Step>(kind, kindStep));
			});
		});
	}

	public static void loadSkills(StepDocument stepDocument, Module module, Queue<Pair<ILoader<?>, Step>> lateConstruct) {
		StepList stepList = stepDocument.getList("module/skills/skills_group");
		stepList.foreach(XMLStepper::hasAttribute, (groupStep) -> {
			SkillGroup group = new SkillGroup(module, groupStep.attribute("id"), groupStep.attribute("name"));
			
			groupStep.getList("skill").foreach(XMLStepper::hasAttribute, (skillStep) -> {
				Skill skill = new Skill(group, skillStep.attribute("id"), skillStep.attribute("name"));
				skill.setDescription(skillStep.attribute("desc"));
				lateConstruct.add(new Pair<ILoader<?>, XMLStepper.Step>(skill, skillStep));
			});
		});
	}

	protected void loadData() {
		Queue<Pair<ILoader<?>, Step>> lateConstruct = new LinkedList<Pair<ILoader<?>, Step>>();
		
		Exception e = ITryDo.checkValue(() -> {
			Node root;
			StepDocument stepDocument;
			
			// STATS
			root = XMLLoader.load(new File("modules/" + module.getName() + "/stats.xml"));
			stepDocument = XMLStepper.from(root.getOwnerDocument());

			ModuleLoader.loadStatistics(stepDocument, module, lateConstruct);

			// RACES
			root = XMLLoader.load(new File("modules/" + module.getName() + "/races.xml"));
			stepDocument = XMLStepper.from(root.getOwnerDocument());

			ModuleLoader.loadRaces(stepDocument, module, lateConstruct);
			
			// SKILLS
			root = XMLLoader.load(new File("modules/" + module.getName() + "/skills.xml"));
			stepDocument = XMLStepper.from(root.getOwnerDocument());

			ModuleLoader.loadSkills(stepDocument, module, lateConstruct);
		
			// BINDING
			for (Pair<ILoader<?>, Step> iLoader : lateConstruct) {
				iLoader.getKey().loadBuild(module, iLoader.getValue());
			}
		});
		if (e != null) {
			throw new ModuleLoaderException(e, module.getName());
		}
		kindChoice.getSelectionModel().selectedItemProperty().addListener((o, l, n) -> {
			if (l != null) l.setSelected(false);
			if (n != null) n.setSelected(true);
		});
		kindChoice.setConverter(Kind.converter());
		raceChoice.setItems(FXCollections.observableArrayList(module.getRacesAsList()));
		raceChoice.getSelectionModel().selectedItemProperty().addListener((o, l, n) -> {
			kindChoice.getSelectionModel().clearSelection();
			kindChoice.setItems(FXCollections.observableArrayList(n.getKindsAsList()));
			kindChoice.getSelectionModel().select(0);
		});
		raceChoice.setConverter(Race.converter());
		raceChoice.getSelectionModel().select(0);
		
		skillStage = new Stage();
		skillStage.initModality(Modality.APPLICATION_MODAL);
		skillStage.setMinHeight(300);
		skillStage.setMinWidth(300);
		
		list = new Accordion();
		module.getSkillGroupsAsList().stream().sorted(SkillGroup::compare).forEach((skillGroup) -> {
			Accordion groupList = new Accordion();
			TitledPane group = new TitledPane(skillGroup.getName(), new ScrollPane(groupList));
			list.getPanes().add(group);
			skillGroup.getSkillsAsList().stream().sorted(Skill::compare).forEach((skill) -> {
				groupList.getPanes().add(SkillEntry.listEntry(skill, ableAdd, characterSkills.getPanes(), editSkills.selectedProperty()));
			});
		});
		
		skillStage.setScene(new Scene(list));
	}

	protected BooleanProperty ableAdd;

	private Accordion list;

	private Stage skillStage;
	
	@FXML
	private void addSkills(ActionEvent e) {
		skillStage.showAndWait();
	}

	protected abstract String getName();
}
