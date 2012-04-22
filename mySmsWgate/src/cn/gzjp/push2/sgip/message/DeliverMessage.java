package cn.gzjp.push2.sgip.message;
/**
*@author gzwenny
*Created:2010-07-05 12:39:35
*/
public class DeliverMessage{

	private int messageLength;
	private int commandId;
	private int[] flowId;
	private String usernumber;
	private String spNumber;
	private byte tppid;
	private byte tpudhi;
	private byte messageCoding;
	private String messageContent;
	private String reserve;
	
	public DeliverMessage(){}
	
	public int getMessageLength(){
		return messageLength;
	}
		
	public void setMessageLength(int messageLength){
		this.messageLength=messageLength;
	}
	
	public int getCommandId(){
		return commandId;
	}
		
	public void setCommandId(int commandId){
		this.commandId=commandId;
	}
	
	public int[] getFlowId(){
		return flowId;
	}
		
	public void setFlowId(int[] flowId){
		this.flowId=flowId;
	}
	
	public String getUsernumber(){
		return usernumber;
	}
		
	public void setUsernumber(String usernumber){
		this.usernumber=usernumber;
	}
	
	public String getSpNumber(){
		return spNumber;
	}
		
	public void setSpNumber(String spNumber){
		this.spNumber=spNumber;
	}
	
	public byte getTppid(){
		return tppid;
	}
		
	public void setTppid(byte tppid){
		this.tppid=tppid;
	}
	
	public byte getTpudhi(){
		return tpudhi;
	}
		
	public void setTpudhi(byte tpudhi){
		this.tpudhi=tpudhi;
	}
	
	public byte getMessageCoding(){
		return messageCoding;
	}
		
	public void setMessageCoding(byte messageCoding){
		this.messageCoding=messageCoding;
	}
	
	public String getMessageContent(){
		return messageContent;
	}
		
	public void setMessageContent(String messageContent){
		this.messageContent=messageContent;
	}
	
	public String getReserve(){
		return reserve;
	}
		
	public void setReserve(String reserve){
		this.reserve=reserve;
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append('[');
		sb.append("messageLength="+messageLength+",");
		sb.append("commandId="+commandId+",");
		sb.append("flowId="+flowId+",");
		sb.append("usernumber="+usernumber+",");
		sb.append("spNumber="+spNumber+",");
		sb.append("tppid="+tppid+",");
		sb.append("tpudhi="+tpudhi+",");
		sb.append("messageCoding="+messageCoding+",");
		sb.append("messageContent="+messageContent+",");
		sb.append("reserve="+reserve+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}