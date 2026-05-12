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
 * 评价反馈
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@TableName("pingjiafankui")
@Data
public class PingjiafankuiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public PingjiafankuiEntity() {
		
	}
	
	public PingjiafankuiEntity(T t) {
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
