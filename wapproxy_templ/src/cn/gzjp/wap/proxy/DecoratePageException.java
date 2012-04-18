package cn.gzjp.wap.proxy;

public class DecoratePageException extends Exception {

	public DecoratePageException(String message){
		super(message);
	}
	
	public DecoratePageException(String message,Throwable ex){
		super(message,ex);
	}
}
