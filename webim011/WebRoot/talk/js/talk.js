var mainFrameId="mainFrame";
var mainFrameClass="mainFrame";

var debugId="debug";
var scrollHeight;
/**init user interface**/
var ui={
	initCss:function(){
		var defaultCssPath=basePath+"talk/css/foot_panelstyle.css";
		this.createCss(defaultCssPath);
	},
	initJs:function(){	
		var jqueryJsPath=basePath+"talk/js/jquery-1.3.2.min.js";
		var effectJsPath=basePath+"talk/js/talk-effect.js"
		var panelJsPath=basePath+"talk/js/jquery.talkpanel.plugin.js";
		var messagerPath=basePath+"talk/js/jquery.messager.js";
		this.createJs(jqueryJsPath);
		this.createJs(panelJsPath);
		this.createJs(effectJsPath);
		this.createJs(messagerPath);
	},
	/**初始化显示与隐藏的点击事件**/
	initClick:function(){
		$(window.top.document).find("#ad").toggle(
		function(){
				$(this).attr("title","open IM");
				$(this).html("显示");
				$(window.top.document).find("#footpanel").animate({height:"3px"},500);
			},
			function(){
				$(this).attr("title","close IM");
				$(this).html("隐藏");
				$(window.top.document).find("#footpanel").animate({height:"29.5px"},500);
			}
		);
	},
	initConstant:function(){
		if(top.document.documentElement.scrollHeight<top.document.documentElement.clientHeight){
			scrollHeight=top.document.documentElement.clientHeight;
		}else{
			scrollHeight=top.document.documentElement.scrollHeight;
		}
	},
	init:function(path){
		basePath=path;	
		this.initCss();
		this.initJs();
		this.initConstant();
		im.createIM();	
		this.initClick();
	},
	formatCss:function(path){
		 return "<link rel=\"stylesheet\" type=\"text/css\" href=\""+path+"\">";
	},
	createCss:function(path){
		$(window.top.document).find("head").append(this.formatCss(path));
	},
	createJs:function(path){
		var scriptVar=window.top.document.createElement("script");
		scriptVar.src=path;
		scriptVar.type ='text/javascript';
		window.top.document.getElementsByTagName("HEAD")[0].appendChild(scriptVar);
	}
};

var im = {
	mainFrameId:mainFrameId, 
	mainFrameClass:mainFrameClass,
	updateScroll:function(){
		mainFram=window.top.document.getElementById(this.mainFrameId);
		mainFram.style.top=eval(top.document.documentElement.clientHeight+top.document.documentElement.scrollTop+top.document.body.scrollTop-mainFram.offsetHeight);
		mainFramTop=mainFram.style.top.replace("px","");
		if(eval(scrollHeight-mainFramTop)<0){
			mainFram.style.top=eval(scrollHeight-mainFram.offsetHeight);
		}
		util.getDebug().html(top.document.documentElement.clientHeight+"+"+top.document.documentElement.scrollTop+"+"+top.document.body.scrollTop+"-"+$(mainFram).height()); 		
	},
	createIM:function(){
		loginModule.login();
		if(loginModule.isLogin){
			this.createMainFrame();
			setTimeout("im.addEffect()",1000);  
			userModule.initUserList();
		}
	},
	createMainFrame:function(){        
		var mainFrameTemplate=$("#mainFrameTemplate").clone(true);
		var adTemplate=$("#adTemplate").clone(true);
		mainFrameTemplate.find("#chatTitle").html(loginUserName);
		$(window.top.document).find("body").append(mainFrameTemplate.html());
		util.getMainFrame().attr("loginUserId",loginUserId);
		util.getMainFrame().attr("loginUserName",loginUserName);	
		util.getMainFrame().attr("basePath",basePath);	
		$(window.top.document).find("body").append(adTemplate.html());
		keepAdOnBottom();
	},
	addEffect:function(){
		if(window.top.document.readyState!="undefined"){
  	 			setTimeout(function(){
  	 				window.top.effect.init();
  	 			},1000);
  	 			return;
  	 	}
  	 	if(top.document.readyState!="complete"){
  			setTimeout("im.addEffect()",1000);  			  				
  		}else{
  			window.top.effect.init();
  		}
	}
};

