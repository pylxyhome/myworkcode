<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" " http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns=" http://www.w3.org/1999/xhtml">
  <head>
    <base href="${basePath}">
    <title>IM template</title>
   <script type='text/javascript' src='dwr/engine.js'></script>
   <script type='text/javascript' src='dwr/interface/LoginService.js'></script>
   <script type='text/javascript' src='dwr/interface/ChatService.js'></script>
   <script type='text/javascript' src='dwr/interface/CheckTimeOutService.js'></script>
   <script type="text/javascript" src="${basePath}js/util/util.js"></script> 
   <script type="text/javascript" src="${basePath}js/util/constant.js"></script>
   <script type="text/javascript" src="${basePath}talk/js/jquery-1.3.2.min.js"></script>
   <script type="text/javascript" src="${basePath}talk/js/talk.js"></script>
  </head>    
  <body >
  	<script type="text/javascript">  
  		document.domain="127.0.0.1";　　
  		$(function(){
  			sleep();
  		});
  	 	function sleep(){
  	 		if(window.top.document.readyState!="undefined"){
  	 			setTimeout(function(){
  	 				ui.init("${basePath}");
  	 			},1000);
  	 			return;
  	 		}
  	 		if(window.top.document.readyState!="complete"){
  				setTimeout("sleep()",1000);  			  				
  			}else{
  				ui.init("${basePath}");
  			}
  	 	}
  	 
  	</script>
    <!-- im的显示与隐藏 -->
  	<div id="adTemplate">
		<div style="position:absolute;border:1px solid #000;right:0;cursor:hand;background-color: #e3e2e2;border-color: #e3e2e2;" title="close IM" id="ad" >隐藏</div>
	</div>	
  	<div id="mainFrameTemplate">  		
	  	<div id="footpanel">
			<ul id="mainpanel">    				 
				
		         <li id="alertpanel">
		        	<a href="javascript:void(0);" class="alerts">Alerts</a>
		            <div class="subpanel">
		            <h3><span class="min"> &ndash; </span>特殊功能</h3>
		            <ul>
		            	<li class="view"><a href="javascript:void(0);">查看所有用户</a></li>
		            	<li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Antehabeo</a> abico quod duis odio tation luctus eu ad <a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		                <li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Et voco </a> Duis vel quis at metuo obruo, turpis quadrum nostrud <a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		                <li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Tego</a> nulla eum probo metuo nullus indoles os consequat commoveo os<a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		                <li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Antehabeo</a> abico quod duis odio tation luctus eu ad <a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		                <li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Nonummy</a> nulla eum probo metuo nullus indoles os consequat commoveo <a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		                <li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Tego</a> minim autem aptent et jumentum metuo uxor nibh euismod si <a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		                <li><a href="javascript:void(0);" class="delete">X</a><p><a href="javascript:void(0);">Antehabeo</a> abico quod duis odio tation luctus eu ad <a href="javascript:void(0);">lobortis facilisis</a>.</p></li>
		            </ul>
		            </div>
		        </li>
				<!-- 客服窗口 -->
		        <li id="chatpanel">
		        	<a href="javascript:void(0);" class="chat" id="chatTitle" title="点击打开在线客服">客户人员</a>
		            <div class="subpanel">
		            <h3><span class="min"> &ndash; </span>在线客服</h3>
		            <!-- 查询输入框
		            <input id="seachInput" type="text" value="" style="width:180px;margin:4px;"/>
		            -->
		             <div id="seachResultDiv" style='z-index: 100; position:relative;left:-10;top:-10;width:30;float:left;display: none;' >
		             <a id="closeSeachResultHref" href="javascript:void(0);" style="margin-left:170px;">x</a>
		             <hr/>
		             <div id="seachResultItem" style="height:100%;">
		             		
		             </div>
		             <hr/> 
		             </div>
		     
		            <ul id="userList" style="overflow: scroll;height: 100%">
		            	
		            </ul>
		            </div>
		            
		             <!-- 发来信息的客户列表面板 -->
		            <div id="newMsgPanel" class="subpanel">
		            <h3 id="msgcount"><span class="min"> &ndash;</span>消息盒子</h3>     
		            <ul  id="notifyuserList" style="overflow: scroll;">
		            <!-- 
		            	<li><a href="javascript:void(0);" ><img src="${basePath}[userimg]" width="24" height="24" alt="" style="DISPLAY: block"/><div id="chatingDisplayName">[displayName]</div></a></li>
		            -->
		            </ul>
		            </div>
		        </li> 
			</ul>
		</div>
		
		<!-- 数据传递模块 -->
  		<input type="hidden" value="" id="loginUserPwd"/>   
  		<input type="hidden" value="${user }" id="loginUserId"/>
		<input type="hidden" value="" id="loginUserName"/>
		<input type="hidden" value="${param.loginUserAccount}" id="loginUserAccount"/>
  		<input type="hidden"  value="${basePath}" id="basePath"/>
		<!-- 用户列表模块 -->
		<div id="userItemTemplate" style="display:none;">
		
	  		<li id="usergroup_[userId]"><a href="javascript:void(0);" id="userItem_[Id]" isOnline="[isOnline]" groupId="[groupId]" class="menber" targetId="[targetId]" targetName="[targetName]" loginId="[loginId]" loginName="[loginName]"><img src="${basePath}talk/images/chat-thumb.gif" alt="" style="DISPLAY: block"/><div id="chatingDisplayName">[displayName]</div></a></li>
	  	</div>  
	  	
	  	<!-- 用户查询结果模块 -->
		<div id="seachUserItemTemplate" style="display:none;">
	  		<a href="javascript:void(0);" class="seachmenber" id="seachUserItem_[Id]" targetId="[targetId]" targetName="[targetName]" loginId="[loginId]" loginName="[loginName]"><img src="${basePath}talk/images/chat-thumb.gif" alt="" />[displayName]</a>
	  	</div>
	  	
		<!-- 聊天模板 -->
		<div id="chatingpanelTemplate" style="display:none;">
		  <li id="chatingpanel" class="chatingpanel">
        	<a href="#" class="chat" id="chatingTitle">Friends (<strong>18</strong>) </a>
            <div class="subchatpanel">
            	<h3><span id="chatMin" class="chatMin" title="最小化"> &ndash; </span><span id="chatClose" title="关闭">x</span>正在与[userTitle]聊天</h3>
		        <ul id="chatFramePanel">
		            <iframe id="chatFrame" frameborder="0" width="398px;" height="335px;" scrolling="auto" src=""></iframe>
		         </ul>
            </div>
        </li>
	</div>
  	</div>
  
  </body>
  
</html>
