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
    
    <title>My JSP 'index.jsp' starting page</title>
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
                'script'         : '${basePath }im/uploadFile.action?receiverId=2&loginUserId=1',//处理上传的路径，这里使用Struts2是XXX.action 
                'cancelImg'      : '${basePath }js/cancel.png',//取消上传文件的按钮图片，就是个叉叉
                'folder'         : 'uploads',//上传文件的目录
                'fileDataName'   : 'uploadify',//和input的name属性值保持一致就好，Struts2就能处理了
                'queueID'        : 'fileQueue',
                'auto'           : true,//是否选取文件后自动上传
                'multi'          : true,//是否支持多文件上传
                'simUploadLimit' : 2,//每次最大上传文件数
                'buttonText'     : 'BROWSE',//按钮上的文字
                'displayData'    : 'percentage',//有speed和percentage两种，一个显示速度，一个显示完成百分比 
                'fileDesc'       : '支持格式:jpg/gif/jpeg/png/bmp.', //如果配置了以下的'fileExt'属性，那么这个属性是必须的 
                'fileExt'        : '*.jpg;*.gif;*.jpeg;*.png;*.bmp',//允许的格式
                'onComplete'     : function (event, queueID, fileObj, response, data){
                   $("#result").html(response);//显示上传成功结果
                  setInterval("showResult()",3000);//两秒后删除显示的上传成功结果
                }
            });
        });
        
        function showResult(){//删除显示的上传成功结果
          $("#result").html("");
        }
        function uploadFile(){//上传文件
         jQuery('#uploadify').uploadifyUpload();
        }
        function clearFile(){//清空所有上传队列
            jQuery('#uploadify').uploadifyClearQueue();
        }
        </script>
   
  </head>
  
  <body>
    <div id="fileQueue"></div>
    	<input type="hidden" id="loginUserId" name="loginUserId" value="${param.loginUserId}" />
        <input type="hidden" id="receiverId" name="receiverId" value="${param.receiverId}" />
        <input type="file" name="uploadify" id="uploadify" />
        <p>
        <a href="javascript:void(0)" onclick="uploadFile();">开始上传</a>&nbsp;
        <a href="javascript:void(0)" onclick="clearFile();">取消所有上传</a>
        </p>
        <div id="result"></div><!--显示结果-->
  </body>
</html>