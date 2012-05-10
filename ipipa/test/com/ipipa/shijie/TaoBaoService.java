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
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

public class TaoBaoService {
	protected static String url = "http://gw.api.taobao.com/router/rest";// 正式环境需要设置为:
	
	protected static String appkey = "12598216";
	protected static String appSecret = "0b82a53a45c625f81aecfc4feaab8786";
	protected static String ipipa = "http://ipipa.cn:8099";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//httpRequest("http://ipipa.cn:8099/services/service.php?m=share&a=save", "", "POST");
		for(int i=0;i<100;i++){
			
			seach(args[0],Long.valueOf(i));
			try {
				Thread.currentThread().sleep(1000*2);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public static void seach(String word,Long gepageNoNo){

		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		
		ItemsGetRequest req = new ItemsGetRequest();
		req.setFields("num_iid,title,nick,pic_url,cid,price,type,delist_time,post_fee,score,volume");
		req.setQ(word);
		req.setPageNo(gepageNoNo);
		req.setPageSize(200L);
		ItemsGetResponse res = new ItemsGetResponse();
		try {
			res = client.execute(req);
			System.out.println("Seach Result:"+res.getBody());
			List<Item> items = res.getItems();
			if(items != null){
				for(int i=0;i<items.size();i++){
					Item item = items.get(i);
					System.out.println("Get:"+i);
					try {
						Thread.currentThread().sleep(1000*10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					check(item.getNumIid()+"");
				}
					
			}
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/**
	 */
	public static void check(String id){
		Map parameters = new HashMap();
		parameters.put("content", "");
		parameters.put("albumid", "0");
		parameters.put("module", "share");
		parameters.put("action", "save");
		parameters.put("tags", "");
		parameters.put("pub_out_check", "1");
		parameters.put("image_server", "null");
		parameters.put("url", "http://item.taobao.com/item.htm?id="+id);
		String result = doPost(ipipa+"/services/service.php?m=share&a=collectgoods", parameters, "utf-8");
		JSONObject json = JSONObject.parseObject(result);
        String status = json.getString("status");
        System.out.println("检查结果："+result);
        if(status != null && status.equals("1")){
        	try {
				Thread.currentThread().sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	save(json.getString("tags"), json.getString("tags"), json.getString("key"), json.getString("info"));
        }
	}

	public static void save(String content,String tags,String key,String info){
		Map parameters = new HashMap();
		parameters.put("content", content);
		parameters.put("albumid", "0");
		parameters.put("module", "share");
		parameters.put("action", "save");
		parameters.put("tags", tags);
		parameters.put("pub_out_check", "1");
		parameters.put("goods["+key+"]", info);
		parameters.put("goods_tags["+key+"]", tags);
		parameters.put("goods_sort["+key+"]", "1");
		doPost(ipipa+"/services/service.php?m=share&a=save", parameters, "utf-8");
	}
	public static String httpRequest(String url, String body, String method) {
//		System.out.println("body:" + body);
//		System.out.println(port + url);
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setDoOutput(true);
			c.setDoInput(true);
			c.setRequestMethod(method);
			c.setUseCaches(false);
			c.setRequestProperty("Content-Type","text/html;charset=utf-8");
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
				String str ;
				StringBuilder sb = new StringBuilder();
				while((str = br.readLine()) != null){
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
		} finally{
			if (c != null) {
				c.disconnect();
			}
		}
		return null;
	}
	 public static String doPost(String reqUrl, Map parameters,
	            String recvEncoding)
	    {
	        HttpURLConnection url_con = null;
	        String responseContent = null;
	        try
	        {
	            StringBuffer params = new StringBuffer();
	            for (Iterator iter = parameters.entrySet().iterator(); iter
	                    .hasNext();)
	            {
	                Entry element = (Entry) iter.next();
	                params.append(URLEncoder.encode(element.getKey().toString(),recvEncoding));
	                params.append("=");
	                params.append(URLEncoder.encode(element.getValue().toString(),
	                		recvEncoding));
	                params.append("&");
	            }

	            if (params.length() > 0)
	            {
	                params = params.deleteCharAt(params.length() - 1);
	            }

	            URL url = new URL(reqUrl);
	            url_con = (HttpURLConnection) url.openConnection();
	            url_con.setRequestMethod("POST");
	            url_con.addRequestProperty("Cookie", "Wj6B_2132_saltkey=xFzrerRY; Wj6B_2132_last_visit=1334627256; Wj6B_2132_last_request=3e2dDvt2%2BIAtK967wqNsLk3eRJWQIl1yP33zXxiOICoaO6wgQ8UZ; Wj6B_2132_auth=c3d1xbHAdmEuaUHtRnC%2FtgjOw04DcqX8G8bgqSgNxkaM392%2FJ2YQ1n4EeAJGfVksP6Km%2BSiJmM4h%2BccOQDDA; Wj6B_2132_last_login_day_0=1334736000; PDrD_2132_saltkey=H02dd993; PDrD_2132_last_visit=1334882088; PDrD_2132_last_request=0942Q3n0QHGUfs244YUznnBnt9%2B9wp%2B7WWnNZBV6M1U%2Bmt1oLLdN; PDrD_2132_auth=eaffIF174F9e2lXH%2B8RoPgrGYZe21qsEMwYCkBQUsUz44e6QZvpnJuwJ1hAY2o5F0zidxrqWvHyTX5i2Gacd; PDrD_2132_last_login_day_0=1334822400; 2H98_2132_saltkey=bpkpwCBm; 2H98_2132_last_visit=1334895527; 32f3_2132_saltkey=g111SGuv; 32f3_2132_last_visit=1334896480; 92c7_2132_saltkey=4SOy4H55; 92c7_2132_last_visit=1334896889; 92c7_2132_last_request=246bj%2FGaPAgaAm2L9klHeImlm3TUuqlijcIYOjpJ0J0Sz8WlA0tB; 92c7_2132_last_login_day_0=1334995200; giIV_2132_saltkey=o97p7sTs; giIV_2132_last_visit=1335036463; giIV_2132_last_request=e850s2Rf8VZ8C9WZfpQstnfRrsu2K5Ro%2FsLxBSwU4506ogFmp2U2; giIV_2132_auth=3913ATLQr8g336DQ12FqbSxQAnnxVic0qLcSBLcb2Q%2F3zsKgSANbGYKnFDtoQr4zmuheRsIldz9xc1ClZfyr; FKVq_2132_saltkey=wwsIoIbd; FKVq_2132_last_visit=1335141551; FKVq_2132_last_request=1829P9bs%2BTgx2tt2OATG41%2FzIR07q2WJF1%2Bf2gEtCzbq6w05S0G9; FKVq_2132_last_login_day_0=1335081600; FKVq_2132_sid=w7k2j5; PHPSESSID=bffe5d12eda0bfb0904e2c8a19de41dd; giIV_2132_sid=apz3X3; giIV_2132_last_login_day_0=1335081600");
	            //System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(HttpRequestProxy.connectTimeOut));// （单位：毫秒）jdk1.4换成这个,连接超时
	            //System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(HttpRequestProxy.readTimeOut)); // （单位：毫秒）jdk1.4换成这个,读操作超时
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
	            String crlf=System.getProperty("line.separator");
	            while (tempLine != null)
	            {
	                tempStr.append(tempLine);
	                tempStr.append(crlf);
	                tempLine = rd.readLine();
	            }
	            responseContent = tempStr.toString();
	            
	            System.out.println("Response:"+responseContent);
	            rd.close();
	            in.close();
	        }
	        catch (IOException e)
	        {
//	            logger.error("网络故障", e);
	        }
	        finally
	        {
	            if (url_con != null)
	            {
	                url_con.disconnect();
	            }
	        }
	        return responseContent;
	    }
}
