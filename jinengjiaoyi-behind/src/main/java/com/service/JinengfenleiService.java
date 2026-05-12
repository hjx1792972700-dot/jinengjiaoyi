package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.JinengfenleiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.JinengfenleiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.JinengfenleiView;


/**
 * 技能分类
 *
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
public interface JinengfenleiService extends IService<JinengfenleiEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
   	List<JinengfenleiVO> selectListVO(Wrapper<JinengfenleiEntity> wrapper);
   	
   	JinengfenleiVO selectVO(@Param("ew") Wrapper<JinengfenleiEntity> wrapper);
   	
   	List<JinengfenleiView> selectListView(Wrapper<JinengfenleiEntity> wrapper);
   	
   	JinengfenleiView selectView(@Param("ew") Wrapper<JinengfenleiEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<JinengfenleiEntity> wrapper);

   	

}

