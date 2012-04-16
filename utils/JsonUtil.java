package com.trendcom.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 处理json的工具类，负责json数据转换成java对象和java对象转换成json
 * @author yulon
 * @date
 * @版本 V 1.0
 */
public class JsonUtil {

	/**
	 * 从一个JSON 对象字符格式中得到一个java对象
	 * 
	 * @param jsonString
	 * @param pojoCalss
	 * @return
	 */
	public static Object getObject4JsonString(String jsonString, Class pojoCalss) {
		Object pojo;
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		pojo = JSONObject.toBean(jsonObject, pojoCalss);
		return pojo;
	}


	/**
	 * 从json HASH表达式中获取一个map，改map支持嵌套功能
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map getMap4Json(String jsonString) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Iterator keyIter = jsonObject.keys();
		String key;
		Object value;
		Map valueMap = new HashMap();

		while (keyIter.hasNext()) {
			key = (String) keyIter.next();
			value = jsonObject.get(key);
			valueMap.put(key, value);
		}

		return valueMap;
	}


	/**
	 * 从json数组中得到相应java数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Object[] getObjectArray4Json(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		return jsonArray.toArray();

	}


	/**
	 * 从json对象集合表达式中得到一个java对象列表
	 * 
	 * @param jsonString
	 * @param pojoClass
	 * @return
	 */
	public static List getList4Json(String jsonString, Class pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;

		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			
			pojoValue = JSONObject.toBean(jsonObject, pojoClass);
			list.add(pojoValue);

		}
		return list;

	}
	public static List searchByJson(String key,String value,String jsonString, Class pojoClass) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;

		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {

			jsonObject = jsonArray.getJSONObject(i);
			String str=(String)jsonObject.get(key);	
			if(removeBlank(str).contains(removeBlank(value))){
				pojoValue = JSONObject.toBean(jsonObject, pojoClass);
				list.add(pojoValue);
			}
		}
		return list;

	}
	public static List searchProductTreeByJson(String key,Long value,String jsonString, Class pojoClass) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			Integer strInt=(Integer)jsonObject.get(key);
			String str=String.valueOf(strInt);
			if(Long.valueOf(str).equals(value)){
				pojoValue = JSONObject.toBean(jsonObject, pojoClass);
				list.add(pojoValue);
			}
		}
		return list;

	}
	public static String searchByKey(String key,String value,String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		Object pojoValue;
		List list = new ArrayList();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String str=(String)jsonObject.get(key);	
			if(removeBlank(str).toLowerCase().contains(removeBlank(value).toLowerCase())){
				pojoValue = JSONObject.toBean(jsonObject);
				list.add(pojoValue);
			}
		}
		return JSONArray.fromObject(list).toString();

	}
	public static List<Long> searchIdsByKey(String key,String value,String idName,String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		JSONObject jsonObject;
		//Object pojoValue;
		List<Long> list = new ArrayList<Long>();
		for (int i = 0; i < jsonArray.size(); i++) {
			jsonObject = jsonArray.getJSONObject(i);
			String str=(String)jsonObject.get(key);	
			if(removeBlank(str).toLowerCase().contains(removeBlank(value).toLowerCase())){
				//pojoValue = JSONObject.toBean(jsonObject);
				Long id=Long.valueOf((String)jsonObject.get(idName));
				list.add(id);
			}
		}
		return list;

	}
	/** 
	* 去掉字符串中的空格 
	* @param str 
	* @return String 
	*/ 
	private static String removeBlank(String str){ 
		StringBuilder sb = new StringBuilder(); 
		char c = ' '; 
		for(int i = 0 ; i < str.length() ; i++){ 
		char ch = str.charAt(i); 
		if(ch != c){ 
		sb.append(ch); 
		} 
		} 
		return sb.toString(); 
	} 


	/**
	 * 从json数组中解析出java字符串数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static String[] getStringArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		String[] stringArray = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			stringArray[i] = jsonArray.getString(i);

		}

		return stringArray;
	}


	/**
	 * 从json数组中解析出javaLong型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Long[] getLongArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Long[] longArray = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			longArray[i] = jsonArray.getLong(i);

		}
		return longArray;
	}


	/**
	 * 从json数组中解析出java Integer型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Integer[] getIntegerArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Integer[] integerArray = new Integer[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			integerArray[i] = jsonArray.getInt(i);

		}
		return integerArray;
	}




	/**
	 * 从json数组中解析出java Integer型对象数组
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Double[] getDoubleArray4Json(String jsonString) {

		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Double[] doubleArray = new Double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			doubleArray[i] = jsonArray.getDouble(i);

		}
		return doubleArray;
	}


	/**
	 * 将java对象转换成json字符串
	 * 
	 * @param javaObj
	 * @return
	 */
	public static String getJsonString4JavaPOJO(Object javaObj) {

		JSONObject json;
		json = JSONObject.fromObject(javaObj);
		return json.toString();

	}

}
