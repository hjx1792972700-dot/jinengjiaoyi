<template>
	<div class="my-need-wrap" :class="{'dark-embedded': embedded}">
		<!-- 用户画像 -->
		<div class="persona-section" v-if="persona.categories.length > 0">
			<div class="persona-row">
				<div class="persona-left">
					<div class="persona-header">
						<div class="persona-avatar">
							<img v-if="userInfo.touxiang" :src="baseUrl + userInfo.touxiang" />
							<el-icon v-else class="persona-avatar-icon"><UserFilled /></el-icon>
						</div>
						<div class="persona-info">
							<div class="persona-name">{{userInfo.yonghuxingming || userInfo.yonghuzhanghao || '用户'}}</div>
							<div class="persona-tagline">基于您的需求生成的个人画像</div>
						</div>
					</div>
					<div class="persona-body">
						<div class="persona-tags">
							<div class="ptag" v-for="(cat, idx) in persona.categories" :key="idx"
								:style="{ background: tagColors[idx % tagColors.length].bg, color: tagColors[idx % tagColors.length].text, borderColor: tagColors[idx % tagColors.length].border }">
								<i class="el-icon-collection-tag"></i>
								{{cat.name}}
								<span class="ptag-count">{{cat.count}}</span>
							</div>
						</div>
						<div class="persona-stats-row">
							<div class="pstat">
								<span class="pstat-num">{{stats.total}}</span>
								<span class="pstat-label">需求总数</span>
							</div>
							<div class="pstat">
								<span class="pstat-num">{{stats.skillTotal || 0}}</span>
								<span class="pstat-label">技能总数</span>
							</div>
							<div class="pstat">
								<span class="pstat-num">{{persona.categories.length}}</span>
								<span class="pstat-label">兴趣方向</span>
							</div>
							<div class="pstat">
								<span class="pstat-num">{{persona.topCategory || '--'}}</span>
								<span class="pstat-label">最关注领域</span>
							</div>
						</div>
						<div class="persona-desc">
							<el-icon class="persona-desc-icon"><InfoFilled /></el-icon>
							<span v-if="persona.categories.length >= 3">
								您的兴趣广泛，涵盖<b>{{persona.categories.slice(0,3).map(c=>c.name).join('、')}}</b>等多个领域，是一位全方位发展的学习者。
							</span>
							<span v-else-if="persona.categories.length === 2">
								您主要关注<b>{{persona.categories[0].name}}</b>和<b>{{persona.categories[1].name}}</b>两个方向，学习目标明确。
							</span>
							<span v-else>
								您当前专注于<b>{{persona.categories[0].name}}</b>领域，可以尝试拓展更多技能方向。
							</span>
						</div>
					</div>
				</div>
				<div class="persona-right">
					<div ref="radarChart" class="radar-chart"></div>
				</div>
			</div>
		</div>

		<!-- 顶部操作栏 -->
		<div class="need-overview">
			<div class="overview-stats">
				<div class="stat-item total">
					<div class="stat-icon"><i class="el-icon-document"></i></div>
					<div class="stat-num">{{stats.total}}</div>
					<div class="stat-label">全部</div>
				</div>
				<div class="stat-item passed">
					<div class="stat-icon"><i class="el-icon-circle-check"></i></div>
					<div class="stat-num">{{stats.passed}}</div>
					<div class="stat-label">已通过</div>
				</div>
				<div class="stat-item pending">
					<div class="stat-icon"><i class="el-icon-time"></i></div>
					<div class="stat-num">{{stats.pending}}</div>
					<div class="stat-label">待审核</div>
				</div>
				<div class="stat-item rejected">
					<div class="stat-icon"><i class="el-icon-circle-close"></i></div>
					<div class="stat-num">{{stats.rejected}}</div>
					<div class="stat-label">未通过</div>
				</div>
		</div>
	</div>

		<!-- 需求列表 -->
		<div class="need-list-section">
			<div class="section-header">
				<h3><i class="el-icon-s-grid"></i> 我的需求列表</h3>
				<el-button type="primary" size="small" class="header-add-btn" @click="publishNeed">
					<template #icon><Plus /></template>
					发布需求
				</el-button>
			</div>

			<div class="need-empty" v-if="!loading && tableData.length === 0">
				<el-icon class="need-empty-icon"><DocumentDelete /></el-icon>
				<p>还没有发布过需求</p>
				<el-button type="primary" size="small" class="empty-btn" @click="publishNeed">
					<i class="el-icon-plus"></i> 发布我的第一个需求
				</el-button>
			</div>

			<div class="need-cards" v-loading="loading" v-if="tableData.length > 0 || loading">
				<div class="need-card" v-for="(item, index) in tableData" :key="index">
					<div class="card-header">
						<div class="card-title">{{item.xuqiubiaoti || '未命名需求'}}</div>
						<span class="card-status status-pass" v-if="item.sfsh==='是'"><i class="el-icon-circle-check"></i> 已通过</span>
						<span class="card-status status-pending" v-else-if="item.sfsh==='待审核'||!item.sfsh"><i class="el-icon-time"></i> 待审核</span>
						<span class="card-status status-fail" v-else-if="item.sfsh==='否'"><i class="el-icon-circle-close"></i> 未通过</span>
					</div>
					<div class="card-body">
						<div class="card-meta">
							<span class="meta-item" v-if="item.jinengfenlei">
								<i class="el-icon-collection-tag"></i> {{item.jinengfenlei}}
							</span>
							<span class="meta-item">
								<i class="el-icon-date"></i> {{item.fabushijian || (item.addtime ? item.addtime.split(' ')[0] : '--')}}
							</span>
						</div>
						<div class="card-desc" v-if="item.xuqiumiaoshu || item.xuqiuxiangqing">
							{{stripHtml(item.xuqiumiaoshu || item.xuqiuxiangqing)}}
						</div>
					</div>
					<div class="card-footer">
						<div class="card-stats">
							<span><i class="el-icon-view"></i> {{item.clicknum || 0}}</span>
						</div>
						<div class="card-actions">
							<el-button size="small" link type="primary" class="btn-edit" @click="editNeed(item)">
								<template #icon><Edit /></template>
								修改
							</el-button>
							<el-button size="small" link type="danger" class="btn-del" @click="delNeed(item)">
								<template #icon><Delete /></template>
								删除
							</el-button>
						</div>
					</div>
				</div>
			</div>

			<el-pagination v-if="total > pageSize" background class="pagination"
				:page-size="pageSize" :total="total" :current-page="page"
				layout="total, prev, pager, next"
				@current-change="pageChange">
			</el-pagination>
		</div>
	</div>
