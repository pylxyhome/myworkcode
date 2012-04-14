/*聊天*/
$(function(){
	setTimeout(function(){
		chatModule.init();
	},1000);	
});
function openWin(f,n,w,h,s){
	sb = s == "1" ? "1" : "0";
	l = (screen.width - w)/2;
	t = (screen.height - h)/2;
	sFeatures = "left="+ l +",top="+ t +",height="+ h +",width="+ w
		+ ",center=1,scrollbars=" + sb + ",status=0,directories=0,channelmode=0";
	openwin = window.open(f , n , sFeatures );
	if (!openwin.opener)
		openwin.opener = self;
		openwin.focus();
	return openwin;			
}
var loginUserId="";
var loginUserName="";
var targetId="";
var targetName="";
//显示未读的信息
var chatModule={  
	//初始化
	init:function(){
		this.constantInit();
		this.initUnReadMsg();  //获取未读消息
		this.btnInit(); 
		this.keyPressInit();
		this.activeReverseAjax(loginUserId,targetId);
	},
	activeReverseAjax:function(loginId,targetId){
		dwr.engine.setActiveReverseAjax(true);
		//绑定聊天窗口到ScriptSession
		//alert("loginId="+loginId+"targetId="+targetId);
		ChatService.bindingChatScriptSession(loginId,targetId);
	},
	initUnReadMsg:function(){
		ChatService.getUnreadMsg(loginUserId,targetId,function(msgList){
			for (var index = 0; index < msgList.length; index++) {
				var msg=msgList[index];
				receiveMessage(msg,targetName); //接收消息
				//修改信息状态为已读
				ChatService.readMsg(msg.msgId);
			}
		});
	},
	btnInit:function(){
		$("#enterBtn").click(function(){
			sendMessage(loginUserId,loginUserName);
		});
	},
	//初始化变量
	constantInit:function(){
		loginUserId=util.getLoginUserId();
		loginUserName=util.getLoginUserName();
		targetId=util.getTargetId();
		targetName=util.getTargetName();
	},
	keyPressInit:function(){
		$("#contentInput").keydown(function(event){ 
			if(event.ctrlKey==true && event.keyCode==13){
				sendMessage(loginUserId,loginUserName);
				return false;
			}  
		})
	}
}
var util={
	getLoginUserId:function(){
		return $("#loginUserId").val();
	},
	getLoginUserName:function(){
		return $("#loginUserName").val();
	},
	getTargetId:function(){
		return $("#targetId").val();
	},
	getTargetName:function(){
		return $("#targetName").val();
	},
	getContent:function(){
		return document.getElementById("contentInput").value;
	}
}
//发送消息
function sendMessage(id, userName){
	if($.trim(util.getContent())==""){
		errorMsg("not_blank");   
		return ;
	}
	if(util.getContent().length>300){
		errorMsg("too_long");   
		return ;
	}
	if(util.getTargetId()==id){
		errorMsg("not_talk");    
		return ;
	}
	var nowDate = new Date();
	var nowDateStr = nowDate.getFullYear()+"-"+(nowDate.getMonth()+1)+"- "+nowDate.getDate()+"   "+nowDate.getHours()+":"+nowDate.getMinutes()+":"+nowDate.getSeconds();
	var chatTemplate=$("#contentTemplate").clone(true);
	chatTemplate.find("#senddate").html(nowDateStr);
	chatTemplate.find("#who").html(userName);
	chatTemplate.find("#content").html(replaceTextarea(util.getContent()));
	$("#chatContentDiv").append(chatTemplate.html());
	var heightVal= document.getElementById("chatContentDiv").scrollHeight
	$("#chatContentDiv").scrollTop(heightVal);
	//将光标保持在输入域里
	$("#contentInput").focus();
	ChatService.send(util.getLoginUserId(), util.getLoginUserName(), util.getTargetId(),util.getContent());
	//清空输入域内容
	$("#contentInput").val("");
}
//接收消息
function receiveMessage(message, senderName){
	var senddate = new Date(message.senddate); 
	var senddateStr = senddate.getFullYear()+"-"+(senddate.getMonth()+1)+"- "+senddate.getDate()+"   "+senddate.getHours()+":"+senddate.getMinutes()+":"+senddate.getSeconds(); 
	var chatTemplate=$("#contentTemplate").clone(true);
	chatTemplate.find("#senddate").html(senddateStr).wrap("<font color='blue'></font>");
	chatTemplate.find("#who").html(senderName).wrap("<font color='blue'></font>");
	chatTemplate.find("#content").html(message.message).wrap("<font color='blue'></font>");
	$("#chatContentDiv").append(chatTemplate.html());
	$("#chatContentDiv").scrollTop(document.getElementById("chatContentDiv").scrollHeight);
}
function errorMsg(id){
	$("#"+id).css("display","");
		setTimeout(function(){
			$("#"+id).fadeOut("slow");
	},3000); 
}
function replaceTextarea(str){
	var reg=new RegExp("\r\n","g");
	str = str.replace(reg,"<br />");
	return str;
}
	