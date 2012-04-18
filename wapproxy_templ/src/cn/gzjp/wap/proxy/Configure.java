package cn.gzjp.wap.proxy;

import java.io.InputStream;
import java.util.Properties;


public class Configure {

	private int port;
	private boolean isDecorate;
	private String decorateUrl;
	private String telnum2SinatwiterGsidFileName;
	private String sinaTwiterUrl;
	private String loginoutUrl;
	private String flushNgFileCnt;
	private String rssPreLink;
	private String ipparserfile;
	
	private static Configure instance=new Configure();
	
	public Configure(){
		
		InputStream in=getClass().getResourceAsStream("/proxy.conf");
		if(in==null)throw new RuntimeException("not found proxy.conf in the class path.");
		try{
			Properties props=new Properties();
			props.load(in);
			
			port=Integer.parseInt(props.getProperty("server_port"));
			isDecorate=Boolean.valueOf(props.getProperty("is_decorate"));
			decorateUrl=props.getProperty("decorate_url");
			telnum2SinatwiterGsidFileName = props.getProperty("telnum2SinatwiterGsidFileName");
			sinaTwiterUrl = props.getProperty("sinaTwiterUrl");
			loginoutUrl = props.getProperty("loginoutUrl");
			flushNgFileCnt = props.getProperty("flushNgFileCnt");
			rssPreLink = props.getProperty("rssPreLink");
			ipparserfile = props.getProperty("ipparserfile");
		}catch(Exception ex){
			throw new RuntimeException("load proxy.conf error.",ex);
		}
	}
	
	public static Configure getConfig(){
		return instance;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isDecorate() {
		return isDecorate;
	}

	public void setDecorate(boolean isDecorate) {
		this.isDecorate = isDecorate;
	}

	public String getDecorateUrl() {
		return decorateUrl;
	}

	public void setDecorateUrl(String decorateUrl) {
		this.decorateUrl = decorateUrl;
	}
	
	public String getTelnum2SinatwiterGsidFileName() {
		return telnum2SinatwiterGsidFileName;
	}

	public String getSinaTwiterUrl() {
		return sinaTwiterUrl;
	}

	public void setSinaTwiterUrl(String sinaTwiterUrl) {
		this.sinaTwiterUrl = sinaTwiterUrl;
	}

	public void setTelnum2SinatwiterGsidFileName(
			String telnum2SinatwiterGsidFileName) {
		this.telnum2SinatwiterGsidFileName = telnum2SinatwiterGsidFileName;
	}

	public String getLoginoutUrl() {
		return loginoutUrl;
	}

	public void setLoginoutUrl(String loginoutUrl) {
		this.loginoutUrl = loginoutUrl;
	}

	public int getFlushNgFileCnt() {
		int fngc = 1;
		try{
			fngc = Integer.parseInt(flushNgFileCnt);
		}catch(Exception excep){
			excep.printStackTrace();
		}
		return fngc;
	}

	public void setFlushNgFileCnt(String flushNgFileCnt) {
		this.flushNgFileCnt = flushNgFileCnt;
	}

	public String getRssPreLink() {
		return rssPreLink;
	}

	public void setRssPreLink(String rssPreLink) {
		this.rssPreLink = rssPreLink;
	}

	public String getIpparserfile() {
		return ipparserfile;
	}

	public void setIpparserfile(String ipparserfile) {
		this.ipparserfile = ipparserfile;
	}

	public static void main(String[] args) {
		try{
			String url=Configure.getConfig().getDecorateUrl();
			System.out.println(url);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
}
