package cn.gzjp.codec.type;

import java.nio.ByteBuffer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import cn.gzjp.codec.Decoder;
import cn.gzjp.codec.Encoder;
import cn.gzjp.codec.Type;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "byteType")
public class ByteType implements Encoder,Decoder,Type{

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected Byte value;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

	@Override
	public ByteBuffer encode(Object o) {
		Byte data=(Byte)o;
		if(data==0&&value!=null&&value>0)data=value;
		return ByteBuffer.allocate(1).put(data);
	}

	@Override
	public Byte decode(ByteBuffer buf) {
		return buf.get();
	}
	
	@Override
	public String getTypeName(){
		return "byte";
	}

}
