package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.JinengxuqiuEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.JinengxuqiuVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.JinengxuqiuView;


/**
 * 技能需求
 *
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public interface JinengxuqiuService extends IService<JinengxuqiuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<JinengxuqiuVO> selectListVO(Wrapper<JinengxuqiuEntity> wrapper);
   	
   	JinengxuqiuVO selectVO(@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);
   	
   	List<JinengxuqiuView> selectListView(Wrapper<JinengxuqiuEntity> wrapper);
   	
   	JinengxuqiuView selectView(@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<JinengxuqiuEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,Wrapper<JinengxuqiuEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,Wrapper<JinengxuqiuEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,Wrapper<JinengxuqiuEntity> wrapper);



}

