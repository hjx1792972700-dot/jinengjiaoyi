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
 * 留言反馈
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:56
 */
@TableName("messages")
@Data
public class MessagesEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public MessagesEntity() {
		
	}
	
	public MessagesEntity(T t) {
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
	 * 留言人id
	 */
	private Long userid;
	
	/**
	 * 用户名
	 */
	private String username;
	
	/**
	 * 头像
	 */
	private String avatarurl;
	
	/**
	 * 留言内容
	 */
	private String content;
	
	/**
	 * 留言图片
	 */
	private String cpicture;
	
	/**
	 * 回复内容
	 */
	private String reply;
	
	/**
	 * 回复图片
	 */
	private String rpicture;
	
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

}
