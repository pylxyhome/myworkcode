package cn.gzjp.wap.proxy;

public class RequiredIOCopyException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequiredIOCopyException(){}
	
	public RequiredIOCopyException(String message){
		super(message);
	}
	
	public RequiredIOCopyException(String message,Throwable ex){
		super(message,ex);
	}

}
