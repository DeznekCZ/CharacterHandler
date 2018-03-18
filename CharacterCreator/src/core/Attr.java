package core;

public class Attr {
	@SafeVarargs
	public static <L> L[] v(L...value) {
		return value;
	}
}
