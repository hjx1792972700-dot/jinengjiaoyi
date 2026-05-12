#!/usr/bin/env python3
"""
基于协同过滤的技能交易系统 - PC端毕设讲解演示视频生成脚本
专业版：简洁背景 + 真实项目内容 + 案例讲解
"""

import asyncio
import os
import tempfile
import shutil
from pathlib import Path

import edge_tts
import numpy as np
from PIL import Image, ImageDraw, ImageFont
from moviepy import ImageClip, AudioFileClip, concatenate_videoclips


# ============ 配置 ============

OUTPUT_PATH = os.path.expanduser("~/Desktop/毕设演示-技能交易系统.mp4")
TTS_VOICE = "zh-CN-YunxiNeural"
SLIDE_WIDTH = 1920
SLIDE_HEIGHT = 1080
FPS = 24

FONT_PATHS = [
    "/System/Library/Fonts/PingFang.ttc",
    "/System/Library/Fonts/STHeiti Light.ttc",
    "/System/Library/Fonts/STHeiti Medium.ttc",
    "/Library/Fonts/Arial Unicode.ttf",
    "/System/Library/Fonts/Hiragino Sans GB.ttc",
]

MONO_FONT_PATHS = [
    "/System/Library/Fonts/SFNSMono.ttf",
    "/System/Library/Fonts/Menlo.ttc",
    "/System/Library/Fonts/Monaco.ttf",
    "/Library/Fonts/Courier New.ttf",
]


def get_font(size):
    for fp in FONT_PATHS:
        if os.path.exists(fp):
            try:
                return ImageFont.truetype(fp, size, index=0)
            except Exception:
                continue
    return ImageFont.load_default()


def get_mono_font(size):
    for fp in MONO_FONT_PATHS:
        if os.path.exists(fp):
            try:
                return ImageFont.truetype(fp, size, index=0)
            except Exception:
                continue
    return get_font(size)


# ============ 配色 ============

COLORS = {
    "bg_dark": (18, 18, 24),
    "bg_card": (28, 30, 38),
    "bg_code": (35, 38, 48),
    "text_white": (240, 240, 245),
    "text_gray": (160, 165, 180),
    "text_dim": (110, 115, 130),
    "accent_blue": (66, 133, 244),
    "accent_green": (52, 199, 89),
    "accent_orange": (255, 159, 10),
    "accent_purple": (175, 82, 222),
    "accent_red": (255, 69, 58),
    "border": (50, 54, 66),
}


# ============ 简洁背景 ============

def create_clean_bg():
    """纯净深色背景，仅有极细微的渐变"""
    img = Image.new("RGB", (SLIDE_WIDTH, SLIDE_HEIGHT))
    pixels = img.load()
    base = COLORS["bg_dark"]
    for y in range(SLIDE_HEIGHT):
        ratio = y / SLIDE_HEIGHT
        r = int(base[0] + 6 * ratio)
        g = int(base[1] + 6 * ratio)
        b = int(base[2] + 10 * ratio)
        for x in range(SLIDE_WIDTH):
            pixels[x, y] = (r, g, b)
    return img


def draw_top_bar(draw, accent_color):
    """顶部细彩条"""
    draw.rectangle([(0, 0), (SLIDE_WIDTH, 4)], fill=accent_color)


def draw_card(draw, x, y, w, h, fill=None):
    """绘制圆角卡片区域"""
    fill = fill or COLORS["bg_card"]
    draw.rounded_rectangle([(x, y), (x + w, y + h)], radius=12, fill=fill)


def draw_code_block(draw, x, y, w, h):
    """绘制代码区域"""
    draw.rounded_rectangle([(x, y), (x + w, y + h)], radius=8, fill=COLORS["bg_code"])
    draw.rounded_rectangle([(x, y), (x + w, y + h)], radius=8, outline=COLORS["border"], width=1)


# ============ 幻灯片渲染 ============

def render_slide(chapter):
    img = create_clean_bg()
    draw = ImageDraw.Draw(img)
    stype = chapter.get("type", "content")

    if stype == "cover":
        return render_cover(draw, img, chapter)
    elif stype == "code":
        return render_code_slide(draw, img, chapter)
    elif stype == "diagram":
        return render_diagram_slide(draw, img, chapter)
    elif stype == "flow":
        return render_flow_slide(draw, img, chapter)
    else:
        return render_content_slide(draw, img, chapter)


