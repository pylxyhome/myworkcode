package cn.gzjp.codec.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messageType", propOrder = {
    "types"
})
public class MessageType {

    @XmlElements({
        @XmlElement(name = "vstring", type = VstringType.class),
        @XmlElement(name = "array", type = ArrayType.class),
        @XmlElement(name = "byte", type = ByteType.class),
        @XmlElement(name = "vbyte", type = VbyteType.class),
        @XmlElement(name = "string", type = StringType.class),
        @XmlElement(name = "varray", type = VarrayType.class),
        @XmlElement(name = "int", type = IntType.class)
    })
    protected List<Object> types;
    @XmlAttribute(name = "class-name", required = true)
    protected String className;
    @XmlAttribute(required = true)
    protected int id;

    public List<Object> getTypes() {
        if (types == null) {
            types = new ArrayList<Object>();
        }
        return this.types;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String value) {
        this.className = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int value) {
        this.id = value;
    }

}
