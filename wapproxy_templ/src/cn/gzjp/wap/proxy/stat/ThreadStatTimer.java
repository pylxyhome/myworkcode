package cn.gzjp.wap.proxy.stat;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;


public class ThreadStatTimer {
	private Server server;
	private Log log=LogFactory.getLog(ThreadStatTimer.class);

	public ThreadStatTimer(Server s){
		this.server=s;
		Timer timer=new Timer();
		TimerTask task=new TimerTask() {		
			@Override
			public void run() {
				int max=server.getThreadPool().getThreads();
				int idle=server.getThreadPool().getIdleThreads();
				log.info("threads:"+max+",idle:"+idle);
			}
		};
		timer.schedule(task, 0, 100*30*1000);
	}
}
