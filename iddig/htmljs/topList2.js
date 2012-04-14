var topList=function(){
		headTitle = "",
		data=[],
		renderTo='',
		rowfixCount='',
		initHtml = function(){
			var html = [];
			html.push('<div class="module" monkey="new-music-up10">');
  			html.push('<div class="title"><span class="h2Title">'+headTitle+'</span></div><div class="rank-info clearfix_top_list"><ul class="rank-top-inner col-rank-top">');
			for(var i=0;i<data.length;i++){
				   var className="";
				   if(i==0){
					   className="orange f14 bold";
					}
					if(i>0&&i<3){
						className="orange";
					}
				   var topBean=data[i];
				   html.push('<li><div class="rank"><em class="'+className+'">'+(i+1)+'</em></div>');
				   className="";
				   if(i==0){
					   className=" bold";
					}
				   html.push('<div class="music-info"><div class="music-name"><a class="'+className+'" target="_blank" href="'+topBean.url+'" title="'+topBean.title+'">'+topBean.text+'</a></div></div>');
				   var statusClass="up";
				   var statusTitle="趋势：上升";
				   html.push('<div class="stay-time">'+topBean.pcount+'</div></li>');
				 
				}
				if(rowfixCount!=''&&data.length<eval(rowfixCount)){
					for(var i=0;i<eval(rowfixCount)-data.length;i++){
						html.push('<li>&nbsp;</li>');
					}
				}
			html.push('</ul></div></div>');
	
			 $('#'+renderTo).html(html.join(''));
		}
		return {
			/**{data:[{title,text,status,url,pcount},{title,text,status,url,pcount},{title,text,status,url,pcount}],id:id,headTitle:headTitle}
			*****/
			show: function(params){
					//_tobj=this;
					if(params.id != undefined){
						renderTo = params.id
					}
					if(params.headTitle!=undefined){
						  headTitle=params.headTitle;
					  }
					if(params.data!=undefined){
						  data=params.data;
					 }
					 if(params.rowfixCount!=undefined){
						 rowfixCount=params.rowfixCount;
						 }
					 initHtml();
				}	
		}	

};