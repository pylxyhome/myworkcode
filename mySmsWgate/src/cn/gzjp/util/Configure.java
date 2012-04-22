package cn.gzjp.util;



import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Configure {
	
	private static final Log log = LogFactory.getLog(Configure.class);
	
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
	public int getValueInt(String key){
		int value = 0;
		try{
			value=Integer.parseInt(getValue(key));
		}catch(Exception excep){
			log.debug("parseIntError key=" + key + " value=0");
		}
		return value;
	}
}
