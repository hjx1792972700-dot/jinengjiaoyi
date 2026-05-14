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

import com.entity.ForumEntity;
import com.entity.view.ForumView;

import com.service.ForumService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 技能社区
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@RestController
@RequestMapping("/forum")
public class ForumController {
    @Autowired
    private ForumService forumService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ForumEntity forum,
		HttpServletRequest request){
        if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
            forum.setUserid((Long)request.getSession().getAttribute("userId"));
        }
        //设置查询条件
        EntityWrapper<ForumEntity> ew = new EntityWrapper<ForumEntity>();


        //查询结果
		PageUtils page = forumService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, forum), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }


    /**
     * 前台列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ForumEntity forum,
                @RequestParam(required = false) Double istopstart,
                @RequestParam(required = false) Double istopend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date toptimestart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date toptimeend,
                @RequestParam(required = false) Double isanonstart,
                @RequestParam(required = false) Double isanonend,
                @RequestParam(required = false) Double delflagstart,
                @RequestParam(required = false) Double delflagend,
		HttpServletRequest request){
    	if(!request.getSession().getAttribute("role").toString().equals("管理员")) {
    		forum.setUserid((Long)request.getSession().getAttribute("userId"));
    	}
        //设置查询条件
        EntityWrapper<ForumEntity> ew = new EntityWrapper<ForumEntity>();
        if(istopstart!=null) ew.ge("istop", istopstart);
        if(istopend!=null) ew.le("istop", istopend);
        if(toptimestart!=null) ew.ge("toptime", toptimestart);
        if(toptimeend!=null) ew.le("toptime", toptimeend);
        if(isanonstart!=null) ew.ge("isanon", isanonstart);
        if(isanonend!=null) ew.le("isanon", isanonend);
        if(delflagstart!=null) ew.ge("delflag", delflagstart);
        if(delflagend!=null) ew.le("delflag", delflagend);

        //查询结果
		PageUtils page = forumService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, forum), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @IgnoreAuth
    @RequestMapping("/flist")
    public R flist(@RequestParam Map<String, Object> params,ForumEntity forum, HttpServletRequest request){
        EntityWrapper<ForumEntity> ew = new EntityWrapper<ForumEntity>();
        ew.eq("sfsh", "是");
        ew.eq("parentid", 0);
        ew.eq("delflag", 0);
    	PageUtils page = forumService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, forum), params), params));
        return R.ok().put("data", page);
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ForumEntity forum){
        EntityWrapper< ForumEntity> ew = new EntityWrapper< ForumEntity>();
 		ew.allEq(MPUtil.allEQMapPre( forum, "forum"));
		ForumView forumView =  forumService.selectView(ew);
		return R.ok("查询技能社区成功").put("data", forumView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ForumEntity forum = forumService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(forum,deSens);
        return R.ok().put("data", forum);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        ForumEntity forum = forumService.selectById(id);
        if(forum == null) return R.error("帖子不存在");
        if(!"是".equals(forum.getSfsh())) {
            Long userId = (Long) request.getSession().getAttribute("userId");
            if(userId == null || !userId.equals(forum.getUserid())) {
                return R.error("该帖子正在审核中，暂不可查看");
            }
        }
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(forum,deSens);
        return R.ok().put("data", forum);
    }

	/**
     * 论坛详情
     */
	@IgnoreAuth
    @RequestMapping("/list/{id}")
    public R list(@PathVariable("id") String id, HttpServletRequest request){
        ForumEntity forum = forumService.selectById(id);
        if(forum == null) return R.error("帖子不存在");
        if(!"是".equals(forum.getSfsh())) {
            String username = (String) request.getSession().getAttribute("username");
            Long userId = (Long) request.getSession().getAttribute("userId");
            if(userId == null || !userId.equals(forum.getUserid())) {
                return R.error("该帖子正在审核中，暂不可查看");
            }
        }
        getChilds(forum);
        return R.ok().put("data", forum);
    }

    private ForumEntity getChilds(ForumEntity forum) {
    	List<ForumEntity> childs = forumService.selectList(
    	    new EntityWrapper<ForumEntity>().eq("parentid", forum.getId()).eq("sfsh", "是"));
    	if(childs == null || childs.size()==0) {
    		return null;
    	}
    	forum.setChilds(childs);
    	for(ForumEntity forumEntity : childs) {
    		getChilds(forumEntity);
    	}
    	return forum;
    }



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增技能社区")
    public R save(@RequestBody ForumEntity forum, HttpServletRequest request){
        //ValidatorUtils.validateEntity(forum);
        if(forum.getUserid()==null){
            forum.setUserid((Long) request.getSession().getAttribute("userId"));
        }
        forumService.insert(forum);
        return R.ok().put("data",forum.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增技能社区")
    @RequestMapping("/add")
    public R add(@RequestBody ForumEntity forum, HttpServletRequest request){
    	forum.setUserid((Long)request.getSession().getAttribute("userId"));
    	forum.setSfsh("是");
        forumService.insert(forum);
        return R.ok().put("data",forum.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改技能社区")
    public R update(@RequestBody ForumEntity forum, HttpServletRequest request){
        //ValidatorUtils.validateEntity(forum);
        //全部更新
        forumService.updateById(forum);
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核技能社区")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<ForumEntity> list = new ArrayList<ForumEntity>();
        for(Long id : ids) {
            ForumEntity forum = forumService.selectById(id);
            forum.setSfsh(sfsh);
            forum.setShhf(shhf);
            list.add(forum);
        }
        forumService.updateBatchById(list);
        return R.ok();
    }




    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除技能社区")
    public R delete(@RequestBody Long[] ids){
        forumService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }










}
