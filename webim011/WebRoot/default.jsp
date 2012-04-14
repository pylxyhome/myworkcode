<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<title>网站业务系统_管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<frameset  framespacing="0" border="0" rows="60,*,15" frameborder="0">
  <frame  name="banner" scrolling="no" noresize target="contents" src="/controller/top.action" marginwidth="0" marginheight="0">
  <frameset cols="200,*">
    <frame src="/controller/left.action" name="left" scrolling="yes">
    <frame name="right" id="main" scrolling="yes" marginwidth="0" marginheight="0" src="/controller/right.action">
  </frameset>
 <frame name="menuframe" src="/controller/end.action" scrolling="no" marginwidth="0" marginheight="0">
</frameset><noframes></noframes>
</html>