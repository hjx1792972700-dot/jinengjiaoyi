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

import com.entity.JiaohuanshenqingEntity;
import com.entity.JiaohuanjiluEntity;
import com.entity.JinengxuqiuEntity;
import com.entity.view.JiaohuanshenqingView;

import com.service.JiaohuanshenqingService;
import com.service.JiaohuanjiluService;
import com.service.JinengxuqiuService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 交换申请
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@RestController
@RequestMapping("/jiaohuanshenqing")
public class JiaohuanshenqingController {
    @Autowired
    private JiaohuanshenqingService jiaohuanshenqingService;

    @Autowired
    private JiaohuanjiluService jiaohuanjiluService;

    @Autowired
    private JinengxuqiuService jinengxuqiuService;










    /**
     * 我发出的申请（我作为供给人/申请人）
     */
    @RequestMapping("/myApply")
    public R myApply(@RequestParam Map<String, Object> params, JiaohuanshenqingEntity jiaohuanshenqing,
        HttpServletRequest request){
        EntityWrapper<JiaohuanshenqingEntity> ew = new EntityWrapper<JiaohuanshenqingEntity>();
        String username = (String) request.getSession().getAttribute("username");
        ew.eq("gongjirenzhanghao", username);
        PageUtils page = jiaohuanshenqingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiaohuanshenqing), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 我收到的申请（别人申请我发布的技能，我是技能发布者）
     */
    @IgnoreAuth
    @RequestMapping("/receivedApply")
    public R receivedApply(@RequestParam Map<String, Object> params, JiaohuanshenqingEntity jiaohuanshenqing,
        HttpServletRequest request){
        EntityWrapper<JiaohuanshenqingEntity> ew = new EntityWrapper<JiaohuanshenqingEntity>();
        String username = (String) params.get("yonghuzhanghao");
        if(username == null || username.isEmpty()) {
            username = (String) request.getSession().getAttribute("username");
        }
        ew.eq("yonghuzhanghao", username);
        PageUtils page = jiaohuanshenqingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiaohuanshenqing), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JiaohuanshenqingEntity jiaohuanshenqing,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<JiaohuanshenqingEntity> ew = new EntityWrapper<JiaohuanshenqingEntity>();

        String tableName = request.getSession().getAttribute("tableName").toString();
        ew.andNew();
        if(tableName.equals("yonghu")) {
            ew.eq("yonghuzhanghao", (String)request.getSession().getAttribute("username"));
        }
        if(tableName.equals("yonghu")) {
            ew.or();
            ew.eq("gongjirenzhanghao", (String)request.getSession().getAttribute("username"));
        }
        ew.andNew("1=1");

