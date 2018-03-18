package core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import core.javafx.Binder;
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
import javafx.util.converter.IntegerStringConverter;

public class ModuleLoader<cRace, cSex> {

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
	protected GridPane primalStats;
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
	protected ChoiceBox<cRace> raceChoice;

	@FXML
	protected ChoiceBox<cSex> sexChoice;
	
	public void initialize(URL location, ResourceBundle resources) {
		characterLevel = new SimpleIntegerProperty(-1);
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

	protected void groupStats(GridPane root, String name, Statistic...statistics) {
		GridPane grid = new GridPane();
		TitledPane pane = new TitledPane(name, grid);
		GridPane.setColumnSpan(pane, 2);
		primalStats.addRow(statCounter++, pane);
		for (int i = 0; i < statistics.length; i++) {
			stat(grid, statistics[i], i);
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

}
