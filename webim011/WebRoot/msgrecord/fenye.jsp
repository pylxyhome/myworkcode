<%@ page language="java" pageEncoding="UTF-8"%>
<span style="font-size: 12px;">
<font color="#FFFFFF">
    显示页数:${pageView.currentpage}/${pageView.totalpage} |总记录数:${pageView.totalrecord}</font>
<c:if test="${pageView.currentpage==1}"><b><font color="greed">首页</font></b></c:if>
<c:if test="${pageView.currentpage!=1}"><a href="javascript:topage('${wp}')" class="a03">首页</a></c:if>  　
<c:forEach begin="${pageView.pageindex.startindex}" end="${pageView.pageindex.endindex}" var="wp">
    <c:if test="${pageView.currentpage==wp}"><b><font color="greed">第${wp}页</font></b></c:if>
    <c:if test="${pageView.currentpage!=wp}"><a href="javascript:topage('${wp}')" class="a03">第${wp}页</a></c:if>
</c:forEach>
<c:if test="${pageView.currentpage==pageView.totalpage}"><b><font color="greed">尾页</font></b></c:if>
<c:if test="${pageView.currentpage!=pageView.totalpage}">
<a href="javascript:topage('${pageView.totalpage}')" class="a03">尾页</a>
</c:if>
</span>