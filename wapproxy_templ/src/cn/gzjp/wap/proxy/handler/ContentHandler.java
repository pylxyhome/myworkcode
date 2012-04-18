package cn.gzjp.wap.proxy.handler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * ContentHandler基类
 * @description
 * @author panyl
 * @date 2012-1-19
 */
public abstract class ContentHandler {
	
	private Pattern pattern;
	
	private Set<String> contentTypeSet=new HashSet<String>();
	
	protected ContentHandler(){
		pattern=setPattern();
		setContentType();
	}
	
	/**
	 * 处理匹配节点内容
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param nodeContent 数组
	 * @param nodeStr 完整字符串
	 * @return
	 */
	public abstract String handlerNodeContent(String[] nodeContent,String nodeStr);

	public Pattern getPattern() {
		return pattern;
	}
	/**
	 * 添加要处理的内容类型
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @param contentType
	 */
	public void addContentType(String... contentType){
		if(contentType!=null){
			contentTypeSet.addAll(Arrays.asList(contentType));
		}
	}
	/**
	 * 获取要处理的内容类型集合
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @return
	 */
	public Set<String> getContentTypeSet(){
		return contentTypeSet;
	}
	/**
	 * 设置要匹配的内容模式
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @return
	 */
	public abstract Pattern setPattern();
	/**
	 * 设置匹配的内容类型
	 * 在子类调用addContentType方法
	 * @description
	 * @author panyl
	 * @date 2012-1-19
	 * @return
	 */
	public abstract void setContentType();
}
