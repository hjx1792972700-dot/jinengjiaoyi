package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 交换记录
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@TableName("jiaohuanjilu")
@Data
public class JiaohuanjiluEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public JiaohuanjiluEntity() {
		
	}
	
	public JiaohuanjiluEntity(T t) {
		try {
			BeanUtils.copyProperties(t, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 主键id
	 */
    @TableId(type = IdType.AUTO)
	private Long id;
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	 * 技能发布者提供的技能
	 */
	private String fabuzhetigong;

	/**
	 * 申请人提供的技能
	 */
	private String shenqingrentigong;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
