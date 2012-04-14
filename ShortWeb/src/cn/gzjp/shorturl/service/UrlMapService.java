package cn.gzjp.shorturl.service;

import cn.gzjp.shorturl.entity.UrlMap;
import cn.gzjp.sproxy.util.DBConstant;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class UrlMapService extends BasicDAO<UrlMap, String> {
	public UrlMapService(Morphia morphia, Mongo mongo) {
		super(mongo, morphia, DBConstant.MONGOTEST);  
		//boolean auth = mongo.getDB(DBConstant.MONGOTEST).authenticate("admin", "admin".toCharArray());
	}
	
}
