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

import com.entity.NewstypeEntity;
import com.entity.NewsEntity;
import com.entity.view.NewstypeView;

import com.service.NewstypeService;
import com.service.NewsService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 资讯分类
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@RestController
@RequestMapping("/newstype")
public class NewstypeController {
    @Autowired
    private NewstypeService newstypeService;

    @Autowired
    private NewsService newsService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,NewstypeEntity newstype,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<NewstypeEntity> ew = new EntityWrapper<NewstypeEntity>();


        //查询结果
		PageUtils page = newstypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, newstype), params), params));
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
    public R list(@RequestParam Map<String, Object> params,NewstypeEntity newstype,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<NewstypeEntity> ew = new EntityWrapper<NewstypeEntity>();

        //查询结果
		PageUtils page = newstypeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, newstype), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( NewstypeEntity newstype){
       	EntityWrapper<NewstypeEntity> ew = new EntityWrapper<NewstypeEntity>();
      	ew.allEq(MPUtil.allEQMapPre( newstype, "newstype"));
        return R.ok().put("data", newstypeService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(NewstypeEntity newstype){
        EntityWrapper< NewstypeEntity> ew = new EntityWrapper< NewstypeEntity>();
 		ew.allEq(MPUtil.allEQMapPre( newstype, "newstype"));
		NewstypeView newstypeView =  newstypeService.selectView(ew);
		return R.ok("查询资讯分类成功").put("data", newstypeView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        NewstypeEntity newstype = newstypeService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(newstype,deSens);
        return R.ok().put("data", newstype);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        NewstypeEntity newstype = newstypeService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(newstype,deSens);
        return R.ok().put("data", newstype);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增资讯分类")
    public R save(@RequestBody NewstypeEntity newstype, HttpServletRequest request){
        //ValidatorUtils.validateEntity(newstype);
        newstypeService.insert(newstype);
        return R.ok().put("data",newstype.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增资讯分类")
    @RequestMapping("/add")
    public R add(@RequestBody NewstypeEntity newstype, HttpServletRequest request){
        //ValidatorUtils.validateEntity(newstype);
        newstypeService.insert(newstype);
        return R.ok().put("data",newstype.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改资讯分类")
    public R update(@RequestBody NewstypeEntity newstype, HttpServletRequest request){
        //ValidatorUtils.validateEntity(newstype);
        //全部更新
        newstypeService.updateById(newstype);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除资讯分类")
    public R delete(@RequestBody Long[] ids){
        for (Long id : ids) {
            NewstypeEntity entity = newstypeService.selectById(id);
            if (entity != null) {
                String name = entity.getTypename();
                int count = newsService.selectCount(
                    new EntityWrapper<NewsEntity>().eq("typename", name)
                );
                if (count > 0) {
                    return R.error("资讯分类「" + name + "」下存在" + count + "条公告资讯记录，无法删除");
                }
            }
        }
        newstypeService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }










}
