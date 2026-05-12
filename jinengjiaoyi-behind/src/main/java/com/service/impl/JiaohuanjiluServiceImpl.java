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


import com.dao.JiaohuanjiluDao;
import com.entity.JiaohuanjiluEntity;
import com.service.JiaohuanjiluService;
import com.entity.vo.JiaohuanjiluVO;
import com.entity.view.JiaohuanjiluView;

@Service("jiaohuanjiluService")
public class JiaohuanjiluServiceImpl extends ServiceImpl<JiaohuanjiluDao, JiaohuanjiluEntity> implements JiaohuanjiluService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JiaohuanjiluEntity> page = this.selectPage(
                new Query<JiaohuanjiluEntity>(params).getPage(),
                new EntityWrapper<JiaohuanjiluEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<JiaohuanjiluEntity> wrapper) {
		  Page<JiaohuanjiluView> page =new Query<JiaohuanjiluView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

    
    @Override
	public List<JiaohuanjiluVO> selectListVO(Wrapper<JiaohuanjiluEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public JiaohuanjiluVO selectVO(Wrapper<JiaohuanjiluEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<JiaohuanjiluView> selectListView(Wrapper<JiaohuanjiluEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public JiaohuanjiluView selectView(Wrapper<JiaohuanjiluEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}

    @Override
    public List<Map<String, Object>> selectValue(Map<String, Object> params, Wrapper<JiaohuanjiluEntity> wrapper) {
        return baseMapper.selectValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectTimeStatValue(Map<String, Object> params, Wrapper<JiaohuanjiluEntity> wrapper) {
        return baseMapper.selectTimeStatValue(params, wrapper);
    }

    @Override
    public List<Map<String, Object>> selectGroup(Map<String, Object> params, Wrapper<JiaohuanjiluEntity> wrapper) {
        return baseMapper.selectGroup(params, wrapper);
    }




}
