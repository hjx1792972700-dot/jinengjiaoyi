<template>
	<div class="my-skill-wrap" :class="{'dark-embedded': embedded}">
		<!-- 审核状态筛选 -->
		<div class="top-bar">
			<div class="stat-chips">
				<div class="chip" :class="{active: filterStatus==='all'}" @click="filterStatus='all'; page=1; getList()">
					<i class="el-icon-grid"></i>
					<span class="chip-label">全部</span>
					<span class="chip-num">{{stats.total}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='待审核'}" @click="filterStatus='待审核'; page=1; getList()">
					<i class="el-icon-time"></i>
					<span class="chip-label">待审核</span>
					<span class="chip-num">{{stats.pending}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='是'}" @click="filterStatus='是'; page=1; getList()">
					<i class="el-icon-circle-check"></i>
					<span class="chip-label">已通过</span>
					<span class="chip-num">{{stats.approved}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='否'}" @click="filterStatus='否'; page=1; getList()">
					<i class="el-icon-circle-close"></i>
					<span class="chip-label">未通过</span>
					<span class="chip-num">{{stats.rejected}}</span>
				</div>
			</div>
		</div>

		<div class="skill-cards" v-loading="loading">
			<div class="skill-card" v-for="(item, index) in tableData" :key="item.id"
				:style="{'animation-delay': (index * 0.06) + 's'}">
				<div class="card-cover" :style="getCoverBg(index)">
					<img v-if="item.fengmian" :src="getImgUrl(item.fengmian)" @error="$event.target.style.display='none'" />
					<div class="cover-overlay">
						<span :class="item.sfsh==='是'?'status-badge status-pass':(item.sfsh==='否'?'status-badge status-fail':'status-badge status-wait')">
							<i :class="item.sfsh==='是'?'el-icon-circle-check':(item.sfsh==='否'?'el-icon-circle-close':'el-icon-time')"></i>
							{{item.sfsh==='是'?'已通过':(item.sfsh==='否'?'未通过':'待审核')}}
						</span>
					</div>
				</div>
				<div class="card-body">
					<div class="card-title" @click="toDetail(item)">{{item.xuqiubiaoti}}</div>
					<div class="card-meta">
						<span class="meta-tag">{{item.jinengfenlei}}</span>
						<span class="meta-price" v-if="item.xuqiufeiyong">¥{{item.xuqiufeiyong}}</span>
						<span class="meta-price meta-free" v-else>面议</span>
					</div>
					<div class="card-stats">
						<span class="stat-item"><i class="el-icon-view"></i>{{item.clicknum||0}}</span>
						<span class="stat-item"><i class="el-icon-star-off"></i>{{item.storeupnum||0}}</span>
						<span class="stat-item stat-time"><i class="el-icon-time"></i>{{item.addtime ? item.addtime.split(' ')[0] : '--'}}</span>
					</div>
				</div>
				<div class="card-actions">
					<div class="action-btn action-edit" @click="toEdit(item)">
						<i class="el-icon-edit"></i><span>编辑</span>
					</div>
					<div class="action-btn action-view" @click="toDetail(item)">
						<i class="el-icon-view"></i><span>查看</span>
					</div>
					<div class="action-btn action-del" @click="delSkill(item)">
						<i class="el-icon-delete"></i><span>删除</span>
					</div>
				</div>
			</div>

			<div class="empty-state" v-if="!loading && tableData.length === 0">
				<div class="empty-icon">
					<svg viewBox="0 0 80 80" width="64" height="64">
						<rect x="12" y="20" width="56" height="44" rx="6" fill="none" stroke="rgba(0,212,255,0.25)" stroke-width="1.5"/>
						<line x1="24" y1="34" x2="56" y2="34" stroke="rgba(0,212,255,0.2)" stroke-width="1.5" stroke-linecap="round"/>
						<line x1="24" y1="42" x2="48" y2="42" stroke="rgba(124,58,237,0.2)" stroke-width="1.5" stroke-linecap="round"/>
						<line x1="24" y1="50" x2="40" y2="50" stroke="rgba(0,212,255,0.15)" stroke-width="1.5" stroke-linecap="round"/>
						<circle cx="56" cy="52" r="12" fill="none" stroke="rgba(0,212,255,0.3)" stroke-width="1.5"/>
						<line x1="56" y1="47" x2="56" y2="57" stroke="rgba(0,212,255,0.3)" stroke-width="1.5" stroke-linecap="round"/>
						<line x1="51" y1="52" x2="61" y2="52" stroke="rgba(0,212,255,0.3)" stroke-width="1.5" stroke-linecap="round"/>
					</svg>
				</div>
				<div class="empty-title">还没有发布技能</div>
				<div class="empty-desc">发布你的技能，让更多人发现你的才华</div>
			</div>
		</div>

		<el-pagination v-if="total > pageSize" background class="pagination"
			:page-size="pageSize" :total="total" :current-page="page"
			layout="total, prev, pager, next"
			@current-change="pageChange">
		</el-pagination>
	</div>
</template>

