
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

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "intType")
public class IntType implements Type,Encoder,Decoder{

    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute
    protected Integer value;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

	@Override
	public ByteBuffer encode(Object o) throws EncodeException{
		Integer data=(Integer)o;
		//由于使用原生类型，所以值不会为null，如果设置值为0配置为不为0，则使用配置值
		if(data==0&&value!=null&&value!=0)data=value;
		ByteBuffer buf=ByteBuffer.allocate(4);
		buf.putInt(data);
		return buf;
	}

	@Override
	public Object decode(ByteBuffer buf) {
		return buf.getInt();
	}
	
	@Override
	public String getTypeName(){
		return "int";
	}

}
