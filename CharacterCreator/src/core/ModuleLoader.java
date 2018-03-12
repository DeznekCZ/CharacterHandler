package core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.util.BuilderFactory;

public class ModuleLoader {

	protected static Scene load (String name){
		Parent root =  new BorderPane();
		try { // , ResourceBundle.getBundle(name)
			FXMLLoader loader = new FXMLLoader();
			root = loader.load(new File("modules/"+name+".fxml").toURI().toURL().openStream());
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new Scene(root);
	}
}
