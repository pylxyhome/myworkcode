package com.im.service.base;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.im.bean.page.QueryResult;
import com.im.util.IMLog;
import com.im.util.base.GenericsUtils;

@SuppressWarnings("unchecked")
@Transactional
public  abstract class DaoSupport<T> implements DAO<T> {
	private Class<T> entityClass = GenericsUtils.getSuperClassGenricType(this.getClass());
	//注入一个实体管理器,spring会从EntityManagerFactory里得到一个EntityManager对象
	@PersistenceContext
	protected EntityManager em;
	public void delete(Serializable... ids) {   
		for(Serializable id :ids){ 
			em.remove(em.getReference(this.entityClass, id));
		}
	}

	public void delete(T object){
		em.remove(object);
	}
	public List<T> findAll() {
		return this.getScrollData().getResultlist();
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public T find(Serializable entityId) {
		
		return em.find(this.entityClass, entityId);
	}
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public T getReference(Serializable entityId){
		return em.getReference(this.entityClass, entityId);
	}
	public void save(T entity) {
		em.persist(entity);
	
	}

	public void update(T entity) {
		//游离状态下更新
		em.merge(entity);
	}

	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public QueryResult<T> getScrollData(int startIndex,int maxResult,String whereJPQL,Object[] params,LinkedHashMap<String, String> orderby){
		QueryResult<T> qr = new QueryResult<T>();
		String entityName = getEntityName(this.entityClass);
		String where = (whereJPQL!=null&&!"".equals(whereJPQL.trim()))?"where "+whereJPQL:"";
System.out.println("hql语句===>select o from "+entityName + " o "+ where + buildOrderBy(orderby));
		Query query= em.createQuery("select o from "+entityName + " o "+ where + buildOrderBy(orderby));
		setParameters(query,params);
		if(startIndex!=-1&&maxResult!=-1){
			query.setFirstResult(startIndex).setMaxResults(maxResult);
			}//判断是否要分页
		qr.setResultlist(query.getResultList());
		query = em.createQuery("select count(o) from "+entityName+ " o "+where);
		setParameters(query,params);
		qr.setTotalrecord((Long)query.getSingleResult());
		return qr;
	}
	public List<T> findListByProperty(String propertyName,Object value){
		String whereJPQL=" o."+propertyName+"= ?1";
		QueryResult<T> queryResult = this.getScrollData(-1, -1, whereJPQL, new Object[]{value});
		return queryResult.getResultlist();
	}
	/**
	 * 多参数查询
	 * @param propertyNames
	 * @param values
	 * @return
	 */
	public List<T> findListByProperty(String[] propertyNames,Object[] values){
		IMLog.info("开始执行findListByProperty方法");
		StringBuffer whereJPQL=new StringBuffer();
		//if(propertyNames.length==values.length){
			for(int i=0;i<propertyNames.length;i++){
				StringBuffer JPQL=new StringBuffer(" o.");
				JPQL.append(propertyNames[i]);
				JPQL.append("= ?").append(i+1);
				JPQL.append(" and");
				whereJPQL.append(JPQL);
			}
		//}
		whereJPQL.delete(whereJPQL.length()-3, whereJPQL.length());
		IMLog.info("hql=>"+whereJPQL.toString());
		QueryResult<T> queryResult = this.getScrollData(-1, -1, whereJPQL.toString(), values);
		return queryResult.getResultlist();
	}
	/**
	 * 
	 * @param propertyName
	 * @param value
	 * @param orderbyName  排序属性
	 * @param orderbyValue  排序方式(desc,asc)
	 * @return
	 */
	public List<T> findListByProperty(String propertyName,Object value,String orderbyName,String orderbyValue){
		String whereJPQL=" o."+propertyName+"= ?1";
		LinkedHashMap<String, String> orderby=null;
		if(orderbyName!=null&&orderbyValue!=null){
			orderby = new LinkedHashMap<String, String>();
			orderby.put(orderbyName, orderbyValue);
		}
		QueryResult<T> queryResult = this.getScrollData(-1, -1, whereJPQL, new Object[]{value},orderby);
		return queryResult.getResultlist();
	}
	public List<T> findListByPropertyInValues(String propertyName,Object[] values){
		StringBuffer JPQL=new StringBuffer(" o.");
		JPQL.append(propertyName).append(" in(");
		for(int i=0;i<values.length;i++){
			JPQL.append("?").append(i+1).append(",");
		}
		JPQL.delete(JPQL.length()-1, JPQL.length());
		JPQL.append(")");
		String whereJPQL=JPQL.toString();
		IMLog.info("In语句==>"+whereJPQL);
		QueryResult<T> queryResult = this.getScrollData(-1, -1, whereJPQL, values);
		return queryResult.getResultlist();
	}
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateProperty(Serializable id,String propertyName,Object value){
		String updateJPQL="update "+this.getEntityName(entityClass) +" o set o."+propertyName+"= ?1"+" where id = ?2";
		IMLog.info("updateJPQL=>"+updateJPQL);
		this.em.createQuery(updateJPQL).setParameter(1, value)
		.setParameter(2, id).executeUpdate();
	}
	public QueryResult<T> getScrollData(){
		return getScrollData(-1,-1,null,null,null);
	}
	public QueryResult<T> getScrollData(int startIndex,int maxResult){
		return getScrollData(startIndex,maxResult,null,null,null);
	}
	public QueryResult<T> getScrollData(int startIndex,int maxResult,LinkedHashMap<String, String> orderby){
		return getScrollData(startIndex,maxResult,null,null,orderby);
	}
	public QueryResult<T> getScrollData(int startIndex,int maxResult,String whereJPQL,Object[] params){
		return getScrollData(startIndex,maxResult,whereJPQL,params,null);
	}
	public QueryResult<T> getScrollData(int startIndex,int maxResult,String whereJPQL){
		return getScrollData(startIndex,maxResult,whereJPQL,null,null);
	}
	private void setParameters(Query query, Object[] params) {
		if(params!=null){
			for(int i=0;i<params.length;i++){
				query.setParameter(i+1, params[i]);
			}
		}
		
	}

	/**
	 * 构建排序语句
	 * @param orderby
	 * @return
	 */
	private static String buildOrderBy(LinkedHashMap<String, String> orderby) {
		StringBuilder sb = new StringBuilder();
		if(orderby!=null&& orderby.size()>0){
			sb.append(" order by ");
			for(String key:orderby.keySet()){
				sb.append("o.").append(key).append(" ").append(orderby.get(key)).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	/**
	 * 获取实体类名称
	 * @param <E>
	 * @param entityClass
	 * @return
	 */
	private static <E> String getEntityName(Class<E> entityClass) {
		String entityname=entityClass.getSimpleName();
//		Entity entity = entityClass.getAnnotation(Entity.class);
//		if(entity.name()!=null && !"".equals(entity.name())){
//			entityname = entity.name();
//		}
		return entityname;
	}
	
	/**
	 * 获取某属性的值
	 * @param jpql
	 * @param params
	 * @return
	 */
	protected Object getField(String jpql,Object[] params){
		Query query = em.createQuery(jpql);
		if(params.length>0){
			for(int i=1;i<params.length;i++){
				query.setParameter(i, params[i]);
			}
		}
		return query.getSingleResult();
	}
}
