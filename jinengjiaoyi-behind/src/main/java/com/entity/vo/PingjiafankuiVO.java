package com.entity.vo;

import com.entity.PingjiafankuiEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 评价反馈
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public class PingjiafankuiVO  implements Serializable {
	private static final long serialVersionUID = 1L;

	 			
	/**
	 * 用户姓名
	 */
	
	private String yonghuxingming;
		
	/**
	 * 手机号
	 */
	
	private String shoujihao;
		
	/**
	 * 标题
	 */
	
	private String biaoti;
		
	/**
	 * 技能分类
	 */
	
	private String jinengfenlei;
		
	/**
	 * 回复图片
	 */
	
	private String huifutupian;
		
	/**
	 * 交换技能
	 */
	
	private String jiaohuanjineng;
		
	/**
	 * 评价时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date pingjiashijian;
		
	/**
	 * 交换备注
	 */
	
	private String jiaohuanbeizhu;
		
	/**
	 * 交换人账号
	 */
	
	private String jiaohuanrenzhanghao;
		
	/**
	 * 交换人姓名
	 */
	
	private String jiaohuanrenxingming;
		
	/**
	 * 联系方式
	 */
	
	private String lianxifangshi;
		
	/**
	 * 回复内容
	 */
	
	private String huifuneirong;
		
	/**
	 * 评价结果
	 */
	
	private String pingjiajieguo;
		
	/**
	 * 回复视频
	 */
	
	private String huifushipin;
		
	/**
	 * 回复附件
	 */
	
	private String huifufujian;
		
	/**
	 * 是否审核
	 */
	
	private String sfsh;
		
	/**
	 * 审核回复
	 */
	
	private String shhf;
				
	
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
	 * 设置：标题
	 */
	 
	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}
	
	/**
	 * 获取：标题
	 */
	public String getBiaoti() {
		return biaoti;
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
	 * 设置：回复图片
	 */
	 
	public void setHuifutupian(String huifutupian) {
		this.huifutupian = huifutupian;
	}
	
	/**
	 * 获取：回复图片
	 */
	public String getHuifutupian() {
		return huifutupian;
	}
				
	
	/**
	 * 设置：交换技能
	 */
	 
	public void setJiaohuanjineng(String jiaohuanjineng) {
		this.jiaohuanjineng = jiaohuanjineng;
	}
	
	/**
	 * 获取：交换技能
	 */
	public String getJiaohuanjineng() {
		return jiaohuanjineng;
	}
				
	
	/**
	 * 设置：评价时间
	 */
	 
	public void setPingjiashijian(Date pingjiashijian) {
		this.pingjiashijian = pingjiashijian;
	}
	
	/**
	 * 获取：评价时间
	 */
	public Date getPingjiashijian() {
		return pingjiashijian;
	}
				
	
	/**
	 * 设置：交换备注
	 */
	 
	public void setJiaohuanbeizhu(String jiaohuanbeizhu) {
		this.jiaohuanbeizhu = jiaohuanbeizhu;
	}
	
	/**
	 * 获取：交换备注
	 */
	public String getJiaohuanbeizhu() {
		return jiaohuanbeizhu;
	}
				
	
	/**
	 * 设置：交换人账号
	 */
	 
	public void setJiaohuanrenzhanghao(String jiaohuanrenzhanghao) {
		this.jiaohuanrenzhanghao = jiaohuanrenzhanghao;
	}
	
	/**
	 * 获取：交换人账号
	 */
	public String getJiaohuanrenzhanghao() {
		return jiaohuanrenzhanghao;
	}
				
	
	/**
	 * 设置：交换人姓名
	 */
	 
	public void setJiaohuanrenxingming(String jiaohuanrenxingming) {
		this.jiaohuanrenxingming = jiaohuanrenxingming;
	}
	
	/**
	 * 获取：交换人姓名
	 */
	public String getJiaohuanrenxingming() {
		return jiaohuanrenxingming;
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
	 * 设置：回复内容
	 */
	 
	public void setHuifuneirong(String huifuneirong) {
		this.huifuneirong = huifuneirong;
	}
	
	/**
	 * 获取：回复内容
	 */
	public String getHuifuneirong() {
		return huifuneirong;
	}
				
	
	/**
	 * 设置：评价结果
	 */
	 
	public void setPingjiajieguo(String pingjiajieguo) {
		this.pingjiajieguo = pingjiajieguo;
	}
	
	/**
	 * 获取：评价结果
	 */
	public String getPingjiajieguo() {
		return pingjiajieguo;
	}
				
	
	/**
	 * 设置：回复视频
	 */
	 
	public void setHuifushipin(String huifushipin) {
		this.huifushipin = huifushipin;
	}
	
	/**
	 * 获取：回复视频
	 */
	public String getHuifushipin() {
		return huifushipin;
	}
				
	
	/**
	 * 设置：回复附件
	 */
	 
	public void setHuifufujian(String huifufujian) {
		this.huifufujian = huifufujian;
	}
	
	/**
	 * 获取：回复附件
	 */
	public String getHuifufujian() {
		return huifufujian;
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
