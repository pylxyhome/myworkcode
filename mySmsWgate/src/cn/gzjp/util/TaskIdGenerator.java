package cn.gzjp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TaskId生成器
 * @description
 * @author panyl
 * @date 2011-12-1
 */
public class TaskIdGenerator {
	private static Log log = LogFactory.getLog(TaskIdGenerator.class);

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");

	private static long lastTaskId=0l;
	public TaskIdGenerator(){
	}
	/**
	 * 生成taskid
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @return
	 */
	public synchronized static long create(){
			String taskidstr=sdf.format(new Date());
			long taskid=Long.valueOf(taskidstr);
			while(lastTaskId>=taskid){
				taskid++;
			}
			lastTaskId=taskid;
			return taskid;
	}
	
	public static boolean validateTaskId(String taskid){
	   if(taskid==null)return false;
	   Pattern pattern = Pattern.compile("[0-9]{14}"); 
  	   Matcher isNum = pattern.matcher(taskid);
  	   if( !isNum.matches() ){
  	       return false; 
  	   } 
  	   return true; 
	}
	public static void main(String[] args) {
		System.out.println(validateTaskId("20111201201812"));
	}
	
}
