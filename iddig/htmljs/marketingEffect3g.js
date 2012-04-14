var ua_range='';
var ua_params; 
var ua_data;
var ua_city=''; 
var reportType='ID-packMarketing';
function _init(){
	_distory();
	initUIResouces("ID-netMarketing","ID-packMarketing","ID-termMarketing","ID-planMarketing");
	$('#selector_0').empty();
	var html='<div id="widget" style="float: left;"><div id="widgetField"><span></span></div><div id="widgetCalendar" style="z-index:9999"></div></div>';
	$('#selector_0').append(html);
	var date = new Date();
	date.setMonth(date.getMonth()-1);
	$('#widgetCalendar').DatePicker(date,'month',marketingEffectl3gHtmlInit);
	g_dimPicker.show2({renderTo:"selector_0",callback:marketingEffectl3gHtmlInit,isMulti:false,subShow:false});
	marketingEffect3gEventInit();
}
//var urlJson={ID-netMarketing:'',ID-packMarketing:'',ID-termMarketing:'marketingEffect/getTerminalUpdateData.do'};
function initAllCount(){
	var tickIMap={'nthDay':2,'nthWeek':3,'nthMonth':4};
	ua_range=$("#widgetField span").text();
	ua_city=$('#ID-loc-text').text();
	var userinfo = {
	        timeRange: ua_range,
	        city: ua_city
	};
	var jsonuserinfo = JSON.stringify(userinfo);
	$('.reportBigButton').each(function(){
		var blockid=$(this).attr("id");
		var jqObj=$(this).find(".ID-value");
		if(blockid!='ID-planMarketing'){
			var url="marketingEffect/getTerminalUpdateData.do";
		if(blockid=='ID-netMarketing'){
			url="marketingEffect/getNetworkUpdateData.do";
		}else if(blockid=='ID-packMarketing'){
			url="marketingEffect/getNetmealUpdateData.do";
		}
		jQuery.ajax({
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : url,   
	        async:false,//同步
	        data : jsonuserinfo,  
	        dataType : 'json',  
	        success : function(data){
	        	sysLFlag = true;
	        	var jsonObj=eval(data);
	        	if(data == null){
	        		return ;
	        	}
	        	if(jsonObj){
	        		jqObj.html(Math.round(jsonObj.all_mdnCount/100)/100);
	        	}
	        },  
	        error : function(e){
	        }  
		}); 
		}
	});
}
function _distory(){
	$('.reportBigButton').unbind('click');
	$('.reportButtonTips').unbind('click');
}
var isDetail=false;
function marketingEffect3gEventInit(){
		_distory();
	$('.reportBigButton').bind('click',function(){
		$('.reportBigButton').each(function(){
			$(this).removeClass("active");
		});
		isDetail=false;
		$(this).addClass("active");
		reportType = $(this).attr("id");
		marketingEffectl3gHtmlInit(reportType);
	});
	
	$('.reportButtonTips').bind('click',function(ev){
		var widgetID = $(this).attr("id");
		var tipJson;
		if(widgetID == 'ID-netMarketing-tips'){
			tipJson = {provN:$('#ID-loc-text').text(),path:"/3g/marketing/networkupdate/"};
		}else if(widgetID == 'ID-packMarketing-tips'){
			tipJson = {provN:$('#ID-loc-text').text(),path:"/3g/marketing/netmealupdate/"};
		}else if(widgetID == 'ID-termMarketing-tips'){
			tipJson = {provN:$('#ID-loc-text').text(),path:"/3g/marketing/terminalupdate/"};
		}/*else if(widgetID == 'ID-planMarketing-tips'){
			tipJson = {provN:"湖南",path:"/3g/marketing/useractive/"};
		}*/
		$().bubble(tipJson,ev);
		return false;
	});
	//lightStatuInit();
	defaultFouthMenuClick();
	initAllCount();
}
function defaultFouthMenuClick(){
	reportType = actionMenu;
	if(reportType == ""){
		reportType = "ID-netMarketing";
	}
	$('#'+reportType).click();
	actionMenu = '';
}

var series='';
var category='';
/**
 * 报表point的click事件处理function
 * @param params 地市名称
 */
