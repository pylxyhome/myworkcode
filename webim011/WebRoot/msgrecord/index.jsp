<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<html>
<head>
<title>聊天记录</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="${basePath}"/>
</head>
<frameset  framespacing="0" border="0" rows="15,*,15" frameborder="0">
 <frame name="banner" src="${basePath}msgrecord/end.jsp" scrolling="no" marginwidth="0" marginheight="0">
  <frameset cols="150,*">
    <frame src="im/getLeftTree.action?loginUserId=${loginUserId}" name="left" scrolling="yes">
    <frameset rows="30,*,20">
    <frame name="search" src="${basePath}msgrecord/search.jsp" scrolling="no" marginwidth="0" marginheight="0" >
    <frame name="right" id="main" scrolling="yes" marginwidth="0" marginheight="0" src="${basePath}im/listRightMsg.action?loginUserId=${loginUserId}&receiverId=${receiverId }">
  
    </frameset>
  </frameset>
 <frame name="menuframe" src="${basePath}msgrecord/end.jsp" scrolling="no" marginwidth="0" marginheight="0">
</frameset><noframes></noframes>
</html>