package com.im.service.base;

import java.util.LinkedHashMap;

import com.im.bean.page.QueryResult;

public interface SearchDao<T> {
	/**
	 * 
	 * @param startIndex 每页的开始索引
	 * @param maxResult  每页显示的最大行数
	 * @param whereJPQL  查询的条件 o.visible=?1
	 * @param params     条件语句的参数值
	 * @param orderby    排序
	 * @return
	 */
	public QueryResult<T> getScrollData(int startIndex,int maxResult,String whereJPQL,Object[] params,LinkedHashMap<String, String> orderby);
	/**
	 * 查询全部记录
	 * @return
	 */
	public QueryResult<T> getScrollData();
	/**
	 * @param startIndex 每页的开始索引
	 * @param maxResult  每页显示的最大行数
	 * @return
	 */
	public QueryResult<T> getScrollData(int startIndex,int maxResult);
	/**
	 * @param startIndex 每页的开始索引
	 * @param maxResult  每页显示的最大行数
	 * @param orderby    排序
	 * @return
	 */
	public QueryResult<T> getScrollData(int startIndex,int maxResult,LinkedHashMap<String, String> orderby);
	/**
	 * @param startIndex 每页的开始索引
	 * @param maxResult  每页显示的最大行数
	 * @param whereJPQL  查询的条件
	 * @param params     条件语句的参数值
	 * @return
	 */
	public QueryResult<T> getScrollData(int startIndex,int maxResult,String whereJPQL,Object[] params);
	/**
	 * @param startIndex 每页的开始索引
	 * @param maxResult  每页显示的最大行数
	 * @param whereJPQL  查询的条件
	 * @return
	 */
	public QueryResult<T> getScrollData(int startIndex,int maxResult,String whereJPQL);
}
