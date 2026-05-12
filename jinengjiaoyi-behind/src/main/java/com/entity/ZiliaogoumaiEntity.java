package com.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.*;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("ziliaogoumai")
@Data
public class ZiliaogoumaiEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	public ZiliaogoumaiEntity() {}

	public ZiliaogoumaiEntity(T t) {
		try { BeanUtils.copyProperties(t, this); } catch (Exception e) { e.printStackTrace(); }
	}

	@TableId(type = IdType.AUTO)
	private Long id;

	private String dingdanhao;

	private Long xuexiziliaoid;

	private String ziliaobiaoti;

	private Integer jiage;

	private String yonghuzhanghao;

	private String yonghuxingming;

	private String zhuangtai;

	private String zhifufangshi;

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;
}
