<template>
	<div class="statistics-page">
		<div class="page-header">
			<h2>数据统计总览</h2>
		</div>

		<div class="summary-cards">
			<div class="summary-card card-blue" v-if="isAuth('yonghu','首页总数')">
				<div class="card-glow"></div>
				<div class="card-icon"><i class="el-icon-user-solid"></i></div>
				<div class="card-info">
					<div class="card-num">{{counts.yonghu}}</div>
					<div class="card-label">用户总数</div>
					<div class="card-sub">注册用户数量</div>
				</div>
				<div class="card-deco"><i class="el-icon-user-solid"></i></div>
			</div>
			<div class="summary-card card-purple" v-if="isAuth('jinengfenlei','首页总数')">
				<div class="card-glow"></div>
				<div class="card-icon"><i class="el-icon-menu"></i></div>
				<div class="card-info">
					<div class="card-num">{{counts.fenlei}}</div>
					<div class="card-label">技能分类</div>
					<div class="card-sub">技能类目总数</div>
				</div>
				<div class="card-deco"><i class="el-icon-menu"></i></div>
			</div>
			<div class="summary-card card-cyan" v-if="isAuth('jinengxuqiu','首页总数')">
				<div class="card-glow"></div>
				<div class="card-icon"><i class="el-icon-s-goods"></i></div>
				<div class="card-info">
					<div class="card-num">{{counts.xuqiu}}</div>
					<div class="card-label">技能市场</div>
					<div class="card-sub">已发布的技能</div>
				</div>
				<div class="card-deco"><i class="el-icon-s-goods"></i></div>
			</div>
			<div class="summary-card card-green" v-if="isAuth('jiaohuanjilu','首页总数')">
				<div class="card-glow"></div>
				<div class="card-icon"><i class="el-icon-s-promotion"></i></div>
				<div class="card-info">
					<div class="card-num">{{counts.jiaohuan}}</div>
					<div class="card-label">交换记录</div>
					<div class="card-sub">累计交换次数</div>
				</div>
				<div class="card-deco"><i class="el-icon-s-promotion"></i></div>
			</div>
			<div class="summary-card card-pink" v-if="isAuth('xuexiziliao','首页总数')">
				<div class="card-glow"></div>
				<div class="card-icon"><i class="el-icon-notebook-2"></i></div>
				<div class="card-info">
					<div class="card-num">{{counts.ziliao}}</div>
					<div class="card-label">学习资料</div>
					<div class="card-sub">共享学习资源</div>
				</div>
				<div class="card-deco"><i class="el-icon-notebook-2"></i></div>
			</div>
		</div>

		<div class="charts-grid">
			<div class="chart-card" v-if="isAuth('yonghu','首页统计',2)">
				<div class="chart-title">用户性别分布</div>
				<div class="chart-box" id="chartUserGender" style="width:100%;height:320px"></div>
			</div>
			<div class="chart-card" v-if="isAuth('jinengxuqiu','首页统计',2)">
				<div class="chart-title">技能分类分布</div>
				<div class="chart-box" id="chartXuqiuType" style="width:100%;height:320px"></div>
			</div>
			<div class="chart-card chart-card-wide" v-if="isAuth('jinengxuqiu','首页统计',2)">
				<div class="chart-title">需求发布趋势</div>
				<div class="chart-box" id="chartXuqiuDaily" style="width:100%;height:300px"></div>
			</div>
			<div class="chart-card chart-card-wide" v-if="isAuth('jiaohuanjilu','首页统计',2)">
				<div class="chart-title">交换趋势</div>
				<div class="chart-box" id="chartJiaohuan" style="width:100%;height:300px"></div>
			</div>
		<div class="chart-card" v-if="isAuth('xuexiziliao','首页统计',2)">
			<div class="chart-title">学习资料类型分布</div>
			<div class="chart-box" id="chartZiliao" style="width:100%;height:320px"></div>
		</div>
		<div class="chart-card">
			<div class="chart-title">技能社区帖子分类分布</div>
			<div class="chart-box" id="chartForum" style="width:100%;height:320px"></div>
		</div>
	</div>
	</div>
