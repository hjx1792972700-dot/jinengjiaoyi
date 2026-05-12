package com.entity.vo;

import com.entity.JinengxuqiuEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 技能需求
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public class JinengxuqiuVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 需求标题
	 */
	
	private String xuqiubiaoti;
		
	/**
	 * 技能分类
	 */
	
	private String jinengfenlei;
		
	/**
	 * 封面
	 */
	
	private String fengmian;
		
	/**
	 * 发布时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date fabushijian;
		
	/**
	 * 需求费用
	 */
	
	private Double xuqiufeiyong;
		
	/**
	 * 需求描述
	 */
	
	private String xuqiumiaoshu;
		
	/**
	 * 需求详情
	 */
	
	private String xuqiuxiangqing;
		
	/**
	 * 用户账号
	 */
	
	private String yonghuzhanghao;
		
	/**
	 * 用户姓名
	 */
	
	private String yonghuxingming;
		
	/**
	 * 手机号
	 */
	
	private String shoujihao;
		
	/**
	 * 信誉指数
	 */
	
	private String xinyuzhishu;
		
	/**
	 * 是否审核
	 */
	
	private String sfsh;
		
	/**
	 * 审核回复
	 */
	
	private String shhf;
		
	/**
	 * 赞
	 */
	
	private Integer thumbsupnum;
		
	/**
	 * 踩
	 */
	
	private Integer crazilynum;
		
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
	 * 评分
	 */
	
	private Double totalscore;
		
	/**
	 * 收藏数
	 */
	
	private Integer storeupnum;
				
	
	/**
	 * 设置：需求标题
	 */
	 
	public void setXuqiubiaoti(String xuqiubiaoti) {
		this.xuqiubiaoti = xuqiubiaoti;
	}
	
	/**
	 * 获取：需求标题
	 */
	public String getXuqiubiaoti() {
		return xuqiubiaoti;
	}
				
	
	/**
	 * 设置：技能分类
	 */
	 
	public void setJinengfenlei(String jinengfenlei) {
		this.jinengfenlei = jinengfenlei;
	}
	
	/**
	 * 获取：技能分类
	 */
	public String getJinengfenlei() {
		return jinengfenlei;
	}
				
	
	/**
	 * 设置：封面
	 */
	 
	public void setFengmian(String fengmian) {
		this.fengmian = fengmian;
	}
	
	/**
	 * 获取：封面
	 */
	public String getFengmian() {
		return fengmian;
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
	 * 设置：需求费用
	 */
	 
	public void setXuqiufeiyong(Double xuqiufeiyong) {
		this.xuqiufeiyong = xuqiufeiyong;
	}
	
	/**
	 * 获取：需求费用
	 */
	public Double getXuqiufeiyong() {
		return xuqiufeiyong;
	}
				

	/**
	 * 设置：需求描述
	 */
	 
	public void setXuqiumiaoshu(String xuqiumiaoshu) {
		this.xuqiumiaoshu = xuqiumiaoshu;
	}
	
	/**
	 * 获取：需求描述
	 */
	public String getXuqiumiaoshu() {
		return xuqiumiaoshu;
	}
				
	
	/**
	 * 设置：需求详情
	 */
	 
	public void setXuqiuxiangqing(String xuqiuxiangqing) {
		this.xuqiuxiangqing = xuqiuxiangqing;
	}
	
	/**
	 * 获取：需求详情
	 */
	public String getXuqiuxiangqing() {
		return xuqiuxiangqing;
	}
				
	
	/**
	 * 设置：用户账号
	 */
	 
	public void setYonghuzhanghao(String yonghuzhanghao) {
		this.yonghuzhanghao = yonghuzhanghao;
	}
	
	/**
	 * 获取：用户账号
	 */
	public String getYonghuzhanghao() {
		return yonghuzhanghao;
	}
				
	
	/**
	 * 设置：用户姓名
	 */
	 
	public void setYonghuxingming(String yonghuxingming) {
		this.yonghuxingming = yonghuxingming;
	}
	
	/**
	 * 获取：用户姓名
	 */
	public String getYonghuxingming() {
		return yonghuxingming;
	}
				
	
	/**
	 * 设置：手机号
	 */
	 
	public void setShoujihao(String shoujihao) {
		this.shoujihao = shoujihao;
	}
	
	/**
	 * 获取：手机号
	 */
	public String getShoujihao() {
		return shoujihao;
	}
				
	
	/**
	 * 设置：信誉指数
	 */
	 
	public void setXinyuzhishu(String xinyuzhishu) {
		this.xinyuzhishu = xinyuzhishu;
	}
	
	/**
	 * 获取：信誉指数
	 */
	public String getXinyuzhishu() {
		return xinyuzhishu;
	}
				
	
	/**
	 * 设置：是否审核
	 */
	 
	public void setSfsh(String sfsh) {
		this.sfsh = sfsh;
	}
	
	/**
	 * 获取：是否审核
	 */
	public String getSfsh() {
		return sfsh;
	}
				
	
	/**
	 * 设置：审核回复
	 */
	 
	public void setShhf(String shhf) {
		this.shhf = shhf;
	}
	
	/**
	 * 获取：审核回复
	 */
	public String getShhf() {
		return shhf;
	}
				
	
	/**
	 * 设置：赞
	 */
	 
	public void setThumbsupnum(Integer thumbsupnum) {
		this.thumbsupnum = thumbsupnum;
	}
	
	/**
	 * 获取：赞
	 */
	public Integer getThumbsupnum() {
		return thumbsupnum;
	}
				
	
	/**
	 * 设置：踩
	 */
	 
	public void setCrazilynum(Integer crazilynum) {
		this.crazilynum = crazilynum;
	}
	
	/**
	 * 获取：踩
	 */
	public Integer getCrazilynum() {
		return crazilynum;
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
	 * 设置：评分
	 */
	 
	public void setTotalscore(Double totalscore) {
		this.totalscore = totalscore;
	}
	
	/**
	 * 获取：评分
	 */
	public Double getTotalscore() {
		return totalscore;
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
