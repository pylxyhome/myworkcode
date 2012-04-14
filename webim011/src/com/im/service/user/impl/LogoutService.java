package com.im.service.user.impl;

import java.util.Iterator;

import javax.annotation.Resource;

import org.directwebremoting.ScriptSession;
import org.springframework.stereotype.Service;

import com.im.bean.constant.UserStatus;
import com.im.bean.user.User;
import com.im.service.dwr.ScriptSessionService;
import com.im.service.user.ILogoutService;
import com.im.service.user.IUserService;
import com.im.util.IMLog;

@Service
public class LogoutService extends ScriptSessionService implements
		ILogoutService {
	@Resource
	private IUserService userService;

	//@Override
	public void exit(int userid) {
		ScriptSession session = super.getScriptSessionByID(userid);
		if (session != null) {
			IMLog.info("用户" + userid + "退出");
			session.removeAttribute("userid");
			session.invalidate();
		}
		removeUser(userid); // 移除用户
		super.refreshAllUser(null, userService.find(userid),
						UserStatus.OFFLINE);
	}

	private void removeUser(int userId) {
//		for (Integer mapUserId : mapUsers.keySet()) {
//			if (mapUserId.equals(userId)) {
//				mapUsers.remove(mapUserId);
//				logger.debug(mapUserId + "用户退出");
//				break;
//			}
//		}
		mapUsers.remove(userId);
		IMLog.info("mapUsers大小："+mapUsers.size());
	}

}
