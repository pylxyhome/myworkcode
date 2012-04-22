package cn.gzjp.push2.sgip.message;
/**
*@author gzwenny
*Created:2010-07-05 12:39:34
*/
public class BindMessage{

	private int messageLength;
	private int commandId;
	private int[] flowId;
	private byte loginType;
	private String loginName;
	private String loginPassword;
	private String reserve;
	
	public BindMessage(){}
	
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
	
	public byte getLoginType(){
		return loginType;
	}
		
	public void setLoginType(byte loginType){
		this.loginType=loginType;
	}
	
	public String getLoginName(){
		return loginName;
	}
		
	public void setLoginName(String loginName){
		this.loginName=loginName;
	}
	
	public String getLoginPassword(){
		return loginPassword;
	}
		
	public void setLoginPassword(String loginPassword){
		this.loginPassword=loginPassword;
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
		sb.append("loginType="+loginType+",");
		sb.append("loginName="+loginName+",");
		sb.append("loginPassword="+loginPassword+",");
		sb.append("reserve="+reserve+",");
		sb.deleteCharAt(sb.length()-1);
		sb.append(']');
		return sb.toString();
	}
}