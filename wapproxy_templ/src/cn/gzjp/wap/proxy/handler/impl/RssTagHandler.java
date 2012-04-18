package cn.gzjp.wap.proxy.handler.impl;

import java.util.regex.Pattern;

import cn.gzjp.wap.proxy.handler.ContentHandler;
import cn.gzjp.wap.proxy.handler.ContentType;
/**
 * <a>标签处理类
 * @description
 * @author panyl
 * @date 2012-1-19
 */
public class RssTagHandler extends ContentHandler{

	@Override
	public String handlerNodeContent(String[] nodeContent,String nodeStr) {
		String preurl="http://127.0.0.1:8081/?";
		StringBuffer buffer=new StringBuffer();
		buffer.append(nodeContent[1])
		.append(preurl)
		.append(nodeContent[2])
		.append(nodeContent[1]);
		return buffer.toString();
	}

	@Override
	public Pattern setPattern() {
		String patternStr ="(<link>)(.*?)(</link>)";
		Pattern pattern=Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
		return pattern;
	}

	@Override
	public void setContentType() {
		this.addContentType(ContentType.TEXT_XML);
	}

}
