package cn.gzjp.gomp.server.push2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gzjp.gomp.client.constant.PushConstant;
import cn.gzjp.gomp.client.constant.R;
import cn.gzjp.gomp.client.entity.SmsArrangeItem;
import cn.gzjp.gomp.client.entity.Task;
import cn.gzjp.gomp.server.DateTimeFormatUtil;
import cn.gzjp.gomp.server.Jdbc;
import cn.gzjp.gomp.server.generateplanfile.Configure;
import cn.gzjp.gomp.server.util.DataUtil;
import cn.gzjp.gomp.server.util.WorkUtils;
import cn.gzjp.push.web.client.SenderService;

/**
 * 与webpush、数据库交互的服务类
 * 
 * @description
 * @author panyl
 * @date 2011-12-13
 */
public class PushManager implements Runnable {

	private static final long serialVersionUID = -4451748700770033123L;
	private Connection conn = null;
	private String nodeCode = "";
	private String pCode = ""; // 省份code
	private DataUtil dataUtil = null;
	private static final String DEM = "|";
	private final static Logger log = LoggerFactory
			.getLogger(PushManager.class);
	public static Map<String, PushManager> pushManagers = new HashMap<String, PushManager>();// key为nodeCode
	private static PushManager pushManager = new PushManager();
	/**
	 * 停止任务map
	 */
	public static ConcurrentHashMap<Integer, Integer> stopTaskMap = new ConcurrentHashMap<Integer, Integer>();
	/**
	 * 是否已经有线程在运行
	 */
	private boolean isRun=false;
	
	private PushManager(String nodeCode) {
		this.nodeCode = nodeCode;
		pCode = WorkUtils.getProvinceCode(nodeCode);
	}

	private PushManager() {
	}

	/**
	 * 获取nodeCode对应的一个PushManager实例
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param nodeCode
	 * @return
	 */
	public synchronized static PushManager getInstance(String nodeCode) {
		PushManager pushManager = pushManagers.get(nodeCode);
		log.info("nodeCode:" + nodeCode);
		if (pushManager == null) {
			pushManager = new PushManager(nodeCode);
			pushManagers.put(nodeCode, pushManager);
		}
		return pushManager;
	}

	/**
	 * 单例
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-16
	 * @return
	 */
	private synchronized static PushManager getInstance() {
		return pushManager;
	}

	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 * @param nodeCode
	 */
	public void push() {
		Thread thread = new Thread(this);
		thread.start();
	}