</template>

<script>
import config from '@/config/config';
import * as echarts from 'echarts';
export default {
	props: { embedded: { type: Boolean, default: false } },
	data() {
		return {
			baseUrl: config.baseUrl,
			tableData: [],
			total: 0,
			page: 1,
			pageSize: 10,
			loading: false,
			stats: { total: 0, skillTotal: 0, passed: 0, pending: 0, rejected: 0 },
			userInfo: {},
			persona: {
				categories: [],
				topCategory: '',
				allCategories: []
			},
			radarChart: null,
			tagColors: [
				{ bg: 'rgba(0,212,255,0.12)', text: '#00d4ff', border: 'rgba(0,212,255,0.25)' },
				{ bg: 'rgba(124,58,237,0.12)', text: '#a78bfa', border: 'rgba(124,58,237,0.25)' },
				{ bg: 'rgba(34,197,94,0.12)', text: '#22c55e', border: 'rgba(34,197,94,0.25)' },
				{ bg: 'rgba(245,158,11,0.12)', text: '#f59e0b', border: 'rgba(245,158,11,0.25)' },
				{ bg: 'rgba(236,72,153,0.12)', text: '#ec4899', border: 'rgba(236,72,153,0.25)' },
				{ bg: 'rgba(59,130,246,0.12)', text: '#3b82f6', border: 'rgba(59,130,246,0.25)' },
			]
		}
	},
	created() {
		this.loadUserInfo();
		this.getList();
	},
	beforeDestroy() {
		if (this.radarChart) {
			this.radarChart.dispose();
			this.radarChart = null;
		}
	},
	methods: {
		stripHtml(html) {
			if (!html) return '';
			let text = html.replace(/<[^>]+>/g, '').replace(/&nbsp;/g, ' ');
			return text.length > 80 ? text.substring(0, 80) + '...' : text;
		},
		loadUserInfo() {
			let session = JSON.parse(localStorage.getItem('sessionForm'));
			if (session) this.userInfo = session;
		},
		buildPersona(needList, skillList) {
			let catMap = {};
			needList.forEach(item => {
				let cat = item.jinengfenlei;
				if (cat) catMap[cat] = (catMap[cat] || 0) + 1;
			});
			skillList.forEach(item => {
				let cat = item.jinengfenlei;
				if (cat) catMap[cat] = (catMap[cat] || 0) + 1;
			});
			let cats = Object.keys(catMap).map(k => ({ name: k, count: catMap[k] }));
			cats.sort((a, b) => b.count - a.count);
			this.persona.categories = cats;
			this.persona.topCategory = cats.length > 0 ? cats[0].name : '--';

			let needCatMap = {};
			needList.forEach(item => {
				if (item.jinengfenlei) needCatMap[item.jinengfenlei] = (needCatMap[item.jinengfenlei] || 0) + 1;
			});
			let skillCatMap = {};
			skillList.forEach(item => {
				if (item.jinengfenlei) skillCatMap[item.jinengfenlei] = (skillCatMap[item.jinengfenlei] || 0) + 1;
			});
			this.persona.allCategories = cats.map(c => ({
				name: c.name,
				needCount: needCatMap[c.name] || 0,
				skillCount: skillCatMap[c.name] || 0
			}));

			this.$nextTick(() => this.renderRadarChart());
		},
		renderRadarChart() {
			if (!this.$refs.radarChart) return;
			if (this.radarChart) this.radarChart.dispose();

			this.radarChart = echarts.init(this.$refs.radarChart);
			let userCatMap = {};
			this.persona.allCategories.forEach(c => { userCatMap[c.name] = c; });
			let allCatNames = ['编程', '设计', '语言', '生活', '艺术', '学习', '工具', '运动'];
			let cats = allCatNames.map(name => ({
				name,
				needCount: (userCatMap[name] && userCatMap[name].needCount) || 0,
				skillCount: (userCatMap[name] && userCatMap[name].skillCount) || 0
			}));
			let maxVal = Math.max(...cats.map(c => Math.max(c.needCount, c.skillCount)), 3);

			let option = {
				backgroundColor: 'transparent',
			tooltip: {
				trigger: 'item',
				appendToBody: true,
				backgroundColor: 'rgba(15,23,42,0.95)',
				borderColor: 'rgba(0,212,255,0.3)',
				borderWidth: 1,
				padding: [10, 14],
				textStyle: { color: '#e2e8f0', fontSize: 12 },
					formatter: function(params) {
						let html = '<div style="font-weight:600;margin-bottom:6px;color:' + params.color + '">' + params.seriesName + ' — ' + params.name + '</div>';
						let indicators = params.data.name === '需求' ? '需求数' : '技能数';
						params.value.forEach(function(v, i) {
							let active = v > 0 ? 'color:#f1f5f9' : 'color:#64748b';
							html += '<div style="' + active + ';font-size:12px;line-height:1.8">' + cats[i].name + '：<b>' + v + '</b></div>';
						});
						return html;
					}
				},
				legend: {
					data: ['需求分布', '技能分布'],
					bottom: 0,
					textStyle: { color: '#94a3b8', fontSize: 11 },
					itemWidth: 16, itemHeight: 8,
					itemGap: 20,
					icon: 'roundRect'
				},
				radar: {
					indicator: cats.map(c => ({ name: c.name, max: maxVal })),
					center: ['50%', '44%'],
					radius: '60%',
					shape: 'polygon',
					startAngle: 90,
					splitNumber: 4,
					name: {
						textStyle: { color: '#cbd5e1', fontSize: 11, fontWeight: 500 },
						formatter: function(name) {
							let cat = cats.find(function(c) { return c.name === name; });
							let total = cat ? (cat.needCount + cat.skillCount) : 0;
							return total > 0 ? name + ' (' + total + ')' : name;
						}
					},
					splitLine: { lineStyle: { color: ['rgba(0,212,255,0.04)','rgba(0,212,255,0.06)','rgba(0,212,255,0.08)','rgba(0,212,255,0.1)','rgba(0,212,255,0.12)'] } },
					splitArea: {
						areaStyle: {
							color: ['rgba(0,212,255,0.01)','rgba(0,212,255,0.02)','rgba(0,212,255,0.03)','rgba(0,212,255,0.04)','rgba(0,212,255,0.05)']
						}
					},
					axisLine: { lineStyle: { color: 'rgba(255,255,255,0.1)' } }
				},
				series: [{
					type: 'radar',
					emphasis: {
						lineStyle: { width: 3 },
						areaStyle: { opacity: 0.3 }
					},
					data: [
						{
							name: '需求分布',
							value: cats.map(c => c.needCount),
							areaStyle: {
								color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [
									{ offset: 0, color: 'rgba(0,212,255,0.25)' },
									{ offset: 1, color: 'rgba(0,212,255,0.05)' }
								]}
							},
							lineStyle: { color: '#00d4ff', width: 2, shadowColor: 'rgba(0,212,255,0.3)', shadowBlur: 6 },
							itemStyle: { color: '#00d4ff', borderColor: '#fff', borderWidth: 1 },
							symbol: 'circle', symbolSize: 6
						},
						{
							name: '技能分布',
							value: cats.map(c => c.skillCount),
							areaStyle: {
								color: { type: 'linear', x: 0, y: 0, x2: 0, y2: 1, colorStops: [
									{ offset: 0, color: 'rgba(124,58,237,0.25)' },
									{ offset: 1, color: 'rgba(124,58,237,0.05)' }
								]}
							},
							lineStyle: { color: '#a78bfa', width: 2, shadowColor: 'rgba(124,58,237,0.3)', shadowBlur: 6 },
							itemStyle: { color: '#a78bfa', borderColor: '#fff', borderWidth: 1 },
							symbol: 'circle', symbolSize: 6
						}
					]
				}]
			};
			this.radarChart.setOption(option);
		},
		getList() {
			this.loading = true;
			let user = JSON.parse(localStorage.getItem('sessionForm'));
			let params = {
				page: this.page,
				limit: this.pageSize,
				sort: 'addtime',
				order: 'desc',
				yonghuzhanghao: user.yonghuzhanghao,
				leixing: '需求'
			};
			this.$http.get('jinengxuqiu/page', { params }).then(res => {
				this.loading = false;
				if (res.data.code == 0) {
					this.tableData = res.data.data.list;
					this.total = Number(res.data.data.total);
				}
			}).catch(() => { this.loading = false; });

			let needList = [];
			let skillList = [];
			let done = 0;
			let buildIfReady = () => {
				done++;
				if (done >= 2) {
					this.stats.total = needList.length;
					this.stats.skillTotal = skillList.length;
					this.buildPersona(needList, skillList);
				}
			};

			this.$http.get('jinengxuqiu/page', {
				params: { page: 1, limit: 999, yonghuzhanghao: user.yonghuzhanghao, leixing: '需求' }
			}).then(res => {
				if (res.data.code == 0) needList = res.data.data.list || [];
				this.stats.passed = needList.filter(i => i.sfsh === '是').length;
				this.stats.rejected = needList.filter(i => i.sfsh === '否').length;
				this.stats.pending = needList.filter(i => i.sfsh === '待审核' || !i.sfsh).length;
				buildIfReady();
			}).catch(() => buildIfReady());

			this.$http.get('jinengxuqiu/page', {
				params: { page: 1, limit: 999, yonghuzhanghao: user.yonghuzhanghao, leixing: '技能' }
			}).then(res => {
				if (res.data.code == 0) skillList = res.data.data.list || [];
				buildIfReady();
			}).catch(() => buildIfReady());
		},
		pageChange(val) {
			this.page = val;
			this.getList();
		},
		publishNeed() {
			this.$router.push({ path: '/main/jinengxuqiuAdd', query: { centerType: 1 } });
		},
		editNeed(row) {
			this.$router.push({ path: '/main/jinengxuqiuAdd', query: { type: 'edit', id: row.id, centerType: 1 } });
		},
		delNeed(row) {
			this.$confirm('确定删除该需求？删除后不可恢复', '提示', { type: 'warning' }).then(() => {
				this.$http.post('jinengxuqiu/delete', [row.id]).then(res => {
					if (res.data.code == 0) {
						this.$message.success('删除成功');
						this.getList();
					}
				});
			}).catch(() => {});
		}
	}
}
</script>

