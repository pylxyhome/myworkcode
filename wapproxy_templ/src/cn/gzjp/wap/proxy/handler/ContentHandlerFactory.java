package cn.gzjp.wap.proxy.handler;

import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * ContentHandler内容处理工厂类
 * @description
 * @author panyl
 * @date 2012-1-19
 */
public class ContentHandlerFactory {
	
	private final static Logger _log = LoggerFactory.getLogger(ContentHandlerFactory.class);
	private HashMap<String,ContentHandler> handlerMap=new HashMap<String,ContentHandler>();
	private static HashMap<String,ContentHandlerFactory> contentHandlerFactoryMap=new HashMap<String,ContentHandlerFactory>();
	private ContentHandlerFactory(){}
	/**
	 * 获取一个ContentHandler工厂实例
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param host  域名
	 * @return
	 */
	public synchronized static ContentHandlerFactory get(String host){
		ContentHandlerFactory chf=contentHandlerFactoryMap.get(host);
		if(chf==null){
			chf=new ContentHandlerFactory();
			contentHandlerFactoryMap.put(host, chf);
		}
		return chf;
	}
	/**
	 * 注册一个ContentHandler
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param contentHandler
	 */
	public void registerContentHandler(ContentHandler contentHandler){
		_log.info(contentHandler.getClass().getName());
		handlerMap.put(contentHandler.getClass().getName(),contentHandler);
	}
	/**
	 * 注册一个ContentHandler
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param contentHandlerClass
	 */
	public void registerContentHandler(Class<? extends ContentHandler> contentHandlerClass){
		try {
			if(this.handlerMap.containsKey(contentHandlerClass.getName()))return;
			ContentHandler instance=contentHandlerClass.newInstance();
			handlerMap.put(contentHandlerClass.getName(),instance);
		} catch (InstantiationException e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			_log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 取消一个已经注册过的ContentHandler
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param contentHandlerClass
	 */
	public void unregisterContentHandler(Class<? extends ContentHandler> contentHandlerClass){
		unregisterContentHandler(contentHandlerClass.getName());
	}
	/**
	 * 取消一个已经注册过的ContentHandler
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param contentHandler
	 */
	public void unregisterContentHandler(ContentHandler contentHandler){
		String classFullName=contentHandler.getClass().getName();
		unregisterContentHandler(classFullName);
	}
	/**
	 * 取消一个已经注册过的ContentHandler
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param classFullName
	 */
	public void unregisterContentHandler(String classFullName){
		handlerMap.remove(classFullName);
	}
	public Collection<ContentHandler> getHandlers(){
		return handlerMap.values();
	}
	public void unregisterAllContentHandler(){
		handlerMap.clear();
		handlerMap=new HashMap<String,ContentHandler>();
	}
}

