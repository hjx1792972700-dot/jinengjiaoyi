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
 * 交换申请
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@TableName("jiaohuanshenqing")
@Data
public class JiaohuanshenqingEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public JiaohuanshenqingEntity() {
		
	}
	
	public JiaohuanshenqingEntity(T t) {
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
	 * 需求编号
	 */
	private String xuqiubianhao;
	
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
	 * 被选中的技能ID（技能发布者从申请人的技能中选择）
	 */
	private Long xuanzejinengid;

	/**
	 * 被选中的技能标题
	 */
	private String xuanzejinengbiaoti;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
