package cn.gzjp.wap.proxy.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
/**
 * 把文件转换成字符串工具类
 * @author gzwenny
 *
 */
public class FileUtils {
	private static String DEFAULT_ENCODE = "utf-8";
	public static String fileToString(String filename) throws IOException {
		return fileToString(filename, DEFAULT_ENCODE);
	}
	public static String fileToString(String filename, String encode) throws IOException {
		InputStream in = new FileInputStream(new File(filename));
		byte[] buffer = new byte[512];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		String result = new String(out.toByteArray(), encode);
		return result;
	}
	public static String defaultStreamToString(InputStream in){
		String str = "";
		try {
			str = streamToString(in, DEFAULT_ENCODE);
		} catch (Exception e) {
		}
		return str;
	}
	
	public static String streamToString(InputStream in) throws IOException{
		return streamToString(in, DEFAULT_ENCODE);
	}
	public static String streamToString(InputStream in, String encode) throws IOException{
		byte[] buffer = new byte[512];
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int len;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		String result = new String(out.toByteArray(), encode);
		return result;
	}
}
