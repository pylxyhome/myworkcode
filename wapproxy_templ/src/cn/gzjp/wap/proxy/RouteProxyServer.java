/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.gzjp.wap.proxy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.gzjp.wap.proxy.servlet.RouteProxyServlet;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.ServletHandler;

/**
 *
 * @author gzwenny
 */
public class RouteProxyServer {
	/**
	 * Logger for this class
	 */
	private static final Log log = LogFactory.getLog(RouteProxyServer.class);

    public static void main(String[]args){
        try{
            Server server=new Server();
            Connector connector = new SocketConnector();
             int port=Configure.getConfig().getPort();
            connector.setPort(port);
            server.setConnectors(new Connector[]{ connector });
            ServletHandler handler = new ServletHandler();
            server.setHandler(handler);
            handler.addServletWithMapping(RouteProxyServlet.class,"/");
            server.start();
            server.join();
        }catch(Exception ex){
        	log.error("", ex);
        }
    }
}
