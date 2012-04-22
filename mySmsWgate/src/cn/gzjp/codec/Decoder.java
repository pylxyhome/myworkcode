package cn.gzjp.codec;

import java.nio.ByteBuffer;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
public interface Decoder{
	/**
	 * 把字节流解码成基本配置类型或者消息bean
	 */
	public Object decode(ByteBuffer buf) throws DecodeException;
}
