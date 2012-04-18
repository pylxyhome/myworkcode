package cn.gzjp.wap.proxy;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHandler;
import org.mortbay.xml.XmlConfiguration;

import cn.gzjp.wap.proxy.servlet.OnlineProxyServlet;
import cn.gzjp.wap.proxy.stat.ThreadStatTimer;

/**
 * 
 * @author gzwenny
 */
public class OnlineProxyServer {

	private static final Log log = LogFactory.getLog(OnlineProxyServer.class);

	public static void main(String[] args) {
		try {
			log.info("online proxy server starting..........");
			Server server = new Server();
			InputStream in=OnlineProxyServer.class.getResourceAsStream("/jetty.xml");
			XmlConfiguration configuration = new XmlConfiguration(in);
			configuration.configure(server);
			ServletHandler handler = new ServletHandler();
			handler.addServletWithMapping(OnlineProxyServlet.class, "/");
			server.setHandler(handler);
			server.start();
			log.info("online proxy server is started.");
			new ThreadStatTimer(server);
			server.join();
		} catch (Exception ex) {
			log.error("", ex);
			System.exit(0);
		}
	}
}
