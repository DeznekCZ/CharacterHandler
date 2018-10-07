package drdplus2;

import java.awt.GridLayout;

import core.Statistic;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HealthControl extends Control {

	private HBox line0;
	private HBox line1;
	private HBox line2;
	private HBox line3;

	private VBox lines;

	private BooleanProperty activeZeroLine;
	private IntegerProperty leftHealth;

	public class HealthSkin implements Skin<HealthControl> {
		
		public HealthSkin() {
			line0 = new HBox();
			line1 = new HBox();
			line2 = new HBox();
			line3 = new HBox();
			
			lines = new VBox(line1, line2, line3);
			
			activeZeroLine.addListener((o,l,n) -> {
				if (n) lines.getChildren().add(0, line0);
				else   lines.getChildren().remove(line0);
			});
		}
		
		@Override
		public HealthControl getSkinnable() {
			return HealthControl.this;
		}

		@Override
		public Node getNode() {
			return lines;
		}

		@Override
		public void dispose() {
			
		}

	}
	public HealthControl(Statistic healthValue) {
		activeZeroLine = new SimpleBooleanProperty(false);
		
		setSkin(new HealthSkin());
	}
	
	public void setActiveZeroLine(boolean value) {
		activeZeroLineProperty().set(value);
	}
	
	public boolean isActiveZeroLine() {
		return activeZeroLineProperty().get();
	}

	private BooleanProperty activeZeroLineProperty() {
		return activeZeroLine;
	}

	public void removeLeftHealth(int value) {
		leftHealthProperty().set(getLeftHealth() - value);
	}

	public void addLeftHealth(int value) {
		leftHealthProperty().set(getLeftHealth() + value);
	}

	public void setLeftHealth(int value) {
		leftHealthProperty().set(value);
	}
	
	public int getLeftHealth() {
		return leftHealthProperty().get();
	}

	private IntegerProperty leftHealthProperty() {
		return leftHealth;
	}
}
