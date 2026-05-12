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

import com.entity.JinengfenleiEntity;
import com.entity.view.JinengfenleiView;

import com.service.JinengfenleiService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 技能分类
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@RestController
@RequestMapping("/jinengfenlei")
public class JinengfenleiController {
    @Autowired
    private JinengfenleiService jinengfenleiService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JinengfenleiEntity jinengfenlei,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<JinengfenleiEntity> ew = new EntityWrapper<JinengfenleiEntity>();


        //查询结果
		PageUtils page = jinengfenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengfenlei), params), params));
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
    public R list(@RequestParam Map<String, Object> params,JinengfenleiEntity jinengfenlei,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<JinengfenleiEntity> ew = new EntityWrapper<JinengfenleiEntity>();

        //查询结果
		PageUtils page = jinengfenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengfenlei), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JinengfenleiEntity jinengfenlei){
       	EntityWrapper<JinengfenleiEntity> ew = new EntityWrapper<JinengfenleiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jinengfenlei, "jinengfenlei"));
        return R.ok().put("data", jinengfenleiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JinengfenleiEntity jinengfenlei){
        EntityWrapper< JinengfenleiEntity> ew = new EntityWrapper< JinengfenleiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jinengfenlei, "jinengfenlei"));
		JinengfenleiView jinengfenleiView =  jinengfenleiService.selectView(ew);
		return R.ok("查询技能分类成功").put("data", jinengfenleiView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JinengfenleiEntity jinengfenlei = jinengfenleiService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(jinengfenlei,deSens);
        return R.ok().put("data", jinengfenlei);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JinengfenleiEntity jinengfenlei = jinengfenleiService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(jinengfenlei,deSens);
        return R.ok().put("data", jinengfenlei);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增技能分类")
    public R save(@RequestBody JinengfenleiEntity jinengfenlei, HttpServletRequest request){
        //验证字段唯一性，否则返回错误信息
        if(jinengfenleiService.selectCount(new EntityWrapper<JinengfenleiEntity>().eq("jinengfenlei", jinengfenlei.getJinengfenlei()))>0) {
            return R.error("技能分类已存在");
        }
        //ValidatorUtils.validateEntity(jinengfenlei);
        jinengfenleiService.insert(jinengfenlei);
        return R.ok().put("data",jinengfenlei.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增技能分类")
    @RequestMapping("/add")
    public R add(@RequestBody JinengfenleiEntity jinengfenlei, HttpServletRequest request){
        //验证字段唯一性，否则返回错误信息
        if(jinengfenleiService.selectCount(new EntityWrapper<JinengfenleiEntity>().eq("jinengfenlei", jinengfenlei.getJinengfenlei()))>0) {
            return R.error("技能分类已存在");
        }
        //ValidatorUtils.validateEntity(jinengfenlei);
        jinengfenleiService.insert(jinengfenlei);
        return R.ok().put("data",jinengfenlei.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改技能分类")
    public R update(@RequestBody JinengfenleiEntity jinengfenlei, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jinengfenlei);
        //验证字段唯一性，否则返回错误信息
        if(jinengfenleiService.selectCount(new EntityWrapper<JinengfenleiEntity>().ne("id", jinengfenlei.getId()).eq("jinengfenlei", jinengfenlei.getJinengfenlei()))>0) {
            return R.error("技能分类已存在");
        }
        //全部更新
        jinengfenleiService.updateById(jinengfenlei);
        return R.ok();
    }





    @Autowired
    private JinengxuqiuService jinengxuqiuService;

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除技能分类")
    public R delete(@RequestBody Long[] ids){
        for (Long id : ids) {
            JinengfenleiEntity fenlei = jinengfenleiService.selectById(id);
            if (fenlei != null) {
                int count = jinengxuqiuService.selectCount(
                    new EntityWrapper<JinengxuqiuEntity>().eq("jinengfenlei", fenlei.getJinengfenlei())
                );
                if (count > 0) {
                    return R.error("分类「" + fenlei.getJinengfenlei() + "」下存在" + count + "条技能需求，无法删除");
                }
            }
        }
        jinengfenleiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }








    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,JinengfenleiEntity jinengfenlei, HttpServletRequest request){
        EntityWrapper<JinengfenleiEntity> ew = new EntityWrapper<JinengfenleiEntity>();
        int count = jinengfenleiService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengfenlei), params), params));
        return R.ok().put("data", count);
    }


}
