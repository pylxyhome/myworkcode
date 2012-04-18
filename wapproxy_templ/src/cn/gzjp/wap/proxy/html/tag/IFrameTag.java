package cn.gzjp.wap.proxy.html.tag;

import org.htmlparser.tags.CompositeTag;

@SuppressWarnings("serial")
public class IFrameTag extends CompositeTag {

	private static final String[] mIds = new String[] { "iframe" };
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
		setAttribute("src",link);
	}

	public String getLink() {
		String link=getAttribute("src");
		link=getPage().getAbsoluteURL(link);
		return link;
	}

	public String getMethod() {
		return super.getAttribute("method");
	}
}