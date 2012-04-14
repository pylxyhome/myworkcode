<%@ page contentType="text/html;charset=UTF-8" %>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>用户组菜单</title>
<script type="text/javascript" src="../js/wtree.js"></script>
<link href="../css/dtree.css" rel="stylesheet" type="text/css"></link>
<script type="text/javascript" src="${basePath}talk/js/jquery-1.3.2.min.js"></script>
</head>
<body>
<div id="treearea">加载用户树...</div>
<script type="text/javascript">
	var mydTree = new dTree('mydTree','../images/system/dept/');
	mydTree.config.folderLinks=true;
	mydTree.add(0,-1,'用户组与用户树',"javascript:;");
	function initTreeUserList(){
		var url='${basePath}im/getGroupUserTree.action?id=${loginUserId}';
		url+="&temp="+Math.random();
		$.ajax({
			url:url, 
			dataType:"json", 
			cache:false, 
			success:function(json){
				$.each(json,function(i,group){
					var groupid=group.id.split("_")[1];
					mydTree.add(groupid,0,group.text,"javascript:;");
					$.each(group.children,function(j,user){
					var userid=1000002+user.id.split("_")[1];
					mydTree.add(userid,groupid,user.text,
					"${basePath}im/listRightMsg.action?query=false&loginUserId=${loginUserId}&receiverId="
					+user.id.split("_")[1],"","right");
				});	
			});	
			document.getElementById('treearea').innerHTML = mydTree;
		  }
		});
	}
	/**初始化用户树**/
	$(function(){
		initTreeUserList();
	});
</script>
<div>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;</p>
</div>
</body>
</html>
