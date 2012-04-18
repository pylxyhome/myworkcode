package test.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configure {
	private static Configure me = null;
	private Properties props = new Properties();
	
	public static Configure get(){
		if(me == null){
			me = new Configure();
		}
		return me;
	}
	private Configure() {
		InputStream in = Configure.class.getResourceAsStream("/config.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(null != in){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public String getValue(String key){
		return props.getProperty(key);
	}

}
