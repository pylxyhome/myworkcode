package test;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.query.Query;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TestConnect {
	/**
	 * 2.mongodb中的collection在Java中使用DBCollection表示（这是一个抽象类，尽管你不必需要知道），
	 * 创建DBCollection实例也是一行代码，和创建DB实例一样，这个操作并不涉及真正的和数据库之间的通信。
	 * 
	 * @author panyl
	 * @date 2011-11-22
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	public static DBCollection getDbConnect(String collection1)
			throws UnknownHostException, MongoException {
		DBCollection coll = getDb().getCollection(collection1);
		return coll;
	}

	/**
	 * 1.尽管这里获得了表示mongodb的db_test数据库连接的对象db，但这时并没有真正和mongodb建立连接，
	 * 所以即便这时数据库没起来也不会抛出异常，尽管你还是需要catch它的实例化过程。mongodb的java
	 * driver对连接做了池化处理，所以应用中只需要实例化一个Mongo对象即可，对它的操作是线程安全 的，这对开发使用来说真的是很方便。
	 * 
	 * @author panyl
	 * @date 2011-11-22
	 * @return
	 * @throws UnknownHostException
	 * @throws MongoException
	 */
	public static DB getDb() throws UnknownHostException, MongoException {
		Mongo m = new Mongo("localhost", 27017);
		DB db = m.getDB("mongotest");
		return db;
	}

	public static void showCollectionNames() throws MongoException,
			UnknownHostException {
		Set<String> colls = getDb().getCollectionNames();
		for (String s : colls) {
			System.out.println(s);
		}
	}

	public static void main(String[] args) {
		try {
			// showCollectionNames();
			//insertTest();
			//prepareMorphia();
			//morphiaFind();
			findHote();
			//prepareMorphia();
			System.out.println(Integer.toHexString(14) );
		} catch (MongoException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static void insertTest() throws UnknownHostException, MongoException {
		Mongo m = new Mongo("localhost", 27017);
		DB db = m.getDB("mongotest");
		DBCollection coll =db.getCollection("Users");
		//DBCollection coll = getDbConnect("Users");
		List<DBObject> list = new ArrayList<DBObject>();
		for (int i = 0; i < 1100000; i++) {
			BasicDBObject doc = new BasicDBObject();
			doc.put("name", "andyyulong2" + i);
			doc.put("age", 11+i);
			doc.put("sex", "男");
			doc.put("adrees", "guangzhoutianhe");
			list.add(doc);
		}
		System.out.println("开始insert-------------");
		//110000次-》use time:744ms
		long startTime=System.currentTimeMillis();
		coll.insert(list);
		 m.close();
		long endTime=System.currentTimeMillis();
		System.out.println("use time:"+(endTime-startTime)+"ms");
	}

	public static void findOneTest() throws UnknownHostException,
			MongoException {
		DBCollection coll = getDbConnect("Users");

	}
	public static void findHote()throws UnknownHostException,
	MongoException{
//		Mongo mongo = new Mongo("localhost", 27017);
//		Morphia morphia = new Morphia();
		HotelDAO hotelDAO=new HotelDAO(getMorphiaInstance(),getMongo());
		Hotel hotel=hotelDAO.findOne("name", "My Hotel");  
		System.out.println(hotel);
	}
	private static Morphia morphia=new Morphia();
	private static Mongo mongo ;
	static{
		morphia.mapPackage("test");
	}
	public static Morphia getMorphiaInstance(){
		return morphia;
	}
	public synchronized static Mongo getMongo() throws UnknownHostException, MongoException{
		if(mongo==null){
			mongo= new Mongo("localhost", 27017);
		}
		return mongo;
	}
	public synchronized static DB getDB(String dbName) throws UnknownHostException, MongoException{
		return getMongo().getDB(dbName);
	}
	public static void morphiaFind()throws UnknownHostException,
	MongoException {
		long startTime=System.currentTimeMillis();
		
		Datastore ds = morphia.createDatastore(mongo, "mongotest");
		int i=0;
		long queryUserTime=0l;
//		ds.createQuery(Hotel.class).filter("foo >", 12);  
//		HotelDAO hotelDAO=new HotelDAO(morphia,mongo);
//		Hotel hotel=hotelDAO.findOne("name", "My Hotel");  
//		System.out.println("hotel:"+hotel.getName());
		Query<Users> q=ds.createQuery(Users.class).filter("age >", 11).limit(100); 
		//q=ds.find(Users.class, "age >", 11).limit(100); 
//		UsersDAO usersDAO=new UsersDAO(morphia,mongo);
//		QueryResults<Users> result=usersDAO.find(q);
		List<Users> userList=q.asList();
		//Iterator<Users> it = q.iterator();
		//List<Users> userList=result.asList();
		long endTime=System.currentTimeMillis();
		queryUserTime=(endTime-startTime);
		
	//	System.out.println("记录数:"+list.size());
//		while(it.hasNext()){
//			i++;
//			Users users=it.next();
//			System.out.println(users);
//		}
//		for(Users users : userList){
//			i++;
//			System.out.println(users);
//		}
//		Gson gson = new Gson();
//		String userJson=gson.toJson(userList);
//		Map<String, String> parmas=new HashMap<String,String>();
//		parmas.put("users", userJson);
//		try {
//			NetTool.getContent("http://localhost/SmsServlet", parmas, "utf-8");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		//System.out.println("userJson："+userJson);
		System.out.println("记录数："+userList.size());
		long endTime2=System.currentTimeMillis();
		System.out.println("queryUserTime:"+queryUserTime+"ms");
		System.out.println("output use time:"+(endTime2-startTime)+"ms");
		System.out.println("i="+i);
	}

	public static void prepareMorphia() throws UnknownHostException,
			MongoException {
		for(int i=0;i<1;i++){
			Mongo mongo = new Mongo("localhost", 27017);
			Morphia morphia = new Morphia();
			//morphia.map(Hotel.class).map(Address.class);
			Datastore ds = morphia.createDatastore(mongo, "mongotest");
			Hotel hotel = new Hotel();
			hotel.setName("yulon"+i);
			hotel.setStars(4);
			Address address = new Address();
			address.setStreet("123 Some street"+i);
			address.setCity("Some city"+i);
			address.setPostCode("123 456"+i);
			address.setCountry("Some country"+i);
			// set address
			hotel.setAddress(address);
			ds.save(hotel);
			mongo.close();
		}
	}

}
