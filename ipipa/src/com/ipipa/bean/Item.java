package com.ipipa.bean;

import java.util.Date;
import java.util.List;

import com.taobao.api.domain.ItemImg;
import com.taobao.api.domain.Location;
import com.taobao.api.domain.PropImg;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

/**
 * 商品
 * @description
 * @author panyl
 * @date 2012-5-9
 */
public class Item {
	
	private long id;
	/**
	 * 商品上传后的状态。onsale出售中，instock库中
	 */
	@ApiField("approve_status")
	private String approveStatus;
	
	/**
	 * 商城返点比例，为5的倍数，最低0.5%
	 */
	@ApiField("auction_point")
	private Long auctionPoint;

	/**
	 * 商品所属的叶子类目 id
	 */
	@ApiField("cid")
	private Long cid;

	/**
	 * 下架时间（格式：yyyy-MM-dd HH:mm:ss）
	 */
	@ApiField("delist_time")
	private Date delistTime;

	/**
	 * 商品描述, 字数要大于5个字符，小于25000个字符
	 */
	@ApiField("desc")
	private String desc;

	/**
	 * 商品url
	 */
	@ApiField("detail_url")
	private String detailUrl;

	/**
	 * 标示商品是否为新品。
值含义：true-是，false-否。
	 */
	@ApiField("is_xinpin")
	private Boolean isXinpin;

	/**
	 * 商品图片列表(包括主图)。fields中只设置item_img可以返回ItemImg结构体中所有字段，如果设置为item_img.id、item_img.url、item_img.position等形式就只会返回相应的字段
	 */
	@ApiListField("item_imgs")
	@ApiField("item_img")
	private List<ItemImg> itemImgs;

	/**
	 * 商品所在地
	 */
	@ApiField("location")
	private Location location;

	/**
	 * 卖家昵称
	 */
	@ApiField("nick")
	private String nick;

	/**
	 * 商品数量
	 */
	@ApiField("num")
	private Long num;

	/**
	 * 商品数字id
	 */
	@ApiField("num_iid")
	private Long numIid;

	/**
	 * 是否淘1站商品
	 */
	@ApiField("one_station")
	private Boolean oneStation;

	/**
	 * 商品主图片地址
	 */
	@ApiField("pic_url")
	private String picUrl;


	/**
	 * 商品价格，格式：5.00；单位：元；精确到：分
	 */
	@ApiField("price")
	private String price;

	/**
	 * 商品属性图片列表。fields中只设置prop_img可以返回PropImg结构体中所有字段，如果设置为prop_img.id、prop_img.url、prop_img.properties、prop_img.position等形式就只会返回相应的字段
	 */
	@ApiListField("prop_imgs")
	@ApiField("prop_img")
	private List<PropImg> propImgs;

	/**
	 * 商品所属卖家的信用等级数，1表示1心，2表示2心……，只有调用商品搜索:taobao.items.get和taobao.items.search的时候才能返回
	 */
	@ApiField("score")
	private Long score;

	/**
	 * 商品新旧程度(全新:new，闲置:unused，二手：second)
	 */
	@ApiField("stuff_status")
	private String stuffStatus;

	/**
	 * 商品标题,不能超过60字节
	 */
	@ApiField("title")
	private String title;

	/**
	 * 对应搜索商品列表页的最近成交量,只有调用商品搜索:taobao.items.get和taobao.items.search的时候才能返回
	 */
	@ApiField("volume")
	private Long volume;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public Long getAuctionPoint() {
		return auctionPoint;
	}

	public void setAuctionPoint(Long auctionPoint) {
		this.auctionPoint = auctionPoint;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Date getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(Date delistTime) {
		this.delistTime = delistTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public Boolean getIsXinpin() {
		return isXinpin;
	}

	public void setIsXinpin(Boolean isXinpin) {
		this.isXinpin = isXinpin;
	}

	public List<ItemImg> getItemImgs() {
		return itemImgs;
	}

	public void setItemImgs(List<ItemImg> itemImgs) {
		this.itemImgs = itemImgs;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public Boolean getOneStation() {
		return oneStation;
	}

	public void setOneStation(Boolean oneStation) {
		this.oneStation = oneStation;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<PropImg> getPropImgs() {
		return propImgs;
	}

	public void setPropImgs(List<PropImg> propImgs) {
		this.propImgs = propImgs;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public String getStuffStatus() {
		return stuffStatus;
	}

	public void setStuffStatus(String stuffStatus) {
		this.stuffStatus = stuffStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}
}
