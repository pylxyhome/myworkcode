(function($){
		var defaults={
				headTitle:"标题",
				data:[],
				rowfixCount:5,
		};
		$.fn.topList=function(params){
			$.extend(defaults,params);
			var html = [];
			html.push('<div class="module" monkey="new-music-up10">');
  			html.push('<div class="title"><span class="h2Title">'+defaults.headTitle+'</span></div><div class="rank-info clearfix_top_list"><ul class="rank-top-inner col-rank-top">');
			for(var i=0;i<defaults.data.length;i++){
				   var className="";
				   if(i==0){
					   className="orange f14 bold";
					}
					if(i>0&&i<3){
						className="orange";
					}
				   var topBean=defaults.data[i];
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
				if(defaults.data.length<eval(defaults.rowfixCount)){
					for(var i=0;i<eval(defaults.rowfixCount)-defaults.data.length;i++){
						html.push('<li>&nbsp;</li>');
					}
				}
			 html.push('</ul></div></div>');
			 this.html(html.join(''));
			 return this;
		}
})(jQuery);