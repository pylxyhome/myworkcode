package cn.gzjp.push2.work;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.push2.bean.SendsResult;
import cn.gzjp.push2.bean.TaskBean;
import cn.gzjp.push2.constant.PushConstant;
import cn.gzjp.push2.listener.PushListener;
import cn.gzjp.push2.sgip.Configure;
import cn.gzjp.push2.sgip.ResultEntity;
import cn.gzjp.util.FileUtil;
import cn.gzjp.util.StrUtil;

/**
 * 按群发顺序发push任务类
 * 
 * @description
 * @author panyl
 * @date 2011-12-1
 */
public class PushTask {

	final TaskService taskService = new TaskService();
	private static Log log = LogFactory.getLog(PushTask.class);
	private ExecutorService service = Executors.newCachedThreadPool();

	/**
	 * 任务消费线程
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-7
	 * @return
	 */
	public Consumer newConsumerThread() {
		return new Consumer();
	}

	/**
	 * 任务生产线程
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-7
	 * @return
	 */
	public Producer newProducerThread() {
		return new Producer();
	}

	/**
	 * 设置要停止的任务id
	 */
	public static long stopTaskId = 0l;
	/**
	 * 是否停止检测ready目录
	 */
	public static boolean stopCheckReady = false;
	/**
	 * 是否全部停止群发
	 */
	public static boolean stopPush = false;
	/**
	 * 检测ready目录 时间间隔（毫秒）
	 */
	public static long CheckReadyTimeInterval = 2000;
	/**
	 * 每一个线程执行群发任务间隔时间（毫秒）
	 */
	public static long push_interval = 20;
	/**
	 * 检测Ready目录时间间隔（毫秒）
	 */
	public static long checkReady_interval = 200;
	/**
	 * 异步时第次发送的号码量
	 */
	public static int onePushCount = Configure.getOnePushCount();
	/**
	 * 是否已初始化了ready目录 和sending目录
	 */
	public static Boolean firstInit = false;

	/**
	 * 执行群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 */
	class Consumer implements Runnable {
		public void run() {
			try {
				// sending();
			} catch (Exception ex) {
				log.info(ex.getMessage());
				ex.printStackTrace();
			}
		}

