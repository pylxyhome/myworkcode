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
/**
 * @author gzwenny
 * created:2010-7-6
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "arrayType")
public class ArrayType implements Encoder,Decoder,Type{

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(required = true)
    protected String type;
    @XmlAttribute(name = "array-length")
    protected Integer arrayLength;
    @XmlAttribute(name = "data-length")
    protected Integer dataLength;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public Integer getArrayLength() {
        return arrayLength;
    }

    public void setArrayLength(Integer value) {
        this.arrayLength = value;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer value) {
        this.dataLength = value;
    }

	@Override
	public ByteBuffer encode(Object o) throws EncodeException {
		if(o==null)
			throw new EncodeException(getTypeName()+" is null of "+name);
		int length=Array.getLength(o);
		if(length!=arrayLength)
			throw new EncodeException("the array length["+length+"] no match config array-length["+arrayLength+"] of "+name);
		Encoder encoder=getEncoder(type);
		ByteBuffer buffer=ByteBuffer.allocate(arrayLength*getTypeLength(type));
		for(int i=0;i<length;i++){
			Object value=Array.get(o,i);
			ByteBuffer buf=encoder.encode(value);
			buf.flip();
			buffer.put(buf);
		}
		return buffer;
	}

	@Override
	public Object decode(ByteBuffer buf) throws DecodeException{
		Object array=Array.newInstance(getType(type),arrayLength);
		Decoder decoder=getDecoder(type);
		for(int i=0;i<arrayLength;i++){			
			Object value=decoder.decode(buf);
			Array.set(array, i, value);
		}
		return array;
	}
	
	@SuppressWarnings("unchecked")
	private Class getType(String type) throws DecodeException {
		if("byte".equals(type))return byte.class;
		if("int".equals(type))return int.class;
		if("String".equals(type))return String.class;
		throw new DecodeException("unrecognizable data type "+type);
	}
	
	private Decoder getDecoder(String type)throws DecodeException {
		if("byte".equals(type))return new ByteType();
		if("int".equals(type))return new IntType();
		if("String".equals(type)){
			StringType stringType=new StringType();
			stringType.setLength(dataLength);
			return stringType;
		}
		throw new DecodeException("unrecognizable data type "+type);
	}
	
	private Encoder getEncoder(String type) throws EncodeException {
		if("byte".equals(type))return new ByteType();
		if("int".equals(type))return new IntType();
		if("String".equals(type)){
			StringType stringType=new StringType();
			stringType.setLength(dataLength);
			return stringType;
		}
		throw new EncodeException("unrecognizable data type "+type);
	}
	
	private int getTypeLength(String type)throws EncodeException {
		if("byte".equals(type))return 1;
		if("int".equals(type))return 4;
		if("String".equals(type)){
			if(dataLength==null)throw new EncodeException("no set data-length of "+name);
			return dataLength;
		}
		throw new EncodeException("unrecognizable data type "+type);
	}
	
	@Override
	public String getTypeName(){
		return type+"[]";
	}

}