function meChartClickEvent(hightChartObj){
	category=$.trim(hightChartObj.category.toString());
	series=$.trim(hightChartObj.series.name.toString());
	$('#ID-me3g_chart2_title').text('计划: '+hightChartObj.category);
	$('#ID-me3g_chart3_title').text('操作系统: '+hightChartObj.category);
	isDetail=true;
	initJsonData({type:reportType,handleFunc:termMarketingHandle});
//	pointClickEvent(hightChartObj.y);
}
/**
 * {type:'',handleFunc:function}
 * @author panyl
 * @date 2012-3-21
 * @param param
 */
function initJsonData(paramObj){
	var tickIMap={'nthDay':2,'nthWeek':3,'nthMonth':4};
	ua_range=$("#widgetField span").text();
	ua_city=$('#ID-loc-text').text();
	var userinfo = {
	        timeRange: ua_range,
	        city: ua_city
	};
	var jsonuserinfo = JSON.stringify(userinfo);
	var callback=paramObj.handleFunc;
	var url="marketingEffect/getTerminalUpdateData.do";
	if(isDetail)url="marketingEffect/getTerminalUpdateDetailData.do";
	if(paramObj.type=='ID-netMarketing'){
		url="marketingEffect/getNetworkUpdateData.do";
		if(isDetail)url="marketingEffect/getNetworkUpdateDetailData.do";
	}else if(paramObj.type=='ID-packMarketing'){
		url="marketingEffect/getNetmealUpdateData.do";
		if(isDetail)url="marketingEffect/getNetmealDetailUpdateData.do";
	}
	jQuery.ajax({
        type : 'POST',  
        contentType : 'application/json',  
        url : url,   
        data : jsonuserinfo,  
        dataType : 'json',  
        success : function(data){
        	sysLFlag = true;
        	var jsonObj=eval(data);
        	if(data == null){
        		$(document).unmask();
        		$("#data_contents").hide();
        		$.confirm({'title':'提示','message':"没有符合该条件的统计报表!"});
        		return ;
        	}
        	if(callback){
        		$("#data_contents").show();
        		callback(jsonObj);
        	}
        	$(document).unmask();
        },  
        error : function(e){
        	$(document).unmask();
        	$.confirm({'title':'提示','message':"session会话过期，请重新登录！"});
        	location.reload();
        }  
	}); 
}
function termMarketingHandle(jsonData){
	//meChart1(data1,"流量减少(万户)","流量增长(万户)");
	//meChart1(data1,"少于300M(万户)","300M及以上(万户)");
	if(!isDetail){
		meChart1(jsonData);
		meChart2(jsonData);
		meChart3(jsonData);
		return;
	}
	if(isDetail){
		meChart2(jsonData.planobj[series][category]);
		meChart3(jsonData.osobj[series][category]);
	}
	
}
function marketingEffectl3gHtmlInit(type,params){
	initAllCount();
	$('#ID-me3g_chart2_title').text('计划: 全省');
	$('#ID-me3g_chart3_title').text('操作系统: 全省');
	if(type==undefined){
		type=reportType;
	}
	if(type == "ID-netMarketing"){//网络升级
		$("#ID-chart1-title").html("流量变化(单位:户)");
		initJsonData({type:'ID-netMarketing',handleFunc:termMarketingHandle});
	}else if(type == "ID-packMarketing"){//套餐升级
		$("#ID-chart1-title").html("套内流量(单位:户)");
		initJsonData({type:'ID-packMarketing',handleFunc:termMarketingHandle});
	}else if(type == "ID-termMarketing"){//终端升级
		$("#ID-chart1-title").html("操作系统(单位:户)");
		initJsonData({type:'ID-termMarketing',handleFunc:termMarketingHandle});
	}
}
function loadSimpleTableData(data,type,userinfo,showtableid,headTitle,firstCol){
	var simpleTable = new htmlSimpleTableWidget();
	var _pageObj = simpleTable.getPageBar();
	var cols = [];
	var bdatas = [];
	var tips = [];
	if(firstCol){
		cols.push(firstCol);
	}
	for(var i=0;i<data.headers.length;i++){
		cols.push(data.headers[i]);
	}
	if(data.rowTotalMap){cols.push('合计');}
	for(var i=0;i<data.rows.length;i++){
			var dd = [];
		for(var j=0;j<data.rows[i].length;j++){ 
			dd.push(data.rows[i][j]);
		}
		if(data.rowTotalMap){
			dd.push(data.rowTotalMap[data.rows[i][0]]);//data.rows[i][0]第一行第一列为key
		}
		bdatas.push(dd);   
	}
	if(data.columnTotalMap){
		var dd = [];
		dd.push("合计");
		for(var i=0;i<data.headers.length;i++){
			dd.push(data.columnTotalMap[data.headers[i]]);//header为key
		}
		if(data.total){
			dd.push(data.total);  //总合计
		}
		bdatas.push(dd);  
	}
	var params={id:showtableid,thead:cols,tbody:bdatas,isPage:false,headTitle:headTitle};
	simpleTable.setTotalNum(data.rows.length);
	simpleTable.show(params);
	ua_params=userinfo;
	ua_data=data;
}
var sDate=new Date();
function initUserChart(chartBean){
	new Highcharts.Chart({
		chart: {
	        renderTo: 'ID-me3g_chart1',
	        type: 'column',
	        height:300
	     },
	     title:{
	    	 text: ''
	     },
	     colors:['#81CFE5','#308DAC','#DBCA70'],
	     credits: {
	    	 enabled: false
	     },
	     legend: {
	    	 align: 'center',
	    	 borderWidth: 0,
	    	 verticalAlign: 'center'
	     },
	     plotOptions:{
	    	 column:{
	    		 stacking: 'normal',
	    		 borderWidth: 0,
	    		 shadow: false,
	    		 pointWidth: 20,
	    		 cursor: 'pointer',
	             point: {
	                 events: {
	                     click: function() {
	                    	 doubleClickEvent(this);
	                     }
	                 }
	             }
	    	 },
	    	 line:{
	    		 lineWidth:0,
	    		 showInLegend: false,
	    		 stickyTracking: false,
	    		 states:{
	    			 hover: {
    					 enabled: false
    				 }
	    		 },
	    		 marker: {
	    			 radius: 0,
	    			 states:{
	    				 hover: {
	    					 enabled: false
	    				 }
	    			 }
	    		 }
	    	 }
	     },
	     xAxis: {
	    	 categories: chartBean.categories
	     },
	     yAxis: {
	    	 title:{
	    		 text:''
	    	 }
	     },
	     series:chartBean.series
	});	
}

