<%@ page contentType="text/html;charset=UTF-8" %>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<HTML><HEAD><TITLE>查询聊天记录</TITLE>
<META http-equiv=Content-Type content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/newStyle.css" type="text/css" />
<STYLE type=text/css>
body{
	MARGIN-TOP: 0px; FONT-SIZE: 9pt; MARGIN-LEFT: 0px; COLOR: #ffffff;
	 LINE-HEIGHT: 150%; MARGIN-RIGHT: 0px; FONT-FAMILY: "宋体"; BACKGROUND-COLOR: #ffffff
}
</STYLE>
<script type="text/javascript" src="${basePath}talk/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript">
function searchMsg(){
	var loginUserId=parent.frames['right'].document.getElementById('loginUserId').value;
	var receiverId=parent.frames['right'].document.getElementById('receiverId').value;
	$("#loginUserId").val(loginUserId);
	$("#receiverId").val(receiverId);
	document.myform.submit();
}
</script>
<body>
<form action="${basePath}im/listRightMsg.action" method="post" name="myform" target="right">
<input type="hidden" name="loginUserId" id="loginUserId" value=""/>
<input type="hidden" name="receiverId" id="receiverId" value=""/>
<!-- 标记为查询 -->
<input type="hidden" name="query" id="query" value="true"/>
<table style="MARGIN: 0px" cellSpacing=0 bgcolor="#BBFFF1" cellPadding=0 width="100%"  valign="top">
  <tr vAlign=top>
      <td vAlign=center width="450" class="My" Align="left">&nbsp;
      关键字:<input onkeydown="javascript:if(event.keyCode==13){searchMsg();}" 
      type="text" name="keywords" value="${keywords}"/>
    <input type="button" onclick="searchMsg();" value="查询"/> 
	</td>
   </tr>
   </table>
</form>
</body>
</html>
