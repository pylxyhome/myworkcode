package cn.gzjp.wap.proxy.handler;

import java.util.Collection;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 内容处理工具类
 * @description
 * @author panyl
 * @date 2012-1-19
 */
public class ContentHandlerUtil {
	private final static Logger _log = LoggerFactory.getLogger(ContentHandlerUtil.class);
	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-1-30
	 * @param host 域名
	 * @param contents 内容
	 * @param contentType 内容类型
	 * @return
	 */
	public static String handler(String host,String contents,String contentType){
		Collection<ContentHandler> contentHandlers =ContentHandlerFactory.get(host).getHandlers();
		_log.info("contentHandlers size:"+contentHandlers.size());
		long startTime=System.currentTimeMillis();
		for(ContentHandler contentHandler : contentHandlers){
			if(contentHandler.getPattern()==null){
				_log.info("----ContentHandler Pattern is null----");
				continue;
			}
			_log.info("contentTypeSet:"+contentHandler.getContentTypeSet().toString());
			if(!contentHandler.getContentTypeSet().contains(contentType)&&
					!contentHandler.getContentTypeSet().contains(ContentType.ALL_CONTENT_TYPE)){
				continue;
			}
			Matcher matcher=contentHandler.getPattern().matcher(contents);
			StringBuffer sb = new StringBuffer();
			boolean result=matcher.find();
			while(result){
				String[] nodeContent=new String[matcher.groupCount()];
				for(int i=0;i<matcher.groupCount();i++){
					nodeContent[i]=matcher.group(i);
					//_log.info(matcher.group(i));
				}
				matcher.appendReplacement(sb, contentHandler.handlerNodeContent(nodeContent,matcher.group())); 
				result=matcher.find();
			}
			matcher.appendTail(sb);
			contents=sb.toString();
		}
		long endTime=System.currentTimeMillis();
		_log.info("content Handler run time:"+(endTime-startTime));
		return contents;
	}
	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-1-30
	 * @param host
	 * @param contents
	 * @return
	 */
	public static String handler(String host,String contents){
		return handler(host,contents,ContentType.ALL_CONTENT_TYPE);
	}
}
