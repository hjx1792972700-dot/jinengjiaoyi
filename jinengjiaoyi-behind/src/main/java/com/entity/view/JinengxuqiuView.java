package com.entity.view;

import com.entity.JinengxuqiuEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.utils.EncryptUtil;
 

/**
 * 技能需求
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@TableName("jinengxuqiu")
public class JinengxuqiuView  extends JinengxuqiuEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public JinengxuqiuView(){
	}
 
 	public JinengxuqiuView(JinengxuqiuEntity jinengxuqiuEntity){
 	try {
			BeanUtils.copyProperties(this, jinengxuqiuEntity);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
