package cn.gzjp.wap.proxy.handler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.gzjp.wap.proxy.handler.impl.FormTagHandler;
import cn.gzjp.wap.proxy.handler.impl.ImgTagHandler;
import cn.gzjp.wap.proxy.handler.impl.LinkTagHandler;
import cn.gzjp.wap.proxy.handler.impl.RssTagHandler;

public class ContentTest {

	public static void main(String[] args)throws Exception {
		String urlpath="http://127.0.0.1:8089/idigg/ws/rule/getRuleList.do";
		String updateRuleInvalid="http://127.0.0.1:8089/idigg/ws/rule/updateRuleInvalid.do";
		String downFile="http://127.0.0.1:8089/idigg/ws/rule/downloadPlanFile.do";
		//String urlpath="http://www.sina.com.cn";
//		String contents=NetTool.getTextContent(urlpath, "utf-8");
//		String host="126.com";
//		String contentType="text/html";
//		ContentHandlerFactory.get(host).registerContentHandler(LinkTagHandler.class);
//		ContentHandlerFactory.get(host).registerContentHandler(RssTagHandler.class);
//		ContentHandlerFactory.get(host).registerContentHandler(ImgTagHandler.class);
//		ContentHandlerFactory.get(host).registerContentHandler(FormTagHandler.class);
//		String content=ContentHandlerUtil.handler(host,contents,contentType);
//		System.out.println(content);
//		String encoding="utf-8";
//		Map<String, String> parmas=new HashMap<String,String>();
//		String username="";
//		String password="";
//		parmas.put("username", username);
//		parmas.put("password", password);
//		InputStream inStream = NetTool.getContent(urlpath,parmas,encoding);
//		byte[] data = NetTool.readStream(inStream);
		//System.out.println(NetTool.getTextContent(urlpath, "utf-8"));
		Map<String,String> params=new HashMap<String,String>();
		params.put("filename", "testtest");
		NetTool.savePlanFile(NetTool.getInStream(downFile, "utf-8"));
	}
}