<script>
export default {
	props: { embedded: { type: Boolean, default: false } },
	data() {
		return {
			tableData: [],
			total: 0,
			page: 1,
			pageSize: 8,
			loading: false,
			baseUrl: '',
			filterStatus: 'all',
			stats: { total: 0, pending: 0, approved: 0, rejected: 0 },
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.getList();
	},
	methods: {
		getImgUrl(img) {
			if (!img) return '';
			if (img.substr(0, 4) === 'http') return img.split(',')[0];
			return this.baseUrl + img.split(',')[0];
		},
		getCoverBg(index) {
			const gradients = [
				'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
				'linear-gradient(135deg, #0ea5e9 0%, #7c3aed 100%)',
				'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
				'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
				'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
				'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
			];
			return { background: gradients[index % gradients.length] };
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
				leixing: '技能'
			};
			if (this.filterStatus !== 'all') params.sfsh = this.filterStatus;
			this.$http.get('jinengxuqiu/page', { params }).then(res => {
				this.loading = false;
				if (res.data.code == 0) {
					this.tableData = res.data.data.list;
					this.total = Number(res.data.data.total);
				}
			}).catch(() => { this.loading = false; });
			this.$http.get('jinengxuqiu/page', { params: { page: 1, limit: 999, yonghuzhanghao: user.yonghuzhanghao, leixing: '技能' } }).then(res => {
				if (res.data.code == 0) {
					let ls = res.data.data.list || [];
					this.stats.total = ls.length;
					this.stats.pending = ls.filter(i => i.sfsh === '待审核' || !i.sfsh).length;
					this.stats.approved = ls.filter(i => i.sfsh === '是').length;
					this.stats.rejected = ls.filter(i => i.sfsh === '否').length;
				}
			});
		},
		pageChange(val) {
			this.page = val;
			this.getList();
		},
		toEdit(row) {
			if (row.sfsh === '是') {
				this.$alert('您的该技能已进入技能市场，如有变动请联系管理员！！', '提示', {
					confirmButtonText: '我知道了',
					type: 'warning'
				});
				return;
			}
			this.$router.push({ path: '/main/jinengxuqiuAdd', query: { type: 'edit', id: row.id } });
		},
		toDetail(row) {
			this.$router.push({ path: '/main/jinengxuqiuDetail', query: { id: row.id, centerType: 1, fromSkill: 1 } });
		},
		delSkill(row) {
			if (row.sfsh === '是') {
				this.$alert('您的该技能已进入技能市场，如有变动请联系管理员！！', '提示', {
					confirmButtonText: '我知道了',
					type: 'warning'
				});
				return;
			}
			this.$confirm('确定删除该技能发布？', '提示', { type: 'warning' }).then(() => {
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
$cyan: #00d4ff;
$purple: #7c3aed;
$green: #22c55e;
$amber: #f59e0b;
$red: #ef4444;
$text-bright: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: rgba(255,255,255,0.35);
$glass: rgba(255,255,255,0.03);
$glass-border: rgba(255,255,255,0.06);

.my-skill-wrap {
	width: 100%;
}

.top-bar {
	display: flex; justify-content: space-between; align-items: center;
	margin-bottom: 16px; gap: 16px; flex-wrap: wrap;
}
.stat-chips {
	display: flex; gap: 8px; flex-wrap: wrap;
}
.chip {
	display: flex; align-items: center; gap: 6px;
	padding: 8px 16px; border-radius: 20px;
	background: $glass; border: 1px solid $glass-border;
	cursor: pointer; transition: all 0.25s; user-select: none;
	i { font-size: 14px; color: rgba(255,255,255,0.3); transition: color 0.25s; }
	.chip-label { font-size: 13px; color: $text-sub; transition: color 0.25s; }
	.chip-num {
		font-size: 12px; font-weight: 700; color: rgba(255,255,255,0.3);
		background: rgba(255,255,255,0.04); padding: 1px 8px; border-radius: 10px;
		min-width: 20px; text-align: center; transition: all 0.25s;
	}
	&:hover { border-color: rgba($cyan, 0.25); background: rgba($cyan, 0.04);
		i, .chip-label { color: $cyan; }
	}
	&.active {
		background: rgba($cyan, 0.08); border-color: rgba($cyan, 0.3);
		i { color: $cyan; }
		.chip-label { color: $text-bright; font-weight: 600; }
		.chip-num { background: rgba($cyan, 0.15); color: $cyan; }
	}
}

.skill-cards {
	display: flex;
	flex-direction: column;
	gap: 12px;
	min-height: 100px;
}

.skill-card {
	display: flex;
	align-items: center;
	gap: 16px;
	padding: 14px 16px;
	background: rgba(255,255,255,0.03);
	border: 1px solid rgba(255,255,255,0.06);
	border-radius: 14px;
	transition: all 0.3s cubic-bezier(0.4,0,0.2,1);
	animation: skillCardIn 0.4s ease-out both;

	&:hover {
		background: rgba(0,212,255,0.04);
		border-color: rgba(0,212,255,0.15);
		transform: translateX(4px);
		box-shadow: 0 4px 20px rgba(0,212,255,0.06);

		.card-cover img { transform: scale(1.08); }
		.card-actions { opacity: 1; transform: translateX(0); }
	}

	.card-cover {
		flex-shrink: 0;
		width: 72px;
		height: 72px;
		border-radius: 12px;
		overflow: hidden;
		position: relative;
		display: flex;
		align-items: center;
		justify-content: center;

		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
			transition: transform 0.4s cubic-bezier(0.4,0,0.2,1);
		}

		.cover-overlay {
			position: absolute;
			top: 6px;
			right: 6px;
		}
	}

	.card-body {
		flex: 1;
		min-width: 0;

		.card-title {
			font-size: 15px;
			font-weight: 600;
			color: $text-bright;
			margin-bottom: 6px;
			cursor: pointer;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			transition: color 0.25s;

			&:hover { color: $cyan; }
		}

		.card-meta {
			display: flex;
			align-items: center;
			gap: 8px;
			margin-bottom: 6px;

			.meta-tag {
				padding: 2px 10px;
				background: rgba(0,212,255,0.1);
				color: $cyan;
				font-size: 11px;
				font-weight: 500;
				border-radius: 10px;
				letter-spacing: 0.5px;
			}

			.meta-price {
				font-size: 14px;
				font-weight: 700;
				color: $cyan;
				letter-spacing: 0.5px;
			}
			.meta-free {
				color: $text-sub;
				font-size: 12px;
				font-weight: 500;
			}
		}

		.card-stats {
			display: flex;
			align-items: center;
			gap: 14px;

			.stat-item {
				font-size: 12px;
				color: $text-dim;
				display: flex;
				align-items: center;
				gap: 3px;
				i { font-size: 12px; }
			}
		}
	}

	.card-actions {
		display: flex;
		gap: 6px;
		flex-shrink: 0;
		opacity: 0.6;
		transform: translateX(4px);
		transition: all 0.3s cubic-bezier(0.4,0,0.2,1);

		.action-btn {
			display: flex;
			align-items: center;
			gap: 4px;
			padding: 6px 14px;
			border-radius: 18px;
			font-size: 12px;
			font-weight: 500;
			cursor: pointer;
			transition: all 0.25s;
			border: 1px solid transparent;
			white-space: nowrap;

			i { font-size: 13px; }
		}

		.action-edit {
			color: #a78bfa;
			background: rgba(167,139,250,0.08);
			border-color: rgba(167,139,250,0.15);
			&:hover {
				background: rgba(167,139,250,0.18);
				border-color: rgba(167,139,250,0.35);
				transform: translateY(-1px);
				box-shadow: 0 4px 12px rgba(167,139,250,0.15);
			}
		}

		.action-view {
			color: #38bdf8;
			background: rgba(56,189,248,0.08);
			border-color: rgba(56,189,248,0.15);
			&:hover {
				background: rgba(56,189,248,0.18);
				border-color: rgba(56,189,248,0.35);
				transform: translateY(-1px);
				box-shadow: 0 4px 12px rgba(56,189,248,0.15);
			}
		}

		.action-del {
			color: #f87171;
			background: rgba(248,113,113,0.06);
			border-color: rgba(248,113,113,0.12);
			&:hover {
				background: rgba(248,113,113,0.15);
				border-color: rgba(248,113,113,0.35);
				transform: translateY(-1px);
				box-shadow: 0 4px 12px rgba(248,113,113,0.12);
			}
		}
	}
}

.status-badge {
	display: inline-flex;
	align-items: center;
	gap: 3px;
	padding: 2px 8px;
	border-radius: 10px;
	font-size: 10px;
	font-weight: 600;
	backdrop-filter: blur(8px);
	i { font-size: 10px; }
}
.status-pass {
	background: rgba(16,185,129,0.2);
	color: #34d399;
	border: 1px solid rgba(16,185,129,0.3);
}
.status-wait {
	background: rgba(251,191,36,0.2);
	color: #fbbf24;
	border: 1px solid rgba(251,191,36,0.3);
}
.status-fail {
	background: rgba(239,68,68,0.2);
	color: #f87171;
	border: 1px solid rgba(239,68,68,0.3);
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 60px 20px;
	animation: skillCardIn 0.5s ease-out;

	.empty-icon {
		margin-bottom: 16px;
		opacity: 0.6;
		animation: emptyFloat 4s ease-in-out infinite;
	}
	.empty-title {
		font-size: 16px;
		font-weight: 600;
		color: $text-sub;
		margin-bottom: 6px;
	}
	.empty-desc {
		font-size: 13px;
		color: $text-dim;
	}
}

@keyframes emptyFloat {
	0%, 100% { transform: translateY(0); }
	50% { transform: translateY(-8px); }
}

@keyframes skillCardIn {
	from { opacity: 0; transform: translateY(12px); }
	to { opacity: 1; transform: translateY(0); }
}

.pagination {
	text-align: center;
	margin-top: 16px;
}
</style>
