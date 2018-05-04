package drdplus2;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;

public class IGen {

	public static StringBinding init(IntegerProperty levelProperty) {
		return new StringBinding() {
			{
				bind(levelProperty);
			}
			@Override
			protected String computeValue() {
				StringBuilder b = new StringBuilder("");
				for (int i = 0; i < levelProperty.intValue(); i++) {
					b.append("I");
				}
				return b.length() > 0 ? b.toString() : "neumí";
			}
		};
	}

}
