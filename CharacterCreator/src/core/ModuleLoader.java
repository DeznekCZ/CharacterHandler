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
import javafx.beans.InvalidationListener;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

public class ModuleLoader {

	protected static Scene load (String name){
		Parent root =  new BorderPane();
		try { // , ResourceBundle.getBundle(name)
			FXMLLoader loader = new FXMLLoader();
			root = loader.load(new File("modules/"+name+"/window.fxml").toURI().toURL().openStream());
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
	protected ChoiceBox<Race> raceChoice;

	@FXML
	protected ChoiceBox<Kind> sexChoice;
	
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
		if (stepList != null)
		{
			stepList.foreach(XMLStepper::hasAttribute, (groupStep) -> {
				StatisticGroup group = new StatisticGroup(module, groupStep.attribute("id"), groupStep.attribute("name"));
				
				groupStep.getList("stat").foreach(XMLStepper::hasAttribute, (statStep) -> {
					Statistic stat = new Statistic(group, statStep.attribute("id"), statStep.attribute("name"));
					stat.description(statStep.attribute("desc"));
					lateConstruct.add(new Pair<ILoader<?>, XMLStepper.Step>(stat, statStep));
				});
			});
		}
	}

	public static void loadRaces(StepDocument stepDocument, Module module, Queue<Pair<ILoader<?>, Step>> lateConstruct) {
		StepList stepList = stepDocument.getList("module/races/race");
		if (stepList != null)
		{
			stepList.foreach(XMLStepper::hasAttribute, (raceStep) -> {
				Race group = new Race(module, raceStep.attribute("id"), raceStep.attribute("name"));
				
				raceStep.getList("kind").foreach(XMLStepper::hasAttribute, (kindStep) -> {
					Kind kind = new Kind(group, kindStep.attribute("id"), kindStep.attribute("name"));
					lateConstruct.add(new Pair<ILoader<?>, XMLStepper.Step>(kind, kindStep));
				});
			});
		}
	}

	protected void loadData(Module module) {
		Queue<Pair<ILoader<?>, Step>> lateConstruct = new LinkedList<Pair<ILoader<?>, Step>>();
		
		Exception e = ITryDo.checkValue(() -> {
			for (String file : Arrays.asList("stats", "races")) {
				Node root = XMLLoader.load(new File("modules/" + module.getName() + "/" + file + ".xml"));
				StepDocument stepDocument = XMLStepper.from(root.getOwnerDocument());

				ModuleLoader.loadStatistics(stepDocument, module, lateConstruct);
				ModuleLoader.loadRaces(stepDocument, module, lateConstruct);
			}
		
			for (Pair<ILoader<?>, Step> iLoader : lateConstruct) {
				iLoader.getKey().loadBuild(module, iLoader.getValue());
			}
		});
		if (e != null) {
			throw new ModuleLoaderException(String.format("Module with name =\"%s\" has issues!\n%s", module.getName(), e.getLocalizedMessage()));
		}
	}
}
