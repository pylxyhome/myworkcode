package com.im.task;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.im.service.dwr.ScriptSessionService;
import com.im.service.user.ICheckTimeOutService;
import com.im.service.user.ILogoutService;
/**
 * 
 * @author pyl
 *
 */
public class CheckOnlineUserJob extends ScriptSessionService{
	@Resource
	ILogoutService logoutService;
	@Resource
	ICheckTimeOutService checkTimeOutService;
	public CheckOnlineUserJob(){}
	public  void execute(){
		long starttime = System.currentTimeMillis();
		 {
		try {	
				HashSet<Integer> onlineUserSet=checkTimeOutService.getOnlineUser();
				Set<Integer> lastOnlineUserSet=new HashSet<Integer>(mapUsers.keySet());
				lastOnlineUserSet.removeAll(onlineUserSet);
				for(Integer outUserId : lastOnlineUserSet){
					System.out.println(outUserId+"---不存在回调");
					logoutService.exit(outUserId);
				}
				long endtime = System.currentTimeMillis();
				System.out.println("检测运行时间:"+(endtime-starttime)+"毫秒");
				System.out.println("在线用户："+onlineUserSet.toString());
				checkTimeOutService.CheckTimeOutInit();
				checkTimeOutService.pushCheckTimeOut();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args){
		Map<Integer,Object> mapUser = new ConcurrentHashMap<Integer,Object>();
		mapUser.put(1, new String());
		mapUser.put(2, new String());
		mapUser.put(3, new String());
		mapUser.put(4, new String());
		Set<Integer> onlineUserSet=new HashSet<Integer>();
		onlineUserSet.add(3);
		onlineUserSet.add(4);
		onlineUserSet.add(5);
		Set<Integer> lastOnlineUserSet=new HashSet<Integer>(mapUser.keySet());
		lastOnlineUserSet.removeAll(onlineUserSet);
		for(Integer out : lastOnlineUserSet){
			System.out.println(out);
		}
		System.out.println("mapUser大小："+mapUser.size());
	}
}
