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


import com.dao.JinengxuqiuDao;
import com.entity.JinengxuqiuEntity;
import com.service.JinengxuqiuService;
import com.entity.vo.JinengxuqiuVO;
import com.entity.view.JinengxuqiuView;

@Service("jinengxuqiuService")
public class JinengxuqiuServiceImpl extends ServiceImpl<JinengxuqiuDao, JinengxuqiuEntity> implements JinengxuqiuService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JinengxuqiuEntity> page = this.selectPage(
                new Query<JinengxuqiuEntity>(params).getPage(),
                new EntityWrapper<JinengxuqiuEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<JinengxuqiuEntity> wrapper) {
		  Page<JinengxuqiuView> page =new Query<JinengxuqiuView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

    
    @Override
	public List<JinengxuqiuVO> selectListVO(Wrapper<JinengxuqiuEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public JinengxuqiuVO selectVO(Wrapper<JinengxuqiuEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<JinengxuqiuView> selectListView(Wrapper<JinengxuqiuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public JinengxuqiuView selectView(Wrapper<JinengxuqiuEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<JinengxuqiuEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<JinengxuqiuEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<JinengxuqiuEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
