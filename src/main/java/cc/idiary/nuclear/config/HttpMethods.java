package cc.idiary.nuclear.config;


import cc.idiary.utils.common.StringTools;

/**
 * 
 * @author Dzb
 *
 */
public class HttpMethods {

	/**
	 * 比较
	 * 
	 * @param compare
	 * @param compareWith
	 * @return
	 */
	public static Boolean match(String compare, String compareWith) {
		if (StringTools.isEmpty(compareWith)
				|| StringTools.isEmpty(compareWith))
			return false;
		if (compareWith.equals(valueOfint(null)))
			return true;
		return compare.equals(compareWith);
	}

	/**
	 * 
	 * 转化数据库中http方法数字为字符
	 * 
	 * @param val
	 * @return
	 */
	public static String valueOfint(Integer val) {
		String res = null;
		if (val == null) {
			res = "BOTH";
		} else {
			if (val == 0)
				res = "GET";
			if (val == 1)
				res = "POST";
		}
		return res;
	}

	/**
	 * 转换字符到数字
	 * 
	 * @param val
	 * @return
	 */
	public static int valueOfStr(String val) {
		int res = 0;
		if (val.toUpperCase().equals("GET"))
			res = 0;
		if (val.toUpperCase().equals("POST"))
			res = 1;
		return res;
	}
}