</template>

<script>
import * as echarts from 'echarts'
const COLORS = ['#4f6ef7','#10b981','#f59e0b','#ec4899','#8b5cf6','#06b6d4','#ef4444','#84cc16','#f97316','#14b8a6']

export default {
	components: {},
	data() {
		return {
			charts: {},
			counts: { yonghu: 0, fenlei: 0, xuqiu: 0, jiaohuan: 0, ziliao: 0 }
		}
	},
	mounted() {
		this.loadAllCounts()
		this.$nextTick(() => this.loadAllCharts())
	},
	beforeDestroy() {
		Object.values(this.charts).forEach(c => c && c.dispose())
	},
	methods: {
		loadAllCounts() {
			const apis = [
				{ key: 'yonghu', url: 'yonghu/count' },
				{ key: 'fenlei', url: 'jinengfenlei/count' },
				{ key: 'xuqiu', url: 'jinengxuqiu/count' },
				{ key: 'jiaohuan', url: 'jiaohuanjilu/count' },
				{ key: 'ziliao', url: 'xuexiziliao/count' },
			]
			apis.forEach(item => {
				this.$http({ url: item.url, method: 'get' }).then(({ data }) => {
					if (data && data.code == 0) this.counts[item.key] = data.data
				})
			})
		},
		loadAllCharts() {
			this.loadUserGender()
			this.loadXuqiuType()
			this.loadXuqiuDaily()
			this.loadJiaohuan()
			this.loadZiliao()
			this.loadForum()
		},
		initChart(id) {
			const el = document.getElementById(id)
			if (!el) return null
			if (this.charts[id]) { this.charts[id].resize(); return this.charts[id] }
			const chart = echarts.init(el)
			this.charts[id] = chart
			window.addEventListener('resize', () => chart.resize())
			return chart
		},
		pieOption(data) {
			return {
				backgroundColor: 'transparent',
				tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)', backgroundColor: 'rgba(15,23,42,0.9)', borderColor: 'rgba(0,180,216,0.2)', textStyle: { color: '#e2e8f0' } },
				legend: { orient: 'vertical', right: 10, top: 'center', icon: 'circle', itemWidth: 10, textStyle: { fontSize: 12, color: '#cbd5e1' } },
				color: COLORS,
				series: [{ type: 'pie', radius: ['40%', '68%'], center: ['40%', '52%'], data,
					emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.15)' } },
					label: { show: true, formatter: '{b}\n{d}%', fontSize: 11, color: '#cbd5e1' }
				}]
			}
		},
		lineOption(categories, values, areaColor) {
			const axisCommon = { axisLabel: { color: '#94a3b8', fontSize: 11 }, axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }, splitLine: { lineStyle: { color: 'rgba(255,255,255,0.06)' } } }
			return {
				backgroundColor: 'transparent',
				tooltip: { trigger: 'axis', backgroundColor: 'rgba(15,23,42,0.9)', borderColor: 'rgba(0,180,216,0.2)', textStyle: { color: '#e2e8f0' } },
				grid: { left: 15, right: 25, top: 20, bottom: 10, containLabel: true },
				xAxis: { type: 'category', data: categories, boundaryGap: false, ...axisCommon, axisLabel: { ...axisCommon.axisLabel, rotate: categories.length > 10 ? 30 : 0, interval: 0 } },
				yAxis: { type: 'value', minInterval: 1, ...axisCommon },
				series: [{ type: 'line', data: values, smooth: true, symbol: 'circle', symbolSize: 6,
					lineStyle: { width: 2.5, color: areaColor },
					itemStyle: { color: areaColor, borderColor: '#1e293b', borderWidth: 2 },
					areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
						{ offset: 0, color: areaColor.replace(')', ',0.3)').replace('rgb', 'rgba') },
						{ offset: 1, color: areaColor.replace(')', ',0.02)').replace('rgb', 'rgba') }
					])}
				}]
			}
		},

		loadUserGender() {
			const chart = this.initChart('chartUserGender')
			if (!chart) return
			this.$http({ url: 'yonghu/group/xingbie', method: 'get', params: { conditionColumn: 'sfsh', conditionValue: '是' } }).then(({ data }) => {
				if (data && data.code === 0) {
					let pieData = data.data.map(r => ({ name: r.xingbie || '未填写', value: parseFloat(r.total) }))
					chart.setOption(this.pieOption(pieData))
				}
			})
		},
		loadXuqiuType() {
			const chart = this.initChart('chartXuqiuType')
			if (!chart) return
			this.$http({ url: 'jinengxuqiu/group/jinengfenlei', method: 'get', params: { conditionColumn: 'sfsh', conditionValue: '是' } }).then(({ data }) => {
				if (data && data.code === 0) {
					let pieData = data.data.map(r => ({ name: r.jinengfenlei || '未填写', value: parseFloat(r.total) }))
					chart.setOption(this.pieOption(pieData))
				}
			})
		},
		loadXuqiuDaily() {
			const chart = this.initChart('chartXuqiuDaily')
			if (!chart) return
			this.$http({ url: 'jinengxuqiu/group/fabushijian', method: 'get', params: { conditionColumn: 'sfsh', conditionValue: '是' } }).then(({ data }) => {
				if (data && data.code === 0) {
					let sorted = data.data.sort((a, b) => (a.fabushijian || '').localeCompare(b.fabushijian || ''))
					let x = sorted.map(r => r.fabushijian || '未填写')
					let y = sorted.map(r => parseFloat(r.total))
					chart.setOption(this.lineOption(x, y, 'rgb(79,110,247)'))
				}
			})
		},
		loadJiaohuan() {
			const chart = this.initChart('chartJiaohuan')
			if (!chart) return
			this.$http({ url: 'jiaohuanjilu/group/jiaohuanshijian', method: 'get' }).then(({ data }) => {
				if (data && data.code === 0) {
					let sorted = data.data.sort((a, b) => (a.jiaohuanshijian || '').localeCompare(b.jiaohuanshijian || ''))
					let x = sorted.map(r => r.jiaohuanshijian || '未填写')
					let y = sorted.map(r => parseFloat(r.total))
					chart.setOption(this.lineOption(x, y, 'rgb(16,185,129)'))
				}
			})
		},
		loadZiliao() {
			const chart = this.initChart('chartZiliao')
			if (!chart) return
			this.$http({ url: 'xuexiziliao/group/ziliaoleixing', method: 'get' }).then(({ data }) => {
				if (data && data.code === 0) {
					let pieData = data.data.map(r => ({ name: r.ziliaoleixing || '未填写', value: parseFloat(r.total) }))
					chart.setOption(this.pieOption(pieData))
				}
			})
		},
		loadForum() {
			const chart = this.initChart('chartForum')
			if (!chart) return
			this.$http({ url: 'group/forum/typename', method: 'get' }).then(({ data }) => {
				if (data && data.code === 0) {
					let pieData = data.data
						.filter(r => r.typename)
						.map(r => ({ name: r.typename, value: parseFloat(r.total) }))
					chart.setOption(this.pieOption(pieData))
				}
			})
		},
	}
}
</script>

