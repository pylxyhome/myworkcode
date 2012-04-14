<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();   
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
request.setAttribute("basePath",basePath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 4.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="utf-8">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>JS弹出层效果,兼容所有浏览器</title>
</head>
<script type="text/javascript" src="${basePath }js/openMoveWin.js"></script>
<body>
<input type="button" value="这是一个默认窗口,窗口位置默认为当前可视区域的居中,把鼠标往下拉,下面有个按钮可以测试"  onclick="MyWin.Create('','标题','这里是内容,可以是变量哦');"/>
<br>
<input type="button" value="这个窗口设置了宽度500px,高度300px" onclick="MyWin.Create('','标题','<button>提交</button><b>这里是内容,可以是变量哦</b>','500','300');" />
<br>
<input type="button" value="这是一个固定了位置x=600px,y=400px的窗口" onclick="MyWin.Create('','标题','这里是内容,可以是变量哦','500','300','600','400');" />
<br>
<input type="button" value="当窗口宽度或高度大于可视区域的宽度或高度,以固定位置的设置优先" onclick="MyWin.Create('','标题','这里是内容,可以是变量哦','700','900','600','400');" />
<br>
<input type="button" value="这是没有设置固定位置时的情况" onclick="MyWin.Create('','标题','这里是内容,可以是变量哦','700','900');" />
<br>
<input type="button" value="以上窗口都有遮罩,但这个没有" onclick="MyWin.Create('no','标题','这里是内容,可以是变量哦','500','300');" />
<br>
<input type="button" value="细心的朋友可能已经发现了,没有遮罩的窗口可以打开无限个哦" onclick="MyWin.Create('no','标题','这里是内容,可以是变量哦','500','300','100','300');" />
<br>
<input type="button" value="窗口里除了内容,也可以放iframe" onclick="MyWin.Create('no','我是和谐的百度','[pg]http://www.baidu.com','800','500');" />
<br>
<input type="button" value="有遮罩的iframe,同上,盖住select没商量" onclick="MyWin.Create('','我是和谐的百度','[pg]http://127.0.0.1/webim009/im/getRecordMsg.action','680','500');" />
<br>
<br>
<input type="button" value="anw" onclick="MyWin.Create('','软件中心','[pg]default.jsp','800','500')" />
<br>
<br>
<input type="button" value="本代码测试通过IE6/7/8/9; FF3.6.2; chorme5.0.375.55; Safari4.0.5; Opera10.60" onclick="MyWin.Create('','^-^','本代码测试通过IE6/7/8/9; FF3.6.2; chorme5.0.375.55; Safari4.0.5; Opera10.60');" />
<br>
<input type="button" value="欢迎转载_易建锋下载频道" onclick="MyWin.Create('','^-^','欢迎');" />

<textarea rows="2" name="S1" cols="20"></textarea><br>
<select size="1" name="D1">
<option>1</option>
<option>2</option>
</select> <br>
<br>
<br>
<input type="button" value="欢迎转载_根据网络资源自己综合改善过的代码" onclick="MyWin.Create('','^-^','欢迎');" />
<br>
<input type="button" value="弹出一个iframe" onclick="MyWin.Create('yes','这是一个iframe','[pg]http://www.baidu.com','600','300')" />
<br>
<br>
</body>
</html>
