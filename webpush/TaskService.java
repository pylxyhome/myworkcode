package cn.gzjp.push2.work;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.push2.bean.TaskBean;
import cn.gzjp.push2.constant.PushConstant;
import cn.gzjp.push2.handle.FileHandler;
import cn.gzjp.push2.listener.PushListener;
import cn.gzjp.push2.sgip.Configure;
import cn.gzjp.push2.sgip.ResultEntity;
import cn.gzjp.push2.sgip.SenderAsyImpl;
import cn.gzjp.push2.sgip.SenderImpl;
import cn.gzjp.util.FileUtil;
import cn.gzjp.util.TaskIdGenerator;

/**
 * 任务服务类
 * 
 * @description
 * @author panyl
 * @date 2011-12-1
 */
public class TaskService {
	SimpleDateFormat Time_Complete_FORMAT_with = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 任务队列
	 */
	final BlockingQueue<TaskBean> taskQueue = new LinkedBlockingQueue<TaskBean>(
			PushConstant.TASK_QUEUE_MAX_SIZE);
	/**
	 * 类共享队列
	 */
	final static BlockingQueue<String> readyQueue = new LinkedBlockingQueue<String>(
			PushConstant.REAY_QUEUE_MAX_SIZE);

	public static LinkedHashMap<Integer, String> dirPathMap = new LinkedHashMap<Integer, String>();
	/**
	 * 暂停任务map
	 */
	public static ConcurrentHashMap<String,String> stopTaskMap=new ConcurrentHashMap<String,String>();
	
	/**
	 * 异步时一次性发送的号码量
	 */
	public static int onePushCount = Configure.getOnePushCount();

	public static LinkedHashMap<Integer, String> getDirPathMap() {
		if (dirPathMap.size() == 0) {
			dirPathMap.put(PushConstant.SENDING_STATUS,
					Configure.getPushFilePath() + File.separator
							+ PushConstant.SENDING_DIR);
			dirPathMap.put(PushConstant.READY_STATUS,
					Configure.getPushFilePath() + File.separator
							+ PushConstant.READY_DIR);
			dirPathMap.put(PushConstant.FINISH_STATUS,
					Configure.getPushFilePath() + File.separator
							+ PushConstant.FINISH_DIR);
			dirPathMap.put(PushConstant.STOP_STATUS,
					Configure.getPushFilePath() + File.separator
							+ PushConstant.STOP_DIR);
		}
		return dirPathMap;
	}
	
