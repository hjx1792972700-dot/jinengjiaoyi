<template>
	<div class="hud-page">
		<div class="hud-hero">
			<div class="hero-grid-bg"></div>
			<div class="hero-scanline"></div>
			<div class="hero-content">
				<div class="hero-left">
					<div class="hero-badge">SYSTEM ONLINE</div>
					<h1 class="hero-title">技能交易管理中心</h1>
					<p class="hero-sub">欢迎回来，<span class="hl">{{adminName}}</span> — 系统运行正常，所有服务已就绪</p>
				</div>
				<div class="hero-right">
					<div class="hero-clock">
						<div class="clock-time">{{currentTime}}</div>
						<div class="clock-date">{{currentDate}}</div>
					</div>
					<div class="hero-pulse">
						<span class="pulse-dot"></span>
						<span class="pulse-label">实时监控中</span>
					</div>
				</div>
			</div>
		</div>

		<div class="hud-stats">
			<div class="stat-card" v-for="(s, i) in statList" :key="i" @click="s.path && $router.push(s.path)">
				<div class="stat-glow" :style="{background: s.glow}"></div>
				<div class="stat-icon-wrap" :style="{background: s.bg}">
					<i :class="'el-icon-' + s.icon"></i>
				</div>
				<div class="stat-body">
					<div class="stat-num">{{s.value}}</div>
					<div class="stat-label">{{s.label}}</div>
				</div>
				<div class="stat-trend" :class="s.trendDir">
					<i :class="s.trendDir === 'up' ? 'el-icon-top' : 'el-icon-bottom'"></i>
					<span>{{s.trend}}</span>
				</div>
			</div>
		</div>

		<div class="hud-nav" v-if="isAdmin">
			<div class="nav-item" v-for="n in navCards" :key="n.path" @click="$router.push(n.path)">
				<div class="nav-icon-box" :style="{background: n.bg}">
					<i :class="'el-icon-' + n.icon"></i>
				</div>
				<div class="nav-info">
					<div class="nav-title">{{n.label}}</div>
					<div class="nav-desc">{{n.desc}}</div>
				</div>
				<el-icon class="nav-arrow"><ArrowRight /></el-icon>
			</div>
		</div>

		<div class="hud-grid">
			<div class="hud-panel panel-chart">
				<div class="panel-head">
					<div class="panel-dot"></div>
					<span>近30天平台活跃趋势</span>
				</div>
				<div class="chart-box" id="miniTrendChart" style="width:100%;height:220px"></div>
			</div>

			<div class="hud-panel panel-pie">
				<div class="panel-head">
					<div class="panel-dot"></div>
					<span>技能分类占比</span>
				</div>
				<div class="chart-box" id="miniPieChart" style="width:100%;height:220px"></div>
			</div>

			<div class="hud-panel panel-news">
				<div class="panel-head">
					<div class="panel-dot"></div>
					<span>公告资讯</span>
					<span class="badge" v-if="newsList.length">{{newsList.length}}</span>
				</div>
				<div class="news-feed" v-if="newsList.length">
					<div class="news-item" v-for="(item, idx) in newsList" :key="idx" @click="showNewsDetail(item)">
						<div class="news-idx" :class="{'top': idx < 3}">{{idx + 1}}</div>
						<div class="news-body">
							<div class="news-title">{{item.title}}</div>
							<div class="news-time">{{item.addtime}}</div>
						</div>
					</div>
				</div>
				<div class="empty-state" v-else><i class="el-icon-document"></i><span>暂无公告</span></div>
			</div>

			<div class="hud-panel panel-side">
				<div class="panel-head">
					<div class="panel-dot dot-orange"></div>
					<span>待办事项</span>
					<span class="badge badge-warn" v-if="todoList.length">{{todoList.length}}</span>
				</div>
				<div class="todo-feed" v-if="todoList.length">
					<div class="todo-item" v-for="t in todoList" :key="t.label" @click="t.path && $router.push(t.path)">
						<div class="todo-indicator" :style="{background: t.color}"></div>
						<span class="todo-text">{{t.label}}</span>
						<span class="todo-count" :style="{color: t.color, borderColor: t.color}">{{t.value}}</span>
					</div>
				</div>
				<div class="empty-state" v-else><i class="el-icon-circle-check"></i><span>无待办事项</span></div>

				<div class="panel-head" style="margin-top:18px">
					<div class="panel-dot dot-green"></div>
					<span>系统状态</span>
				</div>
				<div class="sys-metrics">
					<div class="metric-row" v-for="m in sysMetrics" :key="m.label">
						<span class="metric-label">{{m.label}}</span>
						<div class="metric-bar-wrap">
							<div class="metric-bar" :style="{width: m.pct + '%', background: m.color}"></div>
						</div>
						<span class="metric-val" :style="{color: m.color}">{{m.display}}</span>
					</div>
				</div>

				<div class="panel-head" style="margin-top:18px">
					<div class="panel-dot dot-cyan"></div>
					<span>快速操作</span>
				</div>
				<div class="quick-actions">
					<div class="qa-btn" @click="$router.push('/jinengxuqiu')"><i class="el-icon-s-grid"></i>技能管理</div>
					<div class="qa-btn" @click="$router.push('/forum')"><i class="el-icon-chat-dot-round"></i>社区管理</div>
					<div class="qa-btn" @click="$router.push('/news')"><i class="el-icon-notebook"></i>公告管理</div>
					<div class="qa-btn" @click="$router.push('/syslog')"><i class="el-icon-document-copy"></i>系统日志</div>
				</div>
			</div>
		</div>

		<el-dialog :visible.sync="newsVisible" title="公告详情" width="60%" top="10vh" class="hud-dialog">
			<div style="text-align:center;font-size:18px;font-weight:600;color:#e2e8f0;margin-bottom:16px">{{newsDetail.title}}</div>
			<div class="ql-snow ql-editor" v-html="newsDetail.content" style="color:#cbd5e1"></div>
		</el-dialog>
	</div>
