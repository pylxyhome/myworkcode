package cn.gzjp.wap.proxy.html.tag;

import org.htmlparser.tags.CompositeTag;

@SuppressWarnings("serial")
public class HeadMetaTag extends CompositeTag {

	private static final String[] mIds = new String[] { "meta" };
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
	
	public String getRefleshUrl(){
		String link = "../iphone/index.jsp";
		link = getPage().getAbsoluteURL(link);
		return link;
	}
}