package core;

import java.util.function.BiFunction;
import java.util.function.Function;

import cz.deznekcz.util.xml.XMLStepper.Step;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.IntegerExpression;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn.CellDataFeatures;

public class Statistic implements InvalidationListener, ILoader<Statistic> {


	private ObservableList<Statistic> sumBounds = FXCollections.observableArrayList();


	private SimpleStringProperty name = new SimpleStringProperty();
	
	public String getName() {
		return name.getValue();
	}

	private SimpleIntegerProperty value = new SimpleIntegerProperty();
	private StringBinding valueAsString;
	
	private String id;


	private StatisticGroup group;


	private static int index;

	public Statistic(StatisticGroup group, String id, String name) {
		if (group != StatisticGroup.GLOBAL)
			group.addStatistic(id, this);
		this.group = group;
		this.id = id;
		this.name.set(name);
		
		sumBounds.addListener(this);
	}


	public Statistic(String internalName) {
		this(StatisticGroup.GLOBAL, "GENERATED " + (index++), internalName);
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
	public String toString() {
		return String.format("Stat(%s,%s)", id, name.get());
	}

	@Override
	protected void finalize() throws Throwable {
		sumBounds.forEach(this::removeIncrement);
		super.finalize();
	}

	public static Statistic init(Statistic...sumStatistics) {
		return init("SUM", 0, sumStatistics);
	}

	public static Statistic init(int constant, Statistic...sumStatistics) {
		return init("SUM", constant, sumStatistics);
	}

	public static Statistic init(String name, Statistic...sumStatistics) {
		return init(name, 0, sumStatistics);
	}

	/**
	 * 
	 * @param name
	 * @param sumStatistics Array of 
	 * @return
	 */
	public static Statistic init(String name, int constant, Statistic...sumStatistics) {
		Statistic new_statistic = new Statistic(name);
		if (sumStatistics != null)
		{
			for (Statistic statistic : sumStatistics) {
				new_statistic.addIncrement(statistic);
			}
		}
		if (constant != 0)
		{
			new_statistic.addIncrement(constant);
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

	public static Statistic table1(String name, Function<Integer, Integer> function, Statistic statistic) {
		Statistic new_statistic = new Statistic(name);
		statistic.value.addListener((o,l,n) -> {
			new_statistic.value.set(function.apply(n.intValue()));
		});
		return new_statistic;
	}

	public static Statistic table1(Function<Integer, Integer> function, Statistic statistics) {
		return table1("TABLE1", function, statistics);
	}

	public static Statistic table2(String name, BiFunction<Integer, Integer, Integer> function, Statistic statistic1, Statistic statistic2) {
		Statistic new_statistic = new Statistic(name);
		InvalidationListener listener = (o) -> {
			new_statistic.value.set(function.apply(statistic1.value.get(),statistic2.value.get()));
		};
		statistic1.value.addListener(listener);
		statistic2.value.addListener(listener);
		return new_statistic;
	}

	public static Statistic table2(BiFunction<Integer, Integer, Integer> function, Statistic statistics1, Statistic statistic2) {
		return table2("TABLE2", function, statistics1, statistic2);
	}


	@Override
	public void loadBuild(Module module, Step node) {
		Step rootSum = node.getNode("sum");
		if (rootSum != null)
		{
			loadSubSum(module, rootSum);
		}
		else
		{
			loadRef(module, node);
		}
	}


	private void loadRef(Module module, Step node) {
		addIncrement(module.getStatistic(node.attribute("ref")));
	}


	private void loadSubSum(Module module, Step rootSum) {
		// TODO Auto-generated method stub
		
	}
}