function initNotesPNG(chartBean){
	var func;
	if(reportType=="ID-netMarketing"){
		func = "marketingNote1";
	}else if(reportType=="ID-packMarketing"){
		func = "marketingNote2";
	}else if(reportType=="ID-termMarketing"){
		func = "marketingNote3";
	}else{
		func = "marketingNote4";
	}
	var params = {
			chartType:'column',
			provCode:'',
			areaName:$('#ID-loc-text').text(),
			dateTime:$("#widgetField span").text(),
			func:func,
			resource:'anal/marketingEffect3g.js'
	};
	jQuery.ajax({
	        type : 'POST',  
	        contentType : 'application/json',  
	        url : 'main/getNodeBoardListFc.do',   
	        data : JSON.stringify(params),  
	        success : function(returnVal){
	        	if(returnVal == null || returnVal.length == 0){
	        		initUserChart(chartBean);
	        	}else{
		        	var categories = chartBean.categories;
		        	var series = chartBean.series;
		        	var l = series.length;
		        	for(var k=0;k<l;k++){
		        		var sname = series[k].name;
		        		var data = series[k].data;
		        		for(var j=0;j<returnVal.length;j++){
		        			if(returnVal[j].sName==sname){
		        				var index=0;
		        				for(var i=0;i<categories.length;i++){
				        			if(categories[i]==returnVal[j].x){
				        				index = i;
				        				break;
				        			}
				        		}
		        				data[index] = {"y":data[index],marker:{symbol: 'url(images/annotation_single.gif)'}};
			        		}
		        		}
		        		var line = new Object();
		        		line.type = "line";
		        		line.data = data;
		        		series.push(line);
		        	}
//		        	chartBean.series
		        	initUserChart(chartBean);
	        	}
	        	
	        }
	});
}

