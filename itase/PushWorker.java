package cn.gzjp.gomp.server.push2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.gomp.client.constant.PushConstant;
import cn.gzjp.gomp.client.constant.R;
import cn.gzjp.gomp.client.entity.SmsArrangeItem;
import cn.gzjp.gomp.server.Jdbc;
import cn.gzjp.gomp.server.generateplanfile.Configure;
import cn.gzjp.gomp.server.mail.SendMail;
import cn.gzjp.gomp.server.util.WorkUtils;
import cn.gzjp.push.web.client.ResultCallBack;
import cn.gzjp.push.web.client.SenderService;
import cn.gzjp.push.web.client.SendsResult;

/**
 * 
 * @description
 * @author panyl
 * @date 2011-12-14
 */
public class PushWorker implements Runnable {

	private long iid;
	/**
	 * 是否停止检测任务状态
	 */
	private static boolean stopCheckStatus = false;
	private final static Logger log = LoggerFactory.getLogger(PushWorker.class);
	private SenderService senderService;

	/**
	 * 检查状态时间间隔(毫秒)
	 */
	public final static int CHECK_STATE_TIME = 10000;
	public final static int ACCESS_TIME_MAX = 5;
	public final static String COMMA = ",";
	/**
	 * 批量
	 */
	private static int BatchAmount=2000;
	private String nodeCode = "";
	/**
	 * 检查状态worker类型
	 */
	public final static int CHECK_STATUS_TYPE=1;
	/**
	 * 直接log worker类型
	 */
	public final static int LOG_TYPE=2;
	
	private int workerType=CHECK_STATUS_TYPE;
	
	private int logCnt=-1;
	
	private static ConcurrentHashMap<Long, Long> logTreadMap=new ConcurrentHashMap<Long, Long>();
	private static ConcurrentHashMap<Long, Long> stopTreadMap=new ConcurrentHashMap<Long, Long>();
	private static ConcurrentHashMap<String, String> sendEmailMmap=new ConcurrentHashMap<String, String>();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 启动worker
	 * 默认启动worker类型为CHECK_STATUS_TYPE
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 */
	public void startWorker() {
		Thread thread = new Thread(this);
		thread.start();
	}
	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @param workerType
	 */
	public void startWorker(int workerType) {
		this.workerType=workerType;
		if(workerType==PushWorker.LOG_TYPE){
			if(logTreadMap.containsKey(iid))return;
		}
		logTreadMap.put(iid, iid);
		Thread thread = new Thread(this);
		thread.start();
	}
	public static void putStopTreadMap(Long iid){
		stopTreadMap.put(iid,iid);
	}
	/**
	 * @param iid
	 * @param portURL
	 *            端口url
	 * @throws Exception
	 */
	public PushWorker(long iid, String portURL) throws Exception {
		this.iid = iid;
		nodeCode = getNodeCodeByiid(iid);
		if ("".equals(nodeCode)) {
			String errormsg = "gp_push_plan-》iid :" + iid
					+ " nodecode is null,putlog fail!!";
			log.debug(errormsg);
			throw new Exception(errormsg);
		}
		senderService = SenderService.getInstance(nodeCode, portURL);
	}

	public void putResultToMap(String sid, String state) {

	}

