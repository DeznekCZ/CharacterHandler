package drdplus2;

import core.Statistic;

import static core.Attr.v;

public class Life {

	static Integer[] root = v(
			10,11,12,14,16,18,20,22,25,28,
			32,36,40,45,50,56,63,70,80,90
			);
	
	public static Statistic init(Statistic construct) {
		Statistic lifeValue = Statistic.init();
		construct.getValue().addListener((o,l,n) -> {
			int i = (int) n;
			lifeValue.getValue().set(root[((i - 10) % 20)] * ((i - 10) / 20));
		});
		return lifeValue;
	}

}
