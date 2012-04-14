package com.im.service.base;

import java.io.Serializable;
import java.util.List;

/**
 * 实体辅助类
 * @author pylxy
 * @param <T>
 */
public interface BaseDao<T> {  
	/**
	 * 保存一个实体到数据库
	 * @param entity
	 */
	public void save(T entity);
	/**
	 * 更新一个实体对象
	 * @param entity
	 */
	public void update(T entity);
	/**
	 * 查找一个实体对象
	 * @param entity
	 */
	public T find(Serializable entityId);
	
	/**
	 * 懒加载一个实体对象
	 * @param entityId
	 * @return
	 */
	public T getReference(Serializable entityId);
	/**
	 * 删除一个或多个实体对象
	 * @param entity
	 */
	public void delete(Serializable... ids);//可变参数
	/**
	 * 删除一个实体对象
	 * @param object
	 */
	public void delete(T object);
	/**
	 * 查找实体对象列表
	 * @return
	 */
	public List<T> findAll();
}