	@Override
	public void run() {
		try {
			while (!stopCheckStatus) {
				if(workerType==PushWorker.CHECK_STATUS_TYPE){
					int status=checkTaskStatus();
					if(status == PushConstant.FINISH_STATUS||status == PushConstant.STOP_STATUS||
							status==PushConstant.TASK_NOT_EXIST){
						break;
					}
					Thread.sleep(Configure.get().getCheckStatusTime1());
				}else if(workerType==PushWorker.LOG_TYPE){
					senderService.logGzip(iid, new ResultCallBackImpl());
					logTreadMap.remove(iid);  //完成log统计后从logTreadMap中移除
					break;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	public static void sendExceptionEmail(int tid,int excpType,String title,String body)throws SQLException, ParseException{
		String keystr=excpType+"_"+tid;
		if(sendEmailMmap.containsKey(keystr)){
			String dateStr=sendEmailMmap.get(keystr);
			Date oldDate=sdf.parse(dateStr);
			Calendar oldcalendar=Calendar.getInstance();
			oldcalendar.setTime(oldDate);
			Calendar nowcalendar=Calendar.getInstance();
			nowcalendar.setTime(new Date());
			int oldyear=nowcalendar.get(Calendar.YEAR);
			int nowyear=nowcalendar.get(Calendar.YEAR);
			boolean notallowSendEmail=true;
			if(nowyear>oldyear){
				notallowSendEmail=false;
			}
			int oldmonth=nowcalendar.get(Calendar.MONTH);
			int nowmonth=nowcalendar.get(Calendar.MONTH);
			if(notallowSendEmail&&nowmonth>oldmonth){
				notallowSendEmail=false;
			}
			int oldday=nowcalendar.get(Calendar.DAY_OF_MONTH);
			int nowday=nowcalendar.get(Calendar.DAY_OF_MONTH);
			if(notallowSendEmail&&nowday>oldday){
				notallowSendEmail=false;
			}
			int oldminute=nowcalendar.get(Calendar.MINUTE);
			int nowminute=nowcalendar.get(Calendar.MINUTE);
			if(notallowSendEmail&&nowminute>oldminute&&(nowminute-oldminute)>Configure.get().getSendEmailTime()){
				notallowSendEmail=false;
			}
			if(!notallowSendEmail){
				new Thread(new SendMail(title,body.toString(),Jdbc.getConnection())).start();
				sendEmailMmap.put(keystr, sdf.format(new Date()));
			}
		}else{
			sendEmailMmap.put(keystr, sdf.format(new Date()));
			new Thread(new SendMail(title,body.toString(),Jdbc.getConnection())).start();
		}
		
	}
	private int checkTaskStatus() throws Exception, SQLException,
			InterruptedException {
		String statustr="-100";
		try{
				statustr = senderService.statusAndSid(iid);
			}catch(Exception e){
				log.debug(e.getMessage());
				statustr="-100";
		}
		int status=0;
		if(statustr.equals(String.valueOf(PushConstant.NETWORK_ERROR))){//无法访问接口的异常
			log.info("NETWORK_ERROR");
			this.setPushPlanItemState(iid, R.pushPlan.webpush_exception);
			int tid=this.getTaskIdByiid(iid);
			//发邮件
			String title = "与push端口连接异常";
			StringBuffer body = new StringBuffer("tid：" +tid + "，nodeCode:"+this.nodeCode+",请查看itaste与push端口连接是否正常" + "<br/><br/>"
			  + "异常发生时间：" +sdf.format(new Date()));
			sendExceptionEmail(tid,R.pushPlan.webpush_exception,title,body.toString());
			return PushConstant.NETWORK_ERROR;
		}
		String[] statusArray=statustr.split("_");
		status=Integer.valueOf(statusArray[0]);
		int templogCnt=Integer.valueOf(statusArray[1]);
		if(status==PushConstant.TASK_NOT_EXIST){
			log.info("task not exist!!");
			return status;
		}
		int tid=this.getTaskIdByiid(iid);
		log.info(" iid is : " + iid);
		if (status == PushConstant.READY_STATUS) {// 准备中
			// 更新gp_push_plan状态
			if(!stopTreadMap.containsKey(iid)){
				this.setPushPlanItemState(iid, R.pushPlan.ready_status);
			}
		}
		if (status == PushConstant.SENDING_STATUS) {// 发送中
			// 更新gp_push_plan状态
			//setPushPlanItemSending(iid);
			if(logCnt==templogCnt){
				this.setPushPlanItemState(iid, R.pushPlan.send_exception);
				//发邮件
				String title = "push端口发送异常";
				StringBuffer body = new StringBuffer("tid：" +tid + "，nodeCode:"+this.nodeCode+",请查看push端口与网关接口连接是否正常" + "<br/><br/>"
				  + "异常发生时间：" + sdf.format(new Date()));
				sendExceptionEmail(tid,R.pushPlan.send_exception,title,body.toString());
			}else{
				if(!stopTreadMap.containsKey(iid)){
					this.setPushPlanItemSending(iid);
				}
				this.logCnt=templogCnt;
				//LogNum
				this.updatePushPlanItemLogNum(iid, templogCnt);
			}
			return status;
		}
		if (status == PushConstant.FINISH_STATUS) {// 完成 状态
			senderService.logGzip(iid, new ResultCallBackImpl());
			// 更新gp_push_plan状态
			if(!stopTreadMap.containsKey(iid)){
				setPushPlanItemFinish(iid);
			}
			String keyStr=nodeCode+"_"+tid;
			if(sendEmailMmap.containsKey(keyStr)){
				sendEmailMmap.keySet().remove(keyStr);
			}
			return status;
		}
		if (status == PushConstant.STOP_STATUS) {
			senderService.logGzip(iid, new ResultCallBackImpl());
			if(stopTreadMap.containsKey(iid)){
				this.setPushPlanItemState(iid, R.pushPlan.unarrange_status);
				stopTreadMap.keySet().remove(iid);
			}else{
				this.setPushPlanItemSuspend(iid);
			}
			return status;
		}
		return status;
	}

	/**
	 * 停止检测任务状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 */
	public static void stopCheck() {
		stopCheckStatus = true;
	}
	/**
	 * List方式存储结果
	 * @author Administrator
	 *
	 */
	class ResultCallBackImpl implements ResultCallBack {
		@Override
		public void process(Object[] results) throws SQLException {
			log.info("into process");
			String nodeCode = getNodeCodeByiid(iid);
			log.info("nodeCode:" + nodeCode);
			if ("".equals(nodeCode)) {
				log.error("gp_push_plan-》iid :" + iid
						+ " nodecode is null,putlog fail!!");
				return;
			}
			Connection conn = null;
			Connection updateItemconn = null;
			PreparedStatement updateItempreStat=null;
			try {
				conn = Jdbc.getConn(WorkUtils.getProvinceCode(nodeCode));
				StringBuffer sql = new StringBuffer("replace into gp_send_log")
						.append("(sid,mdn,nodeCode,flowid,send_result,atime,pcnt,position)")
						.append(" values ");
				updateItemconn = Jdbc.getConn(WorkUtils
						.getProvinceCode(nodeCode));
				updateItemconn.setAutoCommit(false);
				StringBuffer updateItemSql = new StringBuffer(
						"update gp_send_item set state=? where sid=?");
				updateItempreStat = conn
						.prepareStatement(updateItemSql.toString());
				StringBuffer values = new StringBuffer("");
				List<Integer> resultList = new ArrayList<Integer>();
				for (int i = 0; i < results.length; i++) {
					Object line = results[i];
					String[] items = line.toString().split("\\|");
					if (items.length > 8) {
						int sid = Integer.valueOf(items[0]);
						int result=-1;
						if(items[4]!=null&&!"null".equals(items[4])){
							result = Integer.valueOf(items[4]);
						}
						int pcnt = Integer.valueOf(items[7]);
						int position = Integer.valueOf(items[8]);
						resultList.add(result);
						if (pcnt == position) {
							int state = R.sendItem.send_success_status;
							for (int j = 0; j < resultList.size(); j++) {
								Integer re = resultList.get(j);
								if (0 != re) {
									state = R.sendItem.fail_status;
									break;
								}
							}
							resultList.clear();
							updateItempreStat.setInt(1, state);
							updateItempreStat.setInt(2, sid);
							updateItempreStat.addBatch();
						}
						values.append("(").append(sid).append(COMMA)
								.append("'" + items[1] + "'").append(COMMA)
								.append("'" + items[2] + "'").append(COMMA)
								.append("'" + items[3] + "'").append(COMMA)
								.append(result).append(COMMA).append(items[5])
								.append(COMMA).append(pcnt).append(COMMA)
								.append(position).append(")").append(COMMA);
						if ((i + 1) % (pcnt * BatchAmount) == 0
								|| i == (results.length - 1)) {
							log.debug("statistics--iid："+iid);
							if (!values.equals("")) {
								values.deleteCharAt(values.length() - 1);
							}
							PreparedStatement preStat=null;
							try{
								preStat = conn
										.prepareStatement(sql.toString()
												+ values.toString());
								preStat.execute();
								values = new StringBuffer("");
								updateItempreStat.executeBatch();
								updateItemconn.commit();
								updateItempreStat.clearBatch();
							}catch(SQLException e){
								log.error(e.getMessage());
								e.printStackTrace();
							}finally{
								if(preStat!=null)preStat.close();
							}
						}
					}
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} finally {
				if(updateItempreStat!=null)updateItempreStat.close();
				if (conn != null)
					conn.close();
				if (updateItemconn != null)
					updateItemconn.close();
			}
			log.info("into process end");
			taskSendStatist(iid);
		}
	}
	/**
	 * Map方式存储结果
	 * @author Administrator
	 *
	 */
	class ResultCallBackImpl2 implements ResultCallBack {
		@Override
		public void process(Object[] results) throws SQLException {
			log.info("into process");
			String nodeCode = getNodeCodeByiid(iid);
			log.info("nodeCode:" + nodeCode);
			if ("".equals(nodeCode)) {
				log.error("gp_push_plan-》iid :" + iid
						+ " nodecode is null,putlog fail!!");
				return;
			}
			Connection conn = null;
			Connection updateItemconn = null;
			PreparedStatement updateItempreStat=null;
			try {
				conn = Jdbc.getConn(WorkUtils.getProvinceCode(nodeCode));
				StringBuffer sql = new StringBuffer("replace into gp_send_log")
						.append("(sid,mdn,nodeCode,flowid,send_result,atime,pcnt,position)")
						.append(" values ");
				updateItemconn = Jdbc.getConn(WorkUtils
						.getProvinceCode(nodeCode));
				updateItemconn.setAutoCommit(false);
				StringBuffer updateItemSql = new StringBuffer(
						"update gp_send_item set state=? where sid=?");
				updateItempreStat = conn
						.prepareStatement(updateItemSql.toString());
				StringBuffer values = new StringBuffer("");
				
				Map<Integer,String> resultMap=new HashMap<Integer ,String>();
				
				for (int i = 0; i < results.length; i++) {
					Object line = results[i];
					String[] items = line.toString().split("\\|");
					if (items.length > 8) {
						Integer sid = Integer.valueOf(items[0]);
						int result=-1;
						if(items[4]!=null&&!"null".equals(items[4])){
							result = Integer.valueOf(items[4]);
						}
						int pcnt = Integer.valueOf(items[7]);
						int position = Integer.valueOf(items[8]);
						if(resultMap.containsKey(sid)){
							String _result=resultMap.get(sid);
							_result=_result+"_"+result;
							resultMap.put(sid, _result);
						}else{
							resultMap.put(sid, String.valueOf(result));
						}
						
						values.append("(").append(sid).append(COMMA)
								.append("'" + items[1] + "'").append(COMMA)
								.append("'" + items[2] + "'").append(COMMA)
								.append("'" + items[3] + "'").append(COMMA)
								.append(result).append(COMMA).append(items[5])
								.append(COMMA).append(pcnt).append(COMMA)
								.append(position).append(")").append(COMMA);
						if ((i + 1) % (pcnt * BatchAmount) == 0
								|| i == (results.length - 1)) {
							log.debug("statistics--iid："+iid);
							if (!values.equals("")) {
								values.deleteCharAt(values.length() - 1);
							}
							PreparedStatement preStat=null;
							try{
								preStat = conn
										.prepareStatement(sql.toString()
												+ values.toString());
								preStat.execute();
								values = new StringBuffer("");
								Set<Integer> delSet=new HashSet<Integer>();
								for(Entry<Integer, String> entry : resultMap.entrySet()){
									String resultstr=entry.getValue();
									if(pcnt==1&&!resultstr.contains("_")){
										updateItempreStat.setInt(1, Integer.valueOf(resultstr));
										updateItempreStat.setInt(2, entry.getKey());
										updateItempreStat.addBatch();
										delSet.add(entry.getKey());
									}
									if(pcnt>1&&resultstr.split("_").length==pcnt){
										String[] resultArray=resultstr.split("_");
										int re=R.sendItem.send_success_status;
										for(int j=0;j<resultArray.length;j++){
											String r=resultArray[j];
											if(!r.equals("0")){
												re=R.sendItem.fail_status;
											}
										}
										updateItempreStat.setInt(1, re);
										updateItempreStat.setInt(2, entry.getKey());
										updateItempreStat.addBatch();
										delSet.add(entry.getKey());
									}
								}
								resultMap.keySet().removeAll(delSet);
								updateItempreStat.executeBatch();
								updateItemconn.commit();
								updateItempreStat.clearBatch();
								
							}catch(SQLException e){
								e.printStackTrace();
							}finally{
								if(preStat!=null)preStat.close();
							}
						}
					}
				}
			} catch (SQLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			} finally {
				if(updateItempreStat!=null)updateItempreStat.close();
				if (conn != null)
					conn.close();
				if (updateItemconn != null)
					updateItemconn.close();
			}
			log.info("into process end");
			taskSendStatist(iid);
		}
	}
	/**
	 * 初始化StateMap
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @param stateMap
	 */
	private void initStateMap(Map<Integer,Integer> stateMap){
		if(stateMap!=null){
			stateMap.put(R.sendItem.ready_status, 0);
			stateMap.put(R.sendItem.send_success_status, 0);
			stateMap.put(R.sendItem.fail_status, 0);
			stateMap.put(R.sendItem.black_status, 0);
			stateMap.put(R.sendItem.down_success_status, 0);
			stateMap.put(R.sendItem.down_success_fail, 0);
		}
	}
	public PushWorker(String nodeCode){this.nodeCode=nodeCode;}
	/**
	 * 任务发送情况统计 
	 * @description
	 * @author panyl
	 * @throws SQLException 
	 * @date 2011-12-19
	 */
	public void taskSendStatist(long iid) throws SQLException{
		log.info("--start taskSendStatist--");
		int taskid=this.getTaskIdByiid(iid);
		if(taskid<=0)return;
		Map<Integer,Integer> stateMap=new HashMap<Integer,Integer>();
		initStateMap(stateMap);
		String sql="SELECT COUNT(*),state FROM gp_send_item where tid=? GROUP BY state";
		Connection connection = null;
		PreparedStatement preStat =null;
		ResultSet rs=null;
		try{
			connection = Jdbc.getConn(WorkUtils.getProvinceCode(nodeCode));
			preStat=connection.prepareStatement(sql);
			preStat.setInt(1, taskid);
			rs= preStat.executeQuery();
			while(rs.next()){
				Integer state=rs.getInt(2);
				Integer allCount=rs.getInt(1);
				stateMap.put(state, allCount);
			}
//			for(Entry<Integer,Integer> entry : stateMap.entrySet()){
//				log.info("key:"+entry.getKey()+" value:"+entry.getValue());
//			}
			updateTask(taskid,stateMap);
		}catch(SQLException e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			if(rs!=null)rs.close();
			if(preStat!=null)preStat.close();
			if(connection!=null)connection.close();
		}
		log.info("--taskSendStatist end--");
	}
	/**
	 * 更新gp_task
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @param taskid
	 * @param stateMap
	 * @throws SQLException
	 */
	private void updateTask(int taskid,Map<Integer,Integer> stateMap)throws SQLException{
		String sql="update gp_task set unsend_num=?,send_num=?,send_err_num=?,black_num=?,send_success=? where id=?";
		Connection connection = null;
		PreparedStatement preStat =null;
		try{
			connection = Jdbc.getConnection();
			preStat=connection.prepareStatement(sql);
			preStat.setInt(1, stateMap.get(R.sendItem.ready_status));
			preStat.setInt(2, stateMap.get(R.sendItem.send_success_status));
			preStat.setInt(3, stateMap.get(R.sendItem.fail_status));
			preStat.setInt(4, stateMap.get(R.sendItem.black_status));
			preStat.setInt(5, stateMap.get(R.sendItem.send_success_status)+stateMap.get(R.sendItem.down_success_status)+stateMap.get(R.sendItem.down_success_fail));
			preStat.setInt(6, taskid);
			preStat.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
			log.error(e.getMessage());
		}finally{
			if(preStat!=null)preStat.close();
			if(connection!=null)connection.close();
		}
	}
	public String getNodeCodeByiid(long iid) throws SQLException {
		String nodeCode = "";
		String sql = "select nodeCode from gp_push_plan" + " where iid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs=null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setLong(1, iid);
			rs = preStat.executeQuery();
			if (rs.next()) {
				nodeCode = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null)rs.close();
			if(preStat!=null)preStat.close();
			if(connection!=null)connection.close();
		}
		return nodeCode;
	}

	public SmsArrangeItem getNodeCodeAndTidByiid(long iid) throws SQLException {
		String sql = "select nodeCode,tid from gp_push_plan" + " where iid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		SmsArrangeItem item = null;
		ResultSet rs=null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setLong(1, iid);
			rs = preStat.executeQuery();
			item = new SmsArrangeItem();
			if (rs.next()) {
				item.setNodeCode(rs.getString(1));
				item.setTid(rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null)rs.close();
			if(preStat!=null)preStat.close();
			if(connection!=null)connection.close();
		}
		return item;
	}

	/**
	 * 通过id获取tid
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-16
	 * @param iid
	 * @return
	 * @throws SQLException
	 */
	private int getTaskIdByiid(long iid) throws SQLException {
		int tid = 0;
		String sql = "select tid from gp_push_plan" + " where iid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs=null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setLong(1, iid);
			rs = preStat.executeQuery();
		if (rs.next()) {
			tid = rs.getInt(1);
		}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null)rs.close();
			if(preStat!=null)preStat.close();
			if(connection!=null)connection.close();
		}
		return tid;
	}


	/**
	 * 批量插入log,每一百条提交一次
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-14
	 * @param results
	 * @param iid
	 */
	@Deprecated
	public void putLogIntoSendLog(List<SendsResult> results, long iid) {
		try {
			String nodeCode = getNodeCodeByiid(iid);
			if ("".equals(nodeCode)) {
				log.error("gp_push_plan-》iid :" + iid
						+ " nodecode is null,putlog fail!!");
				return;
			}
			Connection conn = Jdbc.getConn(WorkUtils.getProvinceCode(nodeCode));
			StringBuffer sql = new StringBuffer("replace into gp_send_log")
					.append("(sid,mdn,nodeCode,flowid,send_result,atime,pcnt,position)")
					.append(" values ");
			StringBuffer values = new StringBuffer("");
			for (int i = 0; i < results.size(); i++) {
				SendsResult result = results.get(i);
				values.append("(").append(result.getNum()).append(COMMA)
						.append("'" + result.getMdn() + "'").append(COMMA)
						.append("'" + nodeCode + "'").append(COMMA)
						.append("'" + result.getFlowId() + "'").append(COMMA)
						.append(Integer.valueOf(result.getResult()))
						.append(COMMA).append(result.getUseTime())
						.append(COMMA).append(result.getCount()).append(COMMA)
						.append(result.getPosition()).append(")").append(COMMA);
				if ((i + 1) % 1000 == 0 || i == (results.size() - 1)) {
					if (!values.equals("")) {
						values.deleteCharAt(values.length() - 1);
					}
					PreparedStatement preStat = conn.prepareStatement(sql
							.toString() + values.toString());
					preStat.execute();
					preStat.close();
					values = new StringBuffer("");
				}
			}
			conn.close();
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 更新SendItem表数据状态
	 * 
	 * @description
	 * @author panyl
	 * @throws SQLException
	 * @date 2011-12-15
	 */
	public void updateSendItemStates(long iid, List<SendsResult> results)
			throws SQLException {
		int tid = getTaskIdByiid(iid);
		String sql = "select * from  gp_send_item where tid=?";
		Connection conn = Jdbc.getConn(WorkUtils.getProvinceCode(nodeCode));
		PreparedStatement preStat = conn.prepareStatement(sql);
		preStat.setInt(1, tid);
		ResultSet rs = preStat.executeQuery();
		while (rs.next()) {

		}

	}

	/**
	 * 更新成完成状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param smsItem
	 * @throws SQLException
	 */
	private void setPushPlanItemFinish(long iid) throws SQLException {
		setPushPlanItemState(iid, R.pushPlan.finish_status);
	}

	/**
	 * 更新状态为发送中
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-14
	 * @param iid
	 * @throws SQLException
	 */
	private void setPushPlanItemSending(long iid) throws SQLException {
		setPushPlanItemState(iid, R.pushPlan.sending_status);
	}

	/**
	 * 更新状态为准备
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-16
	 * @param iid
	 * @throws SQLException
	 */
	private void setPushPlanItemReady(long iid) throws SQLException {
		setPushPlanItemState(iid, R.pushPlan.ready_status);
	}

	/**
	 * 更新状态为暂停状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-14
	 * @param iid
	 * @throws SQLException
	 */
	private void setPushPlanItemSuspend(long iid) throws SQLException {
		setPushPlanItemState(iid, R.pushPlan.suspend_status);
	}
	/**
	 * 获取PushPlanItem的状态
	 * @author panyl
	 * @date 2011-12-29
	 * @param iid
	 * @return
	 * @throws SQLException
	 */
	private int getPushPlanItemState(long iid)throws SQLException{
		String sql = "select state from gp_push_plan where iid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs=null;
		int state=R.pushPlan.arrange_status;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setLong(1, iid);
			rs=preStat.executeQuery();
			if(rs.next()){
				state=rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		return state;
	}
	private void setPushPlanItemState(long iid, int state) throws SQLException {
		String sql = "update gp_push_plan set state=? where iid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setInt(1, state);
			preStat.setLong(2, iid);
			preStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
	}
	/**
	 * 更新lognum
	 * @param iid
	 * @param logNum
	 * @throws SQLException
	 */
	private void updatePushPlanItemLogNum(long iid, int logNum) throws SQLException {
		String sql = "update gp_push_plan set log_num=? where iid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setInt(1, logNum);
			preStat.setLong(2, iid);
			preStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
	}
	public static void main(String[] args) throws Exception {
		// new PushWorker(20111217140722l,"dd").startWorker();
	//	PushWorker worker=new PushWorker("fujian_1303");
	//	worker.taskSendStatist(20111219151210l);
//		Map<Integer,String> resultMap=new HashMap<Integer ,String>();
//		Set<Integer> delSet=new HashSet<Integer>();delSet.add(1);delSet.add(2);
//		resultMap.put(1, "1");
//		resultMap.put(2, "1");
//		resultMap.put(3, "1");
//		System.out.println("resultMap size:"+resultMap.size());
//		resultMap.keySet().removeAll(delSet);
//		System.out.println("resultMap size:"+resultMap.size());
//		int a=9;
//		String b="9";
//		System.out.println(b.equals(String.valueOf(a)));
	}
}
