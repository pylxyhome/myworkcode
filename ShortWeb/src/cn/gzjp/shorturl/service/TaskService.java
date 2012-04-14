package cn.gzjp.shorturl.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TaskService {

	final BlockingQueue<String> queue = new ArrayBlockingQueue<String>(3);
	/**
	 * 准备发送的文件目录名
	 */
	public final static String READY_DIR = "ready";
	/**
	 * 正在发送的文件目录名
	 */
	public final static String SENDING_DIR = "sending";
	/**
	 * 完成发送的文件目录 名
	 */
	public final static String FINISH_DIR = "finish";
	/**
	 * 停止发送的文件目录名
	 */
	public final static String STOP_DIR = "stop";
	
	public final static String USER_DIR=System.getProperty("user.dir");

	public void putIntoReadFile(final String taskid, String msg, String url,
			final InputStream inStream) throws IOException {
				try {
					//URL fileurl=Thread.currentThread().getContextClassLoader().getResource("");
					File file=new File(USER_DIR,READY_DIR+File.separator+taskid+File.separator+"data.txt");
					//如果目录不存在则创建
					if(!file.getParentFile().exists()){
						file.getParentFile().mkdirs();
					}
					OutputStream outStream=new FileOutputStream(file);
					BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(outStream));
					BufferedReader rd = new BufferedReader(new InputStreamReader(inStream));
					String str = null;
					while ((str = rd.readLine()) != null) {
						bw.write(str);
						bw.write("\n");
					}
					bw.flush();
					bw.close();
					rd.close();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	}

}
