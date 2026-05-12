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
 * 技能需求
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@TableName("jinengxuqiu")
@Data
public class JinengxuqiuEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public JinengxuqiuEntity() {
		
	}
	
	public JinengxuqiuEntity(T t) {
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
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
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
	 * 类型（需求/技能）
	 */
	private String leixing;
	
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
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
