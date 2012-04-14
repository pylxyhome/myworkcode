<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
 <base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>聊天记录</title>
<link rel="stylesheet" href="${basePath}css/chat.css" type="text/css" />
<link rel="stylesheet" href="${basePath}css/region.css" type="text/css">
<script type="text/javascript" src="${basePath}talk/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript">
<!--
	//到指定的分页页面
	function topage(page){
		var form = document.forms[0];
		form.page.value=page;
		form.submit();
	}
//-->
</script>
</head>
<body style="overflow:scroll;overflow-x:hidden"> 
<form action="${basePath}im/listRightMsg.action" method="post" name="myform">
<input type="hidden" name="loginUserId" id="loginUserId" value="${loginUserId}"/>
<input type="hidden" name="receiverId" id="receiverId" value="${receiverId}"/>
<input type="hidden" name="query" id="query" value="${query }"/>
<input type="hidden" name="keywords" id="keywords" value="${keywords}"/>
<input type="hidden" name="page" id="page" value="${page}"/>
<table width="100%">
 <tr ><td colspan="10"  bgcolor="6f8ac4" align="left">
  	  <%@ include file="fenye.jsp" %>
  </td></tr>
</table>
<table width="95%" border="0" cellspacing="1" cellpadding="2" align="center">
<c:forEach items="${pageView.records}" var="msg" varStatus="vs">
<tr>
<div class="${msg.sender.userId eq loginUserId?'recordcontentTitle1':'recordcontentTitle2'}">
	<span id="who" class="who">${msg.sender.userInfo.realName}</span>
	<span id="senddate" class="senddate" style="font-size: 12px;">
	<fmt:formatDate value="${msg.senddate }" pattern="yyyy-MM-dd HH:mm:ss"/> 
	</span>
	</div>
	<div id="content" style="padding: 5px;" class="content">${msg.message}</div>
</div>
</tr>
</c:forEach>
</table>
<table width="100%">
 <tr ><td colspan="10"  bgcolor="6f8ac4" align="left">
  	  <%@ include file="fenye.jsp" %>
  </td></tr>
</table>
</form>
</body>
</html>
