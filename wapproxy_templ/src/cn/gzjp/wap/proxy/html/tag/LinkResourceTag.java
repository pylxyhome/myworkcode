package cn.gzjp.wap.proxy.html.tag;

import org.htmlparser.tags.CompositeTag;

@SuppressWarnings("serial")
public class LinkResourceTag extends CompositeTag {

	private static final String[] mIds = new String[] { "link" };

	public String[] getIds() {
		return (mIds);
	}

	public String[] getEnders() {
		return (mIds);
	}

	
	public void setLink(String link){
		setAttribute("href",link);
	}

	public String getLink() {
		String link=getAttribute("href");
		link=getPage().getAbsoluteURL(link);
		return link;
	}
}