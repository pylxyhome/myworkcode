//设置焦点
function setFocus(cmpId){
	var cmp = Ext.getCmp(cmpId);
	if(cmp){
		cmp.focus.defer(100, cmp);
	}
}

//打开窗口
function openWin(url,winFlag,width,height){
	var encodeUrl=encodeURI(url);
	var config='height='+height+',width='+width+',top=200,left=300,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no';
	return window.open (encodeUrl,winFlag,config);
}
//检测连线情况
function checkTimeOut(){
	CheckTimeOutService.checkTimeOutCallBack(loginUserId);
}

//屏蔽F5刷新ie
document.onkeydown = function () {
	if (event.keyCode == 116) {
		event.keyCode = 0;
		event.returnValue = false;
	}
};

//屏蔽右键
//document.oncontextmenu = function () {
//	event.returnValue = false;
//};



//声音提示
/*
*@param sndAction 'stop/play'
*@param sndObj 'document.msg'
*Example:controlSound('play','document.msg');
**/
function controlSound(sndAction,sndObj)
{ 　
  if(eval(sndObj) != null)
  { 　　
    if(navigator.appName=='Netscape')
    {
    　eval( sndObj+ ( (sndAction=='stop') ? '.stop()' : '.play(false)' ) ); 　　
    }
    else if(eval(sndObj+".FileName")) 　
 {
   eval(sndObj+((sndAction=='stop')?'.stop()':'.play()')); 　
    }
  }
}

//显示信息
function alertMessage(title,center,time){
if(time==null){
	time=2000;
}
	Ext.MessageBox.show({
			title:title,
           msg: center,
           buttons: Ext.MessageBox.OK
       });
        setTimeout(function(){
            Ext.MessageBox.hide();
        }, time);
}

/*
function killErrors() { 
	return true; 
} 
window.onerror = killErrors;
*/ 