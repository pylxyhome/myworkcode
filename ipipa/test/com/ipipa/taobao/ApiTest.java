package com.ipipa.taobao;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ipipa.utils.HtmlUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemImg;
import com.taobao.api.domain.ItemSearch;
import com.taobao.api.domain.PropImg;
import com.taobao.api.domain.Shop;
import com.taobao.api.request.CategoryrecommendItemsGetRequest;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.request.ItemsGetRequest;
import com.taobao.api.request.ItemsListGetRequest;
import com.taobao.api.request.ItemsSearchRequest;
import com.taobao.api.request.ProductsGetRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.UserGetRequest;
import com.taobao.api.response.CategoryrecommendItemsGetResponse;
import com.taobao.api.response.ItemcatsGetResponse;
import com.taobao.api.response.ItemsGetResponse;
import com.taobao.api.response.ItemsListGetResponse;
import com.taobao.api.response.ItemsSearchResponse;
import com.taobao.api.response.ProductsGetResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.UserGetResponse;

public class ApiTest {

//	protected static String url = "http://gw.api.tbsandbox.com/router/rest";// 沙箱环境调用地址
	protected static String url = "http://gw.api.taobao.com/router/rest";// 正式环境需要设置为:
	
	protected static String appkey = "12598216";
	protected static String appSecret = "0b82a53a45c625f81aecfc4feaab8786";
	/**
	 * 获取用户信息
	 */
	public static void testUserGet() {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		UserGetRequest req = new UserGetRequest();// 实例化具体API对应的Request类
		req
				.setFields("nick,sex,buyer_credit,seller_credit ,created,last_visit");
		req.setNick("jy67153878");
		UserGetResponse response;
		try {
			response = client.execute(req); // 执行API请求并打印结果
			System.out.println("testUserGet result:" + response.getBody());
			System.out.println("nick:" + response.getUser().getNick());
		} catch (ApiException e) { // deal error

		}
	}
	/**
	 * 获取店铺信息
	 * @description
	 * @author panyl
	 * @date 2012-5-9
	 */
	public static void testShopGet() {
	
		try {
			ShopGetRequest req=new ShopGetRequest();
			req.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified,shop_score");
			req.setNick("小小i沛");
			ShopGetResponse response = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
			Shop shop=response.getShop();
			System.out.println("sid:"+shop.getSid());
			System.out.println("店铺地址:shop"+shop.getSid()+".taobao.com");
			
			System.out.println("Title:"+shop.getTitle());
			System.out.println("Desc:"+shop.getDesc());
			System.out.println("UsedCount:"+shop.getUsedCount());
			System.out.println("ItemScore:"+shop.getShopScore().getItemScore());
		} catch (ApiException e) { // deal error

		}
	}
	/**
	 * 
	 * 获取产品分类
	 */