//流量情况图标及列表绘制
function meChart1(jsonObj,sn1,sn2){
	if(!jsonObj.userTable)return;
	loadSimpleTableData(jsonObj.userTable,"","","ID-me3g_table1",null,"地市");
//	initUserChart(jsonObj.userChartBean);
	initNotesPNG(jsonObj.userChartBean);
}

function meChart2(jsonObj){
	if(!jsonObj.planTable)return;
	loadSimpleTableData(jsonObj.planTable,"","","ID-me3g_table2",null,"计划");
	initOsOrPlanChart(jsonObj.planChartBean,'ID-me3g_chart2');
}
function meChart3(jsonObj){
	if(!jsonObj.osTable)return;
	loadSimpleTableData(jsonObj.osTable,"","","ID-me3g_table3",null,"系统");
	initOsOrPlanChart(jsonObj.osChartBean,'ID-me3g_chart3');
}
function initOsOrPlanChart(chartBean,renderid){
	return new Highcharts.Chart({
		chart: {
	        renderTo: renderid,
	        type: 'bar',
	        height:300
	     },
	     title:{
	    	 text: '',
	     },
	     credits: {
	    	 enabled: false
	     },
	     plotOptions:{
	    	 bar:{
	    		 borderWidth: 0,
	    		 shadow: false	 
	    	 },
	     	 series:{stacking: 'normal'}
	     },
	     colors:['#81CFE5','#308DAC'],
	     xAxis: {
	    	 categories: chartBean.categories
	     },
	     yAxis: {
	    	 min : -100,
			 max : 100,
	    	 title:{
	    		 text:''
	    	 },
	    	 labels:{
	    		 formatter: function(){
	    			 return this.value+"%";
	    		 }
	    	 }
	     },
	     legend: {
	    	 borderWidth: 0,
	    	 verticalAlign: 'top'
	    	 
	     },
	     series:chartBean.series
	});
}

var provTargetJson;
//获取相应省份的指标数据
function lightStatuInit(){
	$('.reportButtonLights').removeClass("active");
	$(".reportButtonLights.red").addClass("active");
	$('.ID-value').each(function(){
		$(this).text("--");
	});
	jQuery.ajax({
      type : 'POST',  
      contentType : 'application/json',  
      url : 'main/getTargets.do',   
      data : JSON.stringify($('#ID-loc-text').text()),  
      success : function(data){
      	provTargetJson = data;
      	for(var i=0;i<data.length;i++){
      		var targetId = data[i].filePath.split("@")[2];
      		if($('#'+targetId).length>0){
      			$('#'+targetId+" .ID-value").text(data[i].value);
      			$('#'+targetId+" .reportButtonLights").removeClass("active");
      			lightStatusChangesEvent(targetId);
//      			$('#'+targetId+" .reportButtonLights").each(function(){$(this).removeClass("active")});
      		}
      	}
      }
	});
}
//灯效果事件处理
function lightStatusChangesEvent(ev){
	var wid = "";
	if(typeof ev === 'string'){
		wid = ev;
	}
	if(ev == undefined){
		wid = reportType;
	}
	var value = parseFloat($('#'+wid+" .ID-value").text());
	for(var i=0;i<provTargetJson.length;i++){
		var targetId = provTargetJson[i].filePath.split("@")[2];
		if(targetId==wid){
			$('#'+wid+" .reportButtonLights.active").removeClass("active");
			var lower = parseFloat(provTargetJson[i].lower);
			var upper = parseFloat(provTargetJson[i].upper);
			if(value>=upper){
				$('#'+wid+" .reportButtonLights.green").addClass("active");
			}else if(value<lower){
				$('#'+wid+" .reportButtonLights.red").addClass("active");
			}else{
				$('#'+wid+" .reportButtonLights.yellow").addClass("active");
			}
			return ;
		}
	}
}

var noteObj = {};