		/**
		 * 把返回结果放入到log文件
		 * 
		 * @description
		 * @author panyl
		 * @param taskBean
		 * @date 2011-12-2
		 * @param results
		 * @throws IOException
		 */
		@Deprecated
		private void putSendsResultToLog(TaskBean taskBean,
				List<SendsResult> results) throws IOException {
			String sendingLogFilePath = PushConstant.SENDING_FULL_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
					+ PushConstant.LOG_FILE_NAME;
			FileUtil.mkdir(sendingLogFilePath, false); // 创建目标文件路径
			OutputStream toFile = new FileOutputStream(sendingLogFilePath, true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					toFile));
			for (SendsResult result : results) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(result.getNum()).append(PushConstant.DEM)
						.append(result.getMdn()).append(PushConstant.DEM)
						.append(result.getNodeCode()).append(PushConstant.DEM)
						.append(result.getFlowId()).append(PushConstant.DEM)
						.append(result.getResult()).append(PushConstant.DEM)
						.append(result.getUseTime()).append(PushConstant.DEM)
						.append(result.getExcpMsg());
				bw.write(buffer.toString());
				bw.write("\n");
			}
			bw.flush();
			bw.close();
			toFile.close();
		}

	}

	/**
	 * 监听群发任务,生成任务bean
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 */
	class Producer implements Runnable {
		public void run() {
			try {
				synchronized (firstInit) {
					if (!firstInit) {
						initReadyDir();
						log.info("----init ready dir finish---");
						initSendingDir();
						log.info("----init sending dir finish---");
						firstInit = true;
					}
				}
				while (!stopCheckReady) {
					long startTime=System.currentTimeMillis();
					final String readTaskid = TaskService.takeReadTask();
					File file = new File(PushListener.Context_path
							+ File.separator + PushConstant.READY_DIR
							+ File.separator + readTaskid);
					log.info("check task start: " + System.currentTimeMillis());
					if (file != null && file.isDirectory()) {
						File[] subfiles = file.listFiles();
						taskService.changeReadyFileToTask(subfiles,readTaskid);
					}
					log.info("check task end: " + System.currentTimeMillis());
					long endTime=System.currentTimeMillis();
					log.info("user time:"+(endTime-startTime));
					Thread.sleep(checkReady_interval);
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
				ex.printStackTrace();
			}
		}

		/**
		 * 检测readyDir
		 * 
		 * @description
		 * @author panyl
		 * @throws InterruptedException
		 * @date 2011-12-9
		 */
		private void initReadyDir() throws InterruptedException {
			File[] files = FileUtil.ls(PushListener.getContextPath()
					+ File.separator + PushConstant.READY_DIR);
			if (files == null)
				return;
			for (File file : files) {
				String taskid = file.getName();
				if (!StrUtil.isNumeric(taskid))
					continue;
				TaskService.triggerReadTask(taskid);
			}

		}
		/**
		 * 
		 * @description
		 * @author panyl
		 * @date 2011-12-10
		 * @param taskBean
		 * @throws InterruptedException
		 * @throws Exception
		 * @throws FileNotFoundException
		 * @throws IOException
		 */
		@Deprecated
		private void sending(TaskBean taskBean) throws InterruptedException,
				Exception, FileNotFoundException, IOException {
			// while(!stopPush){
			// TaskBean taskBean=taskService.getTaskFromTaskQueue();
			if (PushTask.stopTaskId == taskBean.getTaskid()) {
				moveSendToStopdir(taskBean);
				// continue;
			}
			log.info("Startline:" + taskBean.getStartline());
			String[] lines = taskBean.getMdns().split("\n");
			String sendingLogFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.SENDING_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
					+ PushConstant.LOG_FILE_NAME;
			OutputStream outStream = new FileOutputStream(sendingLogFilePath,
					true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					outStream));
			List<ResultEntity> mdns = new ArrayList<ResultEntity>();
			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				if (!line.contains("|"))
					continue;
				if (PushTask.stopTaskId == taskBean.getTaskid()) {
					log.info("stop task:" + taskBean.getTaskid());
					moveSendToStopdir(taskBean);
					break;
				}
				String[] items = line.toString().split("\\|");
				if (Integer.valueOf(items[0]) < taskBean.getStartline())
					continue;
				if (!Configure.isAsynchronous()) {
					List<ResultEntity> results = taskService.pushSend(
							taskBean.getMsg(), taskBean.getUrl(), items[0],
							items[1]);
					putSendsResultToLog(taskBean, results, outStream, bw);
				} else {
					mdns.add(new ResultEntity(items[0], items[1]));
					if (mdns.size() >= onePushCount) {
						List<ResultEntity> results = taskService.pushSendAsy(
								taskBean.getMsg(), taskBean.getUrl(), mdns);
						putSendsResultToLog(taskBean, results, outStream, bw);
						mdns.clear(); // 发完后清空
						mdns = new ArrayList<ResultEntity>();
					}
					if (i == lines.length - 1 && mdns.size() > 0) {
						List<ResultEntity> results = taskService.pushSendAsy(
								taskBean.getMsg(), taskBean.getUrl(), mdns);
						putSendsResultToLog(taskBean, results, outStream, bw);
					}
				}
			}
			closeLogStream(outStream, bw);
			moveSendToFinishdir(taskBean);
			log.info("task：" + taskBean.getTaskid() + " finish ");
			// Thread.sleep(push_interval);
			// }
		}

		/**
		 * 启动时先检测sending目录的任务并放入队列中
		 * 
		 * @description
		 * @author panyl
		 * @throws Exception
		 * @throws InterruptedException
		 * @date 2011-12-2
		 */
		private void initSendingDir() throws InterruptedException, Exception {
			File[] files = FileUtil.ls(PushListener.Context_path
					+ File.separator + PushConstant.SENDING_DIR);
			if (files == null)
				return;
			for (File file : files) {
				String taskid = file.getName();
				if (!StrUtil.isNumeric(taskid))
					continue;
					if (file.isDirectory()) {
						File[] subfiles = file.listFiles();
						if(subfiles==null)continue;
						taskService.changeSendingFileToTask(subfiles,taskid);
					}
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
		@Deprecated
		private void putSendsResultToLog(TaskBean taskBean,
				List<ResultEntity> results, OutputStream toFile,
				BufferedWriter bw) throws IOException {
			if (results.size() == 0)
				return;
			String sendingLogFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.SENDING_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
					+ PushConstant.LOG_FILE_NAME;
			FileUtil.mkdir(sendingLogFilePath, false); // 创建目标文件路径
			for (ResultEntity result : results) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(result.getNum()).append(PushConstant.DEM)
						.append(result.getMdn()).append(PushConstant.DEM)
						.append(result.getNodeCode()).append(PushConstant.DEM)
						.append(result.getFlowId()).append(PushConstant.DEM)
						.append(result.getResult()).append(PushConstant.DEM)
						.append(result.getUseTime()).append(PushConstant.DEM)
						.append(result.getExcpMsg());
				bw.write(buffer.toString());
				bw.write("\n");
			}
			bw.flush();
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
		private void closeLogStream(OutputStream outStream, BufferedWriter bw)
				throws IOException {
			if (bw != null) {
				bw.flush();
				bw.close();
			}
			if (outStream != null) {
				outStream.close();
			}
		}

		/**
		 * 移动文件到finish文件夹,转为完成状态
		 * 
		 * @description
		 * @author panyl
		 * @date 2011-12-1
		 * @param taskBean
		 * @throws Exception
		 */
		@Deprecated
		private void moveSendToFinishdir(TaskBean taskBean) throws Exception {
			String finishFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.FINISH_DIR + File.separator
					+ taskBean.getTaskid() + File.separator
					+ PushConstant.DATA_FILE_NAME;
			String sendingFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.SENDING_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
					+ PushConstant.DATA_FILE_NAME;
			String finishLogFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.FINISH_DIR + File.separator
					+ taskBean.getTaskid() + File.separator
					+ PushConstant.LOG_FILE_NAME;
			String sendingLogFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.SENDING_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
					+ PushConstant.LOG_FILE_NAME;
			// 移到文件
			File datafile = new File(sendingFilePath);
			File logfile = new File(sendingLogFilePath);
			FileUtil.mv(datafile, finishFilePath);
			FileUtil.mv(logfile, finishLogFilePath);
			log.info("move " + datafile.getAbsolutePath() + " to "
					+ finishFilePath);
			// 删除sending下对应taskid的目录
			FileUtil.rm(PushListener.Context_path + File.separator
					+ PushConstant.SENDING_DIR + File.separator
					+ taskBean.getTaskid());
		}

		/**
		 * 移动文件到stop文件夹,转为停止状态
		 * 
		 * @description
		 * @author panyl
		 * @date 2011-12-1
		 * @param taskBean
		 * @throws Exception
		 */
		@Deprecated
		private void moveSendToStopdir(TaskBean taskBean) throws Exception {
			String stopFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.STOP_DIR + File.separator
					+ taskBean.getTaskid() + File.separator
					+ PushConstant.DATA_FILE_NAME;
			String stopLogFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.STOP_DIR + File.separator
					+ taskBean.getTaskid() + File.separator
					+ PushConstant.LOG_FILE_NAME;
			String sendingFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.SENDING_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
					+ PushConstant.DATA_FILE_NAME;
			String sendingLogFilePath = PushListener.getContextPath()
					+ File.separator + PushConstant.SENDING_DIR
					+ File.separator + taskBean.getTaskid() + File.separator
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
					+ taskBean.getTaskid());
		}
	}

	/**
	 * 启动群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1 生产任务线程数1个
	 * @param cosumerCount
	 *            执行群发任务线程数
	 */
	public void startPushTask(int cosumerCount) {
		startPushTask(1, cosumerCount);
	}

	/**
	 * 启动群发任务，生产任务线程数和执行群发任务线程数都为1个
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-3
	 */
	public void startPushTask() {
		startPushTask(1, 1);
	}

	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-9
	 * @param ProducerCount
	 */
	public void startPushProducerTask(int ProducerCount) {
		startPushTask(ProducerCount, 0);
	}

	public static void stopTask(long taskId) {
		stopTaskId = taskId;
	}

	/**
	 * 启动群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-3
	 * @param ProducerCount
	 *            生成任务线程数
	 * @param cosumerCount
	 *            执行任务线程数
	 */
	public void startPushTask(int ProducerCount, int cosumerCount) {
		stopCheckReady = false;
		stopPush = false;
		service = Executors.newCachedThreadPool();
		PushTask pushTask = new PushTask();
		for (int i = 0; i < ProducerCount; i++) {
			service.submit(pushTask.newProducerThread());
		}
		for (int i = 0; i < cosumerCount; i++) {
			service.submit(pushTask.newConsumerThread());
		}
		log.info("----startPushTask----");
	}

	/**
	 * 停止所有群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-1
	 */
	public void shutDownPushTask() {
		stopCheckReady = true;
		stopPush = true;
		try {
			Thread.sleep(1000);
			service.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.debug("----shutDownPushTask----");
	}

	public static void main(String[] args) {
		// ExecutorService service = Executors.newCachedThreadPool();
		// PushTask pushTask=new PushTask();
		// Producer producer = pushTask.new Producer();
		//
		// Consumer consumer = pushTask.new Consumer();
		// Consumer consumer2 = pushTask.new Consumer();
		// Consumer consumer3 = pushTask.new Consumer();
		// Consumer consumer4 = pushTask.new Consumer();
		// service.submit(producer);
		// service.submit(consumer);
		// service.submit(consumer2);
		// service.submit(consumer3);
		// service.submit(consumer4);
		// //程序运行5s后,所有任务停止
		// try{
		// Thread.sleep(5000);
		// }catch(InterruptedException ex){
		// }
		// service.shutdownNow();
		System.out.println(PushConstant.USER_DIR + File.separator
				+ PushConstant.READY_DIR);
		File[] files = FileUtil.ls(PushConstant.USER_DIR + File.separator
				+ PushConstant.READY_DIR);
		for (File file : files) {
			if (file.isDirectory()) {
				File[] subfile = file.listFiles();
				System.out.println(subfile[0].getName());
			}
		}
	}
}
