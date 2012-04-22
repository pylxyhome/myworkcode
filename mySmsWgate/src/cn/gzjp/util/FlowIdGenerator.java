package cn.gzjp.util;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public class FlowIdGenerator {
	private static AtomicInteger counter=new AtomicInteger();

	public static int[] create(int sourceCode){
		int[]flowId=new int[3];
		flowId[0]=sourceCode;
		Calendar calendar=Calendar.getInstance();
	    String month=toString(calendar.get(Calendar.MONTH)+1);
		String day=toString(calendar.get(Calendar.DATE));
		String hour=toString(calendar.get(Calendar.HOUR_OF_DAY));
		String minute=toString(calendar.get(Calendar.MINUTE));
		String second=toString(calendar.get(Calendar.SECOND));
		StringBuffer date=new StringBuffer();
		date.append(month).append(day).append(hour)
		.append(minute).append(second);
			
		flowId[1]=Integer.parseInt(date.toString());
		flowId[2]=counter.getAndIncrement();
		return flowId;
	}
	
	protected static String toString(int i){
		if(i<=9)return "0"+i;
		else return ""+i;
	}
}
