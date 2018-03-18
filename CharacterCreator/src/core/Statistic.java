package core;

import java.util.List;
import java.util.Properties;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class Statistic implements InvalidationListener {


	private ObservableList<Statistic> sumBounds = FXCollections.observableArrayList();


	private SimpleStringProperty name = new SimpleStringProperty();
	
	public String getName() {
		return name.getValue();
	}

	private SimpleIntegerProperty value = new SimpleIntegerProperty();
	private StringBinding valueAsString;
	
	protected Statistic(String name) {
		this.name.set(name);
		value.set(0);
		sumBounds.addListener(this);
	}


	private boolean dividedUp = false;
	private int divider = 0;


	private int compare = 0;
	private boolean minimal = false;
	private boolean maximal = false;

	private String description = null;
	
	public String getDescription() {
		return description;
	}

	public SimpleIntegerProperty getValue() {
		return value;
	}
	
	public Statistic addIncrement(Statistic stat) {
		if (!sumBounds.contains(stat)) {
			sumBounds.add(stat);
			stat.value.addListener(this);
		}
		return this;
	}
	
	public Statistic removeIncrement(Statistic stat) {
		if (sumBounds.contains(stat)) {
			sumBounds.remove(stat);
			stat.value.removeListener(this);
		}
		return this;
	}

	@Override
	public void invalidated(Observable observable) {
		int temp = 0;
		for (Statistic integerStatistic : sumBounds) {
			temp += integerStatistic.value.get();
		}
		this.value.set(
				(minimal) ? Math.min(compare, temp) :
					(maximal) ? Math.max(compare, temp) :	
						(divider > 0) ? division(temp) : temp
								);
	}
	
	private int division(int arg_dValue) {
		return ( arg_dValue % divider ) == 0 ? arg_dValue / divider
				: ( dividedUp ? arg_dValue / divider + 1 : arg_dValue / divider );
	}

	@Override
	protected void finalize() throws Throwable {
		sumBounds.forEach(this::removeIncrement);
		super.finalize();
	}

	public static Statistic init(Statistic...sumStatistics) {
		return init("SUM", sumStatistics);
	}

	public static Statistic init(Object...sumStatistics) {
		return init("SUM", sumStatistics);
	}

	public static Statistic init(String name, Statistic...sumStatistics) {
		Statistic new_statistic = new Statistic(name);
		if (sumStatistics != null) for (Statistic statistic : sumStatistics) {
			new_statistic.addIncrement(statistic);
		}
		return new_statistic;
	}

	/**
	 * 
	 * @param name
	 * @param sumStatistics Array of 
	 * @return
	 */
	public static Statistic init(String name, Object...sumStatistics) {
		Statistic new_statistic = new Statistic(name);
		if (sumStatistics != null) for (Object statistic : sumStatistics) {
			if (statistic instanceof Statistic)
				new_statistic.addIncrement((Statistic) statistic);
			else if (statistic instanceof Integer)
				new_statistic.addIncrement((Integer) statistic);
			else
				System.out.println(statistic.getClass() + " is ignored.");
		}
		return new_statistic;
	}

	public static Statistic constant(int i) {
		Statistic new_statistic = new Statistic(Integer.toString(i));
		new_statistic.value.set(i);
		return new_statistic;
	}

	public Statistic divideDown(int arg_divider) {
		return divideDown("DIV_D", arg_divider);
	}

	public Statistic divideDown(String name, int arg_divider) {
		Statistic new_statistic = new Statistic(name);
		new_statistic.addIncrement(this);
		new_statistic.divider = arg_divider;
		return new_statistic;
	}

	public Statistic divideUp(int arg_divider) {
		return divideUp("DIV_U", arg_divider);
	}

	public Statistic divideUp(String name, int arg_divider) {
		Statistic new_statistic = new Statistic(name);
		new_statistic.addIncrement(this);
		new_statistic.dividedUp = true;
		new_statistic.divider = arg_divider;
		return new_statistic;
	}

	public static ObservableValue<String> nameGenerator(CellDataFeatures<Statistic, String> param) {
		return param.getValue().name;
	}

	public static ObservableValue<String> valueGenerator(CellDataFeatures<Statistic, String> param) {
		return param.getValue().valueAsString;
	}

	public Statistic minimal(int number) {
		return minimal("MIN("+number+")", number);
	}

	public Statistic minimal(String name, int number) {
		Statistic new_statistic = new Statistic(name);
		new_statistic.addIncrement(this);
		new_statistic.compare = number;
		new_statistic.maximal = true;
		return new_statistic;
	}

	public Statistic maximal(int number) {
		return minimal("MAX("+number+")", number);
	}

	public Statistic maximal(String name, int number) {
		Statistic new_statistic = new Statistic(name);
		new_statistic.addIncrement(this);
		new_statistic.compare = number;
		new_statistic.maximal = true;
		return new_statistic;
	}

	public Statistic description(String string) {
		description = string;
		return this;
	}

	public Statistic addIncrement(int i) {
		return addIncrement(Statistic.constant(i));
	}
}
