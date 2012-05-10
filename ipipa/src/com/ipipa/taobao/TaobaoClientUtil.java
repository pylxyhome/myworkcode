package com.ipipa.taobao;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

public class TaobaoClientUtil {
	//调用接口提交地址：http://gw.api.taobao.com/router/rest
	protected static String url = "http://gw.api.taobao.com/router/rest";// 正式环境需要设置为:
	protected static String appkey = "12598216";
	protected static String appSecret = "0b82a53a45c625f81aecfc4feaab8786";
	protected static String ipipa = "http://ipipa.cn:9099";
	
    private TaobaoClientUtil(){}
	private static TaobaoClient client = new DefaultTaobaoClient(url, appkey, appSecret);
	
	public static TaobaoClient getTaobaoClientInstance(){
		return client;
	}
}
