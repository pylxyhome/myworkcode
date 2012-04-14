package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
public class GetConfig {
	static Properties pro = null;
	static InputStream inStream = null;
	static HashMap<String, Properties> props = new HashMap<String, Properties>();

	public static String getValue(String name, String province) {
		synchronized (GetConfig.class) {
			pro = props.get(province);
			if (pro == null) {//加载配置文件
				pro = new Properties();
				inStream =GetConfig.class.getResourceAsStream("/"+ province +"_config.conf");
				try {
					pro.load(inStream);
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();			
				}
				props.put(province, pro);
			}
		}
		String value = pro.getProperty(name);
		return value;
	}
}
