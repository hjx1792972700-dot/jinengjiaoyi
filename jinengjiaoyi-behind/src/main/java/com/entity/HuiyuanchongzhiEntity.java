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
 * 会员充值
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@TableName("huiyuanchongzhi")
@Data
public class HuiyuanchongzhiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public HuiyuanchongzhiEntity() {
		
	}
	
	public HuiyuanchongzhiEntity(T t) {
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
	 * 充值编号
	 */
	private String chongzhibianhao;
	
	/**
	 * 图片
	 */
	private String tupian;
	
	/**
	 * 充值金额
	 */
	private Integer chongzhijine;
	
	/**
	 * 充值日期
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	@DateTimeFormat
	private Date chongzhiriqi;
	
	/**
	 * 用户账号
	 */
	private String yonghuzhanghao;
	
	/**
	 * 用户姓名
	 */
	private String yonghuxingming;
	
	/**
	 * 订单状态
	 */
	private String zhuangtai;

	/**
	 * 套餐类型
	 */
	private String taocan;

	/**
	 * 支付方式
	 */
	private String zhifufangshi;

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