<style lang="scss" scoped>
$accent: #00b4d8;
$surface: #1e293b;
$text: #e2e8f0;
$text2: #94a3b8;
$text3: #64748b;
$border: rgba(255,255,255,0.06);
$radius: 12px;

.statistics-page {
	padding: 0;
}

.page-header {
	margin-bottom: 16px;
	h2 { margin: 0; font-size: 18px; font-weight: 700; color: $text; }
}

.summary-cards {
	display: grid;
	grid-template-columns: repeat(5, 1fr);
	gap: 16px;
	margin-bottom: 20px;

	.summary-card {
		position: relative;
		overflow: hidden;
		border-radius: 16px;
		padding: 24px 20px;
		display: flex;
		align-items: center;
		gap: 16px;
		transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
		cursor: default;
		border: 1px solid transparent;

		&:hover {
			transform: translateY(-4px);
			.card-glow { opacity: 1; }
			.card-deco i { transform: rotate(15deg) scale(1.1); opacity: 0.12; }
		}

		.card-glow {
			position: absolute; inset: 0; border-radius: 16px;
			opacity: 0; transition: opacity 0.35s;
		}

		.card-icon {
			width: 52px; height: 52px; border-radius: 14px;
			display: flex; align-items: center; justify-content: center; flex-shrink: 0;
			position: relative; z-index: 1;
			i { font-size: 24px; color: #fff; }
		}

		.card-info {
			position: relative; z-index: 1; flex: 1;
			.card-num { font-size: 32px; font-weight: 800; color: #fff; line-height: 1.1; letter-spacing: -1px; }
			.card-label { font-size: 14px; color: rgba(255,255,255,0.9); margin-top: 4px; font-weight: 600; }
			.card-sub { font-size: 11px; color: rgba(255,255,255,0.45); margin-top: 2px; }
		}

		.card-deco {
			position: absolute; right: -8px; bottom: -8px; z-index: 0;
			i { font-size: 80px; opacity: 0.07; color: #fff; transition: all 0.4s; }
		}
	}

	.card-blue {
		background: linear-gradient(135deg, #1e40af, #3b82f6);
		border-color: rgba(59,130,246,0.3);
		box-shadow: 0 4px 20px rgba(59,130,246,0.2);
		.card-glow { box-shadow: 0 8px 40px rgba(59,130,246,0.35); }
		.card-icon { background: rgba(255,255,255,0.2); }
		&:hover { box-shadow: 0 8px 32px rgba(59,130,246,0.35); }
	}

	.card-purple {
		background: linear-gradient(135deg, #5b21b6, #8b5cf6);
		border-color: rgba(139,92,246,0.3);
		box-shadow: 0 4px 20px rgba(139,92,246,0.2);
		.card-glow { box-shadow: 0 8px 40px rgba(139,92,246,0.35); }
		.card-icon { background: rgba(255,255,255,0.2); }
		&:hover { box-shadow: 0 8px 32px rgba(139,92,246,0.35); }
	}

	.card-cyan {
		background: linear-gradient(135deg, #0e7490, #06b6d4);
		border-color: rgba(6,182,212,0.3);
		box-shadow: 0 4px 20px rgba(6,182,212,0.2);
		.card-glow { box-shadow: 0 8px 40px rgba(6,182,212,0.35); }
		.card-icon { background: rgba(255,255,255,0.2); }
		&:hover { box-shadow: 0 8px 32px rgba(6,182,212,0.35); }
	}

	.card-green {
		background: linear-gradient(135deg, #047857, #10b981);
		border-color: rgba(16,185,129,0.3);
		box-shadow: 0 4px 20px rgba(16,185,129,0.2);
		.card-glow { box-shadow: 0 8px 40px rgba(16,185,129,0.35); }
		.card-icon { background: rgba(255,255,255,0.2); }
		&:hover { box-shadow: 0 8px 32px rgba(16,185,129,0.35); }
	}

	.card-pink {
		background: linear-gradient(135deg, #be185d, #ec4899);
		border-color: rgba(236,72,153,0.3);
		box-shadow: 0 4px 20px rgba(236,72,153,0.2);
		.card-glow { box-shadow: 0 8px 40px rgba(236,72,153,0.35); }
		.card-icon { background: rgba(255,255,255,0.2); }
		&:hover { box-shadow: 0 8px 32px rgba(236,72,153,0.35); }
	}
}

.charts-grid {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 14px;
	.chart-card {
		background: $surface;
		border: 1px solid $border;
		border-radius: $radius;
		padding: 18px;
		transition: all 0.25s;
		&:hover { box-shadow: 0 0 16px rgba(0,180,216,0.08); }
		.chart-title {
			font-size: 14px;
			font-weight: 600;
			color: $text;
			margin-bottom: 10px;
			padding-left: 10px;
			border-left: 3px solid $accent;
		}
	}
	.chart-card-wide {
		grid-column: span 2;
	}
}
</style>
