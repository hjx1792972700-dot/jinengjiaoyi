package com.entity.model;

import com.entity.JiaohuanjiluEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
 

/**
 * 交换记录
 * 接收传参的实体类  
 *（实际开发中配合移动端接口开发手动去掉些没用的字段， 后端一般用entity就够用了） 
 * 取自ModelAndView 的model名称
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public class JiaohuanjiluModel  implements Serializable {
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
	 * 供给图片
	 */
	
	private String gongjitupian;
		
	/**
	 * 交换技能
	 */
	
	private String jiaohuanjineng;
		
	/**
	 * 供给视频
	 */
	
	private String gongjishipin;
		
	/**
	 * 供给附件
	 */
	
	private String gongjifujian;
		
	/**
	 * 交换时间
	 */
		
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 
	private Date jiaohuanshijian;
		
	/**
	 * 供给内容
	 */
	
	private String gongjineirong;
		
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
	 * 跨表用户id
	 */
	
	private Long crossuserid;
		
	/**
	 * 跨表主键id
	 */
	
	private Long crossrefid;
				
	
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
	 * 设置：供给图片
	 */
	 
	public void setGongjitupian(String gongjitupian) {
		this.gongjitupian = gongjitupian;
	}
	
	/**
	 * 获取：供给图片
	 */
	public String getGongjitupian() {
		return gongjitupian;
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
	 * 设置：供给视频
	 */
	 
	public void setGongjishipin(String gongjishipin) {
		this.gongjishipin = gongjishipin;
	}
	
	/**
	 * 获取：供给视频
	 */
	public String getGongjishipin() {
		return gongjishipin;
	}
				
	
	/**
	 * 设置：供给附件
	 */
	 
	public void setGongjifujian(String gongjifujian) {
		this.gongjifujian = gongjifujian;
	}
	
	/**
	 * 获取：供给附件
	 */
	public String getGongjifujian() {
		return gongjifujian;
	}
				
	
	/**
	 * 设置：交换时间
	 */
	 
	public void setJiaohuanshijian(Date jiaohuanshijian) {
		this.jiaohuanshijian = jiaohuanshijian;
	}
	
	/**
	 * 获取：交换时间
	 */
	public Date getJiaohuanshijian() {
		return jiaohuanshijian;
	}
				
	
	/**
	 * 设置：供给内容
	 */
	 
	public void setGongjineirong(String gongjineirong) {
		this.gongjineirong = gongjineirong;
	}
	
	/**
	 * 获取：供给内容
	 */
	public String getGongjineirong() {
		return gongjineirong;
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
	 * 设置：跨表用户id
	 */
	 
	public void setCrossuserid(Long crossuserid) {
		this.crossuserid = crossuserid;
	}
	
	/**
	 * 获取：跨表用户id
	 */
	public Long getCrossuserid() {
		return crossuserid;
	}
				
	
	/**
	 * 设置：跨表主键id
	 */
	 
	public void setCrossrefid(Long crossrefid) {
		this.crossrefid = crossrefid;
	}
	
	/**
	 * 获取：跨表主键id
	 */
	public Long getCrossrefid() {
		return crossrefid;
	}
			
}
