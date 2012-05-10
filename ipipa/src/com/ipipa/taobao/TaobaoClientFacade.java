package com.ipipa.taobao;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.ipipa.utils.HtmlUtil;
import com.ipipa.utils.JsonUtil;
import com.taobao.api.ApiException;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.ItemSearch;
import com.taobao.api.domain.Shop;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.domain.TaobaokeShop;
import com.taobao.api.request.ItemcatsGetRequest;
import com.taobao.api.request.ItemsGetRequest;
import com.taobao.api.request.ItemsSearchRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.TaobaokeItemsGetRequest;
import com.taobao.api.request.TaobaokeShopsGetRequest;
import com.taobao.api.response.ItemcatsGetResponse;
import com.taobao.api.response.ItemsGetResponse;
import com.taobao.api.response.ItemsSearchResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.TaobaokeItemsGetResponse;
import com.taobao.api.response.TaobaokeShopsGetResponse;

/**
 * 
 * @description
 * @author panyl
 * @date 2012-5-10
 */
public class TaobaoClientFacade {

	/**
	 * 获取ItemSearch
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param qString
	 *            查询关键字
	 * @param orderBy
	 *            ----volume:desc
	 * @param nicks
	 *            ---卖家昵称
	 * @param cid
	 * @param fields
	 * @return
	 */
	public static ItemSearch getItemSearch(long pageNo, long pageSize,
			String qString, String orderBy, String nicks, Long cid,
			String fields) {
		if (pageNo * pageSize > 10240l)
			return null;
		ItemsSearchRequest req = new ItemsSearchRequest();
		if (fields == null) {
			req.setFields("num_iid,title,nick,pic_url,cid,props,props_name,price,type,delist_time,post_fee,score,volume,detail_url,is_xinpin,item_imgs,prop_imgs,promoted_service");
		} else {
			req.setFields(fields);
		}
		if (qString != null)
			req.setQ(qString);
		if (orderBy != null) {
			req.setOrderBy(orderBy);
		}
		if (nicks != null) {
			req.setNicks(nicks);
		}
		if (cid != null)
			req.setCid(cid);
		req.setPageNo(pageNo);
		req.setPageSize(pageSize);
		ItemsSearchResponse res = new ItemsSearchResponse();
		try {
			res = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		ItemSearch itemSearch = res.getItemSearch();
		return itemSearch;
	}

	/**
	 * 获取ItemSearch
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param qString
	 *            查询关键字
	 * @param orderBy
	 * @return
	 */
	public static ItemSearch getItemSearch(long pageNo, long pageSize,
			String qString, String orderBy) {
		return getItemSearch(pageNo, pageSize, qString, orderBy, null, null,
				null);
	}

	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param qString
	 * @param orderBy
	 * @param nicks
	 * @param cid
	 * @return
	 */
	public static ItemSearch getItemSearch(long pageNo, long pageSize,
			String qString, String orderBy, String nicks, Long cid) {
		return getItemSearch(pageNo, pageSize, qString, orderBy, nicks, cid,
				null);
	}

	/**
	 * 获取Item列表
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param qString
	 * @param orderBy
	 * @param nicks
	 * @param cid
	 * @param fields
	 * @return
	 */
	public static List<Item> getItems(long pageNo, long pageSize,
			String qString, String orderBy, String nicks, Long cid,
			String fields) {
		if (pageNo * pageSize > 10240l)
			return null;
		ItemsGetRequest req = new ItemsGetRequest();
		if (fields == null) {
			req.setFields("num_iid,title,nick,pic_url,cid,props,props_name,price,type,delist_time,post_fee,score,volume,detail_url,is_xinpin,item_imgs,prop_imgs,promoted_service");
		} else {
			req.setFields(fields);
		}
		if (qString != null)
			req.setQ(qString);
		if (orderBy != null) {
			req.setOrderBy(orderBy);
		}
		if (nicks != null) {
			req.setNicks(nicks);
		}
		if (cid != null)
			req.setCid(cid);
		req.setPageNo(pageNo);
		req.setPageSize(pageSize);
		ItemsGetResponse res = new ItemsGetResponse();
		try {
			res = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return res.getItems();
	}

	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param qString
	 * @param orderBy
	 * @return
	 */
	public static List<Item> getItems(long pageNo, long pageSize,
			String qString, String orderBy) {
		return getItems(pageNo, pageSize, qString, orderBy, null, null, null);
	}

	/**
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param qString
	 * @param orderBy
	 * @param nicks
	 * @param cid
	 * @return
	 */
	public static List<Item> getItems(long pageNo, long pageSize,
			String qString, String orderBy, String nicks, Long cid) {
		return getItems(pageNo, pageSize, qString, orderBy, nicks, cid, null);
	}

	/**
	 * 根据用户昵称获取店铺
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param nick
	 * @return
	 */
	public static Shop getShopByNick(String nick) {
		ShopGetRequest req = new ShopGetRequest();
		req.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified");
		req.setNick(nick);
		try {
			ShopGetResponse response = TaobaoClientUtil
					.getTaobaoClientInstance().execute(req);
			return response.getShop();
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<ItemCat> getItemcats(String[] cids) {
		ItemcatsGetRequest req = new ItemcatsGetRequest();
		req.setFields("cid,parent_cid,name,is_parent");
		String cidstr = "";
		for (String cid : cids) {
			cidstr += cid + ",";
		}
		req.setCids(cidstr);
		ItemcatsGetResponse response = new ItemcatsGetResponse();
		try {
			response = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		System.out.println("response.getMsg():" + response.getBody());
		return response.getItemCats();
	}
	/**
	 * 淘宝客店铺搜索
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @param cid
	 * @param nick  打入分成的
	 * @param isOnlyMall
	 * @return
	 */
	// 页码.结果页1~99 每页条数.最大每页40
	public static List<TaobaokeShop> getTaobaokeShops(long pageNo,
			long pageSize, String keyword, Long cid, String nick,
			boolean isOnlyMall) {
		TaobaokeShopsGetRequest req = new TaobaokeShopsGetRequest();
		req.setFields("user_id ,seller_credit ,shop_type ,total_auction ,auction_count ,click_url,shop_title,commission_rate");
		req.setCid(cid);
		req.setOnlyMall(isOnlyMall);
		if (keyword != null)
			req.setKeyword(keyword);
		req.setPageNo(pageNo);
		req.setPageSize(pageSize);
		req.setNick(nick);
		TaobaokeShopsGetResponse response = new TaobaokeShopsGetResponse();
		try {
			response = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		System.out.println("total_results :" + response.getTotalResults());
		return response.getTaobaokeShops();
	}
	/**
	 * 获取淘宝客商品
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param pageNo
	 * @param pageSize
	 * @param keyword
	 * @param cid
	 * @param nick
	 * @param sort
	 *            默认排序:default price_desc(价格从高到低) price_asc(价格从低到高)
	 *            credit_desc(信用等级从高到低) commissionRate_desc(佣金比率从高到低)
	 *            commissionRate_asc(佣金比率从低到高) commissionNum_desc(成交量成高到低)
	 *            commissionNum_asc(成交量从低到高) commissionVolume_desc(总支出佣金从高到低)
	 *            commissionVolume_asc(总支出佣金从低到高) delistTime_desc(商品下架时间从高到低)
	 *            delistTime_asc(商品下架时间从低到高)
	 * @return
	 */
	public static List<TaobaokeItem> getTaobaokeItems(long pageNo, long pageSize,
			String keyword, Long cid, String nick, String sort) {
		TaobaokeItemsGetRequest req = new TaobaokeItemsGetRequest();
		req.setFields("num_iid,title,nick,pic_url,price,click_url,commission,commission_rate,commission_num,commission_volume,shop_click_url,seller_credit_score,item_location,volume");
		if(cid!=null)
			req.setCid(cid);
		if (keyword != null)
			req.setKeyword(keyword);
		req.setPageNo(pageNo);
		req.setPageSize(pageSize);
		req.setNick(nick);
		req.setSort(sort);
		TaobaokeItemsGetResponse response = new TaobaokeItemsGetResponse();
		try {
			response = TaobaoClientUtil.getTaobaoClientInstance().execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return response.getTaobaokeItems();
	}
}
