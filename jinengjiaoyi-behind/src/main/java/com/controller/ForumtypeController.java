package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.lang.*;
import java.math.*;
import com.utils.*;
import com.service.*;
import com.entity.*;
import com.entity.view.*;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

import com.entity.ForumtypeEntity;
import com.entity.view.ForumtypeView;

import com.service.ForumtypeService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 论坛类型
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@RestController
@RequestMapping("/forumtype")
public class ForumtypeController {
    @Autowired
    private ForumtypeService forumtypeService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ForumtypeEntity forumtype,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<ForumtypeEntity> ew = new EntityWrapper<ForumtypeEntity>();


        //查询结果
		PageUtils page = forumtypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, forumtype), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ForumtypeEntity forumtype,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<ForumtypeEntity> ew = new EntityWrapper<ForumtypeEntity>();

        //查询结果
		PageUtils page = forumtypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, forumtype), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ForumtypeEntity forumtype){
       	EntityWrapper<ForumtypeEntity> ew = new EntityWrapper<ForumtypeEntity>();
      	ew.allEq(MPUtil.allEQMapPre( forumtype, "forumtype"));
        return R.ok().put("data", forumtypeService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ForumtypeEntity forumtype){
        EntityWrapper< ForumtypeEntity> ew = new EntityWrapper< ForumtypeEntity>();
 		ew.allEq(MPUtil.allEQMapPre( forumtype, "forumtype"));
		ForumtypeView forumtypeView =  forumtypeService.selectView(ew);
		return R.ok("查询论坛类型成功").put("data", forumtypeView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ForumtypeEntity forumtype = forumtypeService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(forumtype,deSens);
        return R.ok().put("data", forumtype);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ForumtypeEntity forumtype = forumtypeService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(forumtype,deSens);
        return R.ok().put("data", forumtype);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增论坛类型")
    public R save(@RequestBody ForumtypeEntity forumtype, HttpServletRequest request){
        //ValidatorUtils.validateEntity(forumtype);
        forumtypeService.insert(forumtype);
        return R.ok().put("data",forumtype.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增论坛类型")
    @RequestMapping("/add")
    public R add(@RequestBody ForumtypeEntity forumtype, HttpServletRequest request){
        //ValidatorUtils.validateEntity(forumtype);
        forumtypeService.insert(forumtype);
        return R.ok().put("data",forumtype.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改论坛类型")
    public R update(@RequestBody ForumtypeEntity forumtype, HttpServletRequest request){
        //ValidatorUtils.validateEntity(forumtype);
        //全部更新
        forumtypeService.updateById(forumtype);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除论坛类型")
    public R delete(@RequestBody Long[] ids){
        forumtypeService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }










}
