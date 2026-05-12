package com.dao;

import com.entity.JinengfenleiEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.entity.vo.JinengfenleiVO;
import com.entity.view.JinengfenleiView;


/**
 * 技能分类
 * 
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public interface JinengfenleiDao extends BaseMapper<JinengfenleiEntity> {
	
	List<JinengfenleiVO> selectListVO(@Param("ew") Wrapper<JinengfenleiEntity> wrapper);
	
	JinengfenleiVO selectVO(@Param("ew") Wrapper<JinengfenleiEntity> wrapper);
	
	List<JinengfenleiView> selectListView(@Param("ew") Wrapper<JinengfenleiEntity> wrapper);

	List<JinengfenleiView> selectListView(Pagination page,@Param("ew") Wrapper<JinengfenleiEntity> wrapper);

	
	JinengfenleiView selectView(@Param("ew") Wrapper<JinengfenleiEntity> wrapper);
	

}
