package com.entity.view;

import com.entity.SensitivewordsEntity;

import com.baomidou.mybatisplus.annotations.TableName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import java.math.BigDecimal;

import java.io.Serializable;
import com.utils.EncryptUtil;
 

/**
 * 敏感词
 * 后端返回视图实体辅助类   
 * （通常后端关联的表或者自定义的字段需要返回使用）
 * @author 
 * @email 
 * @date 2026-04-12 13:54:56
 */
@TableName("sensitivewords")
public class SensitivewordsView  extends SensitivewordsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public SensitivewordsView(){
	}
 
 	public SensitivewordsView(SensitivewordsEntity sensitivewordsEntity){
 	try {
			BeanUtils.copyProperties(this, sensitivewordsEntity);
		} catch (BeansException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
	}


}
