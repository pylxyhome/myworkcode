<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<html>
<head>
<base href="<%=basePath%>">
<title>发送文件</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="css/vip.css" type="text/css">
  <script type="text/javascript" src="${basePath}talk/js/jquery-1.3.2.min.js"></script>
    <script type="text/javascript" src="${basePath}talk/chat.js"></script>
    <script type="text/javascript" src="${basePath}talk/js/ajaxfileupload.js"></script>
  
    <script type="text/javascript">
    function uploadFile(){
     var uploadfile=document.getElementById("uploadfile").value;
     if(uploadfile==""){
    	 alert("上传图片不能为空");
	     return false;
      }	
      	var types = ["jpg","gif","bmp","png","doc","pdf","txt","xls","ppt","swf","zip"];
		var ext = uploadfile.substring(uploadfile.length-3).toLowerCase();
		var sing = false;
		for(var i=0; i<types.length;i++){
			if (ext==types[i]){
				sing = true; 
			}
		}
		if(!sing){
			alert("只允许上传图片/flash动画/word文件/pdf文件/txt文件/xls文件/ppt文件/zip文件");
			return false;
		}
		ajaxFileUpload();	  
    }
    
    function ajaxFileUpload(){
	 with(document.myform){
		  $("#loading").ajaxStart(function(){
   		  $(this).show();
   })
   .ajaxComplete(function(){
   		 $(this).hide();
   });
   $.ajaxFileUpload  
   (
    {
     url:'${basePath}im/sendFile.action?loginUserId='+$("#loginUserId").val()+"&receiverId="+$("#receiverId").val()+"&temp="+Math.random(),//上传调用的Action,即是上传提交的页面url
     secureuri:false,
     fileElementId:'uploadfile',//与页面处理代码中file相对应的ID值
     dataType: 'text',//返回数据类型，有text，xml，json，html,scritp,jsonp五种  
     success: function (data, status)
     { 
    	$("#msg").css("display","").html(data);
    	$("#uploadfile").val(""); //清空文件地址
    	 setTimeout(function(){
				 $("#msg").fadeOut("slow");
			},5000); 
     },
     error: function (data, status, e)
     {
    	
     }
    }
   )	

 }
}
    </script>
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<form action="" name="myform">
	<input type="hidden" id="loginUserId" name="loginUserId" value="${param.loginUserId}" />
    <input type="hidden" id="receiverId" name="receiverId" value="${param.receiverId}" />
	<table width="98%" border="0" cellspacing="2" cellpadding="3" align="center">
    <tr bgcolor="6f8ac4"><td colspan="2"> 
    <font color="#FFFFFF">传送文件：</font>
   <span id="msg" style="display:none;color: #FFFFFF;"></span></td>
    </tr>
	<tr bgcolor="f5f5f5"> 
      <td width="22%" > <div align="right">文件：</div></td>
      <td width="78%"> <input type="file"  onkeydown="return false;" id="uploadfile" name="upload" size="50"><br/>
      只允许上传图片/flash动画/word文件/exe文件/pdf文件/txt文件/xls文件/ppt文件/zip文件
      </td>
    </tr>
    <tr bgcolor="f5f5f5"> 
      <td colspan="2"> <div align="center"> 
     	  <img id="loading" src="${basePath}talk/images/loading.gif" style="display:none;" align="left"/>
          <input type="button" onclick="javacript:uploadFile();" name="SYS_SET" value=" 确 定 " class="frm_btn">
          <input type="button" onclick="javacript:window.close();" name="SYS_SET" value=" 关闭 " class="frm_btn">
        </div></td>
    </tr>
  </table>
  
	</form>
<br>
</body>
</html>
