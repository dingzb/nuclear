package cc.idiary.nuclear.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigTools {

	static final String classPaht = new ConfigTools().getClass()
			.getClassLoader().getResource("").getPath()+"properties/";

	/**
	 * 获取项目属性配置文件夹下的属性
	 * 
	 * @param name
	 * @param key
	 * @return
	 */
	public static String getProperty(String name, String key)
			throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(classPaht + name));
		return prop.getProperty(key);
	}

	public static Properties getProperties(String name) throws IOException {
		Properties prop = new Properties();
		prop.load(new FileInputStream(classPaht + name));
		return prop;
	}

	public static void sortProperties(String name, Properties properties) throws IOException {
		FileOutputStream fos = new FileOutputStream(classPaht + name);
		properties.store(fos, "web modify");
	}
}