</template>

<script>
import * as echarts from 'echarts'
import base from '@/utils/base'
export default {

	data() {
		return {
			currentDate: '',
			currentTime: '',
			timeTimer: null,
			adminName: '',
			isAdmin: false,
			charts: {},
			newsList: [],
			newsDetail: {},
			newsVisible: false,
			statList: [
				{ label: '注册用户', value: '-', icon: 'UserFilled', bg: 'linear-gradient(135deg,#3b82f6,#60a5fa)', glow: 'rgba(59,130,246,0.15)', path: '/yonghu', trend: '+3', trendDir: 'up' },
				{ label: '技能市场', value: '-', icon: 'List', bg: 'linear-gradient(135deg,#06b6d4,#22d3ee)', glow: 'rgba(6,182,212,0.15)', path: '/jinengxuqiu', trend: '+5', trendDir: 'up' },
				{ label: '交换记录', value: '-', icon: 'Promotion', bg: 'linear-gradient(135deg,#10b981,#34d399)', glow: 'rgba(16,185,129,0.15)', path: '/jiaohuanjilu', trend: '+2', trendDir: 'up' },
				{ label: '评价反馈', value: '-', icon: 'StarFilled', bg: 'linear-gradient(135deg,#f59e0b,#fbbf24)', glow: 'rgba(245,158,11,0.15)', path: '/pingjiafankui', trend: '+4', trendDir: 'up' },
				{ label: '学习资料', value: '-', icon: 'Reading', bg: 'linear-gradient(135deg,#ec4899,#f472b6)', glow: 'rgba(236,72,153,0.15)', path: '/xuexiziliao', trend: '+1', trendDir: 'up' },
				{ label: '社区帖子', value: '-', icon: 'ChatDotRound', bg: 'linear-gradient(135deg,#8b5cf6,#a78bfa)', glow: 'rgba(139,92,246,0.15)', path: '/forum', trend: '+7', trendDir: 'up' },
			],
			navCards: [
				{ label: '数据统计', desc: '查看平台核心数据和图表分析', icon: 'DataAnalysis', path: '/statisticsDashboard', bg: 'linear-gradient(135deg,#7c3aed,#a78bfa)' },
				{ label: '用户管理', desc: '管理注册用户、审核和权限', icon: 'UserFilled', path: '/userManagement', bg: 'linear-gradient(135deg,#3b82f6,#60a5fa)' },
				{ label: '业务管理', desc: '技能市场、交换、评价等业务', icon: 'CircleCheck', path: '/businessManagement', bg: 'linear-gradient(135deg,#06b6d4,#22d3ee)' },
				{ label: '系统配置', desc: '公告、分类、敏感词等配置', icon: 'Setting', path: '/systemConfig', bg: 'linear-gradient(135deg,#10b981,#34d399)' },
			],
			todoList: [],
			sysMetrics: [
				{ label: '数据库连接', pct: 85, display: '正常', color: '#10b981' },
				{ label: 'API 响应', pct: 92, display: '< 50ms', color: '#06b6d4' },
				{ label: '存储使用', pct: 37, display: '37%', color: '#3b82f6' },
				{ label: '今日请求', pct: 64, display: '1.2k', color: '#a78bfa' },
			],
		}
	},
	mounted() {
		this.adminName = this.$storage.get('adminName') || '管理员'
		this.isAdmin = this.$storage.get('sessionTable') === 'users'
		this.updateTime()
		this.timeTimer = setInterval(this.updateTime, 1000)
		this.initSession()
		this.loadCounts()
		this.loadTodos()
		this.loadNews()
		this.$nextTick(() => this.loadCharts())
	},
	beforeDestroy() {
		if (this.timeTimer) clearInterval(this.timeTimer)
		Object.values(this.charts).forEach(c => c && c.dispose())
	},
	methods: {
		updateTime() {
			const d = new Date()
			const week = ['日','一','二','三','四','五','六']
			this.currentDate = `${d.getFullYear()}年${d.getMonth()+1}月${d.getDate()}日 星期${week[d.getDay()]}`
			const pad = n => String(n).padStart(2, '0')
			this.currentTime = `${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
		},
		initSession() {
			if (!this.$storage.get('Token')) {
				window.location.href = base.get().indexUrl
				return
			}
			let sessionTable = this.$storage.get('sessionTable')
			this.$http({ url: `${sessionTable}/session`, method: 'get' }).then(({ data }) => {
				if (data && data.code === 0) {
					if (!this.$storage.get('userid') && data.data) {
						this.$storage.set('headportrait', sessionTable === 'yonghu' ? data.data.touxiang : data.data.image)
						this.$storage.set('userForm', JSON.stringify(data.data))
						this.$storage.set('userid', data.data.id)
					}
				} else {
					window.location.href = base.get().indexUrl
				}
			})
		},
		loadNews() {
			this.$http({ url: 'news/list', method: 'get', params: { page: 1, limit: 8, sort: 'addtime', order: 'desc' } }).then(res => {
				if (res.data && res.data.code === 0) this.newsList = res.data.data.list
			})
		},
		loadCounts() {
			const apis = [
				{ idx: 0, url: 'yonghu/count' },
				{ idx: 1, url: 'jinengxuqiu/count' },
				{ idx: 2, url: 'jiaohuanjilu/count' },
				{ idx: 3, url: 'pingjiafankui/count' },
				{ idx: 4, url: 'xuexiziliao/count' },
				{ idx: 5, url: 'group/forum/typename' },
			]
			apis.forEach(item => {
				if (item.idx === 5) {
					this.$http({ url: item.url, method: 'get' }).then(({ data }) => {
						if (data && data.code === 0) {
							let total = data.data.reduce((sum, r) => sum + parseInt(r.total || 0), 0)
							this.statList[item.idx].value = total
						}
					})
				} else {
					this.$http({ url: item.url, method: 'get' }).then(({ data }) => {
						if (data && data.code === 0) {
							this.statList[item.idx].value = data.data
						}
					})
				}
			})
		},
		loadTodos() {
			this.$http({ url: 'jinengxuqiu/page', method: 'get', params: { page: 1, limit: 1, leixing: '技能', sfsh: '待审核' } }).then(({ data }) => {
				if (data && data.code === 0 && data.data.total > 0) {
					this.todoList.push({ label: '待审核技能', value: data.data.total + ' 条', color: '#f59e0b', path: '/jinengxuqiu' })
				}
			})
			this.$http({ url: 'jinengxuqiu/page', method: 'get', params: { page: 1, limit: 1, leixing: '需求', sfsh: '待审核' } }).then(({ data }) => {
				if (data && data.code === 0 && data.data.total > 0) {
					this.todoList.push({ label: '待审核需求', value: data.data.total + ' 条', color: '#e879f9', path: '/jinengxuqiu' })
				}
			})
			this.$http({ url: 'yonghu/page', method: 'get', params: { page: 1, limit: 1, sfsh: '待审核' } }).then(({ data }) => {
				if (data && data.code === 0 && data.data.total > 0) {
					this.todoList.push({ label: '待审核用户', value: data.data.total + ' 人', color: '#3b82f6', path: '/yonghu' })
				}
			})
			this.$http({ url: 'messages/page', method: 'get', params: { page: 1, limit: 1, sort: 'id', order: 'desc' } }).then(({ data }) => {
				if (data && data.code === 0 && data.data.total > 0) {
					this.todoList.push({ label: '留言反馈', value: data.data.total + ' 条', color: '#06b6d4', path: '/messages' })
				}
			})
		},
		loadCharts() {
			this.loadTrendChart()
			this.loadPieChart()
		},
		initChart(id) {
			const el = document.getElementById(id)
			if (!el) return null
			const chart = echarts.init(el)
			this.charts[id] = chart
			window.addEventListener('resize', () => chart.resize())
			return chart
		},
		loadTrendChart() {
			const chart = this.initChart('miniTrendChart')
			if (!chart) return
			this.$http({ url: 'jinengxuqiu/group/fabushijian', method: 'get', params: { conditionColumn: 'sfsh', conditionValue: '是' } }).then(({ data }) => {
				if (data && data.code === 0) {
					let sorted = data.data.sort((a, b) => (a.fabushijian || '').localeCompare(b.fabushijian || ''))
					let recent = sorted.slice(-30)
					let x = recent.map(r => r.fabushijian || '')
					let y = recent.map(r => parseFloat(r.total))
					chart.setOption({
						backgroundColor: 'transparent',
						tooltip: { trigger: 'axis', backgroundColor: 'rgba(15,23,42,0.95)', borderColor: 'rgba(0,180,216,0.3)', textStyle: { color: '#e2e8f0', fontSize: 12 } },
						grid: { left: 10, right: 15, top: 15, bottom: 5, containLabel: true },
						xAxis: { type: 'category', data: x, boundaryGap: false, axisLabel: { color: '#475569', fontSize: 10, interval: Math.max(0, x.length - 6) > 0 ? Math.floor(x.length / 5) : 0 }, axisLine: { lineStyle: { color: 'rgba(255,255,255,0.06)' } }, splitLine: { show: false } },
						yAxis: { type: 'value', minInterval: 1, axisLabel: { color: '#475569', fontSize: 10 }, axisLine: { show: false }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.04)' } } },
						series: [{
							type: 'line', data: y, smooth: true, symbol: 'circle', symbolSize: 5, showSymbol: false,
							lineStyle: { width: 2.5, color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{ offset: 0, color: '#06b6d4' }, { offset: 1, color: '#3b82f6' }]) },
							itemStyle: { color: '#06b6d4', borderColor: '#0f172a', borderWidth: 2 },
							areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(6,182,212,0.25)' }, { offset: 1, color: 'rgba(6,182,212,0)' }]) }
						}]
					})
				}
			})
		},
		loadPieChart() {
			const chart = this.initChart('miniPieChart')
			if (!chart) return
			this.$http({ url: 'jinengxuqiu/group/jinengfenlei', method: 'get', params: { conditionColumn: 'sfsh', conditionValue: '是' } }).then(({ data }) => {
				if (data && data.code === 0) {
					let pieData = data.data.map(r => ({ name: r.jinengfenlei || '未分类', value: parseFloat(r.total) }))
					const COLORS = ['#3b82f6','#06b6d4','#10b981','#f59e0b','#ec4899','#8b5cf6','#ef4444','#84cc16','#f97316','#14b8a6']
					chart.setOption({
						backgroundColor: 'transparent',
						tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)', backgroundColor: 'rgba(15,23,42,0.95)', borderColor: 'rgba(0,180,216,0.3)', textStyle: { color: '#e2e8f0', fontSize: 12 } },
						legend: { orient: 'vertical', right: 5, top: 'center', icon: 'circle', itemWidth: 8, itemGap: 10, textStyle: { color: '#94a3b8', fontSize: 11 } },
						color: COLORS,
						series: [{
							type: 'pie', radius: ['38%', '65%'], center: ['38%', '52%'], data: pieData,
							label: { show: true, formatter: '{d}%', fontSize: 10, color: '#94a3b8', position: 'outside' },
							labelLine: { length: 8, length2: 6, lineStyle: { color: 'rgba(255,255,255,0.15)' } },
							emphasis: { itemStyle: { shadowBlur: 15, shadowColor: 'rgba(0,0,0,0.3)' }, label: { show: true, fontSize: 12, fontWeight: 600 } },
							itemStyle: { borderColor: '#0f172a', borderWidth: 2 }
						}]
					})
				}
			})
		},
		showNewsDetail(item) {
			this.newsDetail = item
			this.newsVisible = true
		},
	}
}
</script>

<style lang="scss" scoped>
$accent: #06b6d4;
$accent2: #3b82f6;
$bg: #0f172a;
$surface: #1e293b;
$surface2: #162033;
$text: #e2e8f0;
$text2: #94a3b8;
$text3: #64748b;
$text4: #475569;
$border: rgba(255,255,255,0.06);
$glow: rgba(6,182,212,0.08);
$radius: 14px;

@keyframes scanline {
	0% { transform: translateY(-100%); }
	100% { transform: translateY(100%); }
}
@keyframes pulse-ring {
	0% { transform: scale(1); opacity: 1; }
	100% { transform: scale(2.5); opacity: 0; }
}
@keyframes float-up {
	0% { opacity: 0; transform: translateY(12px); }
	100% { opacity: 1; transform: translateY(0); }
}

.hud-page {
	display: flex;
	flex-direction: column;
	gap: 16px;
	animation: float-up 0.5s ease-out;
}

.hud-hero {
	position: relative;
	overflow: hidden;
	border-radius: $radius;
	border: 1px solid $border;
	background: linear-gradient(135deg, $surface 0%, $surface2 50%, rgba(6,182,212,0.06) 100%);
	padding: 28px 32px;
}
.hero-grid-bg {
	position: absolute;
	inset: 0;
	background-image:
		linear-gradient(rgba(6,182,212,0.04) 1px, transparent 1px),
		linear-gradient(90deg, rgba(6,182,212,0.04) 1px, transparent 1px);
	background-size: 40px 40px;
	pointer-events: none;
}
.hero-scanline {
	position: absolute;
	inset: 0;
	background: linear-gradient(180deg, transparent 0%, rgba(6,182,212,0.03) 50%, transparent 100%);
	animation: scanline 4s linear infinite;
	pointer-events: none;
}
.hero-content {
	position: relative;
	display: flex;
	justify-content: space-between;
	align-items: center;
	z-index: 1;
}
.hero-left {
	.hero-badge {
		display: inline-block;
		font-size: 10px;
		font-weight: 700;
		letter-spacing: 2px;
		color: $accent;
		background: rgba(6,182,212,0.1);
		border: 1px solid rgba(6,182,212,0.2);
		padding: 3px 12px;
		border-radius: 20px;
		margin-bottom: 10px;
	}
	.hero-title {
		margin: 0 0 6px;
		font-size: 24px;
		font-weight: 800;
		color: $text;
		letter-spacing: 1px;
	}
	.hero-sub {
		margin: 0;
		font-size: 13px;
		color: $text3;
		.hl { color: $accent; font-weight: 600; }
	}
}
.hero-right {
	text-align: right;
	display: flex;
	flex-direction: column;
	align-items: flex-end;
	gap: 10px;
}
.clock-time {
	font-size: 36px;
	font-weight: 800;
	color: $accent;
	font-variant-numeric: tabular-nums;
	letter-spacing: 3px;
	line-height: 1;
	text-shadow: 0 0 20px rgba(6,182,212,0.3);
}
.clock-date {
	font-size: 12px;
	color: $text3;
	letter-spacing: 1px;
}
.hero-pulse {
	display: flex;
	align-items: center;
	gap: 8px;
}
.pulse-dot {
	position: relative;
	width: 8px;
	height: 8px;
	background: #10b981;
	border-radius: 50%;
	&::after {
		content: '';
		position: absolute;
		inset: -2px;
		border-radius: 50%;
		border: 2px solid #10b981;
		animation: pulse-ring 1.5s ease-out infinite;
	}
}
.pulse-label {
	font-size: 11px;
	color: #10b981;
	font-weight: 600;
	letter-spacing: 1px;
}

.hud-stats {
	display: grid;
	grid-template-columns: repeat(6, 1fr);
	gap: 12px;
}
.stat-card {
	position: relative;
	overflow: hidden;
	background: $surface;
	border: 1px solid $border;
	border-radius: $radius;
	padding: 18px 16px;
	display: flex;
	align-items: center;
	gap: 12px;
	cursor: pointer;
	transition: all 0.3s;
	&:hover {
		border-color: rgba(6,182,212,0.2);
		transform: translateY(-3px);
		box-shadow: 0 8px 30px rgba(0,0,0,0.2);
	}
}
.stat-glow {
	position: absolute;
	width: 80px;
	height: 80px;
	border-radius: 50%;
	right: -20px;
	top: -20px;
	filter: blur(25px);
	pointer-events: none;
}
.stat-icon-wrap {
	width: 42px;
	height: 42px;
	border-radius: 12px;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
	.el-icon { font-size: 20px; color: #fff; }
}
.stat-body {
	flex: 1;
	min-width: 0;
	.stat-num { font-size: 22px; font-weight: 800; color: $text; line-height: 1.2; }
	.stat-label { font-size: 11px; color: $text3; margin-top: 2px; white-space: nowrap; }
}
.stat-trend {
	font-size: 11px;
	font-weight: 700;
	padding: 2px 6px;
	border-radius: 6px;
	flex-shrink: 0;
	&.up { color: #10b981; background: rgba(16,185,129,0.1); }
	&.down { color: #ef4444; background: rgba(239,68,68,0.1); }
	.el-icon { font-size: 10px; margin-right: 1px; vertical-align: middle; }
}

.hud-nav {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 12px;
}
.nav-item {
	background: $surface;
	border: 1px solid $border;
	border-radius: $radius;
	padding: 18px 16px;
	display: flex;
	align-items: center;
	gap: 14px;
	cursor: pointer;
	transition: all 0.3s;
	&:hover {
		border-color: rgba(6,182,212,0.25);
		transform: translateY(-2px);
		box-shadow: 0 6px 24px rgba(0,0,0,0.15);
		.nav-arrow { color: $accent; transform: translateX(3px); }
	}
}
.nav-icon-box {
	width: 42px;
	height: 42px;
	border-radius: 12px;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
	.el-icon { font-size: 20px; color: #fff; }
}
.nav-info {
	flex: 1;
	min-width: 0;
	.nav-title { font-size: 14px; font-weight: 700; color: $text; margin-bottom: 2px; }
	.nav-desc { font-size: 11px; color: $text3; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
}
.nav-arrow {
	color: $text4;
	font-size: 14px;
	transition: all 0.25s;
}

.hud-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	grid-template-rows: auto 1fr;
	gap: 14px;
	align-items: stretch;
}
.hud-panel {
	background: $surface;
	border: 1px solid $border;
	border-radius: $radius;
	padding: 20px;
	transition: box-shadow 0.3s;
	display: flex;
	flex-direction: column;
	&:hover { box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
}
.panel-chart { grid-column: 1; grid-row: 1; }
.panel-pie { grid-column: 2; grid-row: 1; }
.panel-news { grid-column: 1; grid-row: 2; }
.panel-side { grid-column: 2; grid-row: 2; }

.panel-head {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 14px;
	font-size: 14px;
	font-weight: 700;
	color: $text;
}
.panel-dot {
	width: 8px;
	height: 8px;
	border-radius: 2px;
	background: $accent;
	&.dot-orange { background: #f59e0b; }
	&.dot-green { background: #10b981; }
	&.dot-cyan { background: $accent; }
}
.badge {
	margin-left: auto;
	font-size: 11px;
	font-weight: 700;
	color: $accent;
	background: rgba(6,182,212,0.1);
	border: 1px solid rgba(6,182,212,0.2);
	padding: 1px 8px;
	border-radius: 10px;
	&.badge-warn {
		color: #f59e0b;
		background: rgba(245,158,11,0.1);
		border-color: rgba(245,158,11,0.2);
	}
}

.chart-box {
	flex: 1;
	min-height: 0;
}
.news-feed {
	display: flex;
	flex-direction: column;
	gap: 2px;
	flex: 1;
}
.news-item {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 9px 12px;
	border-radius: 8px;
	cursor: pointer;
	transition: all 0.15s;
	&:hover { background: rgba(6,182,212,0.04); .news-title { color: $accent; } }
}
.news-idx {
	width: 22px;
	height: 22px;
	border-radius: 6px;
	font-size: 11px;
	font-weight: 700;
	display: flex;
	align-items: center;
	justify-content: center;
	flex-shrink: 0;
	background: rgba(255,255,255,0.04);
	color: $text3;
	&.top { background: rgba(6,182,212,0.12); color: $accent; }
}
.news-body {
	flex: 1;
	min-width: 0;
	display: flex;
	align-items: center;
	gap: 10px;
}
.news-title {
	flex: 1;
	font-size: 13px;
	color: $text2;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	transition: color 0.15s;
}
.news-time {
	font-size: 11px;
	color: $text4;
	flex-shrink: 0;
}

.todo-feed {
	display: flex;
	flex-direction: column;
	gap: 2px;
}
.todo-item {
	display: flex;
	align-items: center;
	gap: 10px;
	padding: 9px 12px;
	border-radius: 8px;
	cursor: pointer;
	transition: background 0.15s;
	&:hover { background: rgba(255,255,255,0.03); }
}
.todo-indicator {
	width: 4px;
	height: 20px;
	border-radius: 3px;
	flex-shrink: 0;
}
.todo-text {
	flex: 1;
	font-size: 13px;
	color: $text2;
}
.todo-count {
	font-size: 12px;
	font-weight: 700;
	padding: 2px 10px;
	border-radius: 6px;
	border: 1px solid;
	flex-shrink: 0;
}

.sys-metrics {
	display: flex;
	flex-direction: column;
	gap: 10px;
	padding: 0 4px;
}
.metric-row {
	display: flex;
	align-items: center;
	gap: 10px;
}
.metric-label {
	width: 72px;
	font-size: 11px;
	color: $text3;
	flex-shrink: 0;
}
.metric-bar-wrap {
	flex: 1;
	height: 6px;
	border-radius: 3px;
	background: rgba(255,255,255,0.04);
	overflow: hidden;
}
.metric-bar {
	height: 100%;
	border-radius: 3px;
	transition: width 1s ease;
}
.metric-val {
	width: 46px;
	text-align: right;
	font-size: 11px;
	font-weight: 700;
	flex-shrink: 0;
}

.quick-actions {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 8px;
	flex: 1;
	align-content: end;
}
.qa-btn {
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 6px;
	padding: 9px 10px;
	border-radius: 8px;
	background: rgba(255,255,255,0.03);
	border: 1px solid $border;
	color: $text2;
	font-size: 12px;
	font-weight: 600;
	cursor: pointer;
	transition: all 0.2s;
	.el-icon { font-size: 14px; }
	&:hover {
		background: rgba(6,182,212,0.08);
		border-color: rgba(6,182,212,0.2);
		color: $accent;
	}
}

.empty-state {
	text-align: center;
	padding: 28px 0;
	color: $text4;
	font-size: 12px;
	.el-icon { font-size: 26px; display: block; margin: 0 auto 6px; opacity: 0.3; }
}
</style>
