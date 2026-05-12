package com.dao;

import com.entity.DiscussjinengxuqiuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.DiscussjinengxuqiuVO;
import com.entity.view.DiscussjinengxuqiuView;


/**
 * 技能需求评论
 * 
 * @author 
 * @email 
 * @date 2026-04-12 13:54:56
 */
public interface DiscussjinengxuqiuDao extends BaseMapper<DiscussjinengxuqiuEntity> {
	
	List<DiscussjinengxuqiuVO> selectListVO(@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);
	
	DiscussjinengxuqiuVO selectVO(@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);
	
	List<DiscussjinengxuqiuView> selectListView(@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);

	List<DiscussjinengxuqiuView> selectListView(Pagination page,@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);

	
	DiscussjinengxuqiuView selectView(@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);
	

}
