package cn.gzjp.push2.sgip.message;
/**
*@author gzwenny
*Created:2010-07-05 12:39:35
*/
public class UnbindResponseMessage{

	private int messageLength;
	private int commandId;
	private int[] flowId;
	
	public UnbindResponseMessage(){}
	
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
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append('[');
		sb.append("messageLength="+messageLength+",");
		sb.append("commandId="+commandId+",");
		sb.append("flowId="+flowId+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}