package test;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;

public class UsersDAO extends BasicDAO<Users, String> {
	public UsersDAO(Morphia morphia, Mongo mongo) {
		super(mongo, morphia, "mongotest");
	}
}
