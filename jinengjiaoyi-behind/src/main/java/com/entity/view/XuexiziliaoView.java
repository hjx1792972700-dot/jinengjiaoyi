package com.entity.view;

import com.entity.XuexiziliaoEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.utils.EncryptUtil;
 

/**
 * 学习资料
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@TableName("xuexiziliao")
public class XuexiziliaoView  extends XuexiziliaoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public XuexiziliaoView(){
	}
 
 	public XuexiziliaoView(XuexiziliaoEntity xuexiziliaoEntity){
 	try {
			BeanUtils.copyProperties(this, xuexiziliaoEntity);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
