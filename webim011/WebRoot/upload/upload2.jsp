<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>给${receiverUser.userInfo.realName}发送文件</title>
 <meta http-equiv="pragma" content="no-cache">
 <meta http-equiv="cache-control" content="no-cache">
 <meta http-equiv="expires" content="0">    
 <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
 <meta http-equiv="description" content="This is my page">
 <link href="${basePath }css/default.css" rel="stylesheet" type="text/css" />
 <link href="${basePath }css/uploadify.css" rel="stylesheet" type="text/css" />
 <script type="text/javascript" src="${basePath }js/jquery-1.3.2.min.js"></script>
 <script type="text/javascript" src="${basePath }js/swfobject.js"></script>
 <script type="text/javascript" src="${basePath }js/jquery.uploadify.v2.1.0.min.js"></script>
 <script type="text/javascript">
        $(document).ready(function() {
            $("#uploadify").uploadify({
                'uploader'       : '${basePath }js/uploadify.swf', //是组件自带的flash，用于打开选取本地文件的按钮 
                'script'         : '${basePath }im/uploadFile.action',//处理上传的路径，这里使用Struts2是XXX.action
                'scriptData'     : {'receiverId':$("#receiverId").val(),'loginUserId': $("#loginUserId").val()},  
                'cancelImg'      : '${basePath }js/cancel.png',//取消上传文件的按钮图片，就是个叉叉
                'folder'         : 'uploads',//上传文件的目录
                'fileDataName'   : 'uploadify',//和input的name属性值保持一致就好，Struts2就能处理了
                'queueID'        : 'fileQueue',
                'auto'           : true,//是否选取文件后自动上传
                'multi'          : false,//是否支持多文件上传
                'sizeLimit'      : 2097152,  //单位字节 2M
               //'simUploadLimit' : 2,//每次最大上传文件数
               // 'buttonText'     : "select",//按钮上的文字
                'buttonImg'      : '${basePath }js/uploadbutton.png',
                'displayData'    : 'speed',//有speed和percentage两种，一个显示速度，一个显示完成百分比 
                'fileDesc'       : '支持格式:图片,zip,word文档,PPT,Excel,TXT文本', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                'fileExt'        : '*.jpg;*.gif;*.jpeg;*.png;*.bmp;*.doc;*.ppt;*.xls;*.txt',//允许的格式
                'onComplete'     : function (event, queueID, fileObj, response, data){
                   clearResult();
                   showResult(response);//显示上传成功结果
                   $("#uploadify").val("");
                }
            });
        });
        function uploadFile(){//上传文件
         jQuery('#uploadify').uploadifyUpload();
        }
        function clearResult(){//清空显示的上传成功结果
          $("#result").html("&nbsp;");
        }
        function showResult(msg){//显示的上传成功结果
          $("#result").html(msg);
        }
  </script>
  </head>
  <body>
    <div id="fileQueue"></div>
    	<input type="hidden" id="loginUserId" name="loginUserId" value="${loginUserId}" />
        <input type="hidden" id="receiverId" name="receiverId" value="${receiverId}" />
        <input type="file" name="uploadify" id="uploadify"/>
        <div id="result">&nbsp;</div>
        <div id="note">
         说明：只允许发送图片,zip,word文档,PPT,Excel文件,TXT文本<br />
        <span style="padding-left: 35px;">上传文件不能超过2M</span>
        </div>
  </body> 
</html>