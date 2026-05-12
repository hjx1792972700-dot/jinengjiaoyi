package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.DiscussjinengxuqiuEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.DiscussjinengxuqiuVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.DiscussjinengxuqiuView;


/**
 * 技能需求评论
 *
 * @author 
 * @email 
 * @date 2026-04-12 13:54:56
 */
public interface DiscussjinengxuqiuService extends IService<DiscussjinengxuqiuEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<DiscussjinengxuqiuVO> selectListVO(Wrapper<DiscussjinengxuqiuEntity> wrapper);
   	
   	DiscussjinengxuqiuVO selectVO(@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);
   	
   	List<DiscussjinengxuqiuView> selectListView(Wrapper<DiscussjinengxuqiuEntity> wrapper);
   	
   	DiscussjinengxuqiuView selectView(@Param("ew") Wrapper<DiscussjinengxuqiuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<DiscussjinengxuqiuEntity> wrapper);

   	

}

