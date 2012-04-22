package cn.gzjp.util;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Logs {
	public Logs(){}
	
	
	public SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public SimpleDateFormat HOURS_OF_DAY_12_FORMAT = new SimpleDateFormat("yyyyMMddhh");
	public SimpleDateFormat HOURS_OF_DAY_24_FORMAT = new SimpleDateFormat("yyyyMMddHH");
	public SimpleDateFormat HOUR_12_FORMAT = new SimpleDateFormat("hh");
	public SimpleDateFormat Time_Complete_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
	public SimpleDateFormat Time_Complete_FORMAT_with = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public SimpleDateFormat MOUNTH_FORMAT = new SimpleDateFormat("yyyyMM");
//	String filename = Configure.getStateReportLogDir() + "stateReportLog" + sdf.format(new Date()) + ".log";
//	public static 
	
	public Date getNow(){
		Calendar cal  = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, 0);
		return cal.getTime();
	}
	
	public Date getHours(int HoursBefore){
		Calendar cal  = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, HoursBefore);
		return cal.getTime();
	}
	
	public Date getDay(int DaysBefore){
		Calendar cal  = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, DaysBefore);
		return cal.getTime();
	}
	
	
	public void log(String filename, String log){
		/***
		 * filename 包括文件的完整路径，由文件夹路径和文件名组成
		 * log 为写日志的内容
		 * */
		FileWriter fileWriter = null;
		BufferedWriter bw = null;
		try {
			File file = new File(filename);
			fileWriter = new FileWriter(file, true);
			bw = new BufferedWriter(fileWriter);
			bw.write(log);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fileWriter!=null)
					fileWriter.close();
				if(bw!=null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args){
		new Logs().ziplog("F://yunnan_20110920.txt");
	}
	
	public void ziplog(String filename) {

		/***
		 * filename为打包文件的文件名，包括文件夹路径和文件名 打包成zip文件
		 * */

		File file = new File(filename);
		if (file.exists()) {
			try {
				FileOutputStream fos = new FileOutputStream(filename + ".zip");
				ZipOutputStream out = new ZipOutputStream(
						new CheckedOutputStream(fos, new CRC32()));
				BufferedInputStream bis = new BufferedInputStream(
						new FileInputStream(file));
				ZipEntry entry = new ZipEntry(file.getName());
				out.putNextEntry(entry);

				byte[] data = new byte[1024];
				int count1 = bis.read(data);
				while (count1 != -1) {
					out.write(data, 0, count1);
					count1 = bis.read(data);
					out.flush();
				}
				bis.close();
				out.close();
				fos.close();

				file.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
