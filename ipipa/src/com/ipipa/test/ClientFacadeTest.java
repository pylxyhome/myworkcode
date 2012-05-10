package com.ipipa.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.ipipa.dao.ItemDao;
import com.ipipa.taobao.TaobaoClientFacade;
import com.ipipa.utils.HtmlUtil;
import com.ipipa.utils.JsonUtil;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.ItemCat;
import com.taobao.api.domain.Shop;
import com.taobao.api.domain.TaobaokeItem;
import com.taobao.api.domain.TaobaokeShop;
/**
 * 测试
 * @description
 * @author panyl
 * @date 2012-5-10
 */
public class ClientFacadeTest {

	
	@Test
	public void testGetTaobaokeItems() {
		List<TaobaokeItem> items=TaobaoClientFacade.getTaobaokeItems(100,40,"饰品",null,"pylxyhome","commissionNum_desc");
		for(TaobaokeItem item : items){
			System.out.println(JsonUtil.objToJson(item));
		}
		 System.out.println("getTaobaokeItems:"+TaobaoClientFacade.getTaobaokeItems(100,40,"饰品",null,"pylxyhome","price_desc").size());
	}

	@Test
	public void testGetTaobaokeShops() {
		List<TaobaokeShop> list = TaobaoClientFacade.getTaobaokeShops(10l, 40l, "饰品", null,
				"pylxyhome", false);
		System.out.println("list-size:" + list.size());
		for (TaobaokeShop shop : list) {
//			System.out.println("shop-title:" + shop.getShopTitle());
//			System.out.println("clickUrl-url:" + shop.getClickUrl());
//			System.out.println("UserId:" + shop.getUserId());
//			System.out.println("commissionRate:" + shop.get);
			System.out.println(JsonUtil.objToJson(shop));
			
		}
	}

	@Test
	public void testShop() {
		Shop shop = TaobaoClientFacade.getShopByNick("changchangtt");
		System.out.println(shop.getTitle());
		System.out.println(shop.getCid());
		System.out.println(shop.getSid());
		List<ItemCat> itemCats = TaobaoClientFacade.getItemcats(new String[] { "50013878",
				"50013875", "50013865","50013864"});
		for (ItemCat itemCat : itemCats) {
			System.out.println("itemCat-name:" + itemCat.getName());
			System.out.println(JsonUtil.objToJson(itemCat));
		}
	}
	@Test
	public void testItemsMax(){
		List<Item> items = TaobaoClientFacade.getItems(51, 200, "饰品", "volume:desc", null, null);
		System.out.println(items.size());
	}
	@Test
	public void testItems() {
		long pageNo=1l;
		long pageSize=200l;
		while(true){
			if(pageNo*pageSize>10240l)break;
			List<Item> items = TaobaoClientFacade.getItems(pageNo, pageSize, "饰品", "volume:desc", null, null);
			if(items == null||items.size()==0)break;
			Set<String> nicks = new HashSet<String>();
			List<com.ipipa.bean.Item> myItems=new ArrayList<com.ipipa.bean.Item>();
			for (Item item : items) {
				item.setTitle(HtmlUtil.HtmltoText(item.getTitle()));
				com.ipipa.bean.Item myItem = new com.ipipa.bean.Item();
				try {
					BeanUtils.copyProperties(myItem, item);
					myItems.add(myItem);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				//System.out.println(JsonUtil.objToJson(myItem));
			}
			pageNo++;
			ItemDao.get().insert(myItems, "饰品");
			System.out.println("pageNo:" +pageNo);
		}
	}
}
