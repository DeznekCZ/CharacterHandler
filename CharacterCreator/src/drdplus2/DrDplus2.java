package drdplus2;

import java.net.URL;
import java.util.ResourceBundle;

import core.ModuleLoader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class DrDplus2 extends ModuleLoader implements Initializable {

	public static final String name = "DrD+2";
	
	public static Scene load() {
		return load(name);
	}

	@FXML
	private VBox healthBar;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