function doubleClickEvent(ev){
	if(reportType=="ID-netMarketing"){
		noteObj.perm = "marketingNetup_note";
		noteObj.func = "marketingNote1";
	}else if(reportType=="ID-packMarketing"){
		noteObj.perm = "marketingPackup_note";
		noteObj.func = "marketingNote2";
	}else if(reportType=="ID-termMarketing"){
		noteObj.perm = "marketingPackup_note";
		noteObj.func = "marketingNote3";
	}else{
		noteObj.perm = "marketingPlanup_note";
		noteObj.func = "marketingNote4";
	}
	var eDate = new Date();
	 if(parseInt(eDate.getTime()-sDate.getTime())<500){
		 if(hasPermission(noteObj.perm)){
			 meChartClickEvent(ev);
       	 var tipJson = {
       			 pageX:ev.pageX,
       			 pageY:ev.pageY,
       			 headingText:ev.category+" - "+ev.series.name,
       			 contentText:"",
       			 params: {
       				 jsonPath:'',
       				 chartType:'column',
       				 provCode:'',
       				 areaName:$('#ID-loc-text').text(),
       				 dateTime:$("#widgetField span").text(),
       				 func:noteObj.func,
       				 resource:'anal/marketingEffect3g.js',
       				 sName:ev.series.name,
       				 sPointX:ev.category
       			 }
       	 };
       	 $().note(tipJson,hasPermission(noteObj.perm+"Add"));
   	 }
	 }else{
		 meChartClickEvent(ev);
	 }
	 sDate = eDate;
	 return ;
}

function marketingNote1(param){
	jQuery.ajax({
        type : 'POST',  
        contentType : 'application/json',  
        url : 'marketingEffect/getNetworkUpdateData.do',   
        data : JSON.stringify({timeRange:param.dateTime,city:param.area}),  
        success : function(data){
        	if(data == null || data == ""){
        		$.confirm({'title':'提示','message':'数据为空,可能文件不存在'});
        		return ;
        	}
        	var jsonObj = eval("("+data+")");
        	noteCharts(jsonObj.userChartBean);
        },  
        error : function(e){
        	$.confirm({'title':'提示','message':'获取相关json数据异常'});
        }  
	});
}

function marketingNote2(param){
	jQuery.ajax({
        type : 'POST',  
        contentType : 'application/json',  
        url : 'marketingEffect/getNetmealUpdateData.do',   
        data : JSON.stringify({timeRange:param.dateTime,city:param.area}),
        success : function(data){
        	if(data == null || data == ""){
        		$.confirm({'title':'提示','message':'数据为空,可能文件不存在'});
        		return ;
        	}
        	var jsonObj = window.eval("("+data+")");
        	noteCharts(jsonObj.userChartBean);
        },  
        error : function(e){
        	$.confirm({'title':'提示','message':'获取相关json数据异常'});
        }  
	});
}

function marketingNote3(param){
	jQuery.ajax({
        type : 'POST',  
        contentType : 'application/json',  
        url : 'marketingEffect/getTerminalUpdateData.do',   
        data : JSON.stringify({timeRange:param.dateTime,city:param.area}),  
        success : function(data){
        	if(data == null || data == ""){
        		$.confirm({'title':'提示','message':'数据为空,可能文件不存在'});
        		return ;
        	}
        	var jsonObj = eval("("+data+")");
        	noteCharts(jsonObj.userChartBean);
        },  
        error : function(e){
        	$.confirm({'title':'提示','message':'获取相关json数据异常'});
        }  
	});
}

function noteCharts(chartBean){
	new Highcharts.Chart({
		chart: {
	        renderTo: 'ID-chartContent',
	        type: 'column',
	        height:300
	     },
	     title:{
	    	 text: ''
	     },
	     colors:['#81CFE5','#308DAC','#DBCA70'],
	     credits: {
	    	 enabled: false
	     },
	     xAxis: {
	    	 categories: chartBean.categories
	     },
	     yAxis: {
	    	 title:{
	    		 text:''
	    	 }
	     },
	     legend: {
	    	 align: 'center',
	    	 borderWidth: 0,
	    	 verticalAlign: 'center'
	     },
	     series:chartBean.series
	});	
}