package cn.gzjp.codec.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * @author gzwenny
 * created:2010-7-6
 */
@XmlRootElement(name="messages")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messagesType", propOrder = {
    "message"
})
public class MessagesType {

    @XmlElement(required = true)
    protected List<MessageType> message;
    @XmlAttribute(name = "package-name", required = true)
    protected String packageName;

    public List<MessageType> getMessage() {
        if (message == null) {
            message = new ArrayList<MessageType>();
        }
        return this.message;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String value) {
        this.packageName = value;
    }
}
