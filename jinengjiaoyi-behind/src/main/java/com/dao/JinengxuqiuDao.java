package com.dao;

import com.entity.JinengxuqiuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.JinengxuqiuVO;
import com.entity.view.JinengxuqiuView;


/**
 * 技能需求
 * 
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public interface JinengxuqiuDao extends BaseMapper<JinengxuqiuEntity> {
	
	List<JinengxuqiuVO> selectListVO(@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);
	
	JinengxuqiuVO selectVO(@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);
	
	List<JinengxuqiuView> selectListView(@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);

	List<JinengxuqiuView> selectListView(Pagination page,@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);

	
	JinengxuqiuView selectView(@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);
	

    List<Map<String, Object>> selectValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);

    List<Map<String, Object>> selectTimeStatValue(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);

    List<Map<String, Object>> selectGroup(@Param("params") Map<String, Object> params,@Param("ew") Wrapper<JinengxuqiuEntity> wrapper);



}
