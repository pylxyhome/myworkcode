package cn.gzjp.wap.proxy.handler.impl;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.gzjp.wap.proxy.handler.ContentHandler;
import cn.gzjp.wap.proxy.handler.ContentType;
import cn.gzjp.wap.proxy.handler.bean.InputNode;
/**
 * a标签处理类
 * @description
 * @author panyl
 * @date 2012-1-19
 */
public class FormTagHandler extends ContentHandler{

	@Override
	public String handlerNodeContent(String[] nodeContent,String nodeStr) {
		System.out.println("nodeStr:"+nodeStr);
		
		String formType=getFormType(nodeStr);
		System.out.println("formType:"+formType);
		String preurl="http://127.0.0.1:8081/?";
		StringBuffer buffer=new StringBuffer();
		buffer.append(nodeContent[1]).append(nodeContent[2])
		.append(preurl)
		.append(nodeContent[3])
		.append(nodeContent[2]);
		return buffer.toString();
	}

	private String getFormType(String nodeStr) {
		String patternStr ="(<form.*?method=)(\"|')(.*?)(\"|')";
		Pattern pattern=Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
		Matcher matcher=pattern.matcher(nodeStr);
		boolean result=matcher.find();
		if(result){
			return matcher.group(3);
		}
		return "post";
	}
	private List<InputNode> getInputNodes(String nodeStr) {
		String patternStr ="(<input.*?name=)(\"|')(.*?)(\"|')";
		Pattern pattern=Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
		Matcher matcher=pattern.matcher(nodeStr);
		boolean result=matcher.find();
		while(result){
			
		}
		return null;
	}
	@Override
	public Pattern setPattern() {
		String patternStr ="(<form.*?action=)(\"|')(.*?)(\"|')(.*?)>(.*?)</form>";
		Pattern pattern=Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
		return pattern;
	}

	@Override
	public void setContentType() {
		this.addContentType(ContentType.TEXT_HTML);
	}

}