def render_cover(draw, img, chapter):
    draw_top_bar(draw, COLORS["accent_blue"])

    title_font = get_font(64)
    sub_font = get_font(32)
    info_font = get_font(26)

    # 中央标题
    title = chapter["title"]
    bbox = draw.textbbox((0, 0), title, font=title_font)
    tw = bbox[2] - bbox[0]
    tx = (SLIDE_WIDTH - tw) // 2
    draw.text((tx, 360), title, font=title_font, fill=COLORS["text_white"])

    # 分隔线
    draw.line([(SLIDE_WIDTH // 2 - 180, 450), (SLIDE_WIDTH // 2 + 180, 450)],
              fill=COLORS["accent_blue"], width=2)

    # 副标题
    sub = chapter["subtitle"]
    bbox2 = draw.textbbox((0, 0), sub, font=sub_font)
    sw = bbox2[2] - bbox2[0]
    draw.text(((SLIDE_WIDTH - sw) // 2, 480), sub, font=sub_font, fill=COLORS["text_gray"])

    # 答辩信息
    info_lines = chapter.get("info", [])
    y = 580
    for line in info_lines:
        bbox3 = draw.textbbox((0, 0), line, font=info_font)
        lw = bbox3[2] - bbox3[0]
        draw.text(((SLIDE_WIDTH - lw) // 2, y), line, font=info_font, fill=COLORS["text_dim"])
        y += 42

    return img


def render_content_slide(draw, img, chapter):
    accent = chapter.get("accent", COLORS["accent_blue"])
    draw_top_bar(draw, accent)

    title_font = get_font(40)
    body_font = get_font(24)
    label_font = get_font(20)

    # 标题
    draw.text((80, 50), chapter["title"], font=title_font, fill=COLORS["text_white"])

    # 内容
    y = 130
    for item in chapter.get("body", []):
        if isinstance(item, dict):
            # 带标签的内容行
            if item.get("type") == "heading":
                y += 10
                draw.text((80, y), item["text"], font=get_font(28), fill=accent)
                y += 44
            elif item.get("type") == "card":
                draw_card(draw, 80, y, SLIDE_WIDTH - 160, item.get("height", 60))
                draw.text((110, y + 15), item["text"], font=body_font, fill=COLORS["text_white"])
                y += item.get("height", 60) + 16
            elif item.get("type") == "label":
                draw.text((100, y), item["text"], font=label_font, fill=COLORS["text_dim"])
                y += 30
        else:
            draw.text((100, y), item, font=body_font, fill=COLORS["text_white"])
            y += 38

    return img


def render_code_slide(draw, img, chapter):
    accent = chapter.get("accent", COLORS["accent_green"])
    draw_top_bar(draw, accent)

    title_font = get_font(38)
    code_font = get_mono_font(18)
    label_font = get_font(18)

    # 标题
    draw.text((80, 45), chapter["title"], font=title_font, fill=COLORS["text_white"])

    # 文件路径标签
    if chapter.get("filepath"):
        draw.text((80, 95), chapter["filepath"], font=label_font, fill=COLORS["text_dim"])

    # 代码区域
    code_y = 130 if not chapter.get("filepath") else 125
    code_lines = chapter.get("code", [])
    code_height = len(code_lines) * 26 + 40
    draw_code_block(draw, 60, code_y, SLIDE_WIDTH - 120, min(code_height, 850))

    # 代码内容
    y = code_y + 20
    for i, line in enumerate(code_lines):
        # 行号
        line_num = str(i + 1).rjust(3)
        draw.text((80, y), line_num, font=code_font, fill=COLORS["text_dim"])
        # 代码着色
        color = colorize_code_line(line)
        draw.text((125, y), line, font=code_font, fill=color)
        y += 26
        if y > SLIDE_HEIGHT - 80:
            break

    return img


def render_diagram_slide(draw, img, chapter):
    accent = chapter.get("accent", COLORS["accent_purple"])
    draw_top_bar(draw, accent)

    title_font = get_font(38)
    box_font = get_font(22)
    label_font = get_font(18)
    arrow_font = get_font(16)

    draw.text((80, 45), chapter["title"], font=title_font, fill=COLORS["text_white"])

    # 绘制方框图
    boxes = chapter.get("boxes", [])
    arrows = chapter.get("arrows", [])

    for box in boxes:
        x, y, w, h = box["rect"]
        color = box.get("color", COLORS["accent_blue"])
        draw.rounded_rectangle([(x, y), (x + w, y + h)], radius=10,
                               fill=(*color, 40) if len(color) == 3 else color,
                               outline=color, width=2)
        # 文本居中
        text = box["text"]
        bbox = draw.textbbox((0, 0), text, font=box_font)
        tw = bbox[2] - bbox[0]
        th = bbox[3] - bbox[1]
        draw.text((x + (w - tw) // 2, y + (h - th) // 2), text, font=box_font, fill=COLORS["text_white"])

        if box.get("sub"):
            sub_bbox = draw.textbbox((0, 0), box["sub"], font=label_font)
            stw = sub_bbox[2] - sub_bbox[0]
            draw.text((x + (w - stw) // 2, y + h // 2 + 18), box["sub"],
                      font=label_font, fill=COLORS["text_gray"])

    for arrow in arrows:
        sx, sy, ex, ey = arrow["line"]
        draw.line([(sx, sy), (ex, ey)], fill=COLORS["text_dim"], width=2)
        # 箭头
        draw.polygon([(ex, ey), (ex - 6, ey - 10), (ex + 6, ey - 10)], fill=COLORS["text_dim"])
        if arrow.get("label"):
            mx, my = (sx + ex) // 2, (sy + ey) // 2
            draw.text((mx + 10, my - 10), arrow["label"], font=arrow_font, fill=COLORS["text_gray"])

    return img


def render_flow_slide(draw, img, chapter):
    accent = chapter.get("accent", COLORS["accent_orange"])
    draw_top_bar(draw, accent)

    title_font = get_font(38)
    step_font = get_font(22)
    desc_font = get_font(18)

    draw.text((80, 45), chapter["title"], font=title_font, fill=COLORS["text_white"])

    steps = chapter.get("steps", [])
    start_y = 140
    step_h = 80
    gap = 20

    for i, step in enumerate(steps):
        y = start_y + i * (step_h + gap)
        # 步骤编号圆
        cx, cy = 120, y + step_h // 2
        draw.ellipse([(cx - 20, cy - 20), (cx + 20, cy + 20)], fill=accent)
        num_bbox = draw.textbbox((0, 0), str(i + 1), font=step_font)
        nw = num_bbox[2] - num_bbox[0]
        draw.text((cx - nw // 2, cy - 12), str(i + 1), font=step_font, fill="white")

        # 步骤卡片
        draw_card(draw, 160, y, SLIDE_WIDTH - 240, step_h)
        draw.text((185, y + 15), step["title"], font=step_font, fill=COLORS["text_white"])
        draw.text((185, y + 48), step["desc"], font=desc_font, fill=COLORS["text_gray"])

        # 连接线
        if i < len(steps) - 1:
            draw.line([(cx, y + step_h), (cx, y + step_h + gap)], fill=COLORS["text_dim"], width=2)

    return img


def colorize_code_line(line):
    """简单的代码着色"""
    stripped = line.strip()
    if stripped.startswith("//") or stripped.startswith("*") or stripped.startswith("/**"):
        return (106, 153, 85)  # green - comments
    if stripped.startswith("@") or stripped.startswith("import") or stripped.startswith("package"):
        return (86, 156, 214)  # blue - annotations/imports
    keywords = ["public", "private", "class", "return", "new", "if", "for", "try", "catch",
                "void", "int", "double", "String", "List", "Map", "Set", "Long"]
    for kw in keywords:
        if kw in stripped.split():
            return (197, 134, 192)  # purple - keywords
    if "=" in stripped or "." in stripped:
        return (220, 220, 200)  # light - expressions
    return COLORS["text_white"]


# ============ 章节定义 ============

CHAPTERS = [
    # ---- 封面 ----
    {
        "title": "基于协同过滤的技能交易系统",
        "subtitle": "毕业设计答辩",
        "info": ["答辩人：XXX", "指导老师：XXX", "2026年5月"],
        "type": "cover",
        "narration": (
            "大家好，我是XXX，我的毕业设计题目是，基于协同过滤的技能交易系统的设计与实现。"
            "下面我将从项目背景、技术架构、数据库设计、核心功能、推荐算法以及系统演示等方面"
            "对本项目进行讲解。"
        ),
    },
    # ---- 项目背景 ----
    {
        "title": "项目背景与选题意义",
        "type": "content",
        "accent": COLORS["accent_blue"],
        "body": [
            {"type": "heading", "text": "研究背景"},
            "• 个人技能已成为知识经济时代的重要可交易资源",
            "• 现有平台（如猪八戒、闲鱼技能类）缺乏精准匹配机制",
            "• 用户面临严重的信息过载问题",
            "",
            {"type": "heading", "text": "解决方案"},
            "• 引入协同过滤推荐算法，基于用户行为实现个性化推荐",
            "• 构建完整的技能交换闭环：发布→匹配→申请→交换→评价",
            "• 多维度相似度计算：需求向量 + 收藏行为 + 模型预测",
            "",
            {"type": "heading", "text": "创新点"},
            "• 需求向量协同过滤：基于用户发布的需求分类构建向量",
            "• 混合推荐策略：UserCF + ItemCF + NCF 三级推荐",
            "• 动态更新：用户修改需求后推荐结果实时变化",
        ],
        "narration": (
            "首先介绍项目背景。在知识经济时代，个人技能已经成为重要的可交易资源。"
            "现有平台如猪八戒、闲鱼技能类，缺乏精准的匹配机制，用户面临信息过载问题。"
            "本系统的解决方案是引入协同过滤推荐算法，基于用户行为实现个性化推荐，"
            "并构建完整的技能交换业务闭环。"
            "本项目的创新点在于：提出了基于需求向量的协同过滤方法，"
            "采用UserCF加ItemCF加NCF的三级混合推荐策略，"
            "并且当用户修改需求后推荐结果能实时更新。"
        ),
    },
    # ---- 技术架构图 ----
    {
        "title": "系统技术架构",
        "type": "diagram",
        "accent": COLORS["accent_blue"],
        "boxes": [
            {"rect": (560, 130, 800, 90), "text": "前端 - Vue 3 + Element Plus + Vite 5",
             "sub": "Hash路由 | Vuex状态管理 | Axios通信", "color": (66, 133, 244)},
            {"rect": (560, 340, 800, 90), "text": "后端 - Spring Boot 2.2.2 + Java 8",
             "sub": "Spring MVC | MyBatis-Plus | Token认证", "color": (52, 199, 89)},
            {"rect": (560, 550, 800, 90), "text": "数据层 - MySQL + MyBatis Mapper",
             "sub": "26张业务表 | Liquibase迁移管理", "color": (255, 159, 10)},
            {"rect": (560, 760, 380, 90), "text": "推荐算法引擎",
             "sub": "UserCF | ItemCF | NCF", "color": (175, 82, 222)},
            {"rect": (980, 760, 380, 90), "text": "WebSocket 通讯",
             "sub": "即时聊天 | AI问答", "color": (255, 69, 58)},
            # 左侧技术标签
            {"rect": (80, 180, 180, 50), "text": "Sass/CSS3", "color": (66, 133, 244)},
            {"rect": (80, 250, 180, 50), "text": "ECharts图表", "color": (66, 133, 244)},
            {"rect": (80, 390, 180, 50), "text": "RESTful API", "color": (52, 199, 89)},
            {"rect": (80, 460, 180, 50), "text": "拦截器认证", "color": (52, 199, 89)},
        ],
        "arrows": [
            {"line": (960, 220, 960, 340), "label": "HTTP/WS"},
            {"line": (960, 430, 960, 550), "label": "JDBC"},
            {"line": (750, 640, 750, 760), "label": ""},
            {"line": (1170, 640, 1170, 760), "label": ""},
        ],
        "narration": (
            "系统采用前后端分离的B/S架构。"
            "前端技术栈为Vue 3框架，搭配Element Plus UI组件库，使用Vite 5构建工具，"
            "采用Hash路由、Vuex状态管理和Axios通信。"
            "后端基于Spring Boot 2.2.2和Java 8，使用Spring MVC处理Web请求，"
            "MyBatis-Plus进行数据库操作，自定义Token机制进行用户身份认证。"
            "数据层使用MySQL数据库，共设计了26张业务表，使用Liquibase进行数据库版本迁移管理。"
            "此外系统还集成了推荐算法引擎，支持用户协同过滤、物品协同过滤和NCF神经网络三种算法，"
            "以及WebSocket通讯模块，支持即时聊天和AI智能问答。"
        ),
    },
    # ---- 项目结构 ----
    {
        "title": "项目工程结构",
        "type": "code",
        "accent": COLORS["accent_blue"],
        "filepath": "jinengjiaoyi/src/main/java/com/",
        "code": [
            "├── SpringbootSchemaApplication.java    // 启动类",
            "├── algorithm/",
            "│   └── recommend/",
            "│       └── RecommendAlgorithmFactory.java  // 推荐算法工厂(1500+行)",
            "├── controller/                         // 29个控制器",
            "│   ├── JinengxuqiuController.java      // 技能需求(含推荐)",
            "│   ├── JiaohuanshenqingController.java  // 交换申请",
            "│   ├── YonghuController.java           // 用户管理",
            "│   ├── XuexiziliaoController.java      // 学习资料",
            "│   ├── ForumController.java            // 社区论坛",
            "│   └── ...共29个",
            "├── entity/                             // 实体类",
            "│   ├── JinengxuqiuEntity.java",
            "│   ├── model/ view/ vo/               // 分层模型",
            "├── dao/                                // 28个DAO接口",
            "├── service/                            // 业务逻辑层",
            "│   └── impl/",
            "├── utils/                              // 工具类",
            "│   └── UserBasedCollaborativeFiltering.java",
            "├── interceptor/                        // Token认证拦截器",
            "├── config/                             // 配置类",
            "├── ws/                                 // WebSocket管理",
            "└── llm/                                // AI大模型客户端",
            "",
            "src/main/resources/",
            "├── front/          // Vue3前端 (用户端)",
            "├── admin/          // Vue3后台 (管理端)",
            "├── mapper/         // 28个MyBatis XML",
            "└── application.yml // 应用配置",
        ],
        "narration": (
            "来看一下项目的工程结构。后端Java代码采用标准的分层架构。"
            "algorithm包下是推荐算法工厂类，有超过1500行代码，实现了多种推荐算法。"
            "controller包有29个控制器，覆盖所有业务模块。"
            "entity包定义实体类，并细分了model、view、vo三层。"
            "dao包有28个数据访问接口，service包实现业务逻辑。"
            "utils包下有协同过滤工具类，interceptor实现Token认证拦截，"
            "ws包处理WebSocket通讯，llm包集成了AI大模型客户端。"
            "在resources目录下，front是用户端前端，admin是管理后台前端，"
            "mapper存放MyBatis的XML映射文件。"
        ),
    },
    # ---- 数据库设计 ----
    {
        "title": "数据库设计 — 核心表结构",
        "type": "code",
        "accent": COLORS["accent_orange"],
        "filepath": "db/jinengjiaoyi.sql — jinengxuqiu 表（技能需求）",
        "code": [
            "CREATE TABLE `jinengxuqiu` (",
            "  `id`              bigint(20) AUTO_INCREMENT  COMMENT '主键',",
            "  `xuqiubianhao`    varchar(200)    COMMENT '需求编号',",
            "  `xuqiubiaoti`     varchar(32)     COMMENT '需求标题',",
            "  `jinengfenlei`    varchar(200)    COMMENT '技能分类',",
            "  `fengmian`        longtext        COMMENT '封面图片',",
            "  `fabushijian`     date            COMMENT '发布时间',",
            "  `xuqiufeiyong`    double          COMMENT '需求费用',",
            "  `xuqiumiaoshu`    longtext        COMMENT '需求描述',",
            "  `yonghuzhanghao`  varchar(16)     COMMENT '用户账号',",
            "  `yonghuxingming`  varchar(16)     COMMENT '用户姓名',",
            "  `xinyuzhishu`     varchar(200)    COMMENT '信誉指数',",
            "  `leixing`         varchar(200)    COMMENT '类型(需求/技能)',",
            "  `sfsh`            varchar(200)    DEFAULT '待审核' COMMENT '审核状态',",
            "  `thumbsupnum`     int(11)         DEFAULT 0  COMMENT '点赞数',",
            "  `storeupnum`      int(11)         DEFAULT 0  COMMENT '收藏数',",
            "  `clicknum`        int(11)         DEFAULT 0  COMMENT '点击次数',",
            "  `discussnum`      int(11)         DEFAULT 0  COMMENT '评论数',",
            "  `totalscore`      double          DEFAULT 0  COMMENT '评分',",
            "  PRIMARY KEY (`id`),",
            "  UNIQUE KEY (`xuqiubianhao`)",
            ") COMMENT='技能需求';",
            "",
            "-- 系统共26张表，核心表包括：",
            "-- yonghu(用户) | users(管理员) | token(认证)",
            "-- jinengxuqiu(技能/需求) | jinengfenlei(技能分类)",
            "-- jiaohuanshenqing(交换申请) | jiaohuanjilu(交换记录)",
            "-- pingjiafankui(评价反馈) | storeup(收藏/点赞)",
            "-- xuexiziliao(学习资料) | forum(社区论坛)",
            "-- chatmessage(即时消息) | friend(好友关系)",
        ],
        "narration": (
            "接下来看数据库设计。系统共有26张数据表。"
            "这里展示的是最核心的jinengxuqiu表，即技能需求表。"
            "它包含需求编号、标题、技能分类、封面图片、发布时间、费用等基本信息，"
            "以及用户账号、信誉指数等关联字段，"
            "还有类型字段区分是需求还是技能，"
            "以及审核状态、点赞数、收藏数、点击次数、评论数、评分等交互统计字段。"
            "系统的其他核心表包括用户表、交换申请表、交换记录表、评价反馈表、"
            "收藏表、学习资料表、论坛表和即时消息表等。"
        ),
    },
    # ---- 案例：交换流程 ----
    {
        "title": "案例讲解 — 技能交换完整流程",
        "type": "flow",
        "accent": COLORS["accent_orange"],
        "steps": [
            {"title": "用户注册登录", "desc": "填写账号密码 → Token认证 → 进入系统首页"},
            {"title": "发布技能/需求", "desc": "选择分类、填写描述、上传封面 → 提交审核 → 管理员审核通过"},
            {"title": "浏览与推荐", "desc": "首页协同过滤推荐 + 技能市场搜索筛选 → 查看详情"},
            {"title": "发起交换申请", "desc": "选择目标技能 → 填写申请备注 → 提交 jiaohuanshenqing"},
            {"title": "对方确认交换", "desc": "供给方收到通知 → 查看申请详情 → 确认/拒绝"},
            {"title": "完成并评价", "desc": "生成交换记录 jiaohuanjilu → 双方互评 pingjiafankui → 更新信誉"},
        ],
        "narration": (
            "下面通过一个完整的案例来讲解系统的核心业务流程。"
            "第一步，用户注册登录。填写账号密码后通过Token认证进入系统首页。"
            "第二步，发布技能或需求。用户选择技能分类，填写描述信息，上传封面图片，"
            "提交后需要管理员审核通过才会在市场展示。"
            "第三步，浏览与推荐。首页基于协同过滤算法展示个性化推荐，"
            "用户也可以在技能市场通过搜索和分类筛选查找感兴趣的技能。"
            "第四步，发起交换申请。用户选中目标技能后填写申请备注，"
            "系统生成一条交换申请记录。"
            "第五步，对方确认交换。技能供给方收到通知后查看申请详情，确认或拒绝交换。"
            "第六步，完成交换并评价。系统生成交换记录，双方可以互相评价，评价结果更新用户信誉。"
        ),
    },
    # ---- 推荐算法核心代码 ----
    {
        "title": "协同过滤推荐算法 — 核心实现",
        "type": "code",
        "accent": COLORS["accent_purple"],
        "filepath": "JinengxuqiuController.java — autoSort2() 需求向量协同过滤",
        "code": [
            "// ===== 第一阶段：构建用户需求向量 =====",
            "List<JinengxuqiuEntity> allNeeds = jinengxuqiuService",
            "    .selectList(new EntityWrapper<>().eq(\"leixing\", \"需求\"));",
            "",
            "Map<String, Map<String, Double>> userNeedVectors = new HashMap<>();",
            "for (JinengxuqiuEntity need : allNeeds) {",
            "    String uname = need.getYonghuzhanghao();",
            "    String cat = need.getJinengfenlei();",
            "    userNeedVectors.computeIfAbsent(uname, k -> new HashMap<>())",
            "        .merge(cat, 1.0, Double::sum);",
            "}",
            "",
            "// ===== 第二阶段：余弦相似度计算 =====",
            "Map<String, Double> myVector = userNeedVectors.get(username);",
            "List<Entry<String, Double>> similarUsers = new ArrayList<>();",
            "for (Entry<String, Map<String, Double>> entry : userNeedVectors) {",
            "    if (entry.getKey().equals(username)) continue;",
            "    double sim = needCosineSimilarity(myVector, entry.getValue());",
            "    if (sim > 0) similarUsers.add(new Entry<>(entry, sim));",
            "}",
            "similarUsers.sort(byValue(reverseOrder()));  // 按相似度降序",
            "",
            "// ===== 第三阶段：聚合推荐 =====",
            "// 从Top-K相似用户的收藏行为中挖掘推荐候选",
            "// 若不足 → 使用 RecommendAlgorithmFactory 补充",
            "// 最终按 sim × behavior 加权排序输出",
        ],
        "narration": (
            "接下来看推荐算法的核心代码实现。"
            "第一阶段，系统从技能需求表中提取所有类型为需求的记录，"
            "按用户名和技能分类构建用户需求向量。每个用户的向量维度是技能分类，值是该分类的需求数量。"
            "第二阶段，取出当前用户的向量，与所有其他用户计算余弦相似度，"
            "过滤掉相似度为零的用户，并按相似度降序排列。"
            "第三阶段，从Top K个相似用户的收藏和点赞行为中挖掘推荐候选，"
            "如果推荐数量不足，则调用推荐算法工厂类进行补充，"
            "最终按相似度乘以行为权重的加权得分排序输出推荐结果。"
        ),
    },
    # ---- 余弦相似度 ----
    {
        "title": "协同过滤 — 余弦相似度计算",
        "type": "code",
        "accent": COLORS["accent_purple"],
        "filepath": "JinengxuqiuController.java — needCosineSimilarity()",
        "code": [
            "/**",
            " * 余弦相似度计算：比较两个用户的需求分类向量",
            " * sim(A,B) = (A·B) / (|A| × |B|)",
            " */",
            "private double needCosineSimilarity(",
            "        Map<String, Double> vec1,",
            "        Map<String, Double> vec2) {",
            "",
            "    Set<String> commonKeys = new HashSet<>(vec1.keySet());",
            "    commonKeys.retainAll(vec2.keySet());  // 取交集维度",
            "    if (commonKeys.isEmpty()) return 0.0;",
            "",
            "    double dotProduct = 0.0;",
            "    for (String key : commonKeys) {",
            "        dotProduct += vec1.get(key) * vec2.get(key);",
            "    }",
            "",
            "    double norm1 = Math.sqrt(",
            "        vec1.values().stream().mapToDouble(v -> v * v).sum());",
            "    double norm2 = Math.sqrt(",
            "        vec2.values().stream().mapToDouble(v -> v * v).sum());",
            "",
            "    if (norm1 == 0 || norm2 == 0) return 0.0;",
            "    return dotProduct / (norm1 * norm2);",
            "}",
            "",
            "// 示例：用户A需求向量 {编程:3, 设计:1, 音乐:2}",
            "//       用户B需求向量 {编程:2, 设计:2, 外语:1}",
            "//       共同维度: {编程, 设计}",
            "//       dot = 3*2 + 1*2 = 8",
            "//       |A|=√(9+1+4)=√14, |B|=√(4+4+1)=3",
            "//       sim = 8/(√14×3) = 0.713",
        ],
        "narration": (
            "这里展示余弦相似度的具体实现代码。"
            "函数接收两个用户的需求分类向量，先取两个向量的交集维度，"
            "如果没有公共维度则相似度为零。"
            "然后计算点积，即对应维度的值相乘后求和。"
            "接着分别计算两个向量的模，即各维度值平方和的平方根。"
            "最终相似度等于点积除以两个模的乘积。"
            "举例说明：用户A的需求向量是编程3、设计1、音乐2，"
            "用户B是编程2、设计2、外语1。"
            "它们的公共维度是编程和设计，点积为8，"
            "经过计算最终相似度为0.713，说明两个用户的需求高度相似。"
        ),
    },
    # ---- 混合推荐策略 ----
    {
        "title": "混合推荐策略",
        "type": "content",
        "accent": COLORS["accent_purple"],
        "body": [
            {"type": "heading", "text": "三级推荐架构"},
            "",
            {"type": "card", "text": "第一级：需求向量协同过滤（主推荐源）", "height": 55},
            {"type": "label", "text": "    基于用户发布的需求分类构建向量，余弦相似度匹配 → 相似用户收藏行为"},
            "",
            {"type": "card", "text": "第二级：用户-物品矩阵协同过滤（行为补充）", "height": 55},
            {"type": "label", "text": "    RecommendAlgorithmFactory.buildUserItemMatrix() → 收藏/点赞行为矩阵"},
            "",
            {"type": "card", "text": "第三级：NCF 神经协同过滤模型（深度特征）", "height": 55},
            {"type": "label", "text": "    基于Weka训练RandomForest/NCF → 模型持久化 → loadModel().recommendItems()"},
            "",
            {"type": "card", "text": "兜底策略：热门内容推荐（冷启动保障）", "height": 55},
            {"type": "label", "text": "    按收藏数、点赞数、点击量加权排序 → 解决新用户无行为数据的冷启动问题"},
        ],
        "narration": (
            "系统采用三级混合推荐架构。"
            "第一级是需求向量协同过滤，作为主推荐源。它基于用户发布的需求分类构建向量，"
            "通过余弦相似度找到相似用户，再从相似用户的收藏行为中挖掘推荐。"
            "第二级是用户物品矩阵协同过滤，作为行为补充。"
            "当第一级推荐数量不足时，使用推荐算法工厂的用户物品矩阵方法进行补充。"
            "第三级是NCF神经协同过滤模型，用于捕获深层特征，"
            "通过训练好的模型进行预测推荐。"
            "此外还有热门内容兜底策略，按收藏数、点赞数、点击量加权排序，"
            "解决新用户没有行为数据时的冷启动问题。"
        ),
    },
    # ---- 前端首页代码 ----
    {
        "title": "前端实现 — 首页推荐展示",
        "type": "code",
        "accent": COLORS["accent_green"],
        "filepath": "front/src/pages/home/home.vue",
        "code": [
            "<template>",
            "  <div class=\"home-preview\">",
            "    <!-- 技能市场推荐 -->",
            "    <div class=\"recommend\">",
            "      <span class=\"recommend_title\">技能市场推荐</span>",
            "      <span class=\"recommend_subhead\">",
            "        {{ userSkillCategories.length > 0",
            "          ? '基于你的需求「'",
            "            + userSkillCategories.slice(0,3).join('、')",
            "            + '」智能推荐'",
            "          : '为你精选优质技能服务' }}",
            "      </span>",
            "      <div class=\"list-item\"",
            "           v-for=\"item in jinengxuqiuRecommend.slice(0,4)\"",
            "           @click=\"toDetail('jinengxuqiuDetail', item)\">",
            "        <img :src=\"resolveImg(item.fengmian)\" />",
            "        <div class=\"name\">{{ item.xuqiubiaoti }}</div>",
            "        <span class=\"tag\">{{ item.jinengfenlei }}</span>",
            "        <span class=\"stat\">",
            "          👍{{ item.thumbsupnum }}",
            "          ⭐{{ item.storeupnum }}",
            "          👁{{ item.clicknum }}",
            "        </span>",
            "      </div>",
            "    </div>",
            "    <!-- 学习资料推荐 / 推荐用户 / 公告资讯 -->",
            "    ...",
            "  </div>",
            "</template>",
            "",
            "// API调用: GET /jinengxuqiu/autoSort2?limit=8",
            "// 返回协同过滤推荐结果列表",
        ],
        "narration": (
            "来看前端的实现。这是首页的Vue组件代码。"
            "页面顶部展示技能市场推荐区域，标题会根据用户是否有需求数据"
            "动态显示基于你的需求某某某智能推荐或为你精选优质技能服务。"
            "推荐列表通过v-for循环渲染，每个卡片展示封面图、标题、分类标签，"
            "以及点赞数、收藏数、点击量等统计数据。"
            "点击卡片可以跳转到详情页面。"
            "后端接口调用的是jinengxuqiu斜杠autoSort2，也就是我们刚才讲解的协同过滤推荐接口，"
            "它会返回个性化的推荐结果列表。"
        ),
    },
    # ---- 系统功能总览 ----
    {
        "title": "系统功能模块总览",
        "type": "content",
        "accent": COLORS["accent_green"],
        "body": [
            {"type": "heading", "text": "用户端功能（前台）"},
            "  • 技能市场：发布技能/需求、搜索筛选、分类浏览、详情查看",
            "  • 技能交换：发起申请、查看收到的申请、确认/拒绝、交换记录",
            "  • 个性推荐：首页协同过滤推荐、相似用户推荐",
            "  • 学习资料：浏览、购买、收藏、评论",
            "  • 社区论坛：发帖、评论、我的帖子",
            "  • 即时聊天：好友列表、WebSocket实时消息",
            "  • AI问答：接入大语言模型、流式输出",
            "  • 个人中心：资料编辑、密码修改、我的收藏、会员充值",
            "",
            {"type": "heading", "text": "管理端功能（后台）"},
            "  • 数据统计：ECharts可视化图表、用户/技能/交换数据看板",
            "  • 内容审核：技能审核、需求审核、论坛管理",
            "  • 用户管理：用户列表、会员充值记录",
            "  • 系统配置：分类管理、公告发布、敏感词过滤、日志审计",
        ],
        "narration": (
            "下面总览系统的所有功能模块。"
            "用户端前台功能包括：技能市场支持发布技能和需求、搜索筛选、分类浏览；"
            "技能交换支持完整的申请确认流程；"
            "个性推荐基于协同过滤算法在首页展示；"
            "学习资料支持浏览购买收藏评论；"
            "社区论坛支持发帖和评论；"
            "即时聊天通过WebSocket实现实时通讯；"
            "AI问答接入大语言模型支持流式输出；"
            "个人中心包含资料编辑、会员充值等。"
            "管理端后台功能包括：数据统计看板基于ECharts可视化；"
            "内容审核包括技能审核、需求审核、论坛管理；"
            "用户管理和系统配置涵盖分类管理、公告发布、敏感词过滤和日志审计。"
        ),
    },
    # ---- 总结 ----
    {
        "title": "项目总结",
        "type": "content",
        "accent": COLORS["accent_blue"],
        "body": [
            {"type": "heading", "text": "完成成果"},
            "  ✓ 前端76个Vue页面 + 后端29个Controller + 26张数据表",
            "  ✓ 基于需求向量的协同过滤推荐算法（创新点）",
            "  ✓ UserCF + ItemCF + NCF 三级混合推荐策略",
            "  ✓ 完整的技能交换业务闭环（发布→匹配→申请→交换→评价）",
            "  ✓ WebSocket即时通讯 + AI大模型问答",
            "  ✓ 完善的后台管理系统（审核、统计、配置）",
            "",
            {"type": "heading", "text": "技术亮点"},
            "  • 推荐结果随用户需求变化实时更新",
            "  • 多维度信誉评价体系影响推荐排序",
            "  • 三级推荐逐级补充解决冷启动问题",
            "",
            {"type": "heading", "text": "致谢"},
            "  感谢指导老师的悉心指导和各位评审老师的审阅！",
        ],
        "narration": (
            "最后进行项目总结。"
            "本系统前端共开发了76个Vue页面组件，后端29个Controller控制器，"
            "数据库26张业务表，代码量充足。"
            "核心创新是提出了基于需求向量的协同过滤推荐算法，"
            "配合三级混合推荐策略，实现了精准的个性化推荐。"
            "系统完整实现了技能交换业务闭环，集成了WebSocket即时通讯和AI大模型问答，"
            "并配备了完善的后台管理系统。"
            "技术亮点包括推荐结果实时更新、信誉评价影响排序、三级补充解决冷启动。"
            "以上就是我的毕业设计全部内容，感谢指导老师的悉心指导和各位评审老师的审阅。谢谢大家！"
        ),
    },
]


# ============ TTS ============

async def generate_tts(text, output_path):
    communicate = edge_tts.Communicate(text, TTS_VOICE, rate="-5%")
    await communicate.save(output_path)


# ============ 主流程 ============

async def main():
    tmp_dir = tempfile.mkdtemp(prefix="bishe_video_")
    print(f"临时目录: {tmp_dir}")

    clips = []

    for idx, chapter in enumerate(CHAPTERS):
        print(f"\n[{idx+1}/{len(CHAPTERS)}] 生成: {chapter['title']}")

        # 1. 渲染幻灯片
        slide_img = render_slide(chapter)
        slide_path = os.path.join(tmp_dir, f"slide_{idx:02d}.png")
        slide_img.save(slide_path, quality=95)
        print(f"  ✓ 幻灯片")

        # 2. 生成语音
        audio_path = os.path.join(tmp_dir, f"audio_{idx:02d}.mp3")
        await generate_tts(chapter["narration"], audio_path)
        print(f"  ✓ 语音")

        # 3. 合成片段
        audio_clip = AudioFileClip(audio_path)
        duration = audio_clip.duration + 1.5

        img_clip = ImageClip(slide_path, duration=duration)
        img_clip = img_clip.with_audio(audio_clip)
        img_clip = img_clip.with_fps(FPS)

        clips.append(img_clip)
        print(f"  ✓ 片段 {duration:.1f}s")

    # 4. 合成视频
    print("\n正在合成最终视频...")
    final = concatenate_videoclips(clips, method="compose")
    final.write_videofile(
        OUTPUT_PATH,
        fps=FPS,
        codec="libx264",
        audio_codec="aac",
        threads=4,
        logger="bar",
    )

    # 5. 清理
    shutil.rmtree(tmp_dir, ignore_errors=True)

    print(f"\n视频生成完成！")
    print(f"输出路径: {OUTPUT_PATH}")
    print(f"总时长: {final.duration:.1f}s ({final.duration/60:.1f}分钟)")


if __name__ == "__main__":
    asyncio.run(main())
