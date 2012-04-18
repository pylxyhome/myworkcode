package cn.gzjp.wap.proxy.html.tag;

import org.htmlparser.tags.CompositeTag;

@SuppressWarnings("serial")
public class WMLGoTag extends CompositeTag {

	private static final String[] mIds = new String[] { "GO" };
	private static final String[] mEndTagEnders = new String[] { "ANCHOR" };

	public String[] getIds() {
		return (mIds);
	}

	public String[] getEnders() {
		return (mIds);
	}

	public String[] getEndTagEnders() {
		return (mEndTagEnders);
	}
	
	public void setLink(String link){
		setAttribute("href",link);
	}

	public String getLink() {
		String link=getAttribute("href");
		link=getPage().getAbsoluteURL(link);
		return link;
	}
	
	public void setMethod(String method){
		super.setAttribute("method",method);
	}

	public String getMethod() {
		return super.getAttribute("method");
	}
}