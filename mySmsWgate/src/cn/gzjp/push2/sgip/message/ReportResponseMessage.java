package cn.gzjp.push2.sgip.message;
/**
*@author gzwenny
*Created:2010-07-05 12:39:35
*/
public class ReportResponseMessage{

	private int messageLength;
	private int commandId;
	private int[] flowId;
	private byte result;
	private String reserve;
	
	public ReportResponseMessage(){}
	
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
	
	public byte getResult(){
		return result;
	}
		
	public void setResult(byte result){
		this.result=result;
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
		sb.append("result="+result+",");
		sb.append("reserve="+reserve+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}