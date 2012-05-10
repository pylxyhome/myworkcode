package com.ipipa.utils;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
/**
 * json工具类
 * @description
 * @author panyl
 * @date 2012-5-10
 */
public class JsonUtil {
	/**
	 * 转成json模式
	 * @author pyl
	 * @date May 7, 2012 5:21:23 PM
	 * @Description: TODO
	 * @param obj
	 * @return
	 */
	public static String objToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
