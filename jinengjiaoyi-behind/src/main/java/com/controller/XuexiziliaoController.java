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
import com.utils.UserBasedCollaborativeFiltering;
import com.algorithm.recommend.RecommendAlgorithmFactory;

import com.entity.XuexiziliaoEntity;
import com.entity.view.XuexiziliaoView;

import com.service.XuexiziliaoService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 学习资料
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:55
 */
@RestController
@RequestMapping("/xuexiziliao")
public class XuexiziliaoController {
    @Autowired
    private XuexiziliaoService xuexiziliaoService;

    @Autowired
    private StoreupService storeupService;

    @Autowired
    private JinengxuqiuService jinengxuqiuService;

    @Autowired
    private YonghuService yonghuService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,XuexiziliaoEntity xuexiziliao,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();


        //查询结果
		PageUtils page = xuexiziliaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xuexiziliao), params), params));
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
    public R list(@RequestParam Map<String, Object> params,XuexiziliaoEntity xuexiziliao,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fabushijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fabushijianend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date clicktimestart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date clicktimeend,
                @RequestParam(required = false) Double clicknumstart,
                @RequestParam(required = false) Double clicknumend,
                @RequestParam(required = false) Double discussnumstart,
                @RequestParam(required = false) Double discussnumend,
                @RequestParam(required = false) Double storeupnumstart,
                @RequestParam(required = false) Double storeupnumend,
		HttpServletRequest request){
        //设置查询条件
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        if(fabushijianstart!=null) ew.ge("fabushijian", fabushijianstart);
        if(fabushijianend!=null) ew.le("fabushijian", fabushijianend);
        if(clicktimestart!=null) ew.ge("clicktime", clicktimestart);
        if(clicktimeend!=null) ew.le("clicktime", clicktimeend);
        if(clicknumstart!=null) ew.ge("clicknum", clicknumstart);
        if(clicknumend!=null) ew.le("clicknum", clicknumend);
        if(discussnumstart!=null) ew.ge("discussnum", discussnumstart);
        if(discussnumend!=null) ew.le("discussnum", discussnumend);
        if(storeupnumstart!=null) ew.ge("storeupnum", storeupnumstart);
        if(storeupnumend!=null) ew.le("storeupnum", storeupnumend);

        //查询结果
		PageUtils page = xuexiziliaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xuexiziliao), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( XuexiziliaoEntity xuexiziliao){
       	EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
      	ew.allEq(MPUtil.allEQMapPre( xuexiziliao, "xuexiziliao"));
        return R.ok().put("data", xuexiziliaoService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(XuexiziliaoEntity xuexiziliao){
        EntityWrapper< XuexiziliaoEntity> ew = new EntityWrapper< XuexiziliaoEntity>();
 		ew.allEq(MPUtil.allEQMapPre( xuexiziliao, "xuexiziliao"));
		XuexiziliaoView xuexiziliaoView =  xuexiziliaoService.selectView(ew);
		return R.ok("查询学习资料成功").put("data", xuexiziliaoView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        XuexiziliaoEntity xuexiziliao = xuexiziliaoService.selectById(id);
        if(null==xuexiziliao.getClicknum()){
            xuexiziliao.setClicknum(0);
        }
		xuexiziliao.setClicknum(xuexiziliao.getClicknum()+1);
		xuexiziliaoService.updateById(xuexiziliao);
        xuexiziliao = xuexiziliaoService.selectView(new EntityWrapper<XuexiziliaoEntity>().eq("id", id));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(xuexiziliao,deSens);
        return R.ok().put("data", xuexiziliao);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        XuexiziliaoEntity xuexiziliao = xuexiziliaoService.selectById(id);
        if(null==xuexiziliao.getClicknum()){
            xuexiziliao.setClicknum(0);
        }
		xuexiziliao.setClicknum(xuexiziliao.getClicknum()+1);
		xuexiziliaoService.updateById(xuexiziliao);
        xuexiziliao = xuexiziliaoService.selectView(new EntityWrapper<XuexiziliaoEntity>().eq("id", id));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(xuexiziliao,deSens);
        return R.ok().put("data", xuexiziliao);
    }




    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增学习资料")
    public R save(@RequestBody XuexiziliaoEntity xuexiziliao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xuexiziliao);
        xuexiziliaoService.insert(xuexiziliao);
        return R.ok().put("data",xuexiziliao.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增学习资料")
    @RequestMapping("/add")
    public R add(@RequestBody XuexiziliaoEntity xuexiziliao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xuexiziliao);
        xuexiziliaoService.insert(xuexiziliao);
        return R.ok().put("data",xuexiziliao.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改学习资料")
    public R update(@RequestBody XuexiziliaoEntity xuexiziliao, HttpServletRequest request){
        //ValidatorUtils.validateEntity(xuexiziliao);
        //全部更新
        xuexiziliaoService.updateById(xuexiziliao);
        return R.ok();
    }





    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除学习资料")
    public R delete(@RequestBody Long[] ids){
        xuexiziliaoService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

	/**
     * 前台智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,XuexiziliaoEntity xuexiziliao, HttpServletRequest request,String pre){
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        // 组装参数
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicknum");
        params.put("order", "desc");

		PageUtils page = xuexiziliaoService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xuexiziliao), params), params));
        return R.ok().put("data", page);
    }



    /**
     * 基于用户需求的协同过滤推荐学习资料
     *
     * 从用户的需求分类中提取兴趣向量，找到相似用户，
     * 推荐相似用户收藏过的学习资料及匹配需求分类的热门资料
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params, XuexiziliaoEntity xuexiziliao, HttpServletRequest request) {
        String userId = request.getSession().getAttribute("userId").toString();
        String username = (String) request.getSession().getAttribute("username");
        Integer limit = params.get("limit") == null ? 10 : Integer.parseInt(params.get("limit").toString());

        List<Long> recommendations = new ArrayList<>();

        try {
            // 获取所有用户需求，构建需求分类向量
            List<JinengxuqiuEntity> allNeeds = jinengxuqiuService.selectList(
                new EntityWrapper<JinengxuqiuEntity>().eq("leixing", "需求"));

            Map<String, Map<String, Double>> userNeedVectors = new HashMap<>();
            for (JinengxuqiuEntity need : allNeeds) {
                String uname = need.getYonghuzhanghao();
                String cat = need.getJinengfenlei();
                if (uname != null && cat != null && !cat.isEmpty()) {
                    userNeedVectors.computeIfAbsent(uname, k -> new HashMap<>())
                        .merge(cat, 1.0, Double::sum);
                }
            }

            Map<String, Double> myVector = userNeedVectors.get(username);
            if (myVector != null && !myVector.isEmpty()) {
                // 计算用户相似度
                List<Map.Entry<String, Double>> similarUsers = new ArrayList<>();
                for (Map.Entry<String, Map<String, Double>> entry : userNeedVectors.entrySet()) {
                    if (entry.getKey().equals(username)) continue;
                    double sim = needCosineSimilarity(myVector, entry.getValue());
                    if (sim > 0) {
                        similarUsers.add(new AbstractMap.SimpleEntry<>(entry.getKey(), sim));
                    }
                }
                similarUsers.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

                int topK = Math.min(similarUsers.size(), 20);
                Map<Long, Double> materialScores = new LinkedHashMap<>();

                Set<Long> myFavorites = storeupService.selectList(
                    new EntityWrapper<StoreupEntity>()
                        .eq("userid", Long.parseLong(userId))
                        .eq("type", 1).eq("tablename", "xuexiziliao")
                ).stream().map(StoreupEntity::getRefid).collect(Collectors.toSet());

                Set<String> simUsernames = new HashSet<>();
                Map<String, Double> simMap = new HashMap<>();
                for (int i = 0; i < topK; i++) {
                    simUsernames.add(similarUsers.get(i).getKey());
                    simMap.put(similarUsers.get(i).getKey(), similarUsers.get(i).getValue());
                }

                if (!simUsernames.isEmpty()) {
                    List<YonghuEntity> simYonghus = yonghuService.selectList(
                        new EntityWrapper<YonghuEntity>().in("yonghuzhanghao", simUsernames));
                    Map<Long, String> idToName = new HashMap<>();
                    for (YonghuEntity u : simYonghus) {
                        idToName.put(u.getId(), u.getYonghuzhanghao());
                    }

                    // 相似用户收藏的学习资料
                    List<StoreupEntity> simFavs = storeupService.selectList(
                        new EntityWrapper<StoreupEntity>()
                            .in("userid", idToName.keySet())
                            .eq("type", 1).eq("tablename", "xuexiziliao"));
                    for (StoreupEntity fav : simFavs) {
                        if (!myFavorites.contains(fav.getRefid())) {
                            String uname = idToName.get(fav.getUserid());
                            double sim = simMap.getOrDefault(uname, 0.0);
                            materialScores.merge(fav.getRefid(), sim * 2.0, Double::sum);
                        }
                    }
                }

                // 按需求分类推荐匹配的学习资料（ziliaoleixing对应jinengfenlei）
                List<XuexiziliaoEntity> catMaterials = xuexiziliaoService.selectList(
                    new EntityWrapper<XuexiziliaoEntity>()
                        .in("ziliaoleixing", myVector.keySet())
                        .orderBy("clicknum", false)
                        .last("limit " + (limit * 3)));
                for (XuexiziliaoEntity m : catMaterials) {
                    materialScores.merge(m.getId(), 0.5, Double::sum);
                }

                recommendations = materialScores.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .limit(limit)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

                System.out.println("[学习资料需求CF] 用户 " + username + " 推荐结果数: " + recommendations.size());
            }

            // 收藏行为CF补充
            if (recommendations.size() < limit) {
                List<RecommendAlgorithmFactory.UserBehavior> userBehaviors = storeupService.selectList(
                    new EntityWrapper<StoreupEntity>().eq("type", 1).eq("tablename", "xuexiziliao")
                ).stream().map(s -> new RecommendAlgorithmFactory.UserBehavior(s.getUserid(), s.getRefid()))
                    .collect(Collectors.toList());
                if (!userBehaviors.isEmpty()) {
                    List<Long> cfRecs = RecommendAlgorithmFactory.buildUserItemMatrix(userBehaviors)
                        .recommendItems(Long.parseLong(userId), limit);
                    Set<Long> existing = new HashSet<>(recommendations);
                    for (Long id : cfRecs) {
                        if (!existing.contains(id) && recommendations.size() < limit) {
                            recommendations.add(id);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[学习资料推荐异常] " + e.getMessage());
            e.printStackTrace();
        }

        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        if (recommendations != null && recommendations.size() > 0) {
            ew.in("id", recommendations);
            ew.last("order by FIELD(id, " + recommendations.stream()
                .map(String::valueOf).collect(Collectors.joining(",")) + ")");
        }

        PageUtils page = xuexiziliaoService.queryPage(params,
            MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xuexiziliao), params), params));
        List<XuexiziliaoEntity> pageList = (List<XuexiziliaoEntity>) page.getList();

        if (pageList.size() < limit) {
            int toAddNum = limit - pageList.size();
            Set<Long> existingIds = pageList.stream().map(XuexiziliaoEntity::getId).collect(Collectors.toSet());
            if (recommendations != null && !recommendations.isEmpty()) existingIds.addAll(recommendations);
            EntityWrapper<XuexiziliaoEntity> fallbackEw = new EntityWrapper<XuexiziliaoEntity>();
            if (!existingIds.isEmpty()) fallbackEw.notIn("id", existingIds);
            fallbackEw.orderBy("clicknum", false);
            fallbackEw.last("limit " + toAddNum);
            pageList.addAll(xuexiziliaoService.selectList(fallbackEw));
        } else if (pageList.size() > limit) {
            pageList = pageList.subList(0, limit);
        }
        page.setList(pageList);
        return R.ok().put("data", page);
    }

    private double needCosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> commonKeys = new HashSet<>(vec1.keySet());
        commonKeys.retainAll(vec2.keySet());
        if (commonKeys.isEmpty()) return 0.0;
        double dotProduct = 0.0;
        for (String key : commonKeys) dotProduct += vec1.get(key) * vec2.get(key);
        double norm1 = Math.sqrt(vec1.values().stream().mapToDouble(v -> v * v).sum());
        double norm2 = Math.sqrt(vec2.values().stream().mapToDouble(v -> v * v).sum());
        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dotProduct / (norm1 * norm2);
    }



    /**
     * （按值统计）
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}")
    public R value(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, @RequestParam(required = false, defaultValue = "总和") String func, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_xuexiziliao_" + xColumnName + "_" + yColumnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        //构建查询统计条件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("method", func);
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                ew.orderBy(true, orderType.equals("x") ? xColumnName : yColumnName, order.equals("desc"));
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }

        //获取结果
        List<Map<String, Object>> result = xuexiziliaoService.selectValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计(多)）
     */
    @RequestMapping("/valueMul/{xColumnName}")
    public R valueMul(@PathVariable("xColumnName") String xColumnName,@RequestParam String yColumnNameMul, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request)  throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_xuexiziliao_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        //构建查询统计条件
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                ew.orderBy(true, orderType.equals("x") ? Arrays.asList(xColumnName) : Arrays.asList(yColumnNames), order.equals("desc"));
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = xuexiziliaoService.selectValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * （按值统计）时间统计类型
     */
    @RequestMapping("/value/{xColumnName}/{yColumnName}/{timeStatType}")
    public R valueDay(@PathVariable("yColumnName") String yColumnName, @PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, @RequestParam(required = false, defaultValue = "总和") String func, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_xuexiziliao_" + xColumnName + "_" + yColumnName + "_"+timeStatType+".json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("timeStatType", timeStatType);
        params.put("method", func);
        //构建查询统计条件
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                ew.orderBy(true, orderType.equals("x") ? xColumnName : yColumnName, order.equals("desc"));
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<Map<String, Object>> result = xuexiziliaoService.selectTimeStatValue(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * （按值统计）时间统计类型(多)
     */
    @RequestMapping("/valueMul/{xColumnName}/{timeStatType}")
    public R valueMulDay(@PathVariable("xColumnName") String xColumnName, @PathVariable("timeStatType") String timeStatType, @RequestParam String yColumnNameMul, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("value_xuexiziliao_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + ".json");
        if (java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        //构建查询统计条件
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        String order = request.getParameter("order");
        if (StringUtils.isNotBlank(order)) {
            String orderType = request.getParameter("orderType");
            if (null != orderType) {
                ew.orderBy(true, orderType.equals("x") ? Arrays.asList(xColumnName) : Arrays.asList(yColumnNames), order.equals("desc"));
            }
        }
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<List<Map<String, Object>>> result2 = new ArrayList<List<Map<String,Object>>>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = xuexiziliaoService.selectTimeStatValue(params, ew);
            for(Map<String, Object> m : result) {
                for(String k : m.keySet()) {
                    if(m.get(k) instanceof Date) {
                        m.put(k, sdf.format((Date)m.get(k)));
                    }
                }
            }
            result2.add(result);
        }
        return R.ok().put("data", result2);
    }

    /**
     * 分组统计
     */
    @RequestMapping("/group/{columnName}")
    public R group(@PathVariable("columnName") String columnName, @RequestParam(required = false) String conditionColumn, @RequestParam(required = false) String conditionValue, HttpServletRequest request) throws IOException {
        //读取文件，如果文件存在，则优先返回文件内容
        java.nio.file.Path path = java.nio.file.Paths.get("group_xuexiziliao_" + columnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)){
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        //构建查询统计条件
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        if(StringUtils.isNotBlank(conditionColumn)&&StringUtils.isNotBlank(conditionValue))
        {
            String[] conditionColumns = conditionColumn.split(";");
            String[] conditionValues = conditionValue.split(";");

            for (int i = 0; i < conditionColumns.length; i++) {
                String column = conditionColumns[i];
                String value = conditionValues[i];

                // 处理范围查询：如果列名包含逗号，表示是范围查询
                if (column.contains(",")) {
                    String[] rangeColumns = column.split(",");
                    String[] rangeValues = value.split(",");

                    if (rangeColumns.length == 2 && rangeValues.length == 2) {
                        // 第一个列名使用 >= 条件
                        ew.ge(rangeColumns[0], rangeValues[0]);
                        // 第二个列名使用 <= 条件
                        ew.le(rangeColumns[1], rangeValues[1]);
                    }
                } else {
                    // 普通等值查询
                    ew.eq(column, value);
                }
            }
        }
        List<Map<String, Object>> result = xuexiziliaoService.selectGroup(params, ew);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map<String, Object> m : result) {
            for(String k : m.keySet()) {
                if(m.get(k) instanceof Date) {
                    m.put(k, sdf.format((Date)m.get(k)));
                }
            }
        }
        return R.ok().put("data", result);
    }




    /**
     * 总数量
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params,XuexiziliaoEntity xuexiziliao, HttpServletRequest request){
        EntityWrapper<XuexiziliaoEntity> ew = new EntityWrapper<XuexiziliaoEntity>();
        int count = xuexiziliaoService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, xuexiziliao), params), params));
        return R.ok().put("data", count);
    }


}
