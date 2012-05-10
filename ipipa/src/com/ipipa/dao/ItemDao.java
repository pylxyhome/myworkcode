package com.ipipa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.ipipa.bean.Item;
import com.ipipa.utils.Jdbc;

/**
 * 
 * @description
 * @author panyl
 * @date 2012-5-10
 */
public class ItemDao {
	/**
	 * 批量
	 */
	private static int BatchAmount = 500;
	private static ItemDao itemDao=new ItemDao();

	private ItemDao() {
	}

	/**
	 * 获取一个单例
	 * 
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @return
	 */
	public static ItemDao get() {
		return itemDao;
	}
	/**
	 * 批量插入Item
	 * @description
	 * @author panyl
	 * @date 2012-5-10
	 * @param items
	 */
	public void insert(List<Item> items,String word) {
		Connection conn = null;
		PreparedStatement stmt=null;
		try {
			conn = Jdbc.getConnection();
			String sql = "insert into items (title,content,cid,nick,picUrl,detailUrl,price,numIid,cname,score,volume)values(?,?,?,?,?,?,?,?,?,?,?)";
			conn.setAutoCommit(false);
			stmt = (PreparedStatement) conn.prepareStatement(sql);
			for (int i = 0; i < items.size(); i++) {
				Item item = items.get(i);
				stmt.setString(1, item.getTitle());
				stmt.setString(2, item.getDesc()==null?item.getTitle():item.getDesc());
				stmt.setLong(3, item.getCid());
				stmt.setString(4, item.getNick());
				stmt.setString(5, item.getPicUrl());
				stmt.setString(6, item.getDetailUrl());
				stmt.setString(7, item.getPrice());
				stmt.setLong(8, item.getNumIid());
				stmt.setString(9, word);
				stmt.setLong(10, item.getScore());
				stmt.setLong(11, item.getVolume());
				if ((i + 1) %  BatchAmount == 0
						|| i == (items.size() - 1)) {
					stmt.executeBatch();
					conn.commit();
					stmt.clearBatch();
				}else {
					stmt.addBatch();
				}
			}
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