        //查询结果
		PageUtils page = jiaohuanshenqingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiaohuanshenqing), params), params));
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
    public R list(@RequestParam Map<String, Object> params,JiaohuanshenqingEntity jiaohuanshenqing,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date shenqingshijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date shenqingshijianend,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<JiaohuanshenqingEntity> ew = new EntityWrapper<JiaohuanshenqingEntity>();
        if(shenqingshijianstart!=null) ew.ge("shenqingshijian", shenqingshijianstart);
        if(shenqingshijianend!=null) ew.le("shenqingshijian", shenqingshijianend);

        //查询结果
		PageUtils page = jiaohuanshenqingService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jiaohuanshenqing), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JiaohuanshenqingEntity jiaohuanshenqing){
       	EntityWrapper<JiaohuanshenqingEntity> ew = new EntityWrapper<JiaohuanshenqingEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jiaohuanshenqing, "jiaohuanshenqing"));
        return R.ok().put("data", jiaohuanshenqingService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JiaohuanshenqingEntity jiaohuanshenqing){
        EntityWrapper< JiaohuanshenqingEntity> ew = new EntityWrapper< JiaohuanshenqingEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jiaohuanshenqing, "jiaohuanshenqing"));
		JiaohuanshenqingView jiaohuanshenqingView =  jiaohuanshenqingService.selectView(ew);
		return R.ok("查询交换申请成功").put("data", jiaohuanshenqingView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JiaohuanshenqingEntity jiaohuanshenqing = jiaohuanshenqingService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(jiaohuanshenqing,deSens);
        return R.ok().put("data", jiaohuanshenqing);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        JiaohuanshenqingEntity jiaohuanshenqing = jiaohuanshenqingService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(jiaohuanshenqing,deSens);
        return R.ok().put("data", jiaohuanshenqing);
    }




    /**
     * 检查是否已提交过申请（排除已拒绝的）
     */
    @IgnoreAuth
    @RequestMapping("/checkDuplicate")
    public R checkDuplicate(@RequestParam String xuqiubianhao, @RequestParam String gongjirenzhanghao) {
        EntityWrapper<JiaohuanshenqingEntity> ew = new EntityWrapper<>();
        ew.eq("xuqiubianhao", xuqiubianhao);
        ew.eq("gongjirenzhanghao", gongjirenzhanghao);
        ew.ne("sfsh", "否");
        int count = jiaohuanshenqingService.selectCount(ew);
        return R.ok().put("data", count > 0);
    }

    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增交换申请")
    public R save(@RequestBody JiaohuanshenqingEntity jiaohuanshenqing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiaohuanshenqing);
        jiaohuanshenqingService.insert(jiaohuanshenqing);
        return R.ok().put("data",jiaohuanshenqing.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增交换申请")
    @RequestMapping("/add")
    public R add(@RequestBody JiaohuanshenqingEntity jiaohuanshenqing, HttpServletRequest request){
        EntityWrapper<JiaohuanshenqingEntity> dupCheck = new EntityWrapper<>();
        dupCheck.eq("xuqiubianhao", jiaohuanshenqing.getXuqiubianhao());
        dupCheck.eq("gongjirenzhanghao", jiaohuanshenqing.getGongjirenzhanghao());
        dupCheck.ne("sfsh", "否");
        int count = jiaohuanshenqingService.selectCount(dupCheck);
        if(count > 0) {
            return R.error("您已对该技能提交过申请，请勿重复提交");
        }
        if(jiaohuanshenqing.getYonghuzhanghao() != null
            && jiaohuanshenqing.getYonghuzhanghao().equals(jiaohuanshenqing.getGongjirenzhanghao())) {
            return R.error("不能对自己发布的技能提交申请");
        }
        jiaohuanshenqingService.insert(jiaohuanshenqing);
        return R.ok().put("data",jiaohuanshenqing.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改交换申请")
    public R update(@RequestBody JiaohuanshenqingEntity jiaohuanshenqing, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jiaohuanshenqing);
        //全部更新
        jiaohuanshenqingService.updateById(jiaohuanshenqing);
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核交换申请")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<JiaohuanshenqingEntity> list = new ArrayList<JiaohuanshenqingEntity>();
        for(Long id : ids) {
            JiaohuanshenqingEntity jiaohuanshenqing = jiaohuanshenqingService.selectById(id);
            jiaohuanshenqing.setSfsh(sfsh);
            jiaohuanshenqing.setShhf(shhf);
            list.add(jiaohuanshenqing);
        }
        jiaohuanshenqingService.updateBatchById(list);
        return R.ok();
    }




    /**
     * 技能发布者选择申请人的一项技能，进入待确认状态
     */
    @RequestMapping("/selectSkill")
    @Transactional
    public R selectSkill(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long id = Long.valueOf(body.get("id").toString());
        Long jinengId = Long.valueOf(body.get("xuanzejinengid").toString());
        String jinengBiaoti = (String) body.get("xuanzejinengbiaoti");

        JiaohuanshenqingEntity entity = jiaohuanshenqingService.selectById(id);
        if (entity == null) return R.error("申请不存在");
        if (!"待审核".equals(entity.getSfsh())) return R.error("当前状态不允许此操作");

        entity.setSfsh("待确认");
        entity.setXuanzejinengid(jinengId);
        entity.setXuanzejinengbiaoti(jinengBiaoti);
        jiaohuanshenqingService.updateById(entity);
        return R.ok();
    }

    /**
     * 申请人确认交换，自动创建交换记录
     */
    @RequestMapping("/confirmExchange")
    @Transactional
    public R confirmExchange(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long id = Long.valueOf(body.get("id").toString());
        JiaohuanshenqingEntity entity = jiaohuanshenqingService.selectById(id);
        if (entity == null) return R.error("申请不存在");
        if (!"待确认".equals(entity.getSfsh())) return R.error("当前状态不允许此操作");

        entity.setSfsh("已完成");
        jiaohuanshenqingService.updateById(entity);

        JiaohuanjiluEntity record = new JiaohuanjiluEntity();
        record.setYonghuzhanghao(entity.getYonghuzhanghao());
        record.setYonghuxingming(entity.getYonghuxingming());
        record.setShoujihao(entity.getShoujihao());
        record.setBiaoti(entity.getXuqiubiaoti());
        record.setJinengfenlei(entity.getJinengfenlei());
        if (entity.getFengmian() != null) {
            record.setGongjitupian(entity.getFengmian().split(",")[0]);
        }
        record.setJiaohuanjineng(entity.getXuanzejinengbiaoti());
        record.setJiaohuanshijian(new Date());
        record.setGongjirenzhanghao(entity.getGongjirenzhanghao());
        record.setGongjirenxingming(entity.getGongjirenxingming());
        record.setLianxifangshi(entity.getLianxifangshi());
        record.setFabuzhetigong(entity.getXuqiubiaoti());
        record.setShenqingrentigong(entity.getXuanzejinengbiaoti());
        record.setCrossuserid(entity.getId());
        record.setCrossrefid(entity.getId());
        jiaohuanjiluService.insert(record);

        return R.ok();
    }

    /**
     * 申请人拒绝交换（从待确认回退）
     */
    @RequestMapping("/rejectExchange")
    @Transactional
    public R rejectExchange(@RequestBody Map<String, Object> body, HttpServletRequest request) {
        Long id = Long.valueOf(body.get("id").toString());
        String reason = body.get("shhf") != null ? body.get("shhf").toString() : "";
        JiaohuanshenqingEntity entity = jiaohuanshenqingService.selectById(id);
        if (entity == null) return R.error("申请不存在");
        if (!"待确认".equals(entity.getSfsh())) return R.error("当前状态不允许此操作");

        entity.setSfsh("否");
        entity.setShhf(reason);
        jiaohuanshenqingService.updateById(entity);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除交换申请")
    public R delete(@RequestBody Long[] ids){
        jiaohuanshenqingService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }










}
