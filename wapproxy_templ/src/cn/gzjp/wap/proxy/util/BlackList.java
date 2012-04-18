package cn.gzjp.wap.proxy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class BlackList {
	
	private static List<String> blacklists=new ArrayList<String>();
	
	static {
		try{
			InputStream in=BlackList.class.getResourceAsStream("/blacklist.conf");
			LineNumberReader reader=new LineNumberReader(new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null)
				blacklists.add(line);
		}catch(IOException ex){
			throw new RuntimeException("read blacklsit error.", ex);
		}
	}

	public static boolean test(String url){
		for(String key:blacklists){
			if(url.startsWith(key))return true;
		}
		return false;
	}
}
