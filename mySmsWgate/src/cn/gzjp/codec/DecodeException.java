package cn.gzjp.codec;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
public class DecodeException extends Exception {

	private static final long serialVersionUID = 1L;

	public DecodeException(){}
	
	public DecodeException(String message){
		super(message);
	}
	
	public DecodeException(String message,Throwable ex){
		super(message,ex);
	}
}
