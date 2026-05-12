package com.entity.vo;

import com.entity.XuexiziliaoEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 学习资料
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
public class XuexiziliaoVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 资料类型
	 */
	
	private String ziliaoleixing;
		
	/**
	 * 资料封面
	 */
	
	private String ziliaofengmian;
		
	/**
	 * 资料视频
	 */
	
	private String ziliaoshipin;
		
	/**
	 * 资料附件
	 */
	
	private String ziliaofujian;
		
	/**
	 * 资料详情
	 */
	
	private String ziliaoxiangqing;
		
	/**
	 * 发布时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date fabushijian;
		
	/**
	 * 最近点击时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date clicktime;
		
	/**
	 * 点击次数
	 */
	
	private Integer clicknum;
		
	/**
	 * 评论数
	 */
	
	private Integer discussnum;
		
	/**
	 * 收藏数
	 */
	
	private Integer storeupnum;
				
	
	/**
	 * 设置：资料类型
	 */
	 
	public void setZiliaoleixing(String ziliaoleixing) {
		this.ziliaoleixing = ziliaoleixing;
	}
	
	/**
	 * 获取：资料类型
	 */
	public String getZiliaoleixing() {
		return ziliaoleixing;
	}
				
	
	/**
	 * 设置：资料封面
	 */
	 
	public void setZiliaofengmian(String ziliaofengmian) {
		this.ziliaofengmian = ziliaofengmian;
	}
	
	/**
	 * 获取：资料封面
	 */
	public String getZiliaofengmian() {
		return ziliaofengmian;
	}
				
	
	/**
	 * 设置：资料视频
	 */
	 
	public void setZiliaoshipin(String ziliaoshipin) {
		this.ziliaoshipin = ziliaoshipin;
	}
	
	/**
	 * 获取：资料视频
	 */
	public String getZiliaoshipin() {
		return ziliaoshipin;
	}
				
	
	/**
	 * 设置：资料附件
	 */
	 
	public void setZiliaofujian(String ziliaofujian) {
		this.ziliaofujian = ziliaofujian;
	}
	
	/**
	 * 获取：资料附件
	 */
	public String getZiliaofujian() {
		return ziliaofujian;
	}
				
	
	/**
	 * 设置：资料详情
	 */
	 
	public void setZiliaoxiangqing(String ziliaoxiangqing) {
		this.ziliaoxiangqing = ziliaoxiangqing;
	}
	
	/**
	 * 获取：资料详情
	 */
	public String getZiliaoxiangqing() {
		return ziliaoxiangqing;
	}
				
	
	/**
	 * 设置：发布时间
	 */
	 
	public void setFabushijian(Date fabushijian) {
		this.fabushijian = fabushijian;
	}
	
	/**
	 * 获取：发布时间
	 */
	public Date getFabushijian() {
		return fabushijian;
	}
				
	
	/**
	 * 设置：最近点击时间
	 */
	 
	public void setClicktime(Date clicktime) {
		this.clicktime = clicktime;
	}
	
	/**
	 * 获取：最近点击时间
	 */
	public Date getClicktime() {
		return clicktime;
	}
				
	
	/**
	 * 设置：点击次数
	 */
	 
	public void setClicknum(Integer clicknum) {
		this.clicknum = clicknum;
	}
	
	/**
	 * 获取：点击次数
	 */
	public Integer getClicknum() {
		return clicknum;
	}
				
	
	/**
	 * 设置：评论数
	 */
	 
	public void setDiscussnum(Integer discussnum) {
		this.discussnum = discussnum;
	}
	
	/**
	 * 获取：评论数
	 */
	public Integer getDiscussnum() {
		return discussnum;
	}
				
	
	/**
	 * 设置：收藏数
	 */
	 
	public void setStoreupnum(Integer storeupnum) {
		this.storeupnum = storeupnum;
	}
	
	/**
	 * 获取：收藏数
	 */
	public Integer getStoreupnum() {
		return storeupnum;
	}
			
}
