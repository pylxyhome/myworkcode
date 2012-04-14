package com.im.service.tree.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;

import com.im.bean.constant.UserStatus;
import com.im.bean.group.Group;
import com.im.bean.tree.Tree;
import com.im.bean.user.User;
import com.im.bean.user.UserGroup;
import com.im.service.group.IGroupService;
import com.im.service.tree.IGroupUserTreeService;
import com.im.service.user.impl.LoginService;
import com.im.service.usergroup.IUserGroupService;
import com.im.util.IMLog;
import com.im.util.SortList;


@Service 
public class GroupUserTreeService implements IGroupUserTreeService{
	@Resource
	private IGroupService groupService;
	@Resource
	private IUserGroupService userGroupService;
	
	public String getGroupTree(HttpServletRequest request) {
		JSONArray jsonObject = JSONArray.fromObject(getGroupUserTreeList());
		IMLog.info(jsonObject.toString());
		return jsonObject.toString();
	}
	
	public List<Tree> getGroupUserTreeList(){ 
		List<Group> groups=groupService.findAll();
		List<Tree> usergroupsTree=new ArrayList<Tree>();
		for (Group group : groups) {
			List<Tree> usersTree=new ArrayList<Tree>();
			//添加分组
			Tree grouptree=new Tree();
			grouptree.setText(group.getGroupName());
			grouptree.setLeaf(false);
			grouptree.setId("group_"+group.getGroupId());
			List<UserGroup> userGroups=userGroupService.findListByGroupId(group.getGroupId());
			for(UserGroup usergroup : userGroups){
				User user = usergroup.getUser();
				Tree userTree=new Tree();
				userTree.setLeaf(true);
				userTree.setText(user.getUserInfo().getRealName());      
				userTree.setId("user_"+user.getUserId());
				userTree.setImg(user.getUserInfo().getHeadpath());
				userTree.setPhone(user.getUserInfo().getPhone());
				//Integer online=0; 
				//判断在线列表是否存在该用户
//				for(User onlineUser : LoginService.users){
//					if(user.getUsername().equals(onlineUser.getUsername())){
//						online=1;
//						break;
//					}
//				}
				if(LoginService.mapUsers.get(user.getUserId())!=null){
					userTree.setIsOnline(UserStatus.ONLINE);
				}
				
				usersTree.add(userTree);
//				System.out.println("-------------排序前---------------");
//				for(Tree  t : usersTree){
//					System.out.println(t.getIsOnline());
//				}
				//对usersTree排序
				SortList<Tree> sortList = new SortList<Tree>();
				sortList.Sort(usersTree, "getIsOnline", "desc");
//				System.out.println("-------------排序后---------------");
//				for(Tree  t : usersTree){
//					System.out.println(t.getIsOnline());
//				}
				
			}
			 
			grouptree.setChildren(usersTree);
			usergroupsTree.add(grouptree);
		}
		
		return usergroupsTree;
	}
}
