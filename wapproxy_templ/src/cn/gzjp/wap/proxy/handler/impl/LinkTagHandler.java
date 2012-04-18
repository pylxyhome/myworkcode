package cn.gzjp.wap.proxy.handler.impl;

import java.util.regex.Pattern;

import cn.gzjp.wap.proxy.handler.ContentHandler;
import cn.gzjp.wap.proxy.handler.ContentType;
/**
 * a标签处理类
 * @description
 * @author panyl
 * @date 2012-1-19
 */
public class LinkTagHandler extends ContentHandler{

	@Override
	public String handlerNodeContent(String[] nodeContent,String nodeStr) {
		String preurl="http://127.0.0.1:8081/?";
		StringBuffer buffer=new StringBuffer();
		buffer.append(nodeContent[1]).append(nodeContent[2])
		.append(preurl)
		.append(nodeContent[3])
		.append(nodeContent[2]);
		return buffer.toString();
	}

	@Override
	public Pattern setPattern() {
		String patternStr ="(<a.*?href=)(\"|')(.*?)(\"|')";
		Pattern pattern=Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
		return pattern;
	}

	@Override
	public void setContentType() {
		this.addContentType(ContentType.TEXT_HTML);
	}

}
