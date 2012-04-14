package com.im.bean.page;

import java.util.List;
/**
 * 查询结果
 * @author Administrator
 *
 * @param <T>
 */
public class QueryResult<T> {
	/**结果集**/
	private List<T> resultlist;
	/**总记录数**/
	private long totalrecord;

	public List<T> getResultlist() { 
		return resultlist;
	}

	public void setResultlist(List<T> resultlist) {
		this.resultlist = resultlist;
	}

	public long getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(long totalrecord) {
		this.totalrecord = totalrecord;
	}
	
}
