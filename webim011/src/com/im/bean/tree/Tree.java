package com.im.bean.tree;

import java.util.List;
/**
 * 
 * @author ylgw
 * 自定义树
 */
public class Tree {
	
	private String id;
	/**所属父级树**/
	private Tree tree;
	/**内容描述**/
    private String text;
    /**是否是叶子节点**/
    private boolean leaf;
    /**头像**/
    private String img;
    /**用户电话**/
    private String phone;
    /**所有子级树**/
    private List<Tree> children;
    /**是否在线 1在线、0不在线**/
    private int isOnline=0;
    /**是否是所在组的最后一个 0否，1是**/
    private int isLast=0;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Tree getTree() {
		return tree;
	}
	public void setTree(Tree tree) {
		this.tree = tree;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public List<Tree> getChildren() {
		return children;
	}
	public void setChildren(List<Tree> children) {
		this.children = children;
	}
	public int getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(int isOnline) {
		this.isOnline = isOnline;
	}
	public int getIsLast() {
		return isLast;
	}
	public void setIsLast(int isLast) {
		this.isLast = isLast;
	}
	
}
