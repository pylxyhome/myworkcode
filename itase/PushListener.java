package cn.gzjp.gomp.server.push2;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @description
 * @author panyl
 * @date 2011-12-6
 */
public class PushListener implements ServletContextListener{
	 private final static Logger log=LoggerFactory.getLogger(PushListener.class);
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		log.debug("------contextDestroyed----------");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		 try {
			PushManager.initCheckPushPlanState();
			PushManager.initCommitFailPushPlan();
		 } catch (Exception e) {
			 log.error(e.getMessage());
			e.printStackTrace();
		}
		 log.debug("------contextInitialized----------");
	}

}
