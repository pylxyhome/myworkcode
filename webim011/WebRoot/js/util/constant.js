	//IM常量
	var basePath="";
	//IM登录后的用户id
	var loginUserId;
	//IM登录后的用户帐户变量
	var loginUserAccount;
	//IM登录后的用户名变量 
	var loginUserName;
	//IM登录后的用户密码变量
	var loginUserPassword;
	//对方id
	var targetId;
	var targetName;
	var snsUrl="http://www.360qzol.com/im/postMsg.action";
	//IM宽度设定
	var imWidth=250;

	//对话传递临时Map变量
	var msgList = new Array();
	//信息传递临时Map变量
	var msgMap = new Object();
	//信息状态:发送请求添加好友状态 
	var MSG_STATE_ADD_FRIEND=0;
	 //信息状态:回复添加好友状态(成功)
	var MSG_STATE_ADD_FRIEND_CALLBACK_TRUE=1;
	 //信息状态:回复添加好友状态(失败)
	var MSG_STATE_ADD_FRIEND_CALLBACK_FALSE=2;

	//信息保存变量
	var sysMsgList=new Array();
	//信息传递临时Map变量
	var sysMsgMap = new Object();
	
	//信息闪动临时变量
	var shakeMsgIconInterval;
	//信息静止临时变量
	var stillMsgIconIntercal;
	//用户组分栏闪动临时变量
	var shakeUserToolInterval;
	//用户组分栏静止临时变量
	var stillUserToolInterval;
	//群组分栏闪动临时变量
	var shakeRoomToolInterval;
	//群组分栏静止临时变量
	var stillRoomToolInterval;
	//团体分栏闪动临时变量
	var shakeSysGroupToolInterval;
	//团体分栏静止临时变量
	var stillSysGroupToolInterval;
