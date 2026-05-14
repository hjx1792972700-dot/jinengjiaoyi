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

import com.entity.ZiliaoleixingEntity;
import com.entity.XuexiziliaoEntity;
import com.entity.view.ZiliaoleixingView;

import com.service.ZiliaoleixingService;
import com.service.XuexiziliaoService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 资料类型
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@RestController
@RequestMapping("/ziliaoleixing")
public class ZiliaoleixingController {
    @Autowired
    private ZiliaoleixingService ziliaoleixingService;

    @Autowired
    private XuexiziliaoService xuexiziliaoService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ZiliaoleixingEntity ziliaoleixing,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<ZiliaoleixingEntity> ew = new EntityWrapper<ZiliaoleixingEntity>();


        //查询结果
		PageUtils page = ziliaoleixingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, ziliaoleixing), params), params));
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
    public R list(@RequestParam Map<String, Object> params,ZiliaoleixingEntity ziliaoleixing,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<ZiliaoleixingEntity> ew = new EntityWrapper<ZiliaoleixingEntity>();

        //查询结果
		PageUtils page = ziliaoleixingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, ziliaoleixing), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ZiliaoleixingEntity ziliaoleixing){
       	EntityWrapper<ZiliaoleixingEntity> ew = new EntityWrapper<ZiliaoleixingEntity>();
      	ew.allEq(MPUtil.allEQMapPre( ziliaoleixing, "ziliaoleixing"));
        return R.ok().put("data", ziliaoleixingService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ZiliaoleixingEntity ziliaoleixing){
        EntityWrapper< ZiliaoleixingEntity> ew = new EntityWrapper< ZiliaoleixingEntity>();
 		ew.allEq(MPUtil.allEQMapPre( ziliaoleixing, "ziliaoleixing"));
		ZiliaoleixingView ziliaoleixingView =  ziliaoleixingService.selectView(ew);
		return R.ok("查询资料类型成功").put("data", ziliaoleixingView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ZiliaoleixingEntity ziliaoleixing = ziliaoleixingService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(ziliaoleixing,deSens);
        return R.ok().put("data", ziliaoleixing);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ZiliaoleixingEntity ziliaoleixing = ziliaoleixingService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(ziliaoleixing,deSens);
        return R.ok().put("data", ziliaoleixing);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增资料类型")
    public R save(@RequestBody ZiliaoleixingEntity ziliaoleixing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(ziliaoleixing);
        ziliaoleixingService.insert(ziliaoleixing);
        return R.ok().put("data",ziliaoleixing.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增资料类型")
    @RequestMapping("/add")
    public R add(@RequestBody ZiliaoleixingEntity ziliaoleixing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(ziliaoleixing);
        ziliaoleixingService.insert(ziliaoleixing);
        return R.ok().put("data",ziliaoleixing.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改资料类型")
    public R update(@RequestBody ZiliaoleixingEntity ziliaoleixing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(ziliaoleixing);
        //全部更新
        ziliaoleixingService.updateById(ziliaoleixing);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除资料类型")
    public R delete(@RequestBody Long[] ids){
        for (Long id : ids) {
            ZiliaoleixingEntity entity = ziliaoleixingService.selectById(id);
            if (entity != null) {
                String name = entity.getZiliaoleixing();
                int count = xuexiziliaoService.selectCount(
                    new EntityWrapper<XuexiziliaoEntity>().eq("ziliaoleixing", name)
                );
                if (count > 0) {
                    return R.error("资料类型「" + name + "」下存在" + count + "条学习资料记录，无法删除");
                }
            }
        }
        ziliaoleixingService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }










}
