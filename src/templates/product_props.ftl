<#list propslist as props>
	<div class="new_productsten">${props.attrName}:</div>
	<#if props.attrInputType=0>
        <div class="new_productsauto">
          <input name="${props.attrNo}" value="${props.selectValues}" type="text" class="new_productsnig200" />
        </div>
	</#if>
	<#if props.attrInputType=1 && props.attrType=0>
	<select name="${props.attrNo}" class="new_productsnig200">
		<#list props.attrValuelist as attrValue>
			<#if attrValue=props.selectValues>
         	<#assign selected='selected'>
         	<#else><#assign selected=''></#if>
          		<option value="${attrValue}" ${selected}>${attrValue}</option>
		</#list>
        </select>
	</#if>
	<!--��ѡ -->
	<#if props.attrInputType=1 && props.attrType=2>
	<select name="${props.attrNo}" class="new_productsnig200">
		<#list props.attrValuelist as attrValue>
			<#if attrValue=props.selectValues>
         	<#assign selected='selected'>
         	<#else><#assign selected=''></#if>
          		<option value="${attrValue}" ${selected}>${attrValue}</option>
		</#list>
        </select>
	</#if>
<div class="h5"></div>
</#list>

