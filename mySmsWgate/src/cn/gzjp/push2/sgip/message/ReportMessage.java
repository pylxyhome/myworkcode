package cn.gzjp.push2.sgip.message;
/**
*@author gzwenny
*Created:2010-07-05 12:39:35
*/
public class ReportMessage{

	private int messageLength;
	private int commandId;
	private int[] flowId;
	private int[] submitFlowId;
	private byte reportType;
	private String usernumber;
	private byte state;
	private byte errorCode;
	private String reserve;
	
	public ReportMessage(){}
	
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
	
	public int[] getSubmitFlowId(){
		return submitFlowId;
	}
		
	public void setSubmitFlowId(int[] submitFlowId){
		this.submitFlowId=submitFlowId;
	}
	
	public byte getReportType(){
		return reportType;
	}
		
	public void setReportType(byte reportType){
		this.reportType=reportType;
	}
	
	public String getUsernumber(){
		return usernumber;
	}
		
	public void setUsernumber(String usernumber){
		this.usernumber=usernumber;
	}
	
	public byte getState(){
		return state;
	}
		
	public void setState(byte state){
		this.state=state;
	}
	
	public byte getErrorCode(){
		return errorCode;
	}
		
	public void setErrorCode(byte errorCode){
		this.errorCode=errorCode;
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
		sb.append("submitFlowId="+submitFlowId+",");
		sb.append("reportType="+reportType+",");
		sb.append("usernumber="+usernumber+",");
		sb.append("state="+state+",");
		sb.append("errorCode="+errorCode+",");
		sb.append("reserve="+reserve+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}