<style lang="scss" scoped>
$primary: #0ea5e9;
$cyan: #00d4ff;
$purple: #7c3aed;
$green: #22c55e;
$amber: #f59e0b;
$red: #ef4444;

/* 用户画像 */
.persona-section {
	background: rgba(255,255,255,0.02);
	border: 1px solid rgba(255,255,255,0.08);
	border-radius: 14px;
	padding: 20px;
	margin-bottom: 18px;
	position: relative;
	overflow: hidden;
	&::before {
		content: '';
		position: absolute;
		top: -60px; right: -60px;
		width: 180px; height: 180px;
		background: radial-gradient(circle, rgba($cyan, 0.06) 0%, transparent 70%);
		pointer-events: none;
	}
}

.persona-row {
	display: flex;
	gap: 16px;
	align-items: stretch;
	flex-wrap: wrap;
}

.persona-left {
	flex: 1;
	min-width: 280px;
}

.persona-right {
	width: 260px;
	flex-shrink: 0;
	display: flex;
	align-items: center;
	justify-content: center;
	background: linear-gradient(135deg, rgba(0,212,255,0.02), rgba(124,58,237,0.02));
	border: 1px solid rgba(255,255,255,0.06);
	border-radius: 12px;
	position: relative;
	&::before {
		content: '技能画像';
		position: absolute;
		top: 8px;
		left: 12px;
		font-size: 11px;
		color: rgba(255,255,255,0.25);
		font-weight: 600;
		letter-spacing: 1px;
	}
	.radar-chart {
		width: 100%;
		height: 280px;
	}
}

