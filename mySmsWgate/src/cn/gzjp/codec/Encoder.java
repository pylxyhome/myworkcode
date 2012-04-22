package cn.gzjp.codec;

import java.nio.ByteBuffer;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
public interface Encoder{
	/**
	 * 把基本配置类型或者消息bean编码成字节流
	 */
	public ByteBuffer encode(Object o) throws EncodeException;
}
