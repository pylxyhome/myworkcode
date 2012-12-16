<table width="100%" border="0" cellspacing="0" cellpadding="0" class="Integral_no817">
        <tr class="Integral_no818">
          <td>姓名</td>
          <td>买卖标示</td>
          <td>成交价格</td>
          <td>成交数量（份）</td>
          <td>成交金额（元）</td>
          <td>成交时间</td>
        </tr>
         <#list list as mptg>
         	<#if (mptg_index+1)%2=0>
         	<#assign listClass='Integral_no819'>
         	<#else><#assign listClass=''></#if>
	        <tr class="${listClass}">
	          <td>${mptg.username}</td>
	          <td><#if mptg.tradeSn = 1>买入<#else>卖出</#if></td>
	          <td>￥${mptg.price}</td>
	          <td>${mptg.volume}</td>
	          <td>￥${mptg.amount}</td>
	          <td>${mptg.tradeTime?string("yyyy-MM-dd HH:mm:ss")}</td>
	        </tr>
        </#list>
      </table>
      <div class="h5"></div>
      <div class="Integral_no811"> <a href="javascript:loadMPiontTradeLogList(${pre_page})"> << 上一页</a> <a href="javascript:loadMPiontTradeLogList(${next_page})">下一页 >></a> </div>