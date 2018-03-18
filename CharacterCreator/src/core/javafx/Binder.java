package core.javafx;

import java.util.function.Function;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;

public class Binder {
	public static <A,B> void bidirectional(
			Property<A> propertyA,
			Property<B> propertyB, 
			Function<A,B> toBound, 
			Function<B,A> fromBound)
	{
		propertyB.addListener((ObservableValue<? extends B> v, B lastB, B newB) ->
				{
			try {
				A value = fromBound.apply(newB);
				propertyA.setValue(value);
			} catch (Exception e) {
				System.err.println(e);
				return;
			}
		});
		propertyA.addListener((ObservableValue<? extends A> v, A lastA, A newA) ->
				{
			try {
				B value = toBound.apply(newA);
				propertyB.setValue(value);
			} catch (Exception e) {
				System.err.println(e);
				return;
			}
		});
	}

}
