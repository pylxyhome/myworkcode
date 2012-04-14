<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" " http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns=" http://www.w3.org/1999/xhtml">
	<script type="text/javascript"> 
    document.domain="127.0.0.1";
    </script>
  <head>   
    <base href="<%=basePath%>" />
     <link rel="stylesheet" href="${basePath}css/chat.css" type="text/css" />
     <script type='text/javascript' src='${basePath}dwr/engine.js'></script>
    <script type="text/javascript" src="${basePath}script/constant.js"></script> 
    <script type="text/javascript" src="${basePath}talk/js/jquery-1.3.2.min.js"></script>
     <script type="text/javascript" src="${basePath}talk/chat.js"></script>
     <script type='text/javascript' src='${basePath}dwr/interface/ChatService.js'></script>
    <title>My JSP 'index.jsp' starting page</title>
  <style type="">
   body{
   	padding:0px;
   	border:0px;
}
  </style>
  </head>
  <%
	String loginUserName=request.getParameter("loginUserName");
	String targetName=request.getParameter("targetName");
  	if(loginUserName!=null && !loginUserName.equals("")){
  		loginUserName=new String(loginUserName.getBytes("ISO8859-1"),"utf-8");
  	}
	if(targetName!=null && !targetName.equals("")){
		targetName=new String(targetName.getBytes("ISO8859-1"),"utf-8");
  	}			
   %>
 <body style="height:300px;">

	 <div class="chatContentDiv" id="chatContentDiv">

	 </div>     
	 <div style="clear:both"></div>
	 <div class="chateditdiv">
	 	<img class="uploadpicpattern"  title="传送文件..." src="${basePath}talk/images/senddocument.gif" onclick="openWin('${basePath}im/uploadInput.action?loginUserId=${param.loginUserId}&receiverId=${param.targetId}&targetId=${param.targetId}','发送文件',600,220,1)"/>
	 	<img class="uploadpicpattern"  title="发送图片..." src="${basePath}talk/images/sendpic.gif" />
	 	<a class="dialogrecorddiv" href="javascript:void(0)" onclick="openWin('${basePath}im/getRecordMsg.action?loginUserId=${param.loginUserId}&receiverId=${param.targetId}&targetId=${param.targetId}','聊天记录',750,500,1)">
	 		<div class="talkabout"></div>
	 		<div class="dialogrecord">聊天记录</div>
	 	</a>
	 </div>
	 <div class="chatInput">
		 <textarea class="contentInput" id="contentInput" name="contentInput"></textarea>
	 </div>
	 <div class="bottommenudiv">
	 	 <span class="msg" id="not_blank" style="display: none;">发送内容不能为空</span>
	 	 <span class="msg" id="not_talk" style="display: none;">不能跟自己聊天</span>
	 	 <span class="msg" id="too_long" style="display: none;">发送内容过长</span>
		 <input type="button" id="enterBtn" value="发送" class="closethewindow"></input>
	 </div>
	 
	 <div id="contentTemplate" style="display: none;">
	 <div class="contentTitle">
	 	<span id="who" class="who"></span><span id="senddate" class="senddate"></span>
	 	</div>
	 	<div id="content" style="padding: 5px;" class="content"></div>
	 </div>
	 <div style="display: none;">
			loginId:
			<input type="text" id="loginUserId" name="loginUserId" value="${param.loginUserId}"/><br/>
			loginUserName:
			<input type="text" id="loginUserName" name="loginUserName" value="<%=loginUserName%>"/><br/>
			targetId:
			<input type="text" id="targetId" name="targetId" value="${param.targetId}" /><br/>
			targetName:
			<input type="text" id="targetName" name="targetName" value="<%=targetName%>"/><br/>
	</div>
  </body>
</html>