	private final static Logger log = LoggerFactory
			.getLogger(TaskService.class);

	
	public long putIntoReadFile(String msg, String url,
			final InputStream inStream) throws IOException {
		return putIntoReadFile(msg,url,inStream,false);
	}
	public long putIntoReadFileGzip(String msg, String url,
			final InputStream inStream) throws IOException {
		return putIntoReadFile(msg,url,inStream,true);
	}
	/**
	 * 将群发任务添加到READY_DIR目录，进入准备发送状态...
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param msg
	 * @param url
	 * @param inStream
	 * @param isGzip 是否需要解压
	 * @return
	 * @throws IOException
	 */
	public long putIntoReadFile(String msg, String url,
			final InputStream inStream,boolean isGzip) throws IOException {
		long taskid = TaskIdGenerator.create();
		try {
			File file = new File(Configure.getPushFilePath(),
					PushConstant.READY_DIR + File.separator + taskid
							+ File.separator + PushConstant.DATA_FILE_NAME);
			// 如果目录不存在则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			OutputStream outStream = new FileOutputStream(file);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					outStream));
			BufferedReader rd=null;
			if(isGzip){
				GZIPInputStream gzipin = new GZIPInputStream(inStream);
				rd = new BufferedReader(new InputStreamReader(
						gzipin));
			}else{
				rd = new BufferedReader(new InputStreamReader(
						inStream));
			}
			String str = null;
			StringBuffer headContent = new StringBuffer();
			headContent.append("msg:").append(msg).append("\n").append("url:")
					.append(url).append("\n").append("\n");
			bw.write(headContent.toString());
			while ((str = rd.readLine()) != null) {
				bw.write(str);
				//bw.write(mdnstr);
				bw.write("\n");
			}
			bw.flush();
			bw.close();
			rd.close();
			outStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return taskid;
	}
	/**
	 * 从任务队列中取出一条任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @return
	 * @throws InterruptedException
	 */
	@Deprecated
	public TaskBean getTaskFromTaskQueue() throws InterruptedException {
		return taskQueue.take();
	}

	/**
	 * 放入一条任务到任务队列中
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param taskBean
	 * @throws InterruptedException
	 */
	@Deprecated
	public void putTaskIntoTaskQueue(TaskBean taskBean)
			throws InterruptedException {
		taskQueue.put(taskBean);
	}

	/**
	 * 发送push
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param msg
	 * @param url
	 * @param num
	 * @param mdn
	 * @return
	 */
	public List<ResultEntity> pushSend(String ctx, String url, String num,
			String mdn) {
		List<ResultEntity> results = Collections.emptyList();
		try {
			long startTime = System.currentTimeMillis();
			results = SenderImpl.sendMsgBySgip(mdn, url, ctx);
			long endTime = System.currentTimeMillis();
			if(results==null)return new ArrayList<ResultEntity>();
			log.info("results size:"+results.size());
			for (ResultEntity entity : results) {
				entity.setNum(num);
				entity.setUseTime(String.valueOf((endTime - startTime)));
			}
			log.info("push sms:" + num);
			Thread.sleep(15);
		} catch (Exception e1) {
			log.error(e1.getMessage());
			e1.printStackTrace();
		}
		return results;
	}

	/**
	 * 异步发送push
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-6
	 * @param ctx
	 * @param url
	 * @param num
	 * @param mdn
	 * @return
	 */
	public List<ResultEntity> pushSendAsy(String ctx, String url,
			List<ResultEntity> rs) {
		List<ResultEntity> results = Collections.emptyList();
		try {
			long startTime = System.currentTimeMillis();
			results = SenderAsyImpl.sendMsgBySgipAsy(rs, url, ctx);
			long endTime = System.currentTimeMillis();
			for (ResultEntity entity : results) {
				log.info("resultEntity:"+entity.toString());
				entity.setUseTime(String.valueOf((endTime - startTime)));
			}
			log.info("results size:" + results.size());
			Thread.sleep(15);
		} catch (Exception e1) {
			log.info(e1.getMessage());
			e1.printStackTrace();
		}
		return results;
	}

	/**
	 * 将file文件转换成TaskBean对象
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param file
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public TaskBean changeFileToTaskBean(File file, String taskid)
			throws Exception {
		TaskBean taskBean = new TaskBean();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder builder = new StringBuilder("");
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("msg")) {
				String[] msgs = line.split(":");
				taskBean.setMsg(msgs[1]);
				continue;
			}
			if (line.startsWith("url")) {
				int index = line.indexOf(":");
				String url = line.substring(index);
				taskBean.setUrl(url);
				continue;
			}
			if (!"".equals(line)) {
				builder.append(line + "\n");
			}
		}
		reader.close();
		taskBean.setMdns(builder.toString());
		taskBean.setTaskid(Long.valueOf(taskid));
		String sendingFilePath = Configure.getPushFilePath() + File.separator
				+ PushConstant.SENDING_DIR + File.separator + taskid
				+ File.separator + PushConstant.DATA_FILE_NAME;
		// 移动文件
		FileUtil.mv(file, sendingFilePath);
		log.debug("move " + file.getAbsolutePath() + " to " + sendingFilePath);
		// 删除READY_DIR下对应taskid的目录
		FileUtil.rm(Configure.getPushFilePath() + File.separator
				+ PushConstant.READY_DIR + File.separator + taskid);
		taskBean.setFilePath(sendingFilePath);
		return taskBean;
	}
	@Deprecated
	public TaskBean changeReadyFileToTaskBean(File[] subfiles,
			final String taskid) throws Exception {
		TaskBean taskBean = createTaskBean(subfiles, taskid, new FileHandler() {
			@Override
			public File handler(File datafile, File logFile) throws Exception {
				String sendingFilePath = Configure.getPushFilePath()
						+ File.separator + PushConstant.SENDING_DIR
						+ File.separator + taskid + File.separator
						+ PushConstant.DATA_FILE_NAME;
				String sendingLogFilePath = Configure.getPushFilePath()
						+ File.separator + PushConstant.SENDING_DIR
						+ File.separator + taskid + File.separator
						+ PushConstant.LOG_FILE_NAME;
				// 移动文件
				FileUtil.mv(datafile, sendingFilePath);
				log.info("move " + datafile.getAbsolutePath() + " to "
						+ sendingFilePath);
				if (logFile != null) {
					FileUtil.mv(logFile, sendingLogFilePath);
					log.info("move " + logFile.getAbsolutePath() + " to "
							+ sendingLogFilePath);
				}
				// 删除READY_DIR下对应taskid的目录
				FileUtil.rm(Configure.getPushFilePath() + File.separator
						+ PushConstant.READY_DIR + File.separator + taskid);
				// 转为发送状态
				return null;
			}

		});
		return taskBean;
	}

	public void changeReadyFileToTask(File[] subfiles, final String taskid)
			throws Exception {
		pushSend(subfiles, taskid, new FileHandler() {
			@Override
			public File handler(File datafile, File logFile) throws Exception {
				String sendingFilePath = Configure.getPushFilePath()
						+ File.separator + PushConstant.SENDING_DIR
						+ File.separator + taskid + File.separator
						+ PushConstant.DATA_FILE_NAME;
				String sendingLogFilePath = Configure.getPushFilePath()
						+ File.separator + PushConstant.SENDING_DIR
						+ File.separator + taskid + File.separator
						+ PushConstant.LOG_FILE_NAME;
				// 移动文件
				FileUtil.mv(datafile, sendingFilePath);
				log.info("move " + datafile.getAbsolutePath() + " to "
						+ sendingFilePath);
				if (logFile != null) {
					FileUtil.mv(logFile, sendingLogFilePath);
					log.info("move " + logFile.getAbsolutePath() + " to "
							+ sendingLogFilePath);
				}
				// 删除READY_DIR下对应taskid的目录
				FileUtil.rm(Configure.getPushFilePath() + File.separator
						+ PushConstant.READY_DIR + File.separator + taskid);
				// 转为发送状态
				return new File(sendingFilePath);
			}

		});
	}

	public void changeSendingFileToTask(File[] subfiles, final String taskid)
			throws Exception {
		pushSend(subfiles, taskid, null);
	}

	/**
	 * 将sending目录下的任务封装成TaskBean
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param subfiles
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public TaskBean changeFileToTaskBean(File[] subfiles, String taskid)
			throws Exception {
		TaskBean taskBean = createTaskBean(subfiles, taskid, null);
		return taskBean;
	}

	/**
	 * 生成taskBean
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param subfiles
	 * @param taskid
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	private TaskBean createTaskBean(File[] subfiles, String taskid,
			FileHandler handler) throws Exception {
		log.info("--createTaskBean start---");
		String logFilePath = "";
		File dataFile = null;
		File logFile = null;
		int num = -1;
		for (File file : subfiles) {
			if (file.getName().equals(PushConstant.DATA_FILE_NAME)) {
				dataFile = file;
			}
			if (file.getName().equals(PushConstant.LOG_FILE_NAME)) {
				logFilePath = file.getAbsolutePath();
				logFile = file;
			}
		}
		if (!logFilePath.equals("")) {
			String lastline = FileUtil.getLastLine(logFilePath);
			log.info("lastline:" + lastline);
			if (!"".equals(lastline) && lastline.contains("|")) {
				String lastnum = lastline.split("\\|")[0];
				num = Integer.valueOf(lastnum);
			}
		}
		TaskBean taskBean = new TaskBean();
		taskBean.setTaskid(Long.valueOf(taskid));
		if (dataFile != null) {
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			StringBuilder builder = new StringBuilder("");
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("msg")) {
					String[] msgs = line.split(":");
					taskBean.setMsg(msgs[1]);
					continue;
				}
				if (line.startsWith("url")) {
					int index = line.indexOf(":");
					String url = line.substring(index);
					taskBean.setUrl(url);
					continue;
				}
				if (num != -1) {
					if (!"".equals(line) && line.contains("|")) {
						int lastnum = Integer.valueOf(line.split("\\|")[0]);
						if (lastnum <= num)
							continue;
					}
				}

				if (!"".equals(line)) {
					builder.append(line + "\n");
				}

			}
			reader.close();
			taskBean.setMdns(builder.toString());
			if (handler != null) {
				handler.handler(dataFile, logFile);
			}
		} else {
			return null;
		}
		taskBean.setStartline(num);
		log.info("--createTaskBean end---");
		log.info("taskid start line:" + num);
		return taskBean;
	}

	/**
	 * 
	 * 发送
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param subfiles
	 * @param taskid
	 * @param handler
	 * @return
	 * @throws Exception
	 */
	private void pushSend(File[] subfiles, String taskid, FileHandler handler)
			throws Exception {
		String sendLogdir = Configure.getSendLogDir();
		SimpleDateFormat sendlogsdf = new SimpleDateFormat("yyyyMMdd");
		String sendlogDate = sendlogsdf.format(new Date());
		
		String logFilePath = "";
		File dataFile = null;
		File logFile = null;
		int num = -1;
		for (File file : subfiles) {
			if (file.getName().equals(PushConstant.DATA_FILE_NAME)) {
				dataFile = file;
			}
			if (file.getName().equals(PushConstant.LOG_FILE_NAME)) {
				logFilePath = file.getAbsolutePath();
				logFile = file;
			}
		}
		File sendlogDirFile = new File(sendLogdir);
		if(!sendlogDirFile.exists()){
			sendlogDirFile.mkdirs();
		}
		
		if (!logFilePath.equals("")&&logFile!=null) {
			num = FileUtil.getLineNum(logFile);
			log.info("linenum:" + num);
		}
		String sendingLogFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.SENDING_DIR + File.separator
				+ taskid + File.separator + PushConstant.LOG_FILE_NAME;
		FileUtil.mkdir(sendingLogFilePath, false); // 创建目标文件路径
		log.info("sendingLogFilePath:"+sendingLogFilePath);
		OutputStream outStream = new FileOutputStream(sendingLogFilePath, true);
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(outStream));
		
		OutputStream sendlogOutStream = new FileOutputStream(sendLogdir + "sendLog" + sendlogDate + ".log", true);
		BufferedWriter sendlogw = new BufferedWriter(new OutputStreamWriter(sendlogOutStream));
		
		if (dataFile != null) {
			if (handler != null) {
				dataFile = handler.handler(dataFile, logFile);
			}
			BufferedReader reader = new BufferedReader(new FileReader(dataFile));
			String line = null;
			String msg = "";
			String url = "";
			List<ResultEntity> mdns = new ArrayList<ResultEntity>();
			boolean isClose=false;
			int readernum=0;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("msg")) {

					int index = line.indexOf(":");
					msg = line.substring(index+1);
					continue;
				}
				if (line.startsWith("url")) {
					int index = line.indexOf(":");
					url = line.substring(index+1);
					continue;
				}
				if (num != -1) {
					if (!"".equals(line) && line.contains("|")) {
						readernum++;
						if (readernum <= num) //跳过
							continue;
					}
				}
				String[] linenum = line.split("\\|");
				//判断任务是否需要暂停
				if(stopTaskMap.keySet().contains(taskid)){
					if (mdns.size() > 0) {
						List<ResultEntity> results = pushSendAsy(msg, url, mdns);
						putSendsResultToLog(taskid, results, outStream, bw);
						putSendLog(taskid, results, outStream, sendlogw, url,msg);
						mdns.clear(); // 发完后清空
					}
					closeLogStream(outStream, bw,reader);
					closeLogStream(sendlogOutStream, sendlogw,reader);
					moveSendToStopdir(taskid);
					stopTaskMap.remove(taskid);
					log.info("------stop----");
					isClose=true;
					log.info("isClose:"+isClose);
					break;
				}
				if (linenum.length > 1) {    
					try {
						if (!Configure.isAsynchronous()) {
							List<ResultEntity> results = pushSend(msg, url,
									linenum[0], linenum[1]);
							// 写入群发日志
							putSendsResultToLog(taskid, results, outStream, bw);
							putSendLog(taskid, results, outStream, sendlogw, url,msg);
							//Thread.sleep(1000);
						}else{
							//异步
							mdns.add(new ResultEntity(linenum[0], linenum[1]));
							if (mdns.size() >= onePushCount) {
								List<ResultEntity> results = pushSendAsy(
										msg, url, mdns);
								putSendsResultToLog(taskid, results, outStream, bw);
								putSendLog(taskid, results, outStream, sendlogw, url,msg);
								mdns.clear(); // 发完后清空
								mdns = new ArrayList<ResultEntity>();
							}
//							
						}
					} catch (Exception e) {
						if(!isClose){
							closeLogStream(sendlogOutStream, sendlogw,reader);
							closeLogStream(outStream, bw,reader);
							isClose=true;}
						log.error(e.getMessage());
					}
				}
			}
			if (mdns.size() > 0) {
				if(outStream!=null&&bw!=null){
					List<ResultEntity> results = pushSendAsy(msg, url, mdns);
					putSendsResultToLog(taskid, results, outStream, bw);
					mdns.clear(); // 发完后清空
				}
			}
			if(!isClose){
					closeLogStream(sendlogOutStream, sendlogw,reader);
					closeLogStream(outStream, bw,reader);
					moveSendToFinishdir(taskid);
					isClose=true;
				}
			}
			
		}
	/**
	 * 关闭群发日志流
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-3
	 * @param outStream
	 * @param bw
	 * @throws IOException
	 */
	private synchronized void closeLogStream(OutputStream outStream, BufferedWriter bw,BufferedReader reader)
			throws IOException {
		
		if (bw != null) {
			bw.flush();
			bw.close();
			bw=null;
		}
		if (outStream != null) {
			outStream.close();
			outStream=null;
		}
		if(reader!=null){
			reader.close();
			reader=null;
		}
	}
	/**
	 * 把返回结果放入到log文件
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-3
	 * @param taskBean
	 * @param results
	 * @param toFile
	 * @param bw
	 * @throws IOException
	 */
	private void putSendsResultToLog(String taskId, List<ResultEntity> results,
			OutputStream toFile, BufferedWriter bw) throws IOException {
		if(results==null)return;
		if (results.size() == 0)
			return;
		for (ResultEntity result : results) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(result.getNum()).append(PushConstant.DEM)
					.append(result.getMdn()).append(PushConstant.DEM)
					.append(result.getNodeCode()).append(PushConstant.DEM)
					.append(result.getFlowId()).append(PushConstant.DEM)
					.append(result.getResult()).append(PushConstant.DEM)
					.append(result.getUseTime()).append(PushConstant.DEM)
					.append(result.getExcpMsg()).append(PushConstant.DEM)
					.append(result.getCount()).append(PushConstant.DEM)
					.append(result.getPosition());
			bw.write(buffer.toString());
			bw.write("\n");
			//bw.flush(); 
			log.info("buffer:"+buffer.toString());
		}
		bw.flush();
	}
	
	private void putSendLog(String taskId, List<ResultEntity> results,
			OutputStream toFile, BufferedWriter bw, String push_url, String msg) throws IOException {
		if(results==null)return;
		if (results.size() == 0)
			return;
		for (ResultEntity result : results) {
			StringBuffer buffer = new StringBuffer();
			
			buffer.append(Time_Complete_FORMAT_with.format(new Date())).append(PushConstant.DEM)
					.append("").append(PushConstant.DEM)
					.append(result.getMdn()).append(PushConstant.DEM)
					.append(result.getNum()).append(PushConstant.DEM)
					.append(push_url).append(PushConstant.DEM)
					.append(java.net.URLEncoder.encode(msg, "utf-8")).append(PushConstant.DEM)
					.append(result.getFlowId()).append(PushConstant.DEM)
					.append(result.getResult()).append(PushConstant.DEM)
					.append(result.getNodeCode()).append(PushConstant.DEM)
					.append(result.getUseTime()).append(PushConstant.DEM)
					.append(result.getExcpMsg()).append(PushConstant.DEM)
					.append(result.getCount()).append(PushConstant.DEM)
					.append(result.getPosition())
					.append("\n");
			
			bw.write(buffer.toString());
			log.debug("buffer:"+buffer.toString());
		}
		bw.flush();
	}
	/**
	 * 移动文件到finish文件夹,转为完成状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param taskid
	 * @throws Exception
	 */
	private void moveSendToFinishdir(String taskid) throws Exception {
		String finishFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.FINISH_DIR + File.separator
				+ taskid + File.separator
				+ PushConstant.DATA_FILE_NAME;
		String sendingFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.SENDING_DIR
				+ File.separator +taskid+ File.separator
				+ PushConstant.DATA_FILE_NAME;
		String finishLogFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.FINISH_DIR + File.separator
				+ taskid + File.separator
				+ PushConstant.LOG_FILE_NAME;
		String sendingLogFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.SENDING_DIR
				+ File.separator + taskid + File.separator
				+ PushConstant.LOG_FILE_NAME;
		// 移到文件
		File datafile = new File(sendingFilePath);
		File logfile = new File(sendingLogFilePath);
		if(datafile==null)return;
		FileUtil.mv(datafile, finishFilePath);
		FileUtil.mv(logfile, finishLogFilePath);
		log.info("move " + datafile.getAbsolutePath() + " to "
				+ finishFilePath);
		// 删除sending下对应taskid的目录
		FileUtil.rm(PushListener.Context_path + File.separator
				+ PushConstant.SENDING_DIR + File.separator
				+ taskid);
	}

	/**
	 * 获取对应taskid的任务状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param taskid
	 * @return
	 */
	public int getTaskStatus(String taskid) {
		for (Entry<Integer, String> entry : getDirPathMap().entrySet()) {
			File[] files = FileUtil.ls(entry.getValue());
			if (files == null)
				continue;
			for (File file : files) {
				if (file.getName().equals(taskid)&&file.list().length>0) {
					int status = entry.getKey();
					log.info(taskid + " status is " + status);
					return status;
				}
			}
		}
		return PushConstant.TASK_NOT_EXIST;
	}
	
	public String getTaskStatusAndLogCnt(String taskid){
		int lineNum=0;
		int status=PushConstant.TASK_NOT_EXIST;
		for (Entry<Integer, String> entry : getDirPathMap().entrySet()) {
			File[] files = FileUtil.ls(entry.getValue());
			if (files == null)
				continue;
			for (File file : files) {
				if (file.getName().equals(taskid)) {
					status = entry.getKey();
					log.info(taskid + " status is " + status);
					File[] taskfiles=file.listFiles();
					if(taskfiles!=null){
						for(File taskfile : taskfiles){
							if(taskfile.getName().equals(PushConstant.LOG_FILE_NAME)){
								try {
									lineNum=FileUtil.getLineNum(taskfile);
									int pcnt=FileUtil.getPcnt(taskfile);
									lineNum=lineNum/pcnt;
								} catch (IOException e) {
									log.error(e.getMessage());
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		return status+"_"+lineNum;
	}
	/**
	 * 停止指定的群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param taskid
	 * @return
	 */
	public int stopTask(String taskid) {
		int status = getTaskStatus(taskid);
		// 任务已完成，停止失败
		if (status == PushConstant.FINISH_STATUS) {
			return status;
		}
		// 任务不存在，停止失败
		if (status == PushConstant.TASK_NOT_EXIST) {
			return status;
		}
		// 任务已处于停止状态
		if (status == PushConstant.STOP_STATUS) {
			return status;
		}
		// 发送状态,设置要停止的任务
		//PushTask.stopTask(Long.valueOf(taskid));
		stopTaskMap.put(taskid,taskid);
		return PushConstant.STOP_SUCCESS;
	}

	/**
	 * 获取对应task的push日志
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	public String getPushLog(String taskid) throws Exception {
		for (Entry<Integer, String> entry : getDirPathMap().entrySet()) {
			if (entry.getKey().equals(PushConstant.READY_STATUS)) {
				// 跳过READY_STATUS状下的目录
				continue;
			}
			File[] files = FileUtil.ls(entry.getValue());
			if(files==null)continue;
			for (File file : files) {
				if (file.getName().equals(taskid)) {
					File[] subfiles = file.listFiles();
					for (File subfile : subfiles) {
						if (subfile.getName()
								.equals(PushConstant.LOG_FILE_NAME)) {
							return FileUtil.readFile(subfile);
						}
					}
				}
			}
		}
		return "";
	}

	/**
	 * 如果任务之前被停止过，激活继续发
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param taskid
	 * @throws Exception
	 */
	public int pushAgain(String taskid) throws Exception {
		int status = getTaskStatus(taskid);
		// 如果任务停止状态
		if (status == PushConstant.STOP_STATUS) {
			if(stopTaskMap.keySet().contains(taskid)){//如果存在则从map去除
				stopTaskMap.remove(taskid);
			}
			String stopFilePath = getDirPathMap().get(PushConstant.STOP_STATUS)
					+ File.separator + taskid + File.separator
					+ PushConstant.DATA_FILE_NAME;
			String stopLogFilePath = getDirPathMap().get(
					PushConstant.STOP_STATUS)
					+ File.separator
					+ taskid
					+ File.separator
					+ PushConstant.LOG_FILE_NAME;
			String readyFilePath = getDirPathMap().get(
					PushConstant.READY_STATUS)
					+ File.separator
					+ taskid
					+ File.separator
					+ PushConstant.DATA_FILE_NAME;
			String readyLogFilePath = getDirPathMap().get(
					PushConstant.READY_STATUS)
					+ File.separator
					+ taskid
					+ File.separator
					+ PushConstant.LOG_FILE_NAME;
			// 移动文件
			File datafile = new File(stopFilePath);
			File logfile = new File(stopLogFilePath);
			FileUtil.mv(datafile, readyFilePath);
			FileUtil.mv(logfile, readyLogFilePath);
			log.info("move " + datafile.getAbsolutePath() + " to "
					+ readyFilePath);
			log.info("move " + logfile.getAbsolutePath() + " to "
					+ stopLogFilePath);
			// 删除sending下对应taskid的目录
			FileUtil.rm(Configure.getPushFilePath() + File.separator
					+ PushConstant.STOP_DIR + File.separator + taskid);
			triggerReadTask(taskid);
			return PushConstant.ACTIVATE_SUCCESS;
		}
		return status;
	}

	/**
	 * 删除任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-2
	 * @param taskid
	 * @return
	 * @throws Exception
	 */
	public int delTask(String taskid) throws Exception {
		for (Entry<Integer, String> entry : getDirPathMap().entrySet()) {
			File[] files = FileUtil.ls(entry.getValue());
			if (files == null)
				continue;
			for (File file : files) {
				if (file.getName().equals(taskid)) {
					int status = entry.getKey();
					// 如果不是处于发送状态，删除文件
					if (status != PushConstant.SENDING_STATUS) {
						log.info(taskid + " is deleted :status" + status);
						FileUtil.rm(file.getAbsolutePath());
						return PushConstant.DEL_SUCCESS;
					}
					return PushConstant.DEL_FAIL;
				}
			}
		}
		return PushConstant.TASK_NOT_EXIST;
	}

	/**
	 * 触发读取ready目录对应的任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-3
	 * @param taskid
	 * @throws InterruptedException
	 */
	public static void triggerReadTask(String taskid)
			throws InterruptedException {
		readyQueue.put(taskid);
	}

	/**
	 * 从任务队列里面获取一条任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-3
	 * @return
	 * @throws InterruptedException
	 */
	public static String takeReadTask() throws InterruptedException {
		return readyQueue.take();
	}
	/**
	 * 移动文件到stop文件夹,转为停止状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 * @param taskId
	 * @throws Exception
	 */
	private void moveSendToStopdir(String taskId) throws Exception {
		String stopFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.STOP_DIR + File.separator
				+ taskId + File.separator
				+ PushConstant.DATA_FILE_NAME;
		String stopLogFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.STOP_DIR + File.separator
				+ taskId + File.separator
				+ PushConstant.LOG_FILE_NAME;
		String sendingFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.SENDING_DIR
				+ File.separator + taskId + File.separator
				+ PushConstant.DATA_FILE_NAME;
		String sendingLogFilePath = PushListener.getContextPath()
				+ File.separator + PushConstant.SENDING_DIR
				+ File.separator + taskId + File.separator
				+ PushConstant.LOG_FILE_NAME;
		// 移到文件
		File datafile = new File(sendingFilePath);
		File logfile = new File(sendingLogFilePath);
		if (datafile.exists()) {
			FileUtil.mv(datafile, stopFilePath);
			log.info("move " + datafile.getAbsolutePath() + " to "
					+ stopFilePath);
		}
		if (logfile.exists()) {
			FileUtil.mv(logfile, stopLogFilePath);
			log.info("move " + logfile.getAbsolutePath() + " to "
					+ stopLogFilePath);
		}
		// 删除sending下对应taskid的目录
		FileUtil.rm(PushConstant.USER_DIR + File.separator
				+ PushConstant.SENDING_DIR + File.separator
				+ taskId);
	}
	public static void main(String[] args) {
//		new Thread(
//				new Runnable() {
//					@Override
//					public void run() {
//						for(int i=0;i<8000;i++){
//							try {
//								TaskService.triggerReadTask(String.valueOf(i));
//							} catch (InterruptedException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//		).start();
//		new Thread(
//				new Runnable() {
//					
//					@Override
//					public void run() {
//						while(true){
//						
//					try {
//						String taskid=TaskService.takeReadTask();
//						System.out.println("taskid:"+taskid);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//						}
//				}
//					}
//		).start();
		while(true){
			System.out.println("-------a----------");
			if(true){
				System.out.println("-------b----------");
				return;
			}
			System.out.println("---------c--------");
		}
	}
}