	public static void testCatelogGet(){
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		CategoryrecommendItemsGetRequest category = new CategoryrecommendItemsGetRequest();
		category.setCount(1000L);
		
		try { 
			CategoryrecommendItemsGetResponse res = client.execute(category);
			System.out.println("testCatelogGet body:"+res.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取商品分类
	 */
	public static void testItemCatsGet(){
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		ItemcatsGetRequest req = new ItemcatsGetRequest();
		req.setFields("cid,parent_cid,name,is_parent");
		req.setParentCid(0L);
		//req.setCids("1,2,3,4,5,6,7,8,9,10,11,12,13,14,19562,50011999,1705");
		try {
			ItemcatsGetResponse res = client.execute(req);
			System.out.println("msg:"+res.getMsg());
			System.out.println("testItemCatsGet Body:"+res.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void testProductGet(){
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		ProductsGetRequest req=new ProductsGetRequest();
		req.setFields("product_id,cat_name,name");
		req.setNick("鲸鲸饰界");
		req.setPageNo(1L);
		req.setPageSize(40L);
		try {
			ProductsGetResponse res = client.execute(req);
			System.out.println("testProductGet Body:"+res.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void testItemList(){
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		ItemsListGetRequest req = new ItemsListGetRequest();
		req.setFields("num_iid,title,nick,price,detail_url,props_name");
		req.setNumIids("1000498");
		ItemsListGetResponse res = new ItemsListGetResponse();
		
		try {
			res = client.execute(req);
			System.out.println("testItemList "+res.getBody());
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void testItemsSearch() throws Exception{
		//TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		long pageNo=1;
		long pageSize=200l;
		long allstartTime=System.currentTimeMillis();
		int allCount=0;
		while(true){
			if(pageNo*pageSize>10240l)break;
		long startTime=System.currentTimeMillis();
	//	ItemsGetRequest req = new ItemsGetRequest();
		ItemsSearchRequest req=new ItemsSearchRequest();
		req.setFields("num_iid,title,nick,pic_url,cid,props,props_name,price,type,delist_time,post_fee,score,volume,detail_url,is_xinpin,item_imgs,prop_imgs,promoted_service");
		req.setQ("项链");
		/**
		 * req.setOrderBy("volume:desc");
//		req.setOrderBy("seller_credit:desc");
		 *  allCount: 10200
			AlluserTime: 95042
			
			没有排序
		 */
//		req.setOrderBy("volume:desc");
//		req.setOrderBy("seller_credit:desc");
		//req.setNicks("喔啦啦时尚家居馆");
//		req.setCid(443376287L);
		//req.setOrderBy("volume:desc");
		req.setPageNo(1L);
		req.setPageSize(200l);
		ItemsSearchResponse  res = new ItemsSearchResponse ();
		try {
			res = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
			ItemSearch itemSearch = res.getItemSearch();
			if(itemSearch.getItems() == null||itemSearch.getItems().size()==0)break;
			if(itemSearch.getItems() != null){
				allCount=allCount+itemSearch.getItems().size();
				for(int i=0;i<itemSearch.getItems().size();i++){
					
					Item item = itemSearch.getItems().get(i);
					System.out.print(i+":"+item.getNumIid()+""+ HtmlUtil.HtmltoText(item.getTitle())+" score:"+item.getScore()+" price:"+item.getPrice()+" volume:"+item.getVolume()+" nick:"+item.getNick()+"\n pageUrl:"+item.getDetailUrl()+"\n picUrl:"+item.getPicUrl()+"\n props_name:"+item.getPropsName()
							+"\n promoted_service:"+item.getPromotedService());
					if(item.getItemImgs()!=null){
					for(ItemImg img : item.getItemImgs()){
						System.out.print("images:"+img.getUrl());
					}
					}
					if(item.getPropImgs()!=null){
						for(PropImg propImg : item.getPropImgs()){
							System.out.print("proimages:"+propImg.getUrl());
						}
						}
					System.out.println();
				}
					
			}
			long endTime=System.currentTimeMillis();
			System.out.println("userTime: "+(endTime-startTime));
			System.out.println("count: "+allCount);
			System.out.println("testItemsGet "+res.getBody());
			pageNo++;
			//System.out.println(NetTool.getTextContent("http://item.taobao.com/item.htm?id=10527746029", "gbk"));
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		long allendTime=System.currentTimeMillis();
		System.out.println("allCount: "+allCount);
		System.out.println("AlluserTime: "+(allendTime-allstartTime));
	}
	public static void testItemsGet() throws Exception{
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);// 实例化TopClient类
		long startTime=System.currentTimeMillis();
		ItemsGetRequest req = new ItemsGetRequest();
		req.setFields("num_iid,title,nick,pic_url,cid,props,props_name,price,type,delist_time,post_fee,score,volume,detail_url,is_xinpin,item_imgs,prop_imgs,promoted_service");
		req.setQ("饰品");
		//req.setNicks("喔啦啦时尚家居馆");
//		req.setCid(443376287L);
		req.setOrderBy("volume:desc");
		req.setPageNo(1L);
		req.setPageSize(200l);
		Set<Long> cids=new HashSet<Long>();
		ItemsGetResponse res = new ItemsGetResponse();
		try {
			res = client.execute(req);
			List<Item> items = res.getItems();
			if(items != null){
				for(int i=0;i<items.size();i++){
					Item item = items.get(i);
					System.out.print(i+":"+item.getNumIid()+""+ HtmlUtil.HtmltoText(item.getTitle())+" score:"+item.getScore()+" price:"+item.getPrice()+" volume:"+item.getVolume()+" nick:"+item.getNick()+"\n pageUrl:"+item.getDetailUrl()+"\n picUrl:"+item.getPicUrl()+"\n props_name:"+item.getPropsName()
							+"\n promoted_service:"+item.getPromotedService()+"\n cid:"+item.getCid());
					cids.add(item.getCid());
					if(item.getItemImgs()!=null){
					for(ItemImg img : item.getItemImgs()){
						System.out.print("images:"+img.getUrl());
					}
					}
					if(item.getPropImgs()!=null){
						for(PropImg propImg : item.getPropImgs()){
							System.out.print("proimages:"+propImg.getUrl());
						}
						}
					System.out.println();
				}
					
			}
			long endTime=System.currentTimeMillis();
			System.out.println("userTime: "+(endTime-startTime));
			System.out.println("testItemsGet "+res.getBody());
			System.out.println("cids size: "+cids.size());
			
			/**
			              
			 */
			//System.out.println(NetTool.getTextContent("http://item.taobao.com/item.htm?id=10527746029", "gbk"));
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
//		ApiTest.testUserGet();
//		ApiTest.testCatelogGet();
//		ApiTest.testItemCatsGet();
//		ApiTest.testProductGet();
//		testItemList();
		try {
			testItemsGet();
			//testItemsSearch();
			//testShopGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