	/**
	 * 暂停群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param iid
	 * @return
	 */
	public int suspendTask(long iid) {
		int status = -1;
		try {
			status = SenderService.getInstance(nodeCode, this.getPortUrl())
					.stop(iid);
			if (status == PushConstant.STOP_SUCCESS) {
				setPushPlanItemState(iid, R.pushPlan.suspend_status);
			} else {
				log.debug(iid + " :suspend fail!!!");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 继续/恢复发送
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param iid
	 * @return
	 */
	public int pushContinue(long iid) {
		int status = -1;
		try {
			status = SenderService.getInstance(nodeCode, this.getPortUrl())
					.commitContinue(iid);
			if (status == PushConstant.ACTIVATE_SUCCESS) {
				log.info("pushContinue:ACTIVATE_SUCCESS");
				this.setPushPlanItemState(iid, R.pushPlan.sending_status);
				// 重新启动work
				new PushWorker(iid, this.getPortUrl()).startWorker();
			} else {
				log.debug(iid + " :pushContinue fail!!!");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 停止发送
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param iid
	 * @return
	 */
	public int stopPush(long iid) {
		int status = -1;
		try {
			status = SenderService.getInstance(nodeCode, this.getPortUrl())
					.stop(iid);
			if (status == PushConstant.STOP_SUCCESS) {
				// 停止后，PushPlan状态变为unarrange_status
				setPushPlanItemState(iid, R.pushPlan.unarrange_status);
				PushWorker.putStopTreadMap(iid);
			} else {
				log.debug(iid + " :stop fail!!!");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 删除群发任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-15
	 * @param iid
	 * @return
	 */
	public int delTask(long iid) {
		int status = -1;
		try {
			status = SenderService.getInstance(nodeCode, this.getPortUrl())
					.del(iid);
			if (PushConstant.DEL_SUCCESS == status) {
				setPushPlanItemState(iid, R.pushPlan.del_status);
			} else {
				log.debug(iid + " :delTask fail!!!");
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 刷新统计正在发送或暂停的任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @return
	 * @throws SQLException
	 */
	public int pushSendingAndSuspStatist() {
		int status = 0;
		try {
			List<SmsArrangeItem> items = getSendingAndSuspendPushPlans(nodeCode);
			for (SmsArrangeItem item : items) {
				if (item.getIid() == 0)
					continue;
				// 启动work
				new PushWorker(item.getIid(), this.getPortUrl(item
						.getNodeCode())).startWorker(PushWorker.LOG_TYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 统计push_plan包含iid对应的任务发送情况
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @return
	 */
	public int pushStatist() {
		int status = 0;
		try {
			List<SmsArrangeItem> items = getPushPlanOfHasIId(nodeCode);
			for (SmsArrangeItem item : items) {
				if (item.getIid() == 0)
					continue;
				// 启动work
				new PushWorker(item.getIid(), this.getPortUrl(item
						.getNodeCode())).startWorker(PushWorker.LOG_TYPE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	/**
	 * 统计iids对应的任务发送情况
	 * 
	 * @param iids
	 * @return
	 */
	public int pushStatist(Long[] iids) {
		int status = 0;
		if (iids != null) {
			log.info("--pushStatist:"+iids.length);
			for (Long iid : iids) {
				try {
					new PushWorker(iid, this.getPortUrl(nodeCode))
							.startWorker(PushWorker.LOG_TYPE);
				} catch (Exception e) {
					log.error(e.getMessage());
					e.printStackTrace();
				}
			}
		};
		return status;
	}

	public int pushStatist(Long iid) {
		Long[] iids = new Long[] { iid };
		return pushStatist(iids);
	}

	/**
	 * 将正在发送的任务在push_plan表的stat设成提交状态
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param smsItem
	 * @throws SQLException
	 */
	private void setPushPlanItemCommit(SmsArrangeItem smsItem)
			throws SQLException {
		String sql = "update gp_push_plan set state=? , iid=? where tid=? and nodecode=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setInt(1, R.pushPlan.commit_status);
			preStat.setLong(2, 0l); //清空iid
			preStat.setInt(3, smsItem.getTid());
			preStat.setString(4, smsItem.getNodeCode());
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
	 * 更新黑名单数量
	 * 
	 * @param tid
	 * @param blackNum
	 * @throws SQLException
	 */
	private void updateBlackNumToTask(int tid, int blackNum)
			throws SQLException {
		String sql = "update gp_task set black_num=? where id=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setInt(1, blackNum);
			preStat.setInt(2, tid);
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
	 * 设置gp_push_plan->iid的值
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @param iid
	 * @param smsItem
	 * @throws SQLException
	 */
	private void setiidToPushPlan(long iid, SmsArrangeItem smsItem)
			throws SQLException {
		String sql = "update gp_push_plan set iid=? where tid=? and nodecode=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setLong(1, iid);
			preStat.setInt(2, smsItem.getTid());
			preStat.setString(3, smsItem.getNodeCode());
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
	 * 发送任务对应的发送项
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 * @param tid
	 * @param send_type
	 * @return
	 * @throws Exception
	 */
	private long sendSendInfoItems(int tid, int send_type) throws Exception {
		log.info("send_type:" + send_type);
		SenderService senderService = SenderService.getInstance(nodeCode,
				this.getPortUrl());
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		initDefaultConn();
		long iid = 0;
		try {
			Task task = dataUtil.getTaskById(tid);
			String msg = task.getGroup_msg();
			String url = task.getShort_ad();
			String sql = "select sid,mdn,state" + " from gp_send_item"
					+ " where tid=?" + " and nodecode=?";
			connection = Jdbc.getConn(pCode);
			preStat = connection.prepareStatement(sql);
			preStat.setInt(1, tid);
			preStat.setString(2, nodeCode);
			rs = preStat.executeQuery();
			StringBuffer mdns = new StringBuffer();
			while (rs.next()) {
				int sid = rs.getInt(1);
				String mdn = rs.getString(2);
				int stat = rs.getInt(3);
				if (stat == R.sendItem.black_status || (stat & send_type) <= 0) {// 不是发送此类型
					continue;
				}
				mdns.append(sid).append(DEM).append(mdn).append("\n");
			}
			if (msg == null) {
				msg = "";
			}
			while (iid == 0 || iid == PushConstant.NETWORK_ERROR) {
				log.info("------commit--------");
				iid = senderService.commit(url, mdns.toString(), msg);
				log.info("iid:" + iid);
				if (iid == PushConstant.NETWORK_ERROR) {// 网络访问出错
					this.setPushPlanItemStateByTid(tid,
							R.pushPlan.webpush_exception);
					String title = "与push端口连接异常";
					StringBuffer body = new StringBuffer(
							"tid："
									+ tid
									+ "，nodeCode:"
									+ this.nodeCode
									+ ",请查看itaste与push端口连接是否正常"
									+ "<br/><br/>"
									+ "异常发生时间："
									+ new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss")
											.format(new Date()));
					PushWorker.sendExceptionEmail(tid,
							R.pushPlan.webpush_exception, title,
							body.toString());
				}
				if (iid == 0 || iid == PushConstant.NETWORK_ERROR) {
					if (stopTaskMap.keySet().contains(tid)) {
						stopTaskMap.remove(tid);
						break;
					}
					Thread.sleep(Configure.get().getCommitAgainTime());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
			closeConn();
		}
		return iid;
	}

	/**
	 * 加载黑名单
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-14
	 * @param provinceCode
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private Set<String> loadBlackMdns(String provinceCode)
			throws FileNotFoundException, IOException {
		String gomp_path = Configure.get().getValue("GOMP_PATH");
		String blackFileName = gomp_path + File.separator + "blackmdns"
				+ File.separator + provinceCode + ".txt";
		File file = new File(blackFileName);
		if (!file.exists())
			return null;
		Set<String> blackMdns = WorkUtils.getSets(file);
		return blackMdns;
	}

	/**
	 * 初始化默认连接对象及dataUtil
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-14
	 * @throws SQLException
	 */
	private void initDefaultConn() throws SQLException {
		conn = Jdbc.getConnection();
		dataUtil = new DataUtil(conn);

	}

	/**
	 * 关闭默认连接对象资源
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-14
	 * @throws SQLException
	 */
	private void closeConn() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	private String getPortUrl() throws SQLException {
		return getPortUrl(nodeCode);
	}

	private String getPortUrl(String nodeCode) throws SQLException {
		String portUrl = "";
		StringBuffer sql = new StringBuffer(
				"select url from gp_port_asy where nodecode=?");
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql.toString());
			preStat.setString(1, nodeCode);
			rs = preStat.executeQuery();
			if (rs.next()) {
				portUrl = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		return portUrl;
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

	private void setPushPlanItemStateByTid(int tid, int state)
			throws SQLException {
		String sql = "update gp_push_plan set state=? where tid=?";
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql);
			preStat.setInt(1, state);
			preStat.setInt(2, tid);
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
	 * 生成一个任务中的一个号码包中的发送项,批量插入
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 * @param tid
	 * @param pid
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws SQLException
	 */
	private void makeSendInfoItems(int tid, int pid, File file)
			throws FileNotFoundException, IOException, SQLException {
		List<String> mdns = WorkUtils.getLinesByFile(file);
		Connection connection = null;
		PreparedStatement preStat = null;
		int blacknum = 0;
		try {
			connection = Jdbc.getConn(pCode);
			connection.setAutoCommit(false);
			String sql = "insert into gp_send_item(tid,pid,mdn,state,nodecode,sendTime)values(?,?,?,?,?,?)";
			preStat = connection.prepareStatement(sql);
			Set<String> blackMdns = loadBlackMdns(pCode);
			for (int i = 0; i < mdns.size(); i++) {
				String mdn = mdns.get(i);
				try {
					preStat.setInt(1, tid);
					preStat.setInt(2, pid);
					preStat.setString(3, mdn);
					if (blackMdns != null && blackMdns.contains(mdn)) {// 包含在黑名单中
						preStat.setInt(4, R.sendItem.black_status);
						blacknum++;
					} else {
						preStat.setInt(4, R.sendItem.ready_status);
					}
					preStat.setString(5, nodeCode);
					preStat.setString(6, DateTimeFormatUtil.getCurrentDate());
					preStat.addBatch();
					if ((i + 1) % 1000 == 0 || i == (mdns.size() - 1)) {
						preStat.executeBatch();
						connection.commit();
						preStat.clearBatch();
					}
				} catch (Exception excep) {
					excep.printStackTrace();
					log.error("makeSendInfoItems error:" + excep.getMessage());// debug
				}
			}
		} catch (SQLException excep) {
			excep.printStackTrace();
		} finally {
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		updateBlackNumToTask(tid, blacknum);
	}

	/**
	 * 查看些任务的发送项是否生成
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 * @param tid
	 * @param pid
	 * @return
	 * @throws SQLException
	 */
	private boolean checkSendItemHas(int tid, int pid) throws SQLException {
		StringBuffer sql = new StringBuffer("select count(1) from gp_send_item")
				.append(" where nodecode=? and pid=? and tid=?");
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		try {
			connection = Jdbc.getConn(pCode);
			preStat = connection.prepareStatement(sql.toString());
			preStat.setString(1, nodeCode);
			preStat.setInt(2, pid);
			preStat.setInt(3, tid);
			rs = preStat.executeQuery();
			if (rs.next()) {
				int cnt = rs.getInt(1);
				if (cnt > 0) { // 如果select出数量大于0，则是没有创建发送项
					preStat.close();
					connection.close();
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null && !preStat.isClosed())
				preStat.close();
			if (connection != null && !connection.isClosed())
				connection.close();
		}
		return false;
	}

	/**
	 * 得到需要发送的任务列表
	 * @description
	 * @author panyl
	 * @date 2011-12-13
	 * @return
	 * @throws SQLException
	 */
	private List<SmsArrangeItem> getPushPlans() throws SQLException {
		return getPushPlans(R.pushPlan.arrange_status, nodeCode);
	}

	private List<SmsArrangeItem> getPushPlans(int state, String nodeCode)
			throws SQLException {
		log.info("state:" + state + " nodeCode:" + nodeCode);
		SmsArrangeItem r = null;
		StringBuffer sql = new StringBuffer(
				"select tid,nodecode,state,send_type,loc ")
				.append("from gp_push_plan where state=?");
		if (nodeCode != null&&!nodeCode.equals("")) {
			sql.append(" and nodecode=? ");
		}
		sql.append(" order by loc");
		log.info("getPushPlans->sql:" + sql);
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		List<SmsArrangeItem> items = new ArrayList<SmsArrangeItem>();
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql.toString());
			preStat.setInt(1, state);
			if (nodeCode != null&&!nodeCode.equals("")) {
				preStat.setString(2, nodeCode);
			}
			rs = preStat.executeQuery();
			while (rs.next()) {
				r = new SmsArrangeItem();
				r.setTid(rs.getInt(1));
				r.setNodeCode(rs.getString(2));
				r.setState(rs.getInt(3));
				r.setSend_type(rs.getInt(4));
				r.setLoc(rs.getString(5));
				items.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		return items;
	}
	/**
	 * 获取需要检查状态的PushPlan
	 * @return
	 * @throws SQLException
	 */
	private List<SmsArrangeItem> getHasCheckPushPlans() throws SQLException {
		SmsArrangeItem r = null;
		StringBuffer sql = new StringBuffer(
				"select tid,nodecode,state,send_type,loc,iid ")
				.append("from gp_push_plan where (state=? or state=? or state=? or state=?) and iid is not null");
		sql.append(" order by loc");
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		List<SmsArrangeItem> items = new ArrayList<SmsArrangeItem>();
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql.toString());
			preStat.setInt(1, R.pushPlan.webpush_exception);
			preStat.setInt(2, R.pushPlan.send_exception);
			preStat.setInt(3, R.pushPlan.sending_status);
			preStat.setInt(4, R.pushPlan.ready_status);
			rs = preStat.executeQuery();
			while (rs.next()) {
				r = new SmsArrangeItem();
				r.setTid(rs.getInt(1));
				r.setNodeCode(rs.getString(2));
				r.setState(rs.getInt(3));
				r.setSend_type(rs.getInt(4));
				r.setLoc(rs.getString(5));
				r.setIid(rs.getLong(6));
				items.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		return items;
	}

	/**
	 * 获取有iid的任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @param nodeCode
	 * @return
	 * @throws SQLException
	 */
	private List<SmsArrangeItem> getPushPlanOfHasIId(String nodeCode)
			throws SQLException {
		SmsArrangeItem r = null;
		StringBuffer sql = new StringBuffer(
				"select tid,nodecode,state,send_type,loc,iid ")
				.append("from gp_push_plan where iid>0 ");
		if (nodeCode != null) {
			sql.append(" and nodecode=? ");
		}
		sql.append("order by loc");
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		List<SmsArrangeItem> items = new ArrayList<SmsArrangeItem>();
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql.toString());
			if (nodeCode != null) {
				preStat.setString(1, nodeCode);
			}
			rs = preStat.executeQuery();
			while (rs.next()) {
				r = new SmsArrangeItem();
				r.setTid(rs.getInt(1));
				r.setNodeCode(rs.getString(2));
				r.setState(rs.getInt(3));
				r.setSend_type(rs.getInt(4));
				r.setLoc(rs.getString(5));
				r.setIid(rs.getLong(6));
				items.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		return items;
	}

	/**
	 * 获取正在发送和暂停发送的计划任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-19
	 * @param nodeCode
	 * @return
	 * @throws SQLException
	 */
	private List<SmsArrangeItem> getSendingAndSuspendPushPlans(String nodeCode)
			throws SQLException {
		SmsArrangeItem r = null;
		StringBuffer sql = new StringBuffer(
				"select tid,nodecode,state,send_type,loc,iid ")
				.append("from gp_push_plan where state=? or state=? ");
		if (nodeCode != null) {
			sql.append(" and nodecode=? ");
		}
		sql.append("order by loc");
		Connection connection = null;
		PreparedStatement preStat = null;
		ResultSet rs = null;
		List<SmsArrangeItem> items = new ArrayList<SmsArrangeItem>();
		try {
			connection = Jdbc.getConnection();
			preStat = connection.prepareStatement(sql.toString());
			preStat.setInt(1, R.pushPlan.sending_status);
			preStat.setInt(2, R.pushPlan.suspend_status);
			if (nodeCode != null) {
				preStat.setString(3, nodeCode);
			}
			rs = preStat.executeQuery();
			while (rs.next()) {
				r = new SmsArrangeItem();
				r.setTid(rs.getInt(1));
				r.setNodeCode(rs.getString(2));
				r.setState(rs.getInt(3));
				r.setSend_type(rs.getInt(4));
				r.setLoc(rs.getString(5));
				r.setIid(rs.getLong(6));
				items.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			if (rs != null)
				rs.close();
			if (preStat != null)
				preStat.close();
			if (connection != null)
				connection.close();
		}
		return items;
	}

	/**
	 * 得到号码包文件对象
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-12
	 * @param taskId
	 * @return
	 * @throws SQLException
	 */
	private List<File> getPackageFileByTaskId(int taskId) throws SQLException {
		ArrayList<File> packageFiles = null;
		String uploadPath = Configure.get().getValue("GOMP_PATH");
		try {
			initDefaultConn();
			Task t = dataUtil.getTaskById(taskId);
			String send_time = t.getSend_time();
			//String fileNamePreFix = Integer.toString(taskId) + "_";
			String taskPath = uploadPath + File.separator + pCode
					+ File.separator + "download" + File.separator
					+ send_time.replace("-", "") + File.separator + t.getId();
			File taskFile = new File(taskPath);
			log.info("taskPath:"+taskPath);
			if (taskFile.isDirectory()) { // 查看此路径是否为文件夹
				packageFiles = new ArrayList<File>();
				File[] list = taskFile.listFiles();
				for (File file : list) {// 找出号码文件
					String fileName = file.getName().toLowerCase();
					if ((fileName.endsWith(".txt"))) {// 找到号码文件
						packageFiles.add(file);
					}
				}
			} else {
				log.info("getPackageFileByTaskId, taskid=" + taskId
						+ " taskPath=" + taskPath + " isnot dir.");// debug
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			closeConn();
		}
		return packageFiles;
	}

	/**
	 * 通过任务文件夹下号码包文件得到号码包id 12_23.txt
	 */
	private static int getPidByTaskPackageFileName(String name) {
		int pid = 0;
		int startIx = name.indexOf("_") + 1;
		int endIx = name.indexOf(".txt");
		String pidStr = name.substring(startIx, endIx);
		pid = Integer.parseInt(pidStr);
		return pid;
	}

	@Override
	public void run() {// 发送
		try {
			if(!isRun){
				isRun=true;
				List<SmsArrangeItem> smsItems = getPushPlans();
				log.info("task num:" + smsItems.size());
				processPushPlan(smsItems);
				log.info("nodeCode:" + nodeCode);
			}else{
				log.debug("has thread is run!!!");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			isRun=false;
			closeConn();
		}
	}

	/**
	 * 处理任务
	 * 
	 * @description
	 * @author panyl
	 * @date 2011-12-16
	 * @param smsItems
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	private void processPushPlan(List<SmsArrangeItem> smsItems)
			throws SQLException, FileNotFoundException, IOException, Exception {
		for (SmsArrangeItem smsItem : smsItems) {
			nodeCode=smsItem.getNodeCode();
			if("".equals(nodeCode)){
				log.info("nodeCode is null!!!!!");continue;
			}
			pCode = WorkUtils.getProvinceCode(nodeCode);
			List<File> packageFiles = getPackageFileByTaskId(smsItem.getTid());
			if (packageFiles == null)
				continue; 
			for (File pfile : packageFiles) { // 对每个号码包进行处理
				int pid = getPidByTaskPackageFileName(pfile.getName());
				boolean hasItem = checkSendItemHas(smsItem.getTid(), pid);
				if (!hasItem) {// 如果没有创建，则需要进行创建
					makeSendInfoItems(smsItem.getTid(), pid, pfile);
				}
			}
			setPushPlanItemCommit(smsItem);
			// 返回iid
			long iid = sendSendInfoItems(smsItem.getTid(),
					smsItem.getSend_type());
			log.info("iid:" + iid);
			if (iid == PushConstant.NETWORK_ERROR || iid == 0) {
				this.setPushPlanItemStateByTid(smsItem.getTid(),
						R.pushPlan.webpush_exception);
				continue;
			}
			// 更新gp_push_plan的iid,两个操作可合并一个
			setiidToPushPlan(iid, smsItem);
			this.setPushPlanItemState(iid, R.pushPlan.ready_status);
			// 启动work
			new PushWorker(iid, this.getPortUrl(smsItem.getNodeCode()))
					.startWorker();
		}
	}

	/**
	 * 初始化检查任务状态
	 * @description
	 * @author panyl
	 * @date 2011-12-16
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void initCheckPushPlanState() throws FileNotFoundException,
			IOException, Exception {
		PushManager manager = PushManager.getInstance();
		List<SmsArrangeItem> smsItems = manager.getHasCheckPushPlans();
		log.info("initCheckPushPlanState->smsItems size:" + smsItems.size());
		for (SmsArrangeItem sai : smsItems) {
			// 启动work
			if (sai.getIid() != 0l && !"".equals(sai.getNodeCode())) {
				log.info("check status :"+sai.getIid()); 
				new PushWorker(sai.getIid(), manager.getPortUrl(sai
						.getNodeCode())).startWorker();
			}
		}
	}
	/**
	 * 初始化未提交成功的pushPlan,更新为安排状态
	 */
	public static void initCommitFailPushPlan() {
		PushManager manager = PushManager.getInstance();
		StringBuffer sql = new StringBuffer("update gp_push_plan set state=? where state=? and (iid is null or iid=0)");
		Connection connection = null;
		PreparedStatement preStat = null;
		try {
			connection = Jdbc.getConnection();
			preStat=connection.prepareStatement(sql.toString());
			preStat.setInt(1, R.pushPlan.arrange_status);
			preStat.setInt(2, R.pushPlan.webpush_exception);
			preStat.execute();
		} catch (Exception e) {
			log.equals(e.getMessage());
			e.printStackTrace();
		}finally{
			try {
				preStat.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		log.info("------------initCommitFailPushPlan---------------");
		manager.push();
	}

	public static void main(String[] args) throws SQLException,
			FileNotFoundException, IOException {
		// PushManager manager = PushManager.getInstance("fujian_1303");
		// manager.push();
		// List<File> packageFiles = manager.getPackageFileByTaskId(1020);
		// for (File pfile : packageFiles) { // 对每个号码包进行处理
		// int pid = getPidByTaskPackageFileName(pfile.getName());
		// boolean hasItem = manager.checkSendItemHas(1020, pid);
		// System.out.println("hasItem:"+hasItem);
		// if (!hasItem) {// 如果没有创建，则需要进行创建
		// manager.makeSendInfoItems(1020, pid, pfile);
		// }
		// }
		// System.out.println("pushManagers大小："+pushManagers.size());
		// new PushWorker("fujian_1303").startWorker(PushWorker.LOG_TYPE);
		PushManager.getInstance("fujian_1303").pushSendingAndSuspStatist();
	}
}
