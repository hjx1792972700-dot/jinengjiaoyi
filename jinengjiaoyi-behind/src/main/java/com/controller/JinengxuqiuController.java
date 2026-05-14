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

import com.entity.JinengxuqiuEntity;
import com.entity.view.JinengxuqiuView;

import com.service.JinengxuqiuService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;
import com.service.YonghuService;
import com.entity.YonghuEntity;

/**
 * 技能需求
 * 后端接口
 * @author 
 * @email 
 * @date 2026-04-12 13:54:54
 */
@RestController
@RequestMapping("/jinengxuqiu")
public class JinengxuqiuController {
    @Autowired
    private JinengxuqiuService jinengxuqiuService;

    @Autowired
    private StoreupService storeupService;

    @Autowired
    private YonghuService yonghuService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,JinengxuqiuEntity jinengxuqiu,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("yonghu")) {
			jinengxuqiu.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
		}

        boolean isAuditSort = "true".equals(params.get("auditSort"));
        if(isAuditSort) {
            params.remove("auditSort");
            params.remove("sort");
            params.remove("order");
        }

        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();

        if(isAuditSort) {
            ew.last("ORDER BY FIELD(sfsh, '待审核', '否', '是'), id DESC");
        }

		PageUtils page = jinengxuqiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengxuqiu), params), params));
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
    public R list(@RequestParam Map<String, Object> params,JinengxuqiuEntity jinengxuqiu,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fabushijianstart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fabushijianend,
                @RequestParam(required = false) Double thumbsupnumstart,
                @RequestParam(required = false) Double thumbsupnumend,
                @RequestParam(required = false) Double crazilynumstart,
                @RequestParam(required = false) Double crazilynumend,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date clicktimestart,
                @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date clicktimeend,
                @RequestParam(required = false) Double clicknumstart,
                @RequestParam(required = false) Double clicknumend,
                @RequestParam(required = false) Double discussnumstart,
                @RequestParam(required = false) Double discussnumend,
                @RequestParam(required = false) Double totalscorestart,
                @RequestParam(required = false) Double totalscoreend,
                @RequestParam(required = false) Double storeupnumstart,
                @RequestParam(required = false) Double storeupnumend,
		HttpServletRequest request){
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
        ew.eq("sfsh", "是");
        if(fabushijianstart!=null) ew.ge("fabushijian", fabushijianstart);
        if(fabushijianend!=null) ew.le("fabushijian", fabushijianend);
        if(thumbsupnumstart!=null) ew.ge("thumbsupnum", thumbsupnumstart);
        if(thumbsupnumend!=null) ew.le("thumbsupnum", thumbsupnumend);
        if(crazilynumstart!=null) ew.ge("crazilynum", crazilynumstart);
        if(crazilynumend!=null) ew.le("crazilynum", crazilynumend);
        if(clicktimestart!=null) ew.ge("clicktime", clicktimestart);
        if(clicktimeend!=null) ew.le("clicktime", clicktimeend);
        if(clicknumstart!=null) ew.ge("clicknum", clicknumstart);
        if(clicknumend!=null) ew.le("clicknum", clicknumend);
        if(discussnumstart!=null) ew.ge("discussnum", discussnumstart);
        if(discussnumend!=null) ew.le("discussnum", discussnumend);
        if(totalscorestart!=null) ew.ge("totalscore", totalscorestart);
        if(totalscoreend!=null) ew.le("totalscore", totalscoreend);
        if(storeupnumstart!=null) ew.ge("storeupnum", storeupnumstart);
        if(storeupnumend!=null) ew.le("storeupnum", storeupnumend);

        //查询结果
		PageUtils page = jinengxuqiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengxuqiu), params), params));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }




	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( JinengxuqiuEntity jinengxuqiu){
       	EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( jinengxuqiu, "jinengxuqiu"));
        return R.ok().put("data", jinengxuqiuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(JinengxuqiuEntity jinengxuqiu){
        EntityWrapper< JinengxuqiuEntity> ew = new EntityWrapper< JinengxuqiuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( jinengxuqiu, "jinengxuqiu"));
		JinengxuqiuView jinengxuqiuView =  jinengxuqiuService.selectView(ew);
		return R.ok("查询技能需求成功").put("data", jinengxuqiuView);
    }

    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        JinengxuqiuEntity jinengxuqiu = jinengxuqiuService.selectById(id);
        if(null==jinengxuqiu.getClicknum()){
            jinengxuqiu.setClicknum(0);
        }
		jinengxuqiu.setClicknum(jinengxuqiu.getClicknum()+1);
		jinengxuqiuService.updateById(jinengxuqiu);
        jinengxuqiu = jinengxuqiuService.selectView(new EntityWrapper<JinengxuqiuEntity>().eq("id", id));
        Map<String, String> deSens = new HashMap<>();
        //给需要脱敏的字段脱敏
        DeSensUtil.desensitize(jinengxuqiu,deSens);
        return R.ok().put("data", jinengxuqiu);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id, HttpServletRequest request){
        JinengxuqiuEntity jinengxuqiu = jinengxuqiuService.selectById(id);
        if(jinengxuqiu == null) return R.error("记录不存在");
        if(!"是".equals(jinengxuqiu.getSfsh())) {
            String username = (String) request.getSession().getAttribute("username");
            if(username == null || !username.equals(jinengxuqiu.getYonghuzhanghao())) {
                return R.error("该内容正在审核中，暂不可查看");
            }
        }
        if(null==jinengxuqiu.getClicknum()){
            jinengxuqiu.setClicknum(0);
        }
		jinengxuqiu.setClicknum(jinengxuqiu.getClicknum()+1);
		jinengxuqiuService.updateById(jinengxuqiu);
        jinengxuqiu = jinengxuqiuService.selectView(new EntityWrapper<JinengxuqiuEntity>().eq("id", id));
        Map<String, String> deSens = new HashMap<>();
        DeSensUtil.desensitize(jinengxuqiu,deSens);
        return R.ok().put("data", jinengxuqiu);
    }



    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    public R vote(@PathVariable("id") String id,String type){
        JinengxuqiuEntity jinengxuqiu = jinengxuqiuService.selectById(id);
        if(type.equals("1")) {
        	jinengxuqiu.setThumbsupnum(jinengxuqiu.getThumbsupnum()+1);
        } else {
        	jinengxuqiu.setCrazilynum(jinengxuqiu.getCrazilynum()+1);
        }
        jinengxuqiuService.updateById(jinengxuqiu);
        return R.ok("投票成功");
    }

    /**
     * 后台保存
     */
    @RequestMapping("/save")
    @SysLog("新增技能需求")
    public R save(@RequestBody JinengxuqiuEntity jinengxuqiu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jinengxuqiu);
        jinengxuqiuService.insert(jinengxuqiu);
        return R.ok().put("data",jinengxuqiu.getId());
    }

    /**
     * 前台保存
     */
    @SysLog("新增技能需求")
    @RequestMapping("/add")
    public R add(@RequestBody JinengxuqiuEntity jinengxuqiu, HttpServletRequest request){
        if("技能".equals(jinengxuqiu.getLeixing())) {
            String username = (String) request.getSession().getAttribute("username");
            if(username != null) {
                YonghuEntity yonghu = yonghuService.selectOne(
                    new EntityWrapper<YonghuEntity>().eq("yonghuzhanghao", username));
                if(yonghu == null || !"是".equals(yonghu.getVip())) {
                    return R.error("仅VIP用户可以发布技能，请先开通VIP");
                }
            }
        }
        jinengxuqiu.setSfsh("待审核");
        jinengxuqiuService.insert(jinengxuqiu);
        return R.ok().put("data",jinengxuqiu.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改技能需求")
    public R update(@RequestBody JinengxuqiuEntity jinengxuqiu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(jinengxuqiu);
        //全部更新
        jinengxuqiuService.updateById(jinengxuqiu);
        return R.ok();
    }

    /**
     * 审核
     */
    @RequestMapping("/shBatch")
    @Transactional
    @SysLog("审核技能需求")
    public R update(@RequestBody Long[] ids, @RequestParam String sfsh, @RequestParam String shhf){
        List<JinengxuqiuEntity> list = new ArrayList<JinengxuqiuEntity>();
        for(Long id : ids) {
            JinengxuqiuEntity jinengxuqiu = jinengxuqiuService.selectById(id);
            jinengxuqiu.setSfsh(sfsh);
            jinengxuqiu.setShhf(shhf);
            list.add(jinengxuqiu);
        }
        jinengxuqiuService.updateBatchById(list);
        return R.ok();
    }




    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除技能需求")
    public R delete(@RequestBody Long[] ids){
        jinengxuqiuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

	/**
     * 前台智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,JinengxuqiuEntity jinengxuqiu, HttpServletRequest request,String pre){
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
        ew.eq("sfsh", "是");
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
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

		PageUtils page = jinengxuqiuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengxuqiu), params), params));
        return R.ok().put("data", page);
    }


    /**
     * 训练推荐，模型名称(随机森林、NCF)
     *
     * @return
     */
    @RequestMapping("/trainRecommend")
    public R trainRecommend() throws Exception {
        Map<String, Map<String, Object>> features = new HashMap<>();
        // 角色特征字段
        String userFeaturesFields = "[xingbie, age]".replaceAll("\\[", "").replaceAll("\\]", "");
        List<Map<String, Object>> userDataList = (List<Map<String, Object>>) SpringContextUtils.getBeanMethod("yonghuService", "selectMaps", new Class[]{Wrapper.class}, new Object[]{new EntityWrapper<>().setSqlSelect("id as userId,"+userFeaturesFields)});
        if (userDataList.isEmpty())  return R.ok("模型训练完成");
        // 目标特征字段
        String itemFeaturesFields = "[xuqiubiaoti, jinengfenlei]".replaceAll("\\[", "").replaceAll("\\]", "");
        List<Map<String, Object>> itemDataList = jinengxuqiuService.selectMaps(new EntityWrapper<JinengxuqiuEntity>().setSqlSelect("id as itemId,"+itemFeaturesFields));
        if (itemDataList.isEmpty())  return R.ok("模型训练完成");

        //用户行为数据
        List<RecommendAlgorithmFactory.UserBehavior> userBehaviors = new ArrayList<>();
        // 查询收藏/关注数据
        userBehaviors.addAll(storeupService.selectList(new EntityWrapper<StoreupEntity>().eq("type", 1 ).eq("tablename", "jinengxuqiu")).stream().map(storeup -> {
            return new RecommendAlgorithmFactory.UserBehavior(storeup.getUserid(), storeup.getRefid());
        }).collect(Collectors.toList()));
        if (userBehaviors.isEmpty())  return R.ok("模型训练完成");
        // 3. 训练模型
        RecommendAlgorithmFactory.buildAlgorithm("NCF深度学习", "jinengxuqiu").train(userDataList, itemDataList, userBehaviors);
        return R.ok("模型训练完成");
    }

    /**
     * 基于用户需求的协同过滤推荐算法
     *
     * 核心思路：
     * 1. 从jinengxuqiu表中提取所有用户的"需求"记录，按技能分类构建用户需求向量
     * 2. 用余弦相似度计算当前用户与其他用户的相似度
     * 3. 从相似用户的收藏/点赞行为中推荐技能
     * 4. 同时推荐匹配当前用户需求分类的热门技能
     * 5. 当用户修改需求后，向量变化 → 相似度变化 → 推荐结果自动更新
     */
    @RequestMapping("/autoSort2")
    public R autoSort2(@RequestParam Map<String, Object> params, JinengxuqiuEntity jinengxuqiu, HttpServletRequest request) {
        String userId = request.getSession().getAttribute("userId").toString();
        String username = (String) request.getSession().getAttribute("username");
        Integer limit = params.get("limit") == null ? 10 : Integer.parseInt(params.get("limit").toString());

        List<Long> recommendations = new ArrayList<>();

        try {
            // ===== 第一阶段：基于用户需求的协同过滤 =====
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
                // 计算与其他用户的余弦相似度并排序
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
                Map<Long, Double> skillScores = new LinkedHashMap<>();

                // 获取当前用户已收藏的技能ID
                Set<Long> myFavorites = storeupService.selectList(
                    new EntityWrapper<StoreupEntity>()
                        .eq("userid", Long.parseLong(userId))
                        .eq("type", 1).eq("tablename", "jinengxuqiu")
                ).stream().map(StoreupEntity::getRefid).collect(Collectors.toSet());

                // 收集相似用户的username→userId映射
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

                    // 相似用户的收藏行为 → 推荐权重较高
                    List<StoreupEntity> simFavs = storeupService.selectList(
                        new EntityWrapper<StoreupEntity>()
                            .in("userid", idToName.keySet())
                            .eq("type", 1).eq("tablename", "jinengxuqiu"));
                    for (StoreupEntity fav : simFavs) {
                        if (!myFavorites.contains(fav.getRefid())) {
                            String uname = idToName.get(fav.getUserid());
                            double sim = simMap.getOrDefault(uname, 0.0);
                            skillScores.merge(fav.getRefid(), sim * 2.0, Double::sum);
                        }
                    }

                    // 相似用户的点赞行为
                    List<StoreupEntity> simLikes = storeupService.selectList(
                        new EntityWrapper<StoreupEntity>()
                            .in("userid", idToName.keySet())
                            .eq("type", 21).eq("tablename", "jinengxuqiu"));
                    for (StoreupEntity like : simLikes) {
                        if (!myFavorites.contains(like.getRefid())) {
                            String uname = idToName.get(like.getUserid());
                            double sim = simMap.getOrDefault(uname, 0.0);
                            skillScores.merge(like.getRefid(), sim * 1.0, Double::sum);
                        }
                    }
                }

                // 补充：按当前用户需求分类推荐已审核的热门技能
                List<JinengxuqiuEntity> catSkills = jinengxuqiuService.selectList(
                    new EntityWrapper<JinengxuqiuEntity>()
                        .in("jinengfenlei", myVector.keySet())
                        .eq("sfsh", "是")
                        .ne("yonghuzhanghao", username)
                        .orderBy("clicknum", false)
                        .last("limit " + (limit * 3)));
                for (JinengxuqiuEntity skill : catSkills) {
                    skillScores.merge(skill.getId(), 0.5, Double::sum);
                }

                recommendations = skillScores.entrySet().stream()
                    .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
                    .limit(limit)
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

                System.out.println("[需求协同过滤] 用户 " + username + " 的需求分类: " + myVector.keySet()
                    + ", 相似用户数: " + topK + ", 推荐结果数: " + recommendations.size());
            }

            // ===== 第二阶段：如果需求CF不足，用传统收藏行为CF补充 =====
            if (recommendations.size() < limit) {
                List<RecommendAlgorithmFactory.UserBehavior> userBehaviors = new ArrayList<>();
                userBehaviors.addAll(storeupService.selectList(
                    new EntityWrapper<StoreupEntity>().eq("type", 1).eq("tablename", "jinengxuqiu")
                ).stream().map(s -> new RecommendAlgorithmFactory.UserBehavior(s.getUserid(), s.getRefid()))
                    .collect(Collectors.toList()));
                userBehaviors.addAll(storeupService.selectList(
                    new EntityWrapper<StoreupEntity>().eq("type", 21).eq("tablename", "jinengxuqiu")
                ).stream().map(s -> new RecommendAlgorithmFactory.UserBehavior(s.getUserid(), s.getRefid()))
                    .collect(Collectors.toList()));

                if (!userBehaviors.isEmpty()) {
                    try {
                        RecommendAlgorithmFactory.buildUserItemMatrix(userBehaviors);
                        List<Long> cfRecs = RecommendAlgorithmFactory.loadModel("jinengxuqiu.model")
                            .recommendItems(Long.parseLong(userId), limit);
                        Set<Long> existing = new HashSet<>(recommendations);
                        for (Long id : cfRecs) {
                            if (!existing.contains(id) && recommendations.size() < limit) {
                                recommendations.add(id);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("[收藏CF补充] 模型加载失败: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[推荐异常] " + e.getMessage());
            e.printStackTrace();
        }

        // ===== 构建返回结果 =====
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
        if (recommendations != null && recommendations.size() > 0) {
            ew.in("id", recommendations);
            ew.last("order by FIELD(id, " + recommendations.stream()
                .map(String::valueOf).collect(Collectors.joining(",")) + ")");
        }

        PageUtils page = jinengxuqiuService.queryPage(params,
            MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengxuqiu), params), params));
        List<JinengxuqiuEntity> pageList = (List<JinengxuqiuEntity>) page.getList();

        // 如果推荐结果不足，用最新已审核技能补充
        if (pageList.size() < limit) {
            int toAddNum = limit - pageList.size();
            Set<Long> existingIds = pageList.stream().map(JinengxuqiuEntity::getId).collect(Collectors.toSet());
            if (recommendations != null && !recommendations.isEmpty()) existingIds.addAll(recommendations);
            EntityWrapper<JinengxuqiuEntity> fallbackEw = new EntityWrapper<JinengxuqiuEntity>();
            fallbackEw.eq("sfsh", "是");
            if (!existingIds.isEmpty()) fallbackEw.notIn("id", existingIds);
            fallbackEw.orderBy("clicknum", false);
            fallbackEw.last("limit " + toAddNum);
            pageList.addAll(jinengxuqiuService.selectList(fallbackEw));
        } else if (pageList.size() > limit) {
            pageList = pageList.subList(0, limit);
        }
        page.setList(pageList);
        return R.ok().put("data", page);
    }

    /**
     * 余弦相似度计算：比较两个用户的需求分类向量
     */
    private double needCosineSimilarity(Map<String, Double> vec1, Map<String, Double> vec2) {
        Set<String> commonKeys = new HashSet<>(vec1.keySet());
        commonKeys.retainAll(vec2.keySet());
        if (commonKeys.isEmpty()) return 0.0;

        double dotProduct = 0.0;
        for (String key : commonKeys) {
            dotProduct += vec1.get(key) * vec2.get(key);
        }
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
        java.nio.file.Path path = java.nio.file.Paths.get("value_jinengxuqiu_" + xColumnName + "_" + yColumnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        //构建查询统计条件
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("yColumn", yColumnName);
        params.put("method", func);
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
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
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            ew.eq("yonghuzhanghao", (String)request.getSession().getAttribute("username"));
        }

        //获取结果
        List<Map<String, Object>> result = jinengxuqiuService.selectValue(params, ew);
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
        java.nio.file.Path path = java.nio.file.Paths.get("value_jinengxuqiu_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + "_timeType.json");
        if(java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        //构建查询统计条件
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
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

        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            ew.eq("yonghuzhanghao", (String)request.getSession().getAttribute("username"));
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = jinengxuqiuService.selectValue(params, ew);
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
        java.nio.file.Path path = java.nio.file.Paths.get("value_jinengxuqiu_" + xColumnName + "_" + yColumnName + "_"+timeStatType+".json");
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
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
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
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            ew.eq("yonghuzhanghao", (String)request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = jinengxuqiuService.selectTimeStatValue(params, ew);
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
        java.nio.file.Path path = java.nio.file.Paths.get("value_jinengxuqiu_" + xColumnName + "_" + String.join("_", yColumnNameMul.split(",")) + ".json");
        if (java.nio.file.Files.exists(path)) {
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        String[] yColumnNames = yColumnNameMul.split(",");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("xColumn", xColumnName);
        params.put("timeStatType", timeStatType);
        //构建查询统计条件
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
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
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            ew.eq("yonghuzhanghao", (String)request.getSession().getAttribute("username"));
        }
        for(int i=0;i<yColumnNames.length;i++) {
            params.put("yColumn", yColumnNames[i]);
            List<Map<String, Object>> result = jinengxuqiuService.selectTimeStatValue(params, ew);
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
        java.nio.file.Path path = java.nio.file.Paths.get("group_jinengxuqiu_" + columnName + "_timeType.json");
        if(java.nio.file.Files.exists(path)){
            String content = new String(java.nio.file.Files.readAllBytes(path), java.nio.charset.StandardCharsets.UTF_8);
            return R.ok().put("data", (new org.json.JSONArray(content)).toList());
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("column", columnName);
        //构建查询统计条件
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
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
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            ew.eq("yonghuzhanghao", (String)request.getSession().getAttribute("username"));
        }
        List<Map<String, Object>> result = jinengxuqiuService.selectGroup(params, ew);
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
    public R count(@RequestParam Map<String, Object> params,JinengxuqiuEntity jinengxuqiu, HttpServletRequest request){
        String tableName = request.getSession().getAttribute("tableName").toString();
        if(tableName.equals("yonghu")) {
            jinengxuqiu.setYonghuzhanghao((String)request.getSession().getAttribute("username"));
        }
        EntityWrapper<JinengxuqiuEntity> ew = new EntityWrapper<JinengxuqiuEntity>();
        int count = jinengxuqiuService.selectCount(MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, jinengxuqiu), params), params));
        return R.ok().put("data", count);
    }


}
