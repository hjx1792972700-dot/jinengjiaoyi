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


import com.dao.JinengfenleiDao;
import com.entity.JinengfenleiEntity;
import com.service.JinengfenleiService;
import com.entity.vo.JinengfenleiVO;
import com.entity.view.JinengfenleiView;

@Service("jinengfenleiService")
public class JinengfenleiServiceImpl extends ServiceImpl<JinengfenleiDao, JinengfenleiEntity> implements JinengfenleiService {
	
	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JinengfenleiEntity> page = this.selectPage(
                new Query<JinengfenleiEntity>(params).getPage(),
                new EntityWrapper<JinengfenleiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<JinengfenleiEntity> wrapper) {
		  Page<JinengfenleiView> page =new Query<JinengfenleiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}

    
    @Override
	public List<JinengfenleiVO> selectListVO(Wrapper<JinengfenleiEntity> wrapper) {
 		return baseMapper.selectListVO(wrapper);
	}
	
	@Override
	public JinengfenleiVO selectVO(Wrapper<JinengfenleiEntity> wrapper) {
 		return baseMapper.selectVO(wrapper);
	}
	
	@Override
	public List<JinengfenleiView> selectListView(Wrapper<JinengfenleiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public JinengfenleiView selectView(Wrapper<JinengfenleiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}


}
