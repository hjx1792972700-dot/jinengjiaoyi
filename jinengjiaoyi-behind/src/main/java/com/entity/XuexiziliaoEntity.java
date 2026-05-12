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
 * 学习资料
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@TableName("xuexiziliao")
@Data
public class XuexiziliaoEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public XuexiziliaoEntity() {
		
	}
	
	public XuexiziliaoEntity(T t) {
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
	 * 资料标题
	 */
	private String ziliaobiaoti;
	
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	 * 资料价格(元)
	 */
	private Integer jiage;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
