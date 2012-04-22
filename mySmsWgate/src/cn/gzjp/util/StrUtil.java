package cn.gzjp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 字符串工具类
 * @description
 * @author panyl
 * @date 2011-12-2
 */
public class StrUtil {
	 /**
     * 判断是否是数字
     * @description
     * @author panyl
     * @date 2011-12-2
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){ 
    	   Pattern pattern = Pattern.compile("[0-9]+"); 
    	   Matcher isNum = pattern.matcher(str);
    	   if( !isNum.matches() ){
    	       return false; 
    	   } 
    	   return true; 
    	}
    /**
     * 
     * @description
     * @author panyl
     * @date 2011-12-5
     * @param flowIdInts
     * @return
     */
	public static String getFlowIdStr(int[] flowIdInts){
		StringBuilder flowIdStr = new StringBuilder();
		for(int i = 0; i < flowIdInts.length; ++i){
			flowIdStr.append(flowIdInts[i]);
			flowIdStr.append("_");
		}
		return flowIdStr.toString();
	}
}
