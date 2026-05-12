package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.ZiliaogoumaiEntity;
import java.util.List;
import java.util.Map;
import com.entity.vo.ZiliaogoumaiVO;
import org.apache.ibatis.annotations.Param;
import com.entity.view.ZiliaogoumaiView;

public interface ZiliaogoumaiService extends IService<ZiliaogoumaiEntity> {
    PageUtils queryPage(Map<String, Object> params);
   	List<ZiliaogoumaiVO> selectListVO(Wrapper<ZiliaogoumaiEntity> wrapper);
   	ZiliaogoumaiVO selectVO(@Param("ew") Wrapper<ZiliaogoumaiEntity> wrapper);
   	List<ZiliaogoumaiView> selectListView(Wrapper<ZiliaogoumaiEntity> wrapper);
   	ZiliaogoumaiView selectView(@Param("ew") Wrapper<ZiliaogoumaiEntity> wrapper);
   	PageUtils queryPage(Map<String, Object> params, Wrapper<ZiliaogoumaiEntity> wrapper);
}
