package com.controller;

import java.util.*;
import com.utils.*;
import com.service.*;
import com.entity.*;
import com.entity.view.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.annotation.IgnoreAuth;
import com.annotation.SysLog;

@RestController
@RequestMapping("/ziliaogoumai")
public class ZiliaogoumaiController {
    @Autowired
    private ZiliaogoumaiService ziliaogoumaiService;

    @Autowired
    private YonghuService yonghuService;

    @Autowired
    private XuexiziliaoService xuexiziliaoService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZiliaogoumaiEntity ziliaogoumai, HttpServletRequest request) {
        String tableName = request.getSession().getAttribute("tableName").toString();
        if (tableName.equals("yonghu")) {
            ziliaogoumai.setYonghuzhanghao((String) request.getSession().getAttribute("username"));
        }
        EntityWrapper<ZiliaogoumaiEntity> ew = new EntityWrapper<ZiliaogoumaiEntity>();
        PageUtils page = ziliaogoumaiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, ziliaogoumai), params), params));
        return R.ok().put("data", page);
    }

    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ZiliaogoumaiEntity ziliaogoumai, HttpServletRequest request) {
        EntityWrapper<ZiliaogoumaiEntity> ew = new EntityWrapper<ZiliaogoumaiEntity>();
        PageUtils page = ziliaogoumaiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, ziliaogoumai), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        ZiliaogoumaiEntity ziliaogoumai = ziliaogoumaiService.selectById(id);
        return R.ok().put("data", ziliaogoumai);
    }

    @RequestMapping("/save")
    @SysLog("新增资料购买")
    public R save(@RequestBody ZiliaogoumaiEntity ziliaogoumai, HttpServletRequest request) {
        ziliaogoumaiService.insert(ziliaogoumai);
        return R.ok();
    }

    @RequestMapping("/update")
    @SysLog("修改资料购买")
    public R update(@RequestBody ZiliaogoumaiEntity ziliaogoumai, HttpServletRequest request) {
        ziliaogoumaiService.updateById(ziliaogoumai);
        return R.ok();
    }

    @RequestMapping("/delete")
    @SysLog("删除资料购买")
    public R delete(@RequestBody Long[] ids) {
        ziliaogoumaiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    @RequestMapping("/checkPurchased")
    public R checkPurchased(@RequestParam Long xuexiziliaoid, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (StringUtils.isBlank(username)) {
            return R.ok().put("data", false);
        }

        YonghuEntity user = yonghuService.selectOne(
            new EntityWrapper<YonghuEntity>().eq("yonghuzhanghao", username));
        if (user != null && "是".equals(user.getVip()) && user.getVipExpire() != null && user.getVipExpire().after(new Date())) {
            return R.ok().put("data", true).put("reason", "vip");
        }

        ZiliaogoumaiEntity record = ziliaogoumaiService.selectOne(
            new EntityWrapper<ZiliaogoumaiEntity>()
                .eq("xuexiziliaoid", xuexiziliaoid)
                .eq("yonghuzhanghao", username)
                .eq("zhuangtai", "已支付"));
        return R.ok().put("data", record != null).put("reason", record != null ? "purchased" : "none");
    }

    @RequestMapping("/createOrder")
    @SysLog("创建资料购买订单")
    public R createOrder(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        if (StringUtils.isBlank(username)) {
            return R.error("请先登录");
        }
        YonghuEntity user = yonghuService.selectOne(
            new EntityWrapper<YonghuEntity>().eq("yonghuzhanghao", username));
        if (user == null) {
            return R.error("用户不存在");
        }

        if ("是".equals(user.getVip()) && user.getVipExpire() != null && user.getVipExpire().after(new Date())) {
            return R.error("VIP会员可免费下载，无需购买");
        }

        Long xuexiziliaoid = Long.parseLong(params.get("xuexiziliaoid").toString());

        ZiliaogoumaiEntity existing = ziliaogoumaiService.selectOne(
            new EntityWrapper<ZiliaogoumaiEntity>()
                .eq("xuexiziliaoid", xuexiziliaoid)
                .eq("yonghuzhanghao", username)
                .eq("zhuangtai", "已支付"));
        if (existing != null) {
            return R.error("您已购买过该资料");
        }

        XuexiziliaoEntity ziliao = xuexiziliaoService.selectById(xuexiziliaoid);
        if (ziliao == null) {
            return R.error("资料不存在");
        }

        int price = ziliao.getJiage() != null ? ziliao.getJiage() : 0;
        if (price <= 0) {
            return R.error("该资料为免费资料，无需购买");
        }

        String payMethod = params.get("payMethod") != null ? params.get("payMethod").toString() : "支付宝";
        String orderNo = "ZL" + System.currentTimeMillis() + (int)(Math.random() * 1000);

        ZiliaogoumaiEntity record = new ZiliaogoumaiEntity();
        record.setDingdanhao(orderNo);
        record.setXuexiziliaoid(xuexiziliaoid);
        record.setZiliaobiaoti(ziliao.getZiliaobiaoti());
        record.setJiage(price);
        record.setYonghuzhanghao(username);
        record.setYonghuxingming(user.getYonghuxingming());
        record.setZhuangtai("待支付");
        record.setZhifufangshi(payMethod);
        ziliaogoumaiService.insert(record);

        String payUrl = "https://skillpay.mock-gateway.com/pay?orderNo=" + orderNo
            + "&amount=" + price + "&merchant=jinengjiaoyi&sign=" + orderNo.hashCode();

        Map<String, Object> data = new HashMap<>();
        data.put("orderNo", orderNo);
        data.put("orderId", record.getId());
        data.put("amount", price);
        data.put("payUrl", payUrl);
        data.put("ziliaobiaoti", ziliao.getZiliaobiaoti());
        data.put("payMethod", payMethod);
        return R.ok().put("data", data);
    }

    @RequestMapping("/payCallback")
    @Transactional
    @SysLog("资料购买支付确认")
    public R payCallback(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        String orderNo = params.get("orderNo") != null ? params.get("orderNo").toString() : "";
        if (StringUtils.isBlank(orderNo)) {
            return R.error("订单号不能为空");
        }

        ZiliaogoumaiEntity order = ziliaogoumaiService.selectOne(
            new EntityWrapper<ZiliaogoumaiEntity>().eq("dingdanhao", orderNo));
        if (order == null) {
            return R.error("订单不存在");
        }
        if ("已支付".equals(order.getZhuangtai())) {
            return R.ok().put("data", "already_paid");
        }

        order.setZhuangtai("已支付");
        ziliaogoumaiService.updateById(order);

        Map<String, Object> data = new HashMap<>();
        data.put("status", "success");
        data.put("orderNo", orderNo);
        data.put("amount", order.getJiage());
        data.put("ziliaobiaoti", order.getZiliaobiaoti());
        return R.ok().put("data", data);
    }

    @RequestMapping("/queryOrder")
    public R queryOrder(@RequestParam String orderNo, HttpServletRequest request) {
        ZiliaogoumaiEntity order = ziliaogoumaiService.selectOne(
            new EntityWrapper<ZiliaogoumaiEntity>().eq("dingdanhao", orderNo));
        if (order == null) {
            return R.error("订单不存在");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("status", order.getZhuangtai());
        data.put("orderNo", orderNo);
        return R.ok().put("data", data);
    }
}
