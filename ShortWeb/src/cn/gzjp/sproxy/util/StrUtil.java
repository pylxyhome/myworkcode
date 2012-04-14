package cn.gzjp.sproxy.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;



public class StrUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "1234567";
		String subStr = "AA";
//		System.out.println(setCharsetInContentType("text/vnd.wap.wml; charset=utf-8", "gbk"));
		String url = "http://g.hn165.com:80/";
		System.out.println(getHostFromURL(url));
	}
	
	public static String tnull(String str){
		if(str == null){
			str = "";
		}
		return str;
	}
	
	public static String trim(String str){
		return tnull(str).trim();
	}
	
	public static String addSubStr(int ix, String str, String subStr){
		str = tnull(str);
		subStr = tnull(subStr);
		String result = "";
		StringBuilder strbd = new StringBuilder();
		strbd.append(str.substring(0, ix));
		strbd.append(subStr);
		strbd.append(str.substring(ix, str.length()));
		result = strbd.toString();
		return result;
	}
	
	public static String getCharsetByContentType(String contentType) {
		//Content-Type:text/vnd.wap.wml; charset=utf-8
		contentType = contentType.trim().toLowerCase();
		String sChar = "charset=";
		int scharIx = contentType.indexOf(sChar);
		if(scharIx < 0){
			return Configure.get().getValue("defaultPageCharset");
		}
		int sIx = scharIx + sChar.length();
		int eIx = contentType.indexOf(" ", sIx);
		if(eIx == -1){
			eIx = contentType.length();
		}
		String charset = contentType.substring(sIx, eIx);
		return charset;
	}
	public static String setCharsetInContentType(String ctxType, String charset){
		//text/html; charset=GBK
		String nctxType = ctxType.trim().toLowerCase();
		String sChar = "charset=";
		int scharIx = ctxType.indexOf(sChar);
		if(scharIx < 0){
			return ctxType;
		}
		int sIx = scharIx + sChar.length();
		nctxType = ctxType.substring(0, sIx) + charset;
		return nctxType;
	}
	public static String trimAndToLow(String str){
		return trim(str).toLowerCase();
	}
	public static String trimAndToUpp(String str){
		return trim(str).toUpperCase();
	}
	
	public static byte[] ioCopyAndData(InputStream inds, OutputStream clOut, int buffSize) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] serBuff = new byte[buffSize];
		int r = 0;
		while((r = inds.read(serBuff)) != -1){
			baos.write(serBuff, 0, r);
			clOut.write(serBuff, 0, r);
		}
		byte[] data = baos.toByteArray();
		return data;
	}
	public static void ioCopy(InputStream inds, OutputStream clOut, int buffSize) throws IOException {
		byte[] serBuff = new byte[buffSize];
		int r = 0;
		while((r = inds.read(serBuff)) != -1){;
			clOut.write(serBuff, 0, r);
		}
	}
	public static void ioCopy(InputStream inds, OutputStream clOut, int buffSize, int dataSize) throws IOException {
		int rcnt = 0;
		byte[] serBuff = new byte[buffSize];
		int r = 0;
		while((r = inds.read(serBuff)) != -1){
			clOut.write(serBuff, 0, r);
			rcnt+=r;
			if(rcnt >= dataSize){
				break;
			}
		}
	}
	public static byte[] getDataByStm(InputStream inds, int buffSize, int dataSize) throws IOException {
		int rcnt = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] serBuff = new byte[buffSize];
		int r = 0;
		while((r = inds.read(serBuff)) != -1){
			baos.write(serBuff, 0, r);
			rcnt+=r;
			if(rcnt >= dataSize){
				break;
			}
		}
		return baos.toByteArray();
	}
	public static byte[] getDataByStm(InputStream inds, int buffSize) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] serBuff = new byte[buffSize];
		int r = 0;
		while((r = inds.read(serBuff)) != -1){
			baos.write(serBuff, 0, r);
		}
		return baos.toByteArray();
	}
	
	public static String getCtxByStm(InputStream inds, int buffSize, String charset) throws IOException {
		return getCtxByData(getDataByStm(inds, buffSize), charset);
	}

	public static byte[] gunzip(byte[] data) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		GZIPInputStream gin = new GZIPInputStream(bais);
		data = getDataByStm(gin, 1024);
		return data;
	}
	
	public static String getHostFromURL(String url){
		//TODO 处理url为空和找不到://的异常
		int start=url.indexOf("://")+3;
		if(start < 3){
			return "";
		}
		int end=url.indexOf('/',start);
		if(end==-1){
			url=url.substring(start);
		}else{
			url=url.substring(start,end);
		
		}
		//判断里面是否含有问号，有问号则不是正确的host
		int loc=url.indexOf('?');
		if(loc>1)url=url.substring(0,loc);
		//判断url里面是否含有&号
		loc=url.indexOf('&');
		if(loc>1)url=url.substring(0,loc);
		return url;
	}
	public static byte[] getDataByFile(File file, int buffSize) throws IOException{
		FileInputStream fileInStm = new FileInputStream(file);
		byte[] data = getDataByStm(fileInStm, buffSize);
		fileInStm.close();
		return data;
	}
	
	public static String getCtxByData(byte[] data, String charset) throws UnsupportedEncodingException{
		String result = new String(data, charset);
		return result;
	}
	public static String getCtxByData(byte[] data) throws UnsupportedEncodingException{
		String result = new String(data, "utf-8");
		return result;
	}
	
	public static String getFileCtx(String filename, int buffSize, String charset) throws IOException{
		String result = getCtxByData(getDataByFile(new File(filename), buffSize), charset);	
		return result ;
	}
	public static void putCtxToFile(String fileName,String ctx, boolean append) throws IOException{
		File file = new File(fileName);
		if (!file.getParentFile().exists())file.getParentFile().mkdirs();
		file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(file, append));
		pw.print(ctx);
		pw.flush();
		pw.close();
	}
}
