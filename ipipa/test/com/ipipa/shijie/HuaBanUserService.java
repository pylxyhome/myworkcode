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
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.request.ItemsGetRequest;
import com.taobao.api.response.ItemsGetResponse;

public class HuaBanUserService {
	protected static String url = "http://gw.api.taobao.com/router/rest";// 正式环境需要设置为:

	protected static String appkey = "12598216";
	protected static String appSecret = "0b82a53a45c625f81aecfc4feaab8786";
	protected static String ipipa = "http://ipipa.cn:9099";

	private static Properties userProperties = new Properties();
	
	private static Properties tusProperties = new Properties();

	private static int currentUser = 1;
	private static int userTotal = 0;

	private static BlockingQueue<Runnable> writeQueue = new LinkedBlockingDeque<Runnable>();
	private static ThreadPoolExecutor writePool = new ThreadPoolExecutor(30,
			60, 1000 * 2, TimeUnit.DAYS, writeQueue);

	/**
	 * 查询产品Map
	 */
	private HashMap<Integer, Item> map = new HashMap<Integer, Item>();
	/**
	 * 淘宝用户map
	 */
	private HashMap<Integer, String> tbumap = new HashMap<Integer, String>();
	/**
	 * 原淘宝用户KEY
	 */
	private int old_key = 1;
	private Random random = new Random();
	
	private int old_item_key = 1;
	private Random itemRandom = new Random();
	/**
	 * 产品总数
	 */
	private Integer count = 0;

	private final static Integer MAX_SIZE = 100;


	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		InputStream userStream = ClassLoader
				.getSystemResourceAsStream("user.properties");
		userProperties.load(userStream);
		userTotal = Integer.parseInt(userProperties.getProperty("Total"));
		/**/
		InputStream tusStream = ClassLoader
				.getSystemResourceAsStream("taobao_users.properties");
		tusProperties.load(tusStream);

		InputStream keyStream = ClassLoader
				.getSystemResourceAsStream("keys.properties");
		Properties keyProperties = new Properties();
		keyProperties.load(keyStream);
		int keyTotal = Integer.parseInt(keyProperties.getProperty("Total"));

		HuaBanUserService huaban = new HuaBanUserService();
		
		huaban.initTaoBaoUser();
		
