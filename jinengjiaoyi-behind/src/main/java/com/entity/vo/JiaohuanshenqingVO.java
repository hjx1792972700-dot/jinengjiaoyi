package com.entity.vo;

import com.entity.JiaohuanshenqingEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 交换申请
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public class JiaohuanshenqingVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
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
	 * 需求标题
	 */
	
	private String xuqiubiaoti;
		
	/**
	 * 技能分类
	 */
	
	private String jinengfenlei;
		
	/**
	 * 需求费用
	 */
	
	private Double xuqiufeiyong;
		
	/**
	 * 封面
	 */
	
	private String fengmian;
		
	/**
	 * 申请时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date shenqingshijian;
		
	/**
	 * 申请备注
	 */
	
	private String shenqingbeizhu;
		
	/**
	 * 供给人账号
	 */
	
	private String gongjirenzhanghao;
		
	/**
	 * 供给人姓名
	 */
	
	private String gongjirenxingming;
		
	/**
	 * 联系方式
	 */
	
	private String lianxifangshi;
		
	/**
	 * 信誉
	 */
	
	private String xinyu;
		
	/**
	 * 是否审核
	 */
	
	private String sfsh;
		
	/**
	 * 审核回复
	 */
	
	private String shhf;
				
	
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
	 * 设置：申请时间
	 */
	 
	public void setShenqingshijian(Date shenqingshijian) {
		this.shenqingshijian = shenqingshijian;
	}
	
	/**
	 * 获取：申请时间
	 */
	public Date getShenqingshijian() {
		return shenqingshijian;
	}
				
	
	/**
	 * 设置：申请备注
	 */
	 
	public void setShenqingbeizhu(String shenqingbeizhu) {
		this.shenqingbeizhu = shenqingbeizhu;
	}
	
	/**
	 * 获取：申请备注
	 */
	public String getShenqingbeizhu() {
		return shenqingbeizhu;
	}
				
	
	/**
	 * 设置：供给人账号
	 */
	 
	public void setGongjirenzhanghao(String gongjirenzhanghao) {
		this.gongjirenzhanghao = gongjirenzhanghao;
	}
	
	/**
	 * 获取：供给人账号
	 */
	public String getGongjirenzhanghao() {
		return gongjirenzhanghao;
	}
				
	
	/**
	 * 设置：供给人姓名
	 */
	 
	public void setGongjirenxingming(String gongjirenxingming) {
		this.gongjirenxingming = gongjirenxingming;
	}
	
	/**
	 * 获取：供给人姓名
	 */
	public String getGongjirenxingming() {
		return gongjirenxingming;
	}
				
	
	/**
	 * 设置：联系方式
	 */
	 
	public void setLianxifangshi(String lianxifangshi) {
		this.lianxifangshi = lianxifangshi;
	}
	
	/**
	 * 获取：联系方式
	 */
	public String getLianxifangshi() {
		return lianxifangshi;
	}
				
	
	/**
	 * 设置：信誉
	 */
	 
	public void setXinyu(String xinyu) {
		this.xinyu = xinyu;
	}
	
	/**
	 * 获取：信誉
	 */
	public String getXinyu() {
		return xinyu;
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
			
}
