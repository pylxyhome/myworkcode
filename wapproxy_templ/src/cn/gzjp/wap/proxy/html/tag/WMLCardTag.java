package cn.gzjp.wap.proxy.html.tag;

import org.htmlparser.tags.CompositeTag;

/**
 *
 * @author gzwenny
 */
public class WMLCardTag extends CompositeTag {

        private static final String[] mIds = new String[]{"CARD"};
        private static final String[] mEndTagEnders = new String[]{"CARD"};

        @Override
        public String[] getIds() {
            return (mIds);
        }

        @Override
        public String[] getEnders() {
            return (mIds);
        }

        @Override
        public String[] getEndTagEnders() {
            return (mEndTagEnders);
        }

        public String getLink() {
            return super.getAttribute("href");
        }

        public String getMethod() {
            return super.getAttribute("method");
        }
        
        public String getOntimerLink(){
        	String timerLink=getAttribute("ontimer");
        	return getPage().getAbsoluteURL(timerLink);
        }
        
        public void setOntimerLink(String link){
        	super.setAttribute("ontimer",link);
        }
    }