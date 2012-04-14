package test;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;

public class HotelDAO extends BasicDAO<Hotel, String> {
	public HotelDAO(Morphia morphia, Mongo mongo) {
		super(mongo, morphia, "mongotest");
	}
}
