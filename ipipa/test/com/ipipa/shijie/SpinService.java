package com.ipipa.shijie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ipipa.shijie.HuaBanUserService.HuaBanThread;
import com.ipipa.shijie.POJO.TaobaoKey;
import com.ipipa.shijie.POJO.UserCategory;
import com.ipipa.shijie.POJO.UserKey;
import com.ipipa.taobao.TaobaoClientUtil;
import com.ipipa.utils.HttpUtil;
import com.ipipa.utils.MySqlUtil;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemsGetRequest;
import com.taobao.api.response.ItemsGetResponse;

public class SpinService {
	protected static String url = "http://gw.api.taobao.com/router/rest";// 正式环境需要设置为:

	protected static String appkey = "12598216";
	protected static String appSecret = "0b82a53a45c625f81aecfc4feaab8786";
	protected static String ipipa = "http://ipipa.cn:9099";

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		/**
		 * 1、查询淘宝用户及用户的关键
		 * 2、搜索淘宝数据
		 * 3、根据搜索关键字查找对应的用户cookie及其分类id
		 * 4、把数据写入到数据库
		 */
		SpinService ss = new SpinService();
		ss.Start();
		
	}

	private void Start(){
		ThreadGroup group = new ThreadGroup("Spin");
		
		Thread readThread = new Thread(group,new ReadThread());
		Thread writeThread = new Thread(group,new WriteThread());
		
		readThread.start();
		writeThread.start();
	}
	private List<UserKey> readTbUserKeys(){
		PreparedStatement stmt;
		List<UserKey> userKeys = new ArrayList<UserKey>();
		try {
			String sql = "select tu.name,tk.keyword from tbuser tu,tbkeys tk,tbkeymap tm where tu.id = tm.tbuserid and tk.id = tm.tbkeyid";
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			
			ResultSet rst = (ResultSet) stmt.executeQuery();
			while(rst.next()){
				UserKey uk = new UserKey();
				uk.setName(rst.getString(1));
				uk.setKeyword(rst.getString(2));
				userKeys.add(uk);
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userKeys;
	}
	private List<TaobaoKey> readTbKeys(){
		PreparedStatement stmt;
		List<TaobaoKey> keys = new ArrayList<TaobaoKey>();
		try {
			String sql = "select * from tbkeys";
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			
			ResultSet rst = (ResultSet) stmt.executeQuery();
			while(rst.next()){
				TaobaoKey uk = new TaobaoKey();
				uk.setId(rst.getLong(1));
				uk.setKeyword(rst.getString(2));
				keys.add(uk);
			}
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return keys;
	}	
	/**
	 * 查询淘宝宝贝的方法
	 * @param word
	 * @param nick
	 * @param pageNoNo
	 */
	private int seachTbItemsByUser(String word,String nick,Long pageNoNo){
		//
		System.out.println("Seach "+nick+","+word);
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		ItemsGetRequest req = new ItemsGetRequest();
		
		req.setFields("num_iid,title,nick,desc,pic_url,cid,price,type,delist_time,post_fee,score,volume,detail_url");
		req.setQ(word);
		if(nick != null && !nick.equals("")){
			req.setNicks(nick);
		}
		
		req.setPageNo(pageNoNo);
		req.setPageSize(200L);
		
		ItemsGetResponse res = new ItemsGetResponse();
		try {
			res = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
			List<Item> items = res.getItems();

			if (items != null) {
				System.out.println("Seach Result:"+items.size());
				
				//saveItems(items, word);
				
				for(int i=0;i<items.size();i++){
					if(!isExist(items.get(i).getNumIid()+"")){
						String title = items.get(i).getTitle();
						if(title!=null){
							title = title.replaceAll("<span class=H>", " ");
							title = title.replaceAll("</span>", " ");
						}
						String content=items.get(i).getDesc()==null?title:items.get(i).getDesc();
						saveItems(content, title, items.get(i).getPicUrl(), items.get(i).getDetailUrl(), items.get(i).getNumIid()+"", "0", word, true);
					}
				}
				
				return items.size();
			}
			

		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	private Map<Long,UserCategory> seachUserCid(String key){

		PreparedStatement stmt;
		Map<Long,UserCategory> ucs = new HashMap<Long,UserCategory>();
		try {
			key ="%"+key+"%";
			String sql = "select distinct u.id,u.cookie,c.cid from user u,category c where c.name like '"+key+"' and u.id = c.userid";
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			ResultSet rst = (ResultSet) stmt.executeQuery();
			Long i=0L;
			while(rst.next()){
				UserCategory uc = new UserCategory();
				uc.setUserid(rst.getLong(1));
				uc.setCookie(rst.getString(2));
				uc.setCid(rst.getLong(3));
				ucs.put(i,uc);
				
				i++;
				
			}
			rst.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ucs;
	
	}
	private void saveItems(String content,String title,String imageUrl,String pageUrl,String num_id,String cid,String cname,boolean bat){
		
		PreparedStatement stmt;
		try {
			String sql = "insert into items (content,title,imageUrl,pageUrl,num_id,cid,cname,status)values(?,?,?,?,?,?,?,?)";
			System.out.println("SQL:"+sql);
			System.out.println("pageUrl:"+pageUrl);
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			stmt.setString(1, content);
			stmt.setString(2, title);
			stmt.setString(3, imageUrl);
			stmt.setString(4, pageUrl);
			stmt.setString(5, num_id);
			stmt.setString(6, cid);
			stmt.setString(7, cname);
			stmt.setString(8, "0");
			stmt.execute();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	private boolean isExist(String numid){

		PreparedStatement stmt;
		try {
			String sql = "select * from items where num_id=?";
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			stmt.setString(1, numid);
			ResultSet rst = (ResultSet) stmt.executeQuery();
			if(rst.next())return true;
			rst.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	
	
	}
	private void saveItems(List<Item> items,String word){
		
		PreparedStatement stmt;
		try {
			String sql = "insert into items (content,title,imageUrl,pageUrl,num_id,cid,cname,status)values(?,?,?,?,?,?,?,?)";
			MySqlUtil.getConnection().setAutoCommit(false);
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			
			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				//save(item.getDesc(), titile, item.getPicUrl(), item.getDetailUrl(),cid);
				String title = item.getTitle();
				if(title!=null){
					title = title.replaceAll("<span class=H>", " ");
					title = title.replaceAll("</span>", " ");
				}
				String content=item.getDesc()==null?title:item.getDesc();
				stmt.setString(1, content);
				stmt.setString(2, title);
				stmt.setString(3, item.getPicUrl());
				stmt.setString(4, item.getDetailUrl());
				stmt.setString(5, item.getNumIid()+"");
				stmt.setString(6, "0");
				stmt.setString(7, word);
				stmt.setString(8, "0");
				if(i==items.size() -1){
					stmt.executeBatch();
					MySqlUtil.getConnection().commit();
					MySqlUtil.getConnection().setAutoCommit(true);
					
				}else{
					stmt.addBatch();
				}
			}
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private List<String> getItemKeys(){
		PreparedStatement stmt;
		List<String> itmeKeys = new ArrayList<String>();
		try {
			String sql = "SELECT DISTINCT  CNAME FROM items";
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			
			ResultSet rst = (ResultSet) stmt.executeQuery();
			while(rst.next()){
				itmeKeys.add(rst.getString(1));
			}
			rst.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itmeKeys;
	}
	private List<com.ipipa.shijie.POJO.Item> getItems(List<String> itemKeys){
		
		List<com.ipipa.shijie.POJO.Item> itmes = new ArrayList<com.ipipa.shijie.POJO.Item>();
		try {
			String sql = "SELECT *  FROM items where cname=? and status=0 limit 1,1";
			for (int i = 0; i < itemKeys.size(); i++) {
				
				PreparedStatement stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
				stmt.setString(1, itemKeys.get(i));
				ResultSet rst = (ResultSet) stmt.executeQuery();
				if(rst.next()){
					//id, content, album_id, title, imageUrl, pageUrl, num_id, cid, cname, status
					com.ipipa.shijie.POJO.Item item = new com.ipipa.shijie.POJO.Item();
					item.setId(rst.getLong(1));
					item.setContent(rst.getString(2));
					item.setAlbum_id(rst.getString(3));
					item.setTitle(rst.getString(4));
					item.setImageUrl(rst.getString(5));
					item.setPageUrl(rst.getString(6));
					item.setNum_id(rst.getString(7));
					item.setCid(rst.getString(8));
					item.setCname(rst.getString(9));
					item.setStatus(rst.getString(10));
					itmes.add(item);
				}
				rst.close();
				stmt.close();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return itmes;
	
	}
	private void updateStatus(Long id){

		PreparedStatement stmt;
		try {
			String sql = "update items set status='1' where id=?";
			stmt = (PreparedStatement) MySqlUtil.getConnection().prepareStatement(sql);
			stmt.setLong(1, id);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String saveItem2Ipipa(com.ipipa.shijie.POJO.Item item,String cid,String cookie){
		
		Map parameters = new HashMap();
		parameters.put("content", item.getContent());
		parameters.put("album_id", cid);
		parameters.put("title", item.getTitle());
		parameters.put("videoArray[]", "undefined");
		parameters.put("imgArray[]", item.getImageUrl());
		parameters.put("pub_out_check", "0");
		parameters.put("pageUrl", item.getPageUrl());
		return HttpUtil.doPost(ipipa + "/services/service.php?m=collectshare&a=save",
				parameters, "utf-8",cookie);
	
	}
	public class ReadThread implements Runnable {

		public void run() {
			// TODO Auto-generated method stub
			List<UserKey> userKeys = readTbUserKeys();
			if(userKeys.size()>=0){
				for (Iterator iterator = userKeys.iterator(); iterator.hasNext();) {
					UserKey key = (UserKey) iterator.next();
					Long pageNo =1L;
					while(seachTbItemsByUser(key.getKeyword(), key.getName(), pageNo)>0){
						pageNo++;
					}
				}
			}
			
			/*
			List<TaobaoKey> tbKeys = readTbKeys();
			if(tbKeys.size()>0){
				for (Iterator iterator = tbKeys.iterator(); iterator.hasNext();) {
					TaobaoKey key = (TaobaoKey) iterator.next();
					Long pageNo =1L;
					while(seachTbItemsByUser(key.getKeyword(), null, pageNo)>0){
						pageNo++;
						try {
							Thread.currentThread().sleep(1000*3);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(pageNo>10)break;
					}
				}
			}
			*/
		}
		
	}
	public class WriteThread implements Runnable {
		private int old_id=0;
		
		public void run() {
			// TODO Auto-generated method stub
			//获取Itemkeys
			//获取Items
			//根据item cname查询用户
			//随机选择一个用户保存数据
			while(true){
				List<String> itemKeys = getItemKeys();
				if(itemKeys.size()>0){
					List<com.ipipa.shijie.POJO.Item> items = getItems(itemKeys);
					
					for (int i = 0; i < itemKeys.size(); i++) {
						String cname = itemKeys.get(i);
						Map<Long,UserCategory> ucs = seachUserCid(cname);
						if(ucs.isEmpty()){
							ucs = seachUserCid("其他");
						}
						
						
						//
						Random random = new Random();
						int inx = random.nextInt(ucs.size());
						while(inx==old_id)
							inx = random.nextInt(ucs.size());
						
						Long key = new Long(inx);
						UserCategory uc = ucs.get(key);
						
						if(i<items.size()){
							System.out.println("User:"+uc.getUserid());
							old_id = inx;
							saveItem2Ipipa(items.get(i), uc.getCid()+"", uc.getCookie());
							updateStatus(items.get(i).getId());
						}
					}
				}
				//
				try {
					Thread.currentThread().sleep(1000*3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
