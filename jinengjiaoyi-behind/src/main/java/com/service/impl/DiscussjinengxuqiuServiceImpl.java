package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;


import com.dao.DiscussjinengxuqiuDao;
import com.entity.DiscussjinengxuqiuEntity;
import com.service.DiscussjinengxuqiuService;
import com.entity.vo.DiscussjinengxuqiuVO;
import com.entity.view.DiscussjinengxuqiuView;

@Service("discussjinengxuqiuService")
public class DiscussjinengxuqiuServiceImpl extends ServiceImpl<DiscussjinengxuqiuDao, DiscussjinengxuqiuEntity> implements DiscussjinengxuqiuService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<DiscussjinengxuqiuEntity> page = this.selectPage(
                new Query<DiscussjinengxuqiuEntity>(params).getPage(),
                new EntityWrapper<DiscussjinengxuqiuEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<DiscussjinengxuqiuEntity> wrapper) {
		  Page<DiscussjinengxuqiuView> page =new Query<DiscussjinengxuqiuView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

    
    @Override
	public List<DiscussjinengxuqiuVO> selectListVO(Wrapper<DiscussjinengxuqiuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public DiscussjinengxuqiuVO selectVO(Wrapper<DiscussjinengxuqiuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<DiscussjinengxuqiuView> selectListView(Wrapper<DiscussjinengxuqiuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public DiscussjinengxuqiuView selectView(Wrapper<DiscussjinengxuqiuEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
