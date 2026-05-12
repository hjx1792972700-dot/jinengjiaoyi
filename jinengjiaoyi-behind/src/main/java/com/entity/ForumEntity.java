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
 * 技能社区
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@TableName("forum")
@Data
public class ForumEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public ForumEntity() {
		
	}
	
	public ForumEntity(T t) {
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
	 * 分类名称
	 */
	private String typename;
	
	/**
	 * 帖子标题
	 */
	private String title;
	
	/**
	 * 帖子内容
	 */
	private String content;
	
	/**
	 * 父节点id
	 */
	private Long parentid;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 头像
	 */
	private String avatarurl;
	
	/**
	 * 状态
	 */
	private String isdone;
	
	/**
	 * 是否置顶
	 */
	private Integer istop;
	
	/**
	 * 置顶时间
	 */
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date toptime;
	
	/**
	 * 封面
	 */
	private String cover;
	
	/**
	 * 是否匿名(1:是,0:否)
	 */
	private Integer isanon;
	
	/**
	 * 是否删除(1:是,0:否)
	 */
	private Integer delflag;
	
	/**
	 * 是否审核
	 */
	private String sfsh;
	
	/**
	 * 审核回复
	 */
	private String shhf;
	
	/**
	 * 用户id
	 */
	private Long userid;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	private Integer thumbsupnum;
	private Integer crazilynum;
	private Integer storeupnum;

	@TableField(exist = false)
	private List<ForumEntity> childs;
}
