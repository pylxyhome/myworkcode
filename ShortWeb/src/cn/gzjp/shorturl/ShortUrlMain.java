package cn.gzjp.shorturl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;

import cn.gzjp.shorturl.servlet.AddUrlServlet;
import cn.gzjp.shorturl.servlet.ShortUrlServlet;
import cn.gzjp.shorturl.servlet.SmsServlet;
import cn.gzjp.sproxy.util.Configure;



public class ShortUrlMain {

	private static final Log log = LogFactory.getLog(ShortUrlMain.class);
	
	public static void main(String[] args) {
		try {
			Server server = new Server();
			Connector connector = new SocketConnector();
			int port = Configure.get().getValueInt("port");
			connector.setPort(port);
			server.setConnectors(new Connector[] { connector });
			ServletHandler handler = new ServletHandler();
			server.setHandler(handler);
			handler.addServletWithMapping(AddUrlServlet.class, "/AddUrlServlet");
			handler.addServletWithMapping(SmsServlet.class, "/SmsServlet");
			handler.addServletWithMapping(ShortUrlServlet.class, "/*");
			server.start();
			server.join();
			log.info("Sproxy Start Success...");
		} catch (Exception ex) {
			log.error("start jetty Error:", ex);
			ex.printStackTrace();
		}
	}

}