		for (int pageNo = 1; pageNo < 600; pageNo++) {
			for (int ki = 1; ki <= keyTotal; ki++) {
				String key = keyProperties.getProperty("item_" + ki);
				if (key == null)
					continue;

				String[] keys = key.split(";");
				for (int i = 0; i < keys.length; i++) {
					huaban.seach(keys[i], Long.valueOf(pageNo), ki);
					try {
						Thread.currentThread().sleep(1000 * 3);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	private String getTaoBaoUser() {
		
		while(true){
			Integer key = random.nextInt(tbumap.size());
			if(key ==old_key )continue;
			
			String obj = tbumap.get(key);
			if(obj != null){old_key = key; return obj;}
		}
		
	}

	private void initTaoBaoUser() {
		int keyTotal = Integer.parseInt(tusProperties.getProperty("Total"));
		for (int ki = 1; ki < keyTotal; ki++) {
			String key = tusProperties.getProperty("item_" + ki);
			if(key == null ) continue;
			//
			tbumap.put(new Integer(ki), key);
		}
	}

	public void seach(String word, Long gepageNoNo, int cid) {
		
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类

		ItemsGetRequest req = new ItemsGetRequest();
		req
				.setFields("num_iid,title,nick,desc,pic_url,cid,price,type,delist_time,post_fee,score,volume,detail_url");
		req.setQ(word);
		req.setNicks(getTaoBaoUser());
		
		System.out.println("Seach "+req.getNicks()+"'s Word:" + word);
		req.setPageNo(gepageNoNo);
		req.setPageSize(25L);
		ItemsGetResponse res = new ItemsGetResponse();
		try {
			try {
				Thread.currentThread().sleep(1000 * 3);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res = client.execute(req);
			List<Item> items = res.getItems();

			if (items != null) {
				System.out.println("Get Result:" + items.size());
				for (int i = 0; i < items.size(); i++) {
					Item item = items.get(i);
					//
					if (count <= MAX_SIZE) {
						map.put(count, item);
						count++;
					} else {
						break;
					}
				}
				/**
				 * 保存数据
				 */
				System.out.println("已获取产品总数:" + count);
				if (count >= MAX_SIZE) {
					while(true){
						
						if(map.size()==0)break;
						Integer key = itemRandom.nextInt(map.size());
						if(key ==old_item_key )continue;
						
						Item itemObject = map.get(key);
						if(itemObject != null){
							old_item_key = key;
							writePool.execute(new HuaBanThread(itemObject, cid));
							map.remove(key);
						}
					}
					
					count = 0;
					System.gc();

				}
			}

		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 */
	public static void check(String id) {
		Map parameters = new HashMap();
		parameters.put("content", "");
		parameters.put("albumid", "0");
		parameters.put("module", "share");
		parameters.put("action", "save");
		parameters.put("tags", "");
		parameters.put("pub_out_check", "1");
		parameters.put("image_server", "null");
		parameters.put("url", "http://item.taobao.com/item.htm?id=" + id);
		String result = doPost(ipipa
				+ "/services/service.php?m=share&a=collectgoods", parameters,
				"utf-8");
		JSONObject json = JSONObject.parseObject(result);
		String status = json.getString("status");
		System.out.println("检查结果：" + result);
		if (status != null && status.equals("1")) {
			try {
				Thread.currentThread().sleep(1000 * 5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			save(json.getString("tags"), json.getString("tags"), json
					.getString("key"), json.getString("info"), 0);
		}
	}

	public static void save(String content, String title, String imageUrl,
			String pageUrl, int cid) {
		Map parameters = new HashMap();
		parameters.put("content", content == null ? title : content);
		parameters.put("album_id", cid);
		parameters.put("title", title);
		parameters.put("videoArray[]", "undefined");
		parameters.put("imgArray[]", imageUrl);
		parameters.put("pub_out_check", "0");
		parameters.put("pageUrl", pageUrl);
		doPost(ipipa + "/services/service.php?m=collectshare&a=save",
				parameters, "utf-8");
	}

	public static String httpRequest(String url, String body, String method) {
		// System.out.println("body:" + body);
		// System.out.println(port + url);
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setDoOutput(true);
			c.setDoInput(true);
			c.setRequestMethod(method);
			c.setUseCaches(false);
			c.setRequestProperty("Content-Type", "text/html;charset=utf-8");
			c.connect();
			if (method.toUpperCase() == "POST" || method.toUpperCase() == "PUT") {
				OutputStream out = c.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out);
				BufferedWriter bw = new BufferedWriter(osw);
				bw.write(body);
				bw.flush();
				bw.close();
				osw.close();
				out.close();
			}
			c.disconnect();
			System.out.println(c.getResponseCode());
			if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStream in = c.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr);
				String str;
				StringBuilder sb = new StringBuilder();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				br.close();
				isr.close();
				in.close();
				c.disconnect();
				return sb.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.disconnect();
			}
		}
		return null;
	}

	public static String doPost(String reqUrl, Map parameters,
			String recvEncoding) {
		HttpURLConnection url_con = null;
		String responseContent = null;
		try {
			StringBuffer params = new StringBuffer();
			for (Iterator iter = parameters.entrySet().iterator(); iter
					.hasNext();) {
				Entry element = (Entry) iter.next();
				params.append(URLEncoder.encode(element.getKey().toString(),
						recvEncoding));
				params.append("=");

				params.append(URLEncoder.encode(element.getValue().toString(),
						recvEncoding));
				params.append("&");
			}

			if (params.length() > 0) {
				params = params.deleteCharAt(params.length() - 1);
			}

			URL url = new URL(reqUrl);
			url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("POST");
			String cookie = "";
			if (currentUser <= userTotal) {
				cookie = userProperties.getProperty("user_" + currentUser);
			} else {
				currentUser = 1;
				cookie = userProperties.getProperty("user_" + currentUser);
			}

			currentUser++;
			url_con.addRequestProperty("Cookie", cookie);

			// System.setProperty("sun.net.client.defaultConnectTimeout",
			// String.valueOf(HttpRequestProxy.connectTimeOut));//
			// （单位：毫秒）jdk1.4换成这个,连接超时
			// System.setProperty("sun.net.client.defaultReadTimeout",
			// String.valueOf(HttpRequestProxy.readTimeOut)); //
			// （单位：毫秒）jdk1.4换成这个,读操作超时
			// url_con.setConnectTimeout(5000);//（单位：毫秒）jdk
			// 1.5换成这个,连接超时
			// url_con.setReadTimeout(5000);//（单位：毫秒）jdk 1.5换成这个,读操作超时
			url_con.setDoOutput(true);
			byte[] b = params.toString().getBytes();
			url_con.getOutputStream().write(b, 0, b.length);
			url_con.getOutputStream().flush();
			url_con.getOutputStream().close();

			InputStream in = url_con.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in,
					recvEncoding));
			String tempLine = rd.readLine();
			StringBuffer tempStr = new StringBuffer();
			String crlf = System.getProperty("line.separator");
			while (tempLine != null) {
				tempStr.append(tempLine);
				tempStr.append(crlf);
				tempLine = rd.readLine();
			}
			responseContent = tempStr.toString();

			System.out.println("Response:" + responseContent);
			rd.close();
			in.close();
		} catch (IOException e) {
			// logger.error("网络故障", e);
		} finally {
			if (url_con != null) {
				url_con.disconnect();
			}
		}
		return responseContent;
	}

	public class HuaBanThread implements Runnable {
		private Item itemObject;
		private int cid;

		public HuaBanThread(Item itemObject, int cid) {
			this.itemObject = itemObject;
			this.cid = cid;
		}

		public void run() {
			// TODO Auto-generated method stub
			String titile = itemObject.getTitle().substring(0,
					itemObject.getTitle().indexOf("<span"));
			save(itemObject.getDesc(), titile, itemObject.getPicUrl(),
					itemObject.getDetailUrl(), cid);
		}

	}
}
