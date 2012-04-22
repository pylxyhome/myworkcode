package cn.gzjp.codec;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.xwork.StringUtils;

import cn.gzjp.codec.type.MessageType;
import cn.gzjp.codec.type.MessagesType;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 * 协议约定：
 * 1.协议的第一个字段为4个字节的整数，表示协议消息长度
 * 2.协议的第二个字段为4个字节的整数，表示协议消息类型
 */
public class MessageCodec {
	
	private MessagesType messages;
	
	private Map<String,MessageType> names=new HashMap<String, MessageType>();
	private Map<Integer,MessageType> ids=new HashMap<Integer,MessageType>();
	
	public MessageCodec(String url){
		this(MessageCodec.class.getResourceAsStream(url));
	}
	public MessageCodec(InputStream in){
		try{
			JAXBContext jc = JAXBContext.newInstance(MessagesType.class);
			Unmarshaller um = jc.createUnmarshaller();
			messages=(MessagesType)um.unmarshal(in);
			for(MessageType m:messages.getMessage()){
				String classname=messages.getPackageName()+"."+m.getClassName();
	    		names.put(classname, m);
	    		int id=m.getId();
	    		ids.put(id,m);
	    	}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	public ByteBuffer encode(Object o) throws EncodeException {
		ByteBuffer buffer=ByteBuffer.allocate(1024);
		String className=o.getClass().getName();
		MessageType message=getMessageByName(className);
		if(message==null)
			throw new EncodeException("unrecognizable class ["+className+"]");
		List<Object> types=message.getTypes();
		try{		
			
			for(Object type:types){
				Type t=(Type)type;
				String name=StringUtils.capitalize(t.getName());
				Method m=o.getClass().getMethod("get"+name);
				Object value=m.invoke(o);
				Encoder encoder=(Encoder)type;
				ByteBuffer data=encoder.encode(value);
				data.flip();
				buffer.put(data);
			}
			//TODO 这里假设了数据流头4个字节是消息长度，可能不适合所有协议
			buffer.putInt(0,buffer.position());
			buffer.flip();
			return buffer;
		}catch(Exception ex){
			throw new EncodeException("error in encode "+className,ex);
		}
	}
	
	public Object decode(ByteBuffer buf) throws DecodeException {
		buf.getInt();
		int commandId=buf.getInt();
		buf.rewind();
		MessageType message=getMessageById(commandId);
		if(message==null)throw new DecodeException("unrecognizable command id["+commandId+"]");	
		String className=messages.getPackageName()+"."+message.getClassName();
		try{			
			Object bean=Class.forName(className).newInstance();
			List<Object> types=message.getTypes();
			for(Object type:types){
				Decoder decoder=(Decoder)type;
				Object value=decoder.decode(buf);
				String name=StringUtils.capitalize(((Type)type).getName());
				Method m=bean.getClass().getMethod("set"+name,getType(value));
				m.invoke(bean, value);
			}
			return bean;
		}catch(Exception ex){
			throw new DecodeException("error in decode "+className,ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Class getType(Object value) {
		if(value instanceof Byte)return byte.class;
		if(value instanceof Byte[])return byte[].class;
		if(value instanceof Integer)return int.class;
		if(value instanceof Integer[])return int[].class;
		return value.getClass();
	}
	
	private MessageType getMessageByName(String className) {
		return names.get(className);
	}

	private MessageType getMessageById(int commandId) {
		return ids.get(commandId);
	}
}
