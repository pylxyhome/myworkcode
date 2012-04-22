package cn.gzjp.codec.type;

import java.nio.ByteBuffer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

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
@XmlType(name = "stringType")
public class StringType implements Type,Encoder,Decoder{

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected String value;
    @XmlAttribute(required = true)
    protected int length;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int value) {
        this.length = value;
    }

	@Override
	public ByteBuffer encode(Object o) throws EncodeException{
		String value=(String)o;
		//尝试使用配置值
		if(value==null)value=this.value;
		if(value==null)throw new EncodeException("the ["+name+"] value is empty.");
		if(value.getBytes().length>length)
			throw new EncodeException("the ["+name+"] is too length. the config length["+length+"]");
		byte[]result=new byte[length];
		byte[]data=value.getBytes();
		System.arraycopy(data,0,result,0,data.length);
		return ByteBuffer.allocate(length).put(result);
	}

	@Override
	public String decode(ByteBuffer buf) {
		byte[]data=new byte[length];
		buf.get(data);
		return ByteUtils.toString(data);
	}
	
	@Override
	public String getTypeName(){
		return "String";
	}

}
