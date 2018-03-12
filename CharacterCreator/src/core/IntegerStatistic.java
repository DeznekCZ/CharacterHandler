package core;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IntegerStatistic implements InvalidationListener {

	private SimpleIntegerProperty value = new SimpleIntegerProperty();

	public SimpleIntegerProperty getValue() {
		return value;
	}
	
	private ObservableList<IntegerStatistic> bounds = FXCollections.observableArrayList();
	
	void addIncrement(IntegerStatistic stat) {
		if (!bounds.contains(stat)) {
			bounds.add(stat);
			stat.value.addListener(this);
		}
	}
	
	void removeIncrement(IntegerStatistic stat) {
		if (bounds.contains(stat)) {
			bounds.remove(stat);
			stat.value.removeListener(this);
		}
	}

	@Override
	public void invalidated(Observable observable) {
		int temp = 0;
		for (IntegerStatistic integerStatistic : bounds) {
			temp += integerStatistic.value.get();
		}
		this.value.set(temp);
	}
	
	@Override
	protected void finalize() throws Throwable {
		bounds.forEach(this::removeIncrement);
		super.finalize();
	}
}
