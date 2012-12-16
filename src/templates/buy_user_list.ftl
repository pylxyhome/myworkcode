<table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="Integral_l4">
        <td>${tradeName}</td>
        <td>价格(元)</td>
        <td>数量(个)</td>
      </tr>
      <#list list as mPointTradeStore>
      <tr>
        <td>${mPointTradeStore.username}</td>
        <td>${mPointTradeStore.price}</td>
        <td>${mPointTradeStore.volume-mPointTradeStore.dealVloume}</td>
      </tr>
      </#list>
</table>