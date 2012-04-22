package cn.gzjp.codec;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
public class EncodeException extends Exception {

	private static final long serialVersionUID = 1L;

public EncodeException(){}
	
	public EncodeException(String message){
		super(message);
	}
	
	public EncodeException(String message,Throwable ex){
		super(message,ex);
	}
}