function shakeChat(userId){
	 window.top.effect.shakeChat(userId);
}

function shakeUserIcon(userId){
	window.top.effect.shakeUserItem(userId);
}
var userModule={
	initUserList:function(){
		var url=basePath+'im/getGroupUserTree.action?id='+loginUserId;
		url+="&temp="+Math.random();
		$.ajax({
			url:url, 
			dataType:"json", 
			cache:false, 
			success:function(json){
				$.each(json,function(i,group){
					window.top.effect.addUserGroup(group,"userList");
					$.each(group.children,function(j,user){
					window.top.effect.addUserItem(user,group,"userList");
				});	
			});	
			//初始化未读消息
			msgInit(loginUserId);
		  }
		});
	},
	//刷新用户列表
	refreshUserList:function(){
			var url=basePath+'im/getGroupUserTree.action?id='+loginUserId+"&temp="+Math.random();
			$.ajax({
				url:url, 
				dataType:"json", 
				cache:false, 
				success:function (json) {
					window.top.effect.refreshUserList(json);
				}
			});
	},
	//notifyUser通知者
	refreshUserTree:function(notifyUser,isOnline){
		window.top.effect.refreshnotifyUser(notifyUser,isOnline);
	},
	//检测连线情况
	checkTimeOut:function(){
		CheckTimeOutService.checkTimeOutCallBack(loginUserId);
	}
};
//登陆模块
var loginModule={
	isLogin:false,
	login:function(){
		dwr.engine.setAsync(false);   
		loginUserAccount=$("#loginUserAccount").val();
		LoginService.login(loginUserAccount,loginCallBack);
		dwr.engine.setAsync(true); 
		function loginCallBack(loginUser){
			if(loginUser!=null){
				loginModule.loginSuccess(loginUser);
			}
		}
	},
	loginSuccess:function(loginUser){
		loginUserId=loginUser.userId;
		loginUserName=loginUser.userInfo.realName;
		$("#loginUserId").val(loginUserId);
		$("#loginUserName").val(loginUserName);
		dwr.engine.setActiveReverseAjax(true); //激活
		CheckTimeOutService.checkTimeOutCallBack(loginUserId)
	/**	setInterval(function(){
	   	   // 
			CheckTimeOutService.checkTimeOutCallBack(loginUserId);
		},3500);**/
		this.isLogin=true;
	}
};
//登陆时初始化，获取未读的信息 add
function msgInit(loginUserId){	
	//显示未读的信息
	var idsArray = new Array();
	var isExist = false;
	ChatService.getAllUnreadMsg(loginUserId,function(msgList){
			for (var index = 0; index < msgList.length; index++) {
				var msg=msgList[index];
				for ( var i=0 ; i < idsArray.length ; ++i ){
					if(idsArray[i]==msg.sender.userId)isExist=true;
				}
				if(!isExist){
					idsArray.push(msg.sender.userId);
				}
			}
			for ( var i=0 ; i < idsArray.length ; ++i ){
				shakeUserIcon(idsArray[i]);
			}
	});
}
var util={  
	getMainFrame:function(){
		return $(window.top.document).find("#"+mainFrameId);
	},
	getDebug:function(){
		return $(window.top.document).find("#"+debugId);
	}
}
/**保持显示的位置**/
function keepAdOnBottom(){
 var adobj = window.top.document.getElementById("ad");
 adobj.style.top = top.document.documentElement.scrollTop+top.document.documentElement.clientHeight-20+"px"; 
 setTimeout(function(){keepAdOnBottom();},1000);  
}
