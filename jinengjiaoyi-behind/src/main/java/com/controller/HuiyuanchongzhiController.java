package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Calendar;
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

import com.entity.HuiyuanchongzhiEntity;
import com.entity.view.HuiyuanchongzhiView;

import com.service.HuiyuanchongzhiService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 会员充值
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@RestController
@RequestMapping("/huiyuanchongzhi")
public class HuiyuanchongzhiController {
    @Autowired
    private HuiyuanchongzhiService huiyuanchongzhiService;

    @Autowired
    private YonghuService yonghuService;










    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,HuiyuanchongzhiEntity huiyuanchongzhi,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			huiyuanchongzhi.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
		}
        //设置查询条件
        EntityWrapper<HuiyuanchongzhiEntity> ew = new EntityWrapper<HuiyuanchongzhiEntity>();


        //查询结果
		PageUtils page = huiyuanchongzhiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huiyuanchongzhi), params), params));
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
    public R list(@RequestParam Map<String, Object> params,HuiyuanchongzhiEntity huiyuanchongzhi,
                @RequestParam(required = false) Double chongzhijinestart,
                @RequestParam(required = false) Double chongzhijineend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date chongzhiriqistart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date chongzhiriqiend,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<HuiyuanchongzhiEntity> ew = new EntityWrapper<HuiyuanchongzhiEntity>();
        if(chongzhijinestart!=null) ew.ge("chongzhijine", chongzhijinestart);
        if(chongzhijineend!=null) ew.le("chongzhijine", chongzhijineend);
        if(chongzhiriqistart!=null) ew.ge("chongzhiriqi", chongzhiriqistart);
        if(chongzhiriqiend!=null) ew.le("chongzhiriqi", chongzhiriqiend);

        //查询结果
		PageUtils page = huiyuanchongzhiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, huiyuanchongzhi), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( HuiyuanchongzhiEntity huiyuanchongzhi){
       	EntityWrapper<HuiyuanchongzhiEntity> ew = new EntityWrapper<HuiyuanchongzhiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( huiyuanchongzhi, "huiyuanchongzhi"));
        return R.ok().put("data", huiyuanchongzhiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(HuiyuanchongzhiEntity huiyuanchongzhi){
        EntityWrapper< HuiyuanchongzhiEntity> ew = new EntityWrapper< HuiyuanchongzhiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( huiyuanchongzhi, "huiyuanchongzhi"));
		HuiyuanchongzhiView huiyuanchongzhiView =  huiyuanchongzhiService.selectView(ew);
		return R.ok("查询会员充值成功").put("data", huiyuanchongzhiView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        HuiyuanchongzhiEntity huiyuanchongzhi = huiyuanchongzhiService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(huiyuanchongzhi,deSens);
        return R.ok().put("data", huiyuanchongzhi);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        HuiyuanchongzhiEntity huiyuanchongzhi = huiyuanchongzhiService.selectById(id);
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(huiyuanchongzhi,deSens);
        return R.ok().put("data", huiyuanchongzhi);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增会员充值")
    public R save(@RequestBody HuiyuanchongzhiEntity huiyuanchongzhi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(huiyuanchongzhi);
        huiyuanchongzhiService.insert(huiyuanchongzhi);
        return R.ok().put("data",huiyuanchongzhi.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增会员充值")
    @RequestMapping("/add")
    public R add(@RequestBody HuiyuanchongzhiEntity huiyuanchongzhi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(huiyuanchongzhi);
        huiyuanchongzhiService.insert(huiyuanchongzhi);
        return R.ok().put("data",huiyuanchongzhi.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改会员充值")
    public R update(@RequestBody HuiyuanchongzhiEntity huiyuanchongzhi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(huiyuanchongzhi);
        //全部更新
        huiyuanchongzhiService.updateById(huiyuanchongzhi);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除会员充值")
    public R delete(@RequestBody Long[] ids){
        huiyuanchongzhiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 创建支付订单
     */
    @RequestMapping("/createOrder")
    @SysLog("创建VIP支付订单")
    public R createOrder(@RequestBody Map<String, Object> params, HttpServletRequest request){
        String username = (String) request.getSession().getAttribute("username");
        if(StringUtils.isBlank(username)) {
            return R.error("请先登录");
        }
        YonghuEntity user = yonghuService.selectOne(
            new EntityWrapper<YonghuEntity>().eq("yonghuzhanghao", username));
        if(user == null) {
            return R.error("用户不存在");
        }

        if("是".equals(user.getVip()) && user.getVipExpire() != null) {
            if(user.getVipExpire().after(new Date())) {
                return R.error("会员未过期，无需续费。待会员到期时系统将自动扣费完成续费。");
            }
        }

        String orderNo = "VIP" + System.currentTimeMillis() + (int)(Math.random()*1000);
        int amount = params.get("amount") != null ? Integer.parseInt(params.get("amount").toString()) : 199;
        String taocan = params.get("taocan") != null ? params.get("taocan").toString() : "永久会员";
        String payMethod = params.get("payMethod") != null ? params.get("payMethod").toString() : "支付宝";

        HuiyuanchongzhiEntity record = new HuiyuanchongzhiEntity();
        record.setChongzhibianhao(orderNo);
        record.setChongzhijine(amount);
        record.setChongzhiriqi(new Date());
        record.setYonghuzhanghao(username);
        record.setYonghuxingming(user.getYonghuxingming());
        record.setZhuangtai("待支付");
        record.setTaocan(taocan);
        record.setZhifufangshi(payMethod);
        huiyuanchongzhiService.insert(record);

        String payUrl = "https://skillpay.mock-gateway.com/pay?orderNo=" + orderNo
            + "&amount=" + amount + "&merchant=jinengjiaoyi&sign=" + orderNo.hashCode();

        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("orderId", record.getId());
        data.put("amount", amount);
        data.put("payUrl", payUrl);
        data.put("taocan", taocan);
        data.put("payMethod", payMethod);
        return R.ok().put("data", data);
    }

    /**
     * 模拟第三方支付回调（确认支付）
     */
    @RequestMapping("/payCallback")
    @Transactional
    @SysLog("VIP支付回调确认")
    public R payCallback(@RequestBody Map<String, Object> params, HttpServletRequest request){
        String orderNo = params.get("orderNo") != null ? params.get("orderNo").toString() : "";
        if(StringUtils.isBlank(orderNo)) {
            return R.error("订单号不能为空");
        }

        HuiyuanchongzhiEntity order = huiyuanchongzhiService.selectOne(
            new EntityWrapper<HuiyuanchongzhiEntity>().eq("chongzhibianhao", orderNo));
        if(order == null) {
            return R.error("订单不存在");
        }
        if("已支付".equals(order.getZhuangtai())) {
            return R.ok().put("data", "already_paid");
        }

        YonghuEntity user = yonghuService.selectOne(
            new EntityWrapper<YonghuEntity>().eq("yonghuzhanghao", order.getYonghuzhanghao()));

        if(user != null && "是".equals(user.getVip()) && user.getVipExpire() != null) {
            if(user.getVipExpire().after(new Date())) {
                order.setZhuangtai("已取消");
                huiyuanchongzhiService.updateById(order);
                return R.error("会员未过期，无需续费。订单已取消。");
            }
        }

        order.setZhuangtai("已支付");
        huiyuanchongzhiService.updateById(order);

        if(user != null) {
            user.setVip("是");
            String taocan = order.getTaocan();
            Calendar cal = Calendar.getInstance();
            if("永久会员".equals(taocan)) {
                cal.set(2099, Calendar.DECEMBER, 31);
            } else {
                cal.setTime(new Date());
                if("连续包月".equals(taocan)) {
                    cal.add(Calendar.MONTH, 1);
                } else if("连续包季".equals(taocan)) {
                    cal.add(Calendar.MONTH, 3);
                } else if("年度会员".equals(taocan)) {
                    cal.add(Calendar.YEAR, 1);
                } else {
                    cal.add(Calendar.MONTH, 1);
                }
            }
            user.setVipExpire(cal.getTime());
            yonghuService.updateById(user);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> data = new HashMap<>();
        data.put("status", "success");
        data.put("orderNo", orderNo);
        data.put("amount", order.getChongzhijine());
        data.put("taocan", order.getTaocan());
        if(user != null && user.getVipExpire() != null) {
            data.put("expireDate", sdf.format(user.getVipExpire()));
        }
        return R.ok().put("data", data);
    }

    /**
     * 查询订单支付状态
     */
    @RequestMapping("/queryOrder")
    public R queryOrder(@RequestParam String orderNo, HttpServletRequest request){
        if(StringUtils.isBlank(orderNo)) {
            return R.error("订单号不能为空");
        }
        HuiyuanchongzhiEntity order = huiyuanchongzhiService.selectOne(
            new EntityWrapper<HuiyuanchongzhiEntity>().eq("chongzhibianhao", orderNo));
        if(order == null) {
            return R.error("订单不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("status", order.getZhuangtai());
        data.put("amount", order.getChongzhijine());
        data.put("taocan", order.getTaocan());
        return R.ok().put("data", data);
    }

    /**
     * 查询当前用户VIP状态（含到期检测）
     */
    @RequestMapping("/vipStatus")
    public R vipStatus(HttpServletRequest request){
        String username = (String) request.getSession().getAttribute("username");
        if(StringUtils.isBlank(username)) {
            return R.error("请先登录");
        }
        YonghuEntity user = yonghuService.selectOne(
            new EntityWrapper<YonghuEntity>().eq("yonghuzhanghao", username));
        if(user == null) {
            return R.error("用户不存在");
        }

        boolean isVip = "是".equals(user.getVip());
        boolean expired = false;
        long remainDays = 0;
        String expireDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if(isVip && user.getVipExpire() == null) {
            List<HuiyuanchongzhiEntity> records = huiyuanchongzhiService.selectList(
                new EntityWrapper<HuiyuanchongzhiEntity>()
                    .eq("yonghuzhanghao", username)
                    .orderBy("id", false));
            if(records != null && records.size() > 0) {
                HuiyuanchongzhiEntity latest = records.get(0);
                Calendar cal = Calendar.getInstance();
                Date baseDate = latest.getChongzhiriqi() != null ? latest.getChongzhiriqi() : latest.getAddtime();
                cal.setTime(baseDate != null ? baseDate : new Date());
                String tc = latest.getTaocan();
                if("永久会员".equals(tc)) { cal.set(2099, Calendar.DECEMBER, 31); }
                else if("年度会员".equals(tc)) { cal.add(Calendar.YEAR, 1); }
                else if("连续包季".equals(tc)) { cal.add(Calendar.MONTH, 3); }
                else { cal.add(Calendar.MONTH, 1); }
                user.setVipExpire(cal.getTime());
                for(HuiyuanchongzhiEntity r : records) {
                    if(r.getZhuangtai() == null || r.getZhuangtai().isEmpty()) {
                        r.setZhuangtai("已支付");
                        if(r.getTaocan() == null || r.getTaocan().isEmpty()) { r.setTaocan("VIP会员"); }
                        if(r.getZhifufangshi() == null || r.getZhifufangshi().isEmpty()) { r.setZhifufangshi("支付宝"); }
                        huiyuanchongzhiService.updateById(r);
                    }
                }
                yonghuService.updateById(user);
            } else {
                isVip = false;
                user.setVip("否");
                yonghuService.updateById(user);
            }
        }

        boolean permanent = false;
        if(isVip && user.getVipExpire() != null) {
            Calendar expireCal = Calendar.getInstance();
            expireCal.setTime(user.getVipExpire());
            permanent = expireCal.get(Calendar.YEAR) >= 2099;

            if(permanent) {
                remainDays = 0;
                expireDate = null;
            } else {
                Calendar todayCal = Calendar.getInstance();
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.set(Calendar.MINUTE, 0);
                todayCal.set(Calendar.SECOND, 0);
                todayCal.set(Calendar.MILLISECOND, 0);

                expireCal.set(Calendar.HOUR_OF_DAY, 0);
                expireCal.set(Calendar.MINUTE, 0);
                expireCal.set(Calendar.SECOND, 0);
                expireCal.set(Calendar.MILLISECOND, 0);

                if(expireCal.before(todayCal)) {
                    expired = true;
                    isVip = false;
                    user.setVip("否");
                    user.setVipExpire(null);
                    yonghuService.updateById(user);
                } else {
                    remainDays = (expireCal.getTimeInMillis() - todayCal.getTimeInMillis()) / (1000L * 60 * 60 * 24);
                    expireDate = sdf.format(user.getVipExpire());
                }
            }
        }

        String latestTaocan = null;
        if(isVip) {
            List<HuiyuanchongzhiEntity> paidRecords = huiyuanchongzhiService.selectList(
                new EntityWrapper<HuiyuanchongzhiEntity>()
                    .eq("yonghuzhanghao", username)
                    .eq("zhuangtai", "已支付")
                    .orderBy("id", false));
            if(paidRecords != null && paidRecords.size() > 0) {
                latestTaocan = paidRecords.get(0).getTaocan();
            }
        }

        Map<String, Object> data = new HashMap<>();
        data.put("vip", isVip);
        data.put("expired", expired);
        data.put("permanent", permanent);
        data.put("remainDays", remainDays);
        data.put("expireDate", expireDate);
        data.put("taocan", latestTaocan);
        data.put("username", user.getYonghuzhanghao());
        data.put("name", user.getYonghuxingming());
        return R.ok().put("data", data);
    }








}
