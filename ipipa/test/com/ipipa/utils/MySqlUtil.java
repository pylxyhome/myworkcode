package com.ipipa.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class MySqlUtil {

	private static Connection conn = null;
	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, Exception {
		// TODO Auto-generated method stub
		for(int i=0;i<10;i++){
			System.out.println(i+":");
			Statement stmt = (Statement) getConnection().createStatement();
			ResultSet rst = (ResultSet) stmt.executeQuery("select * from user");
			while(rst.next()){
				System.out.println(rst.getString(2));
			}
		}
		
	}
	public static Connection getConnection(){
		if(conn != null){
			return conn;
		}else{
			try {
				init();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(conn != null)return conn;
		}
		return null;
	}
	
	private static void init() throws Exception{
		Properties dbProperties = new Properties();
		InputStream dbStream = ClassLoader.getSystemResourceAsStream("db.properties");
		dbProperties.load(dbStream);
		String url = dbProperties.getProperty("url");
		String user = dbProperties.getProperty("user");
		String pwd = dbProperties.getProperty("password");
		System.out.println("MySql Connection:"+url);
		
		if(url==null){
			return ;
		}else{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = (Connection)java.sql.DriverManager.getConnection(url,user, pwd);
		}
	}

}
