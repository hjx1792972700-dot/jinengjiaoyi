package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.JiaohuanjiluEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.JiaohuanjiluVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.JiaohuanjiluView;


/**
 * 交换记录
 *
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public interface JiaohuanjiluService extends IService<JiaohuanjiluEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<JiaohuanjiluVO> selectListVO(Wrapper<JiaohuanjiluEntity> wrapper);
   	
   	JiaohuanjiluVO selectVO(@Param("ew") Wrapper<JiaohuanjiluEntity> wrapper);
   	
   	List<JiaohuanjiluView> selectListView(Wrapper<JiaohuanjiluEntity> wrapper);
   	
   	JiaohuanjiluView selectView(@Param("ew") Wrapper<JiaohuanjiluEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<JiaohuanjiluEntity> wrapper);

   	

    List<Map<String, Object>> selectValue(Map<String, Object> params,Wrapper<JiaohuanjiluEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params,Wrapper<JiaohuanjiluEntity> wrapper);

    List<Map<String, Object>> selectGroup(Map<String, Object> params,Wrapper<JiaohuanjiluEntity> wrapper);



}

