package com.im.bean.page;

/**
 * 页面索引
 * @author Administrator
 *
 */
public class PageIndex {
	/**开始页索引**/
	private long startindex;
	/**结束页索引**/
	private long endindex;
	
	public PageIndex(long startindex, long endindex) { 
		this.startindex = startindex;
		this.endindex = endindex;
	}
	public long getStartindex() {
		return startindex;
	}
	public void setStartindex(long startindex) {
		this.startindex = startindex;
	}
	public long getEndindex() {
		return endindex;
	}
	public void setEndindex(long endindex) {
		this.endindex = endindex;
	}
	/**
	 * 
	 * @param viewpagecount  在页面显示多少页
	 * @param currentPage    当前页第几页
	 * @param totalpage      总页数
	 * @return
	 */
	public static PageIndex getPageIndex(long viewpagecount, int currentPage, long totalpage){
			long startpage = currentPage-(viewpagecount%2==0? viewpagecount/2-1 : viewpagecount/2);
			long endpage = currentPage+viewpagecount/2;
			if(startpage<1){
				startpage = 1;
				if(totalpage>=viewpagecount) endpage = viewpagecount;
				else endpage = totalpage;
			}
			if(endpage>totalpage){
				endpage = totalpage;
				if((endpage-viewpagecount)>0) startpage = endpage-viewpagecount+1;
				else startpage = 1;
			}
			return new PageIndex(startpage, endpage);		
	}
	
	public static void main(String[] args){
		PageIndex pageIndex = getPageIndex(5,1,20);
		System.out.println(pageIndex.getStartindex());
		System.out.println(pageIndex.getEndindex());
	}
}
