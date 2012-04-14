package test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
/**
 * http工具类
 * @author panyl
 * @date 2011-11-7
 */
public final class NetTool {

	public static String getTextContent(String urlpath, String encoding)
			throws Exception {
		URL url = new URL(urlpath);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(6 * 1000);// 设置超时时间
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			byte[] data = readStream(inStream);
			return new String(data, encoding);
		}
		return null;
	}

	public static byte[] readStream(InputStream inStream) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 二进制数据
		outStream.close();
		inStream.close();
		return data;
	}

	/**
	 * 
	 * @param urlpath
	 * @param parmas
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static InputStream getContent(String urlpath,
			Map<String, String> parmas, String encoding) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : parmas.entrySet()) {
			sb.append(entry.getKey()).append("=").append(
					URLEncoder.encode(entry.getValue(), encoding));
			sb.append("&");
		}
		if (sb.indexOf("&") != -1)
			sb.deleteCharAt(sb.length() - 1);

		byte[] data = sb.toString().getBytes();
		URL url = new URL(urlpath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Content-Length", String
				.valueOf(data.length));
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		DataOutputStream dataOutputStream = new DataOutputStream(connection
				.getOutputStream());
		dataOutputStream.write(data);
		dataOutputStream.flush();
		dataOutputStream.close();
		int responseCode=sendContent(urlpath, parmas,  encoding);
		if (responseCode == 200) {
			return connection.getInputStream();
		}
		return null;
	}
	public static void main(String[] args){
		Map<String, String> parmas=new HashMap<String,String>();
		parmas.put("mdn", "13625332763");
		try {
			System.out.println(sendContent("http://127.0.0.1",parmas,"utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int sendContent(String urlpath,
			Map<String, String> parmas, String encoding) throws Exception {
		int returnCode=0;
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : parmas.entrySet()) {
			sb.append(entry.getKey()).append("=").append(
					URLEncoder.encode(entry.getValue(), encoding));
			sb.append("&");
		}
		if (sb.indexOf("&") != -1)
			sb.deleteCharAt(sb.length() - 1);

		byte[] data = sb.toString().getBytes();
		URL url = new URL(urlpath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST"); 
		connection.setDoOutput(true);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("Content-Length", String
				.valueOf(data.length));
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		DataOutputStream dataOutputStream = new DataOutputStream(connection
				.getOutputStream());
		dataOutputStream.write(data); 
		dataOutputStream.flush();
		dataOutputStream.close();
		returnCode=connection.getResponseCode();
		connection.disconnect(); 
		return returnCode;
	}
}

