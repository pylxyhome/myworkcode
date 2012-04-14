package com.im.service.user.impl;

import java.util.LinkedList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.ScriptSession;
import org.springframework.stereotype.Service;

import com.im.bean.constant.UserStatus;
import com.im.bean.user.User;
import com.im.service.dwr.ScriptSessionService;
import com.im.service.user.ILoginService;
import com.im.service.user.IUserService;
import com.im.util.IMLog;

@Service
public class LoginService extends ScriptSessionService implements ILoginService {
	@Resource
	private IUserService userService;
	//保存当前在线用户列表
	public static LinkedList<User> users = new LinkedList<User>();
	public synchronized User login(String username, HttpServletRequest request,
			HttpServletResponse response) {
		synchronized(this){
			IMLog.info("====调用LoginService.login方法====");
			User loginUser = userService.findUserByUsername(username);
			initLogin(loginUser, request);
			return loginUser;
		}
	}

	private void initLogin(User loginUser, HttpServletRequest request) {
		boolean isExist=false;
		//检测在线用户列表中是否存在用户
//		for (int i = 0; i < users.size(); i++) {
//			if(users.get(i).getUserId().equals(loginUser.getUserId())){
//				IMLog.info("删除用户:"+loginUser.getUserInfo().getRealName());
//				ScriptSession onLineUserSession=getScriptSessionByID(loginUser.getUserId(),request);
//				if(onLineUserSession!=null){
//					IMLog.info("注销用户:"+loginUser.getUserInfo().getRealName());
//					onLineUserSession.invalidate();
//					isExist=true;        
//					break;
//				}    
//			}
//		}
		for(Integer userid : mapUsers.keySet()){
			if(userid.equals(loginUser.getUserId())){
				IMLog.info("删除用户:"+loginUser.getUserInfo().getRealName());
				ScriptSession onLineUserSession=getScriptSessionByID(loginUser.getUserId(),request);
				if(onLineUserSession!=null){
					IMLog.info("注销用户:"+loginUser.getUserInfo().getRealName());
					onLineUserSession.invalidate();
					isExist=true;        
					break;
				}  
			}
		}
		//添加在线用户
		if(!isExist){
			//添加在线用户
		//	users.add(loginUser);
			mapUsers.put(loginUser.getUserId(), loginUser);
		}
		//mapUsers.put(loginUser.getUserId(), loginUser);
		super.setScriptSessionFlag(loginUser.getUserId(),loginUser.getUsername());
		//上线通知所有用户
		super.refreshAllUser(request, loginUser, UserStatus.ONLINE);
		IMLog.info("用户"+loginUser.getUserInfo().getRealName()+"登陆");
	}

}
