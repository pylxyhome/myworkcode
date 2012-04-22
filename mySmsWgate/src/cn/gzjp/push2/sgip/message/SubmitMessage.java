package cn.gzjp.push2.sgip.message;
/**
*@author gzwenny
*Created:2010-07-05 12:39:35
*/
public class SubmitMessage{

	private int messageLength;
	private int commandId;
	private int[] flowId;
	private String spNumber;
	private String chargeNumber;
	private String[] usernumbers;
	private String corpId;
	private String serviceType;
	private byte feeType;
	private String feeValue;
	private String givenValue;
	private byte agentFlag;
	private byte morelatetoMTFlag;
	private byte priority;
	private String expireTime;
	private String scheduleTime;
	private byte reportFlag;
	private byte tppid;
	private byte tpudhi;
	private byte messageCoding;
	private byte messageType;
	private byte[] messageContent;
	private String reserve;
	private String mdn;
	public SubmitMessage(){}
	
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
	
	public String getSpNumber(){
		return spNumber;
	}
		
	public void setSpNumber(String spNumber){
		this.spNumber=spNumber;
	}
	
	public String getChargeNumber(){
		return chargeNumber;
	}
		
	public void setChargeNumber(String chargeNumber){
		this.chargeNumber=chargeNumber;
	}
	
	public String[] getUsernumbers(){
		return usernumbers;
	}
		
	public void setUsernumbers(String[] usernumbers){
		this.usernumbers=usernumbers;
	}
	
	public String getCorpId(){
		return corpId;
	}
		
	public void setCorpId(String corpId){
		this.corpId=corpId;
	}
	
	public String getServiceType(){
		return serviceType;
	}
		
	public void setServiceType(String serviceType){
		this.serviceType=serviceType;
	}
	
	public byte getFeeType(){
		return feeType;
	}
		
	public void setFeeType(byte feeType){
		this.feeType=feeType;
	}
	
	public String getFeeValue(){
		return feeValue;
	}
		
	public void setFeeValue(String feeValue){
		this.feeValue=feeValue;
	}
	
	public String getGivenValue(){
		return givenValue;
	}
		
	public void setGivenValue(String givenValue){
		this.givenValue=givenValue;
	}
	
	public byte getAgentFlag(){
		return agentFlag;
	}
		
	public void setAgentFlag(byte agentFlag){
		this.agentFlag=agentFlag;
	}
	
	public byte getMorelatetoMTFlag(){
		return morelatetoMTFlag;
	}
		
	public void setMorelatetoMTFlag(byte morelatetoMTFlag){
		this.morelatetoMTFlag=morelatetoMTFlag;
	}
	
	public byte getPriority(){
		return priority;
	}
		
	public void setPriority(byte priority){
		this.priority=priority;
	}
	
	public String getExpireTime(){
		return expireTime;
	}
		
	public void setExpireTime(String expireTime){
		this.expireTime=expireTime;
	}
	
	public String getScheduleTime(){
		return scheduleTime;
	}
		
	public void setScheduleTime(String scheduleTime){
		this.scheduleTime=scheduleTime;
	}
	
	public byte getReportFlag(){
		return reportFlag;
	}
		
	public void setReportFlag(byte reportFlag){
		this.reportFlag=reportFlag;
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
	
	public byte getMessageType(){
		return messageType;
	}
		
	public void setMessageType(byte messageType){
		this.messageType=messageType;
	}
	
	public byte[] getMessageContent(){
		return messageContent;
	}
		
	public void setMessageContent(byte[] messageContent){
		this.messageContent=messageContent;
	}
	
	public String getReserve(){
		return reserve;
	}
		
	public void setReserve(String reserve){
		this.reserve=reserve;
	}
	
	public String getMdn() {
		return mdn;
	}

	public void setMdn(String mdn) {
		this.mdn = mdn;
	}

	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append('[');
		sb.append("messageLength="+messageLength+",");
		sb.append("commandId="+commandId+",");
		sb.append("flowId="+flowId+",");
		sb.append("spNumber="+spNumber+",");
		sb.append("chargeNumber="+chargeNumber+",");
		sb.append("usernumbers="+usernumbers+",");
		sb.append("corpId="+corpId+",");
		sb.append("serviceType="+serviceType+",");
		sb.append("feeType="+feeType+",");
		sb.append("feeValue="+feeValue+",");
		sb.append("givenValue="+givenValue+",");
		sb.append("agentFlag="+agentFlag+",");
		sb.append("morelatetoMTFlag="+morelatetoMTFlag+",");
		sb.append("priority="+priority+",");
		sb.append("expireTime="+expireTime+",");
		sb.append("scheduleTime="+scheduleTime+",");
		sb.append("reportFlag="+reportFlag+",");
		sb.append("tppid="+tppid+",");
		sb.append("tpudhi="+tpudhi+",");
		sb.append("messageCoding="+messageCoding+",");
		sb.append("messageType="+messageType+",");
		sb.append("messageContent="+messageContent+",");
		sb.append("reserve="+reserve+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}