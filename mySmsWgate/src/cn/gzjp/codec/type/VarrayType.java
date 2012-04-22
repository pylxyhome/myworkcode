package cn.gzjp.codec.type;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import cn.gzjp.codec.DecodeException;
import cn.gzjp.codec.Decoder;
import cn.gzjp.codec.EncodeException;
import cn.gzjp.codec.Encoder;
import cn.gzjp.codec.Type;
import cn.gzjp.codec.util.ByteUtils;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "varrayType")
public class VarrayType implements Type,Encoder,Decoder{

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String vtype;
    @XmlAttribute(name = "data-type", required = true)
    protected String dataType;
    @XmlAttribute(name = "data-length", required = true)
    protected int dataLength;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getVtype() {
        return vtype;
    }

    public void setVtype(String value) {
        this.vtype = value;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String value) {
        this.dataType = value;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int value) {
        this.dataLength = value;
    }

	@Override
	public String getTypeName() {
		return dataType+"[]";
	}

	@Override
	public ByteBuffer encode(Object o) throws EncodeException {
		int len=Array.getLength(o);
		ByteBuffer buffer=ByteBuffer.allocate(getTypeLength(vtype)+(len*getTypeLength(dataType)));
		if("byte".equals(vtype))buffer.put((byte)len);
		if("int".equals(vtype))buffer.putInt(len);
		Encoder encoder=getEncoder(dataType);
		for(int i=0;i<len;i++){
			Object element=Array.get(o,i);
			ByteBuffer buf=encoder.encode(element);
			buf.flip();
			buffer.put(buf);
		}
		return buffer;
	}	

	@Override
	public Object decode(ByteBuffer buf) throws DecodeException{
		int len=0;
		if("byte".equals(vtype))len=buf.get();
		if("int".equals(vtype))len=buf.getInt();
		if("byte".equals(dataType)){
			byte[]bytes=new byte[len];
			buf.get(bytes);
			return bytes;
		}
		if("int".equals(dataType)){
			int[]items=new int[len];
			for(int i=0;i<len;i++)
				items[i]=buf.getInt();
			return items;
		}
		if("String".equals(dataType)){
			String[]items=new String[len];
			if(dataLength==0)
				throw new DecodeException("no config varray data-length of "+name);
			for(int i=0;i<len;i++){
				byte[]data=new byte[dataLength];
				buf.get(data);
				items[i]=ByteUtils.toString(data);
			}
			return items;
		}
		throw new DecodeException("unrecognizable varray data-type "+dataType+" of "+name);
	}
	
	private Encoder getEncoder(String type) throws EncodeException{
		if("byte".equals(type))return new ByteType();
		if("int".equals(type))return new IntType();
		if("String".equals(type)){
			StringType stringType=new StringType();
			stringType.setLength(dataLength);
			return stringType;
		}
		throw new EncodeException("unrecognizable varray vtype ["+type+"] of "+name);
	}
	
	private int getTypeLength(String type) throws EncodeException {
		if("byte".equals(type))return 1;
		if("int".equals(type))return 4;
		if("String".equals(type)&&dataLength>0)return dataLength;
		throw new EncodeException("not support type or data-length is "+dataLength);
	}

}