.persona-header {
	display: flex;
	align-items: center;
	gap: 14px;
	margin-bottom: 18px;
}

.persona-avatar {
	width: 52px; height: 52px;
	border-radius: 50%;
	background: linear-gradient(135deg, rgba($cyan, 0.15), rgba($purple, 0.15));
	border: 2px solid rgba($cyan, 0.2);
	display: flex; align-items: center; justify-content: center;
	overflow: hidden;
	flex-shrink: 0;
	img { width: 100%; height: 100%; object-fit: cover; }
	.persona-avatar-icon { font-size: 26px; color: $cyan; }
}

.persona-info {
	.persona-name { font-size: 16px; font-weight: 700; color: #e2e8f0; }
	.persona-tagline { font-size: 12px; color: #64748b; margin-top: 2px; }
}

.persona-body {
	.persona-tags {
		display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 16px;
	}
}

.ptag {
	display: inline-flex; align-items: center; gap: 5px;
	padding: 5px 12px; border-radius: 20px; font-size: 12px; font-weight: 500;
	border: 1px solid;
	.el-icon { font-size: 12px; }
	.ptag-count {
		display: inline-flex; align-items: center; justify-content: center;
		min-width: 18px; height: 18px; border-radius: 9px;
		background: rgba(255,255,255,0.12); font-size: 10px; font-weight: 700;
		padding: 0 4px;
	}
}

.persona-stats-row {
	display: flex; gap: 16px; margin-bottom: 14px;
	padding: 10px 14px;
	background: rgba(255,255,255,0.02);
	border-radius: 10px;
	border: 1px solid rgba(255,255,255,0.04);
	flex-wrap: wrap;
}

.pstat {
	display: flex; flex-direction: column; align-items: center;
	white-space: nowrap;
	.pstat-num { font-size: 16px; font-weight: 700; color: #e2e8f0; }
	.pstat-label { font-size: 11px; color: #64748b; margin-top: 2px; }
}

.persona-desc {
	font-size: 13px; color: #94a3b8; line-height: 1.6;
	display: flex; gap: 6px; align-items: flex-start;
	padding: 10px 14px;
	background: rgba($cyan, 0.03);
	border-radius: 8px;
	border: 1px solid rgba($cyan, 0.06);
	.persona-desc-icon { color: $cyan; margin-top: 2px; flex-shrink: 0; }
	b { color: $cyan; font-weight: 600; }
}

/* 概览卡片 */
.need-overview {
	display: flex;
	align-items: stretch;
	gap: 12px;
	margin-bottom: 18px;
}

.overview-stats {
	flex: 1;
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 0;
	background: rgba(255,255,255,0.03);
	border: 1px solid rgba(255,255,255,0.06);
	border-radius: 12px;
	overflow: hidden;
}

.stat-item {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 14px 6px;
	transition: all 0.3s;
	position: relative;
	&:not(:last-child)::after {
		content: '';
		position: absolute;
		right: 0;
		top: 20%;
		height: 60%;
		width: 1px;
		background: rgba(255,255,255,0.06);
	}
	&:hover { background: rgba(255,255,255,0.04); }
	.stat-icon {
		width: 32px; height: 32px; border-radius: 50%;
		display: flex; align-items: center; justify-content: center;
		margin-bottom: 6px;
		.el-icon { font-size: 16px; }
	}
	.stat-num { font-size: 20px; font-weight: 700; color: #e2e8f0; line-height: 1.2; }
	.stat-label { font-size: 11px; color: #94a3b8; margin-top: 2px; white-space: nowrap; }
	&.total .stat-icon { background: rgba($primary, 0.12); .el-icon { color: $primary; } }
	&.passed .stat-icon { background: rgba($green, 0.12); .el-icon { color: $green; } }
	&.pending .stat-icon { background: rgba($amber, 0.12); .el-icon { color: $amber; } }
	&.rejected .stat-icon { background: rgba($red, 0.12); .el-icon { color: $red; } }
}

/* 列表 */
.need-list-section {
	background: rgba(255,255,255,0.02);
	border: 1px solid rgba(255,255,255,0.06);
	border-radius: 12px;
	padding: 18px;
}

.section-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 16px;
	h3 {
		font-size: 15px; color: #e2e8f0; margin: 0; font-weight: 600;
		.el-icon { color: $cyan; margin-right: 6px; vertical-align: middle; }
	}
	.header-add-btn {
		border-radius: 16px !important;
		font-size: 12px !important;
		padding: 6px 14px !important;
		background: linear-gradient(135deg, $cyan, $purple) !important;
		border: none !important;
		color: #fff !important;
		flex-shrink: 0;
	}
}

.need-empty {
	text-align: center; padding: 48px 20px;
	.need-empty-icon { font-size: 48px; color: #475569; display: block; margin: 0 auto 12px; }
	p { font-size: 14px; color: #64748b; margin: 0 0 16px; }
	.empty-btn {
		border-radius: 20px;
		background: linear-gradient(135deg, $cyan, $purple) !important;
		border: none !important;
	}
}

.need-cards {
	display: flex; flex-direction: column; gap: 12px;
}

.need-card {
	background: rgba(255,255,255,0.02);
	border: 1px solid rgba(255,255,255,0.06);
	border-radius: 10px;
	padding: 16px;
	transition: all 0.3s;
	&:hover {
		border-color: rgba($cyan, 0.2);
		background: rgba($cyan, 0.02);
	}
}

.card-header {
	display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;
	.card-title {
		font-size: 15px; font-weight: 600; color: #e2e8f0;
		flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
		margin-right: 12px;
	}
	.card-status {
		flex-shrink: 0;
		display: inline-flex; align-items: center; gap: 4px;
		font-size: 12px; font-weight: 500;
		padding: 3px 10px; border-radius: 12px;
		white-space: nowrap;
		.el-icon { font-size: 13px; }
		&.status-pass {
			background: rgba($green, 0.12); color: $green; border: 1px solid rgba($green, 0.25);
		}
		&.status-pending {
			background: rgba($amber, 0.12); color: $amber; border: 1px solid rgba($amber, 0.25);
		}
		&.status-fail {
			background: rgba($red, 0.12); color: $red; border: 1px solid rgba($red, 0.25);
		}
	}
}

.card-body {
	.card-meta {
		display: flex; gap: 16px; margin-bottom: 8px;
		.meta-item {
			font-size: 12px; color: #94a3b8;
			display: flex; align-items: center; gap: 4px;
			.el-icon { color: #64748b; font-size: 13px; }
		}
	}
	.card-desc {
		font-size: 13px; color: #94a3b8; line-height: 1.5;
		overflow: hidden; text-overflow: ellipsis;
		display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
	}
}

.card-footer {
	display: flex; justify-content: space-between; align-items: center;
	margin-top: 12px; padding-top: 10px;
	border-top: 1px solid rgba(255,255,255,0.04);
	.card-stats {
		display: flex; gap: 14px;
		span {
			font-size: 12px; color: #64748b;
			display: flex; align-items: center; gap: 4px;
			.el-icon { font-size: 14px; }
		}
	}
	.card-actions {
		display: flex; gap: 4px;
		.btn-edit { color: $cyan !important; font-size: 12px; }
		.btn-del { color: #94a3b8 !important; font-size: 12px; &:hover { color: $red !important; } }
	}
}

.pagination {
	text-align: center; margin-top: 16px;
	::v-deep .el-pager li {
		background: rgba(255,255,255,0.04) !important; color: #94a3b8 !important; border: none !important;
		&.active { background: linear-gradient(135deg, $cyan, $purple) !important; color: #fff !important; }
	}
	::v-deep .btn-prev, ::v-deep .btn-next {
		background: rgba(255,255,255,0.04) !important; color: #94a3b8 !important;
	}
	::v-deep .el-pagination__total { color: rgba(255,255,255,0.35) !important; }
}
</style>
