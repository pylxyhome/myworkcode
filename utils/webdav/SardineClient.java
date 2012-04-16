package cn.gzjp.idigg.server.webdav;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineUtil;

public class SardineClient {
	private WebDavConfig webDavConfig;
	private Sardine sardine;
	
	public void setWebDavConfig(WebDavConfig webDavConfig) {
		this.webDavConfig = webDavConfig;
	}
	
	public WebDavConfig getWebDavConfig() {
		return webDavConfig;
	}

	private Sardine getCurrentSardine() throws IOException {
		if (sardine == null)
			sardine = SardineFactory.begin(webDavConfig.getWebdavUser(), webDavConfig.getWebdavPassword());
		return sardine;
	}
	
	public SardineClient() {
	}
	
	public void batchCreateDirectory(String url) throws IOException {
		batchCreateDirectory(url, false);
	}
	public void batchCreateDirectory(String url, boolean ignoreExists) throws IOException {
		String[] dirs = url.split("/");
		String dir = "";
		for (String dir2 : dirs) {
			if (dir2.isEmpty()) continue;
			dir += dir2 + "/";
			createDirectory(dir, ignoreExists);
		}
	}
	
	public void createDirectory(String url) throws IOException {
		createDirectory(url, false);
	}
	public void createDirectory(String url, boolean ignoreExists) throws IOException {
		if (ignoreExists && directoryExists(url)) return;
		
		url = getFullUrl(url);
		getCurrentSardine().createDirectory(url);
	}
	
	// Directory Exists
	public boolean directoryExists(String url) throws IOException{
		url = getFullUrl(url);
		try {
			getCurrentSardine().list(url);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	// File Exists
	public boolean fileExists(String url) throws IOException{
		url = getFullUrl(url);
		return getCurrentSardine().exists(url);
	}
	// Put File
	public void putFile(String url, InputStream data) throws IOException {
		url = getFullUrl(url);
		getCurrentSardine().put(url, data);
	}
	public void putFile(String url, InputStream data, String contentType) throws IOException {
		url = getFullUrl(url);
		getCurrentSardine().put(url, data, contentType);
	}
	public void putFile(String url, byte[] data) throws IOException {
		url = getFullUrl(url);
		getCurrentSardine().put(url, data);
	}
	public void putFile(String url, byte[] data, String contentType) throws IOException {
		url = getFullUrl(url);
		getCurrentSardine().put(url, data, contentType);
	}
	
	public void move(String sourceUrl, String targetUrl) throws IOException {
		getCurrentSardine().move(getFullUrl(sourceUrl), getFullUrl(targetUrl));
	}
	
	public InputStream getStream(String url) throws IOException{
		url = getFullUrl(url);
		return getCurrentSardine().get(url);
	}
	
	public void deleteResource(String url) throws IOException {
		url = getFullUrl(url);
		getCurrentSardine().delete(url);
	}
	
	public DavResource getResource(String url) throws IOException{
		url = getFullUrl(url);
		List<DavResource> resources = getCurrentSardine().list(url);
		if (resources.size() > 0)
			return resources.get(0);
		else
			return null;
	}
	public List<DavResource> getChildResources(String url) throws IOException{
		url = getFullUrl(url);
		List<DavResource> resources = getCurrentSardine().list(url);
		List<DavResource> res = new ArrayList<DavResource>();
		for (DavResource davResource : resources) {
			if(davResource.isDirectory()) continue;
			res.add(davResource);
		}
		return res;
	}
	public List<DavResource> getChildFiles(String url) throws IOException{
		url = getFullUrl(url);
		List<DavResource> resources = getCurrentSardine().list(url);
		List<DavResource> res = new ArrayList<DavResource>();
		for (DavResource davResource : resources) {
			if(davResource.isDirectory() || davResource.isDirectory()) continue;
			res.add(davResource);
		}
		return res;
	}
	public void setCustomProperties(String url, Map<String, String> addProps, List<String> removeProps) throws IOException {
		url = getFullUrl(url);
		getCurrentSardine().patch(url, SardineUtil.toQName(addProps), SardineUtil.toQName(removeProps));
	}
	
	public String getFullUrl(String relativeUrl){
		String url = webDavConfig.getWebdavUrl() + "/" + relativeUrl;
		String str = url.replaceAll("(.*://).*", "$1");
		url = str + adjustUrl(url.replaceAll(".*://(.*)$", "$1"));
		return url;
	}
	public String adjustUrl(String url){
		return url.replaceAll("//", "/");
	}
	
//	public static void main(String[] args) {
//		WebDavConfig wdc = new WebDavConfig();
//		wdc.setWebdavUrl("http://172.31.36.158:5050/jackrabbit/repository/default/");
//		wdc.setWebdavUser("admin");
//		wdc.setWebdavPassword("admin");
//		SardineClient sc = new SardineClient();
//		
//		sc.setWebDavConfig(wdc);
////		StringBuffer sb = new StringBuffer();
////		sb.append("{" +
////				"headingText:" +
////					"\"流量经营现状 - 内部员工\"," +
////				"contentText:" +
////					"\"" +
////					"说明：<br/>" +
////					"图1、2，终端类型比较：从用户量与户均流量两个维度，对内部员工使用的各种终端类型作比较，使您直观地了解其用户规模与质量，以及对户均流量的影响程度；默认展现当前月全省的内部员工数据；您亦可通过工具面板的日期窗格，选择任一个【月份】，切换全部数据<br/>" +
////					"图3、4、5，地市、计划、操作系统：从多个维度为您展现内部员工，流量占比与用户占比的关联关系；同时，数据将展开至各个地市、各种资费套餐、各类操作系统，便于您更全面地了解内部员工的流量经营现状，并对诸如“劳动竞赛、业务体验、终端体验”等专题营销的实施，包括具体形式及持续周期，作出有效的决策；默认展现当前月的数据；您亦可通过图1，选择任一个【终端类型】细分用户群，切换图3、4、5的数据；您还可以通过图3，选择任一个【地市】切换图4、5的数据<br/>" +
//////					"图3、4、5，地市、计划、操作系统：从多个维度为您展现监控用户，流量占比与用户占比的关联关系；同时，数据将展开至各个地市、各种资费套餐、各类操作系统，便于您更全面地了解监控用户的流量异动情况，有效监测及控制其流量消耗，及时制定应对措施；默认展现当前月的数据；您亦可通过图1，选择任一个【监控类型】细分用户群，切换图3、4、5的数据；您还可以通过图3，选择任一个【地市】切换图4、5的数据" +
////				"\"}");
//		try {
////			sc.batchCreateDirectory("hunan/3g/flowmanagedetail/innerStaff/", true);
////			sc.putFile("hunan/3g/flowmanagedetail/innerStaff/boreshow.json", sb.toString().getBytes("utf-8"));
//			sc.deleteResource("hunan");
//			
////			System.out.println(sc.getResource("hunan/3g/targetdetail/usernum/boreshow.json"));
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	public static void main(String[] args) {
		WebDavConfig wdc = new WebDavConfig();
		wdc.setWebdavUrl("http://172.31.36.158:5050/jackrabbit/repository/default/");
		wdc.setWebdavUser("admin");
		wdc.setWebdavPassword("admin");
		SardineClient sc = new SardineClient();
		
		sc.setWebDavConfig(wdc);

		 try {
			 String str=FileUtils.readFileToString(new File("D:/change.json"),"utf-8");
			sc.putFile("hunan/3g/flowmanagedetail/newinnet/201201_plan.json", str.getBytes("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
