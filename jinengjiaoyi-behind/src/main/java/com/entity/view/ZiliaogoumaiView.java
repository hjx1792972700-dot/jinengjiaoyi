package com.entity.view;

import com.entity.ZiliaogoumaiEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import java.io.Serializable;

@TableName("ziliaogoumai")
public class ZiliaogoumaiView extends ZiliaogoumaiEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public ZiliaogoumaiView() {}

	public ZiliaogoumaiView(ZiliaogoumaiEntity ziliaogoumaiEntity) {
		try { BeanUtils.copyProperties(this, ziliaogoumaiEntity); } catch (BeansException e) { e.printStackTrace(); }
	}
}
