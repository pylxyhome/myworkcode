package util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

public class Jdbc {
	    
	
	
	private static HashMap<String, DataSource> dses = new HashMap<String, DataSource>();
	
	public static Connection getConnection() throws SQLException{
		return getConn("default");  
	}
	
	public static Connection getConn(String name) throws SQLException{
		DataSource ds = dses.get(name);
		if(ds == null){
			Properties prop = new Properties();
			InputStream inStream =GetConfig.class.getResourceAsStream("/db_"+ name +".properties");
			try {
				prop.load(inStream);
				inStream.close();
				String driverName = prop.getProperty("driverName");
				String url = prop.getProperty("url");    
				String username = prop.getProperty("name");
				String psw = prop.getProperty("psw");
				System.out.println("getConn name=" + name);
				System.out.println("getConn driverName=" + driverName);
				System.out.println("getConn url=" + url);
				System.out.println("getConn username=" + username);
				System.out.println("getConn psw=" + psw);
				BasicDataSource bds = new BasicDataSource();
				bds.setRemoveAbandoned(true);//设置连接无用时收回
				bds.setRemoveAbandonedTimeout(60*60);//活动连接在3600秒内无任何操作则回收
				bds.setInitialSize(8);//初始化连接数
				bds.setMaxActive(20);//连接池中可同时连接的最大的连接数,默认值8
         		bds.setMaxIdle(8);//最大空闲连接数,默认值8
				bds.setMinIdle(0);//最少空闲连接数,默认值0，现调整为3
				bds.setTestWhileIdle(true);//打开检查,用异步线程evict进行检查连接情况，若无效连接会自动关闭后，适当建立最小的minIdle连接数
				bds.setTimeBetweenEvictionRunsMillis(1000*60);//设置的Evict线程的时间，5分钟启用Evict线程
				bds.setValidationQuery("select 1");
				bds.setValidationQueryTimeout(3);//在执行检查时,query超过设置秒数后当做连接无效处理
				bds.setDriverClassName(driverName);
				bds.setUrl(url);
				bds.setUsername(username);
				bds.setPassword(psw);
				ds = bds;
				dses.put(name, bds);				
				
			} catch (IOException e) {
				e.printStackTrace();			
			}
		}
		
		return ds.getConnection();
	} 
	
	public static void main(String[] args){
		try {
			int n=1;
//			while(n<=8){
				Connection conn = Jdbc.getConnection();
				conn.createStatement().executeQuery("select 1");
				
				System.out.println(n);
				n++;
//			}
			Thread.sleep(1000*32);
			System.out.println(20);
			Connection conn2 = Jdbc.getConnection();
			conn2.createStatement().executeQuery("select 1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
