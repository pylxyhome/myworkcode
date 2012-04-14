package cn.gzjp.sproxy.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;

public class AutoIncreaseIDUtil {
	public synchronized static Integer getAutoIncreaseID(String idName,DB db,String icn) {  
        BasicDBObject query = new BasicDBObject();  
        query.put("name", idName);  
        BasicDBObject update = new BasicDBObject();  
        update.put("$inc", new BasicDBObject("id", 1));  
        DBObject dbObject2 = db.getCollection(icn).findAndModify(query,  
                null, null, false, update, true, true);  
        Integer id = (Integer) dbObject2.get("id");  
        return id;  
    }  
	
}
