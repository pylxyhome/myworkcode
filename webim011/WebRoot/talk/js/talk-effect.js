var basePath="";

var loginUserId="";
var loginUserName="";
var chatShakeArray=new Object();
var userShakeArray=new Object();
var userItemNameArray=new Object();
function killErrors() { 
	return true; 
} 
window.onerror = killErrors;
var effect={
	chatIdFormat:"chatingTemplate_[loginId]_[targetId]",
	chatFrameFormat:"chatFrame_[loginId]_[targetId]",
	addChat:function(loginId,targetId,targetName,src){
		effect.hideAll();
		effect.stillUserItem(targetId);//取消闪烁
		var chatingTemplate=$("#chatingpanelTemplate").clone(true);
		var chatId=effect.chatIdFormat.replace("[loginId]",loginId).replace("[targetId]",targetId);
		var chatFrame=effect.chatFrameFormat.replace("[loginId]",loginId).replace("[targetId]",targetId);
		if($("#"+chatId).length>0){
			this.showCurrentChat(chatId);
			return;
		}
		var userTitle=chatingTemplate.find("h3").html();
		userTitle=userTitle.replace("[userTitle]",targetName);
		chatingTemplate.find("h3").html(userTitle);
		chatingTemplate.find("#chatingTitle").html("(<strong>"+targetName+"</strong>)");
		chatingTemplate.find("#chatFrame").attr("id",chatFrame);
		chatingTemplate.find("#chatingpanel").attr("id",chatId);		
		$("#mainpanel").append(chatingTemplate.html());
		setTimeout(function(){
			$("#"+chatFrame).attr("src",src);		
			effect.initChat(chatId,targetId);	
			effect.showCurrentChat(chatId);
		},300);	
	},
	showCurrentChat:function(chatId){
		var charFrame=$("#"+chatId);
		$(".subchatpanel").hide(); //Hide all subpanels
		charFrame.find(".chat").next(".subchatpanel").show(); //Toggle the subpanel to make active
		$("#footpanel li a").removeClass('active'); //Remove active class on all subpanel trigger
		charFrame.find(".chat").addClass('active'); //Toggle the active class on the subpanel trigger
	},
	hideAll:function(){
		$(".subpanel").hide(); 
		$(".subchatpanel").hide();
		$("#footpanel li a").removeClass('active');
	},
	initChat:function(chatId,targetId){
		var charFrame=$("#"+chatId);
		var chatFrameId=effect.chatFrameFormat.replace("[loginId]",loginUserId).replace("[targetId]",targetId);
		charFrame.talkPanel();
		$(window).resize(function () { 
			charFrame.talkPanel();
		});	
		charFrame.find(".chat").click(function() { //If clicked on the first link of #chatpanel and #alertpanel...
			effect.stillChat(targetId); //取消聊天窗口闪烁
			$(".subpanel").hide();
			if($(this).next(".subchatpanel").is(':visible')){ //If subpanel is already active...
				$("#"+chatId).find("#chatingTitle").css("background-color","#e3e2e2");
				$(this).next(".subchatpanel").hide(); //Hide active subpanel
				$("#footpanel li a").removeClass('active'); //Remove active class on the subpanel trigger
			}
			else { //if subpanel is not active...
				$("#"+chatId).find("#chatingTitle").css("background-color","#FFFFFF");
				$(".subchatpanel").hide(); //Hide all subpanels
				$(this).next(".subchatpanel").toggle(); //Toggle the subpanel to make active
				$("#footpanel li a").removeClass('active'); //Remove active class on all subpanel trigger
				$(this).toggleClass('active'); //Toggle the active class on the subpanel trigger
			}
			return false; //Prevent browser jump to link anchor
		});
		charFrame.find("#chatMin").click(function() { //Click anywhere and...
			$(".subpanel").hide(); //hide subpanel
			$(".subchatpanel").hide();
			$("#footpanel li a").removeClass('active'); //remove active class on subpanel trigger
		});
		charFrame.find("#chatClose").click(function() { //Click anywhere and...
			var url=basePath+"im/leaveChat.action?loginId=";
			url+=loginUserId;
			url+="&targetId=";
			url+=targetId;
			url+="&temp="
			url+=Math.random();
			$("#"+chatFrameId).attr("src",url);//先移除绑定的scriptSession
			setTimeout(function(){charFrame.remove();},300);
		});		
	},
		
	shakeChat:function(userId){
		var chatId=effect.chatIdFormat.replace("[loginId]",loginUserId).replace("[targetId]",userId);
		var isHidden=$("#"+chatId).find(".chat").next(".subchatpanel").is(":hidden");
		var targetName=$("#userItem_"+userId).attr("targetName");
		if(isHidden){
			if(chatShakeArray[userId]==null){
			chatShakeArray[userId]=setInterval(function(){
					$("#"+chatId).find("#chatingTitle").css("background-color","#FFCC00");
					setTimeout(function(){
						$("#"+chatId).find("#chatingTitle").css("background-color","#FFFFFF");
					},300);
			},600);	
			//分段讲解2
			setTimeout(function(){
				effect.stillChat(userId);
				setTimeout(function(){
					isHidden=$("#"+chatId).find(".chat").next(".subchatpanel").is(":hidden");
					if(isHidden)
					$("#"+chatId).find("#chatingTitle").css("background-color","#FFCC00");
				},500);    
			   },3500);    
			}
		}
		rollTitle(targetName+"发来信息啦!!","ttt");
	  },
	//取消聊天窗口闪烁
    stillChat:function(userId){
    	if(chatShakeArray[userId]!=null){
			window.clearInterval(chatShakeArray[userId]);  
		}
		chatShakeArray[userId]=null;
	 },
	  //闪动用户列表项
	shakeUserItem:function(userId){
		var chatTitle="chatTitle";
		var targetName=$("#userItem_"+userId).attr("targetName");
		var targetId=$("#userItem_"+userId).attr("targetId");
		userShakeArray[userId]=setInterval(function(){
				//用户图标项
				$("#userItem_"+userId).css("background-color","#FFCC00");
				//用户列表图标项
				$("#"+chatTitle).css("background-color","#FFCC00");
				setTimeout(function(){
					$("#userItem_"+userId).css("background-color","#FFFFFF");
					$("#"+chatTitle).css("background-color","#e3e2e2");
				},300);
			},600);	
		rollTitle(targetName+"发来信息啦!!","ttt");
		var userItem="<li id=\"li2_"+userId+"\"><a href=\"javascript:void(0);\" id=\"userItem2_"+userId+"\" loginId=\""+loginUserId+"\" loginName=\""+loginUserName+"\" targetName=\""+targetName+"\" targetId=\""+targetId+"\">"+$("#userItem_"+userId).clone(true).html()+"</a></li>";
		$("#notifyuserList").append(userItem);
		effect.initCurrentUserItemClick("userItem2_",userId);
	},
	stillUserItem:function(userId){
		window.clearInterval(userShakeArray[userId]);
		$("li").remove("#li2_"+userId);
	},
	addUserGroup:function(group,userlistid){
		var divGroup="div_"+group.id;   //增加div
		//列出组名
		$("#"+userlistid).append("<div id="+divGroup+"><li id="+group.id+"><span>"+group.text+"</span></li></div>");
	},
	addUserItem:function(user,group,userlistid){
		var userId=user.id.split("_")[1];;
		var isOnline=user.isOnline;
		var userName=user.text;
		var groupView=$("#"+userlistid).find("#"+group.id);
		var userItemTemplate=$("#userItemTemplate").clone(true);		
		if(isOnline==1){
			userItemTemplate.find("#chatingDisplayName").html(userName).wrap("<font color='red'></font>");			
		}else{
			userItemTemplate.find("#chatingDisplayName").html(userName).wrap("<font color='gray'></font>");;
		}
		var userItemTemplateHTML=userItemTemplate.html();
		userItemTemplateHTML=userItemTemplateHTML.replace("[targetName]",userName);
		userItemTemplateHTML=userItemTemplateHTML.replace("[targetId]",userId);
		userItemTemplateHTML=userItemTemplateHTML.replace("[Id]",userId);
		userItemTemplateHTML=userItemTemplateHTML.replace("[isOnline]",isOnline);
		userItemTemplateHTML=userItemTemplateHTML.replace("[userId]",userId);
		userItemTemplateHTML=userItemTemplateHTML.replace("[groupId]",group.id);
		groupView.after(userItemTemplateHTML);
	},
	//初始化聊天窗口 
	initCurrentUserItemClick:function(prefixStr,targetId){
		var userItemId=prefixStr+targetId;
		$("#"+userItemId).attr("style","DISPLAY: block");
		$("#"+userItemId).click(function(even){
			effect.openChatWin(targetId);
		});		
	},
	//弹出对话窗口
	openChatWin:function(userId){
		var targetId=$("#userItem_"+userId).attr("targetId");
		var targetName=$("#userItem_"+userId).attr("targetName");
		var chatFrameSrc=basePath+"/talk/chat.jsp?loginUserId={loginUserId}&loginUserName={loginUserName}";
		chatFrameSrc+="&targetId={targetId}&targetName={targetName}&temp={temp}";
		chatFrameSrc=chatFrameSrc.replace("{temp}",Math.random());
		chatFrameSrc=chatFrameSrc.replace("{loginUserId}",loginUserId);
		chatFrameSrc=chatFrameSrc.replace("{loginUserName}",loginUserName);
		chatFrameSrc=chatFrameSrc.replace("{targetId}",targetId);
		chatFrameSrc=chatFrameSrc.replace("{targetName}",targetName);
		effect.addChat(loginUserId,targetId,targetName,encodeURI(chatFrameSrc));
	},
	init:function(){
		basePath=$("#basePath").val();
		loginUserId=$("#loginUserId").val();
		loginUserName=$("#loginUserName").val();
		$(".chatingpanel").talkPanel();
		$("#chatpanel").talkPanel(); //Run the talkPanel function on #chatpanel
		$("#customerChatpanel").talkPanel();
		$("#alertpanel").talkPanel(); //Run the talkPanel function on #alertpanel
		//Each time the viewport is adjusted/resized, execute the function
		$(window).resize(function () { 
			$("#chatpanel").talkPanel();
			$("#customerChatpanel").talkPanel();
			$("#alertpanel").talkPanel();
			$(".chatingpanel").talkPanel();
		});
		
		$("#chatpanel a:first,#customerChatpanel a:first, #alertpanel a:first , #chatingpanel a:first").click(function() { //If clicked on the first link of #chatpanel and #alertpanel...
			$(".subchatpanel").hide();
			if($(this).next(".subpanel").is(':visible')){ //If subpanel is already active...
				$(this).next(".subpanel").hide(); //Hide active subpanel
				$("#footpanel li a").removeClass('active'); //Remove active class on the subpanel trigger
			}
			else { //if subpanel is not active...
				$(".subpanel").hide(); //Hide all subpanels
				$(this).next(".subpanel").toggle(); //Toggle the subpanel to make active
				$("#footpanel li a").removeClass('active'); //Remove active class on all subpanel trigger
				$(this).toggleClass('active'); //Toggle the active class on the subpanel trigger
			}
			return false; //Prevent browser jump to link anchor
		});		
		//双击打开新消息窗口�
		$("#chatTitle").dblclick(function(){
			$(".subchatpanel").hide();
			$(".subpanel").hide(); 
			$(this).next(".subpanel").next(".subpanel").toggle(); 
			$("#footpanel li a").removeClass('active'); 
			$(this).toggleClass('active');
			return false; 
		});
		$(window.top.document).click(function(){
			effect.hideAll();
			}	
		);
		$(".menber").each(function(){
			var targetId=$(this).attr("targetId");
			effect.initCurrentUserItemClick("userItem_",targetId);    
		});
		//Click event outside of subpanel
		$(".min").click(function() { //Click anywhere and...
			$(this).find("#chatingTitle").css("background-color","#e3e2e2");
			$(".subpanel").hide(); //hide subpanel
			$(".subchatpanel").hide();
			$("#footpanel li a").removeClass('active'); //remove active class on subpanel trigger
		});
		$('.subpanel ul').click(function(e) { 
			e.stopPropagation(); //Prevents the subpanel ul from closing on click
		});
		//Delete icons on Alert Panel
		$("#alertpanel li").hover(function() {
			$(this).find("a.delete").css({'visibility': 'visible'}); //Show delete icon on hover
		},function() {
			$(this).find("a.delete").css({'visibility': 'hidden'}); //Hide delete icon on hover out
		});
	},
	//刷新用户列表
	refreshUserList:function(json){
			$(".menber").hide();   
			$.each(json,function(i,group){
				$.each(group.children,function(j,user){
					/*window.top.effect.addUserItem(user);*/
					var id=user.id.split("_")[1];
					if($("#userItem_"+id).length>0){
						var isOnline=user.isOnline;
						var strOnline=$("#userItem_"+id).attr("isOnline");
						if(isOnline==1){
							if(strOnline=='1'){
							$("#userItem_"+id).find("#chatingDisplayName").css("color","red").html(user.text);
							}else{
								$("#userItem_"+id).remove();
								effect.addUserItem(user,group,"userList");
							}							
						}
						$("#userItem_"+id).show();
					}else{
						effect.addUserItem(user,group,"userList");
					}
				});
			});			
	},
	refreshnotifyUser:function(notifyUser,isOnline){
		var userId = notifyUser.userId;
		var groupid=$("#userItem_"+userId).attr("groupId");
		var strOnline=isOnline;
		var notifyUserName=notifyUser.userInfo.realName;
		var useronlineshake="";
		var messagestr=notifyUserName+"上线了！";
		if(1==isOnline){
			$("#userItem_"+userId).find("#chatingDisplayName").css("color","red");
			var clonestr=$("#userItem_"+userId).clone(true);
			//把用户移到组的最前面
			$("#usergroup_"+userId).insertAfter("#"+groupid);
			//右下角显示提示框
			$.messager.anim('fade',1500);
			$.messager.show('<font color=red>IM用户上线提示</font>', messagestr);
		}else{
			$("#userItem_"+userId).find("#chatingDisplayName").css("color","gray");
			//把用户移到组的最后面
			$("#div_"+groupid).append($("#usergroup_"+userId));
		}
	}
};
var oldTitle=window.top.document.title;
var rollTitleShakeID=null;
function rollTitle(newTitle,notifyUser)
{
	if(rollTitleShakeID!=null){stopRollTitle()};
	var rollTitle=newTitle;
	rollTitleShakeID=setInterval(function(){
		window.top.document.title=rollTitle.substring(1,rollTitle.length)+rollTitle.substring(0,1);
		rollTitle=window.top.document.title.substring(0,rollTitle.length);
	}, 200);  
  window.top.document.onmousemove = function() { stopRollTitle(); };
  window.top.document.onkeydown = function() { stopRollTitle(); };

}
function stopRollTitle(){
  clearInterval(rollTitleShakeID);
  rollTitleShakeID = null;
  window.top.document.title = oldTitle;
  window.top.document.onmousemove = null;
  window.top.document.onkeydown = null;
}
