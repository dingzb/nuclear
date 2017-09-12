package cc.idiary.nuclear.config;

public class StateType {

	public static String valueOfint(Integer i) {
		String str = null;
		if (i.equals(2))
			str = "subsystem";
		if (i.equals(1))
			str = "menugroup";
		if (i.equals(0))
			str = "menu";
		return str;
	}

	public static Integer valuOfStr(String str) {
		Integer i = null;
		if (str.equals("subsystem"))
			i = 2;
		if (str.equals("menugroup"))
			i = 1;
		if (str.equals("menu"))
			i = 0;
		return i;
	}
}
