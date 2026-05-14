<template>
	<div class="my-skill-wrap" :class="{'dark-embedded': embedded}">
		<!-- 审核状态筛选 -->
		<div class="top-bar">
			<div class="stat-chips">
				<div class="chip" :class="{active: filterStatus==='all'}" @click="filterStatus='all'; page=1; getList()">
					<el-icon><Grid /></el-icon>
					<span class="chip-label">全部</span>
					<span class="chip-num">{{stats.total}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='待审核'}" @click="filterStatus='待审核'; page=1; getList()">
					<el-icon><Clock /></el-icon>
					<span class="chip-label">待审核</span>
					<span class="chip-num">{{stats.pending}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='是'}" @click="filterStatus='是'; page=1; getList()">
					<el-icon><CircleCheck /></el-icon>
					<span class="chip-label">已通过</span>
					<span class="chip-num">{{stats.approved}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='否'}" @click="filterStatus='否'; page=1; getList()">
					<el-icon><CircleClose /></el-icon>
					<span class="chip-label">未通过</span>
					<span class="chip-num">{{stats.rejected}}</span>
				</div>
			</div>
		</div>

		<el-table :stripe="false" :border="true" :data="tableData" class="skill-table" v-loading="loading">
			<el-table-column label="封面" prop="fengmian" width="80">
				<template #default="scope">
					<img v-if="scope.row.fengmian" :src="getImgUrl(scope.row.fengmian)" class="cover-img" />
					<span v-else class="no-img">--</span>
				</template>
			</el-table-column>
			<el-table-column label="技能名称" prop="xuqiubiaoti" show-overflow-tooltip></el-table-column>
			<el-table-column label="技能分类" prop="jinengfenlei" width="110"></el-table-column>
			<el-table-column label="费用" prop="xuqiufeiyong" width="90">
				<template #default="scope">
					<span v-if="scope.row.xuqiufeiyong" class="price">¥{{scope.row.xuqiufeiyong}}</span>
					<span v-else class="price-free">面议</span>
				</template>
			</el-table-column>
			<el-table-column label="发布时间" prop="addtime" width="110">
				<template #default="scope">
					{{scope.row.addtime ? scope.row.addtime.split(' ')[0] : '--'}}
				</template>
			</el-table-column>
			<el-table-column label="审核状态" prop="sfsh" width="90">
				<template #default="scope">
					<span :class="scope.row.sfsh==='是'?'tag-pass':(scope.row.sfsh==='否'?'tag-fail':'tag-wait')">{{scope.row.sfsh==='是'?'已通过':(scope.row.sfsh==='否'?'未通过':'待审核')}}</span>
				</template>
			</el-table-column>
			<el-table-column label="热度" width="120">
				<template #default="scope">
					<span class="stat"><el-icon><View /></el-icon>{{scope.row.clicknum||0}}</span>
					<span class="stat"><el-icon><Star /></el-icon>{{scope.row.storeupnum||0}}</span>
				</template>
			</el-table-column>
		<el-table-column label="操作" width="160" fixed="right">
			<template #default="scope">
				<el-button size="small" link type="primary" class="edit" @click="toEdit(scope.row)">编辑</el-button>
				<el-button size="small" link type="primary" class="view" @click="toDetail(scope.row)">查看</el-button>
				<el-button size="small" link type="danger" class="del" @click="delSkill(scope.row)">删除</el-button>
			</template>
		</el-table-column>
		</el-table>
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
			this.$router.push({ path: '/main/skillAdd', query: { type: 'edit', id: row.id } });
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
$glass: rgba(255,255,255,0.03);
$glass-border: rgba(255,255,255,0.06);
$text1: #f1f5f9;
$text2: #94a3b8;
$text3: #64748b;

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
	.el-icon { font-size: 14px; color: $text3; transition: color 0.25s; vertical-align: middle; }
	.chip-label { font-size: 13px; color: $text2; transition: color 0.25s; }
	.chip-num {
		font-size: 12px; font-weight: 700; color: $text3;
		background: rgba(255,255,255,0.04); padding: 1px 8px; border-radius: 10px;
		min-width: 20px; text-align: center; transition: all 0.25s;
	}
	&:hover { border-color: rgba($cyan, 0.25); background: rgba($cyan, 0.04);
		.el-icon, .chip-label { color: $cyan; }
	}
	&.active {
		background: rgba($cyan, 0.08); border-color: rgba($cyan, 0.3);
		.el-icon { color: $cyan; }
		.chip-label { color: $text1; font-weight: 600; }
		.chip-num { background: rgba($cyan, 0.15); color: $cyan; }
	}
}

.my-skill-wrap {
	.cover-img {
		width: 50px;
		height: 50px;
		border-radius: 6px;
		object-fit: cover;
	}
	.no-img { color: #94a3b8; }
	.price { color: #0ea5e9; font-weight: 600; }
	.price-free { color: #94a3b8; font-size: 12px; }
	.tag-pass {
		display: inline-block;
		padding: 2px 8px;
		border-radius: 10px;
		font-size: 12px;
		background: rgba(16, 185, 129, 0.1);
		color: #10b981;
	}
	.tag-wait {
		display: inline-block;
		padding: 2px 8px;
		border-radius: 10px;
		font-size: 12px;
		background: rgba(251, 191, 36, 0.1);
		color: #f59e0b;
	}
	.tag-fail {
		display: inline-block;
		padding: 2px 8px;
		border-radius: 10px;
		font-size: 12px;
		background: rgba(239, 68, 68, 0.1);
		color: #f87171;
	}
	.stat {
		font-size: 12px;
		color: #94a3b8;
		margin-right: 8px;
		.el-icon { margin-right: 2px; vertical-align: middle; }
	}
	.edit { color: #a78bfa !important; &.is-disabled { color: #475569 !important; cursor: not-allowed; } }
	.view { color: #38bdf8 !important; }
	.del { color: #f87171 !important; &.is-disabled { color: #475569 !important; cursor: not-allowed; } }
	.pagination {
		text-align: center;
		margin-top: 12px;
	}
}

.dark-embedded {
	.el-table, .el-table__header-wrapper, .el-table__body-wrapper {
		background: transparent !important;
	}
	.el-table::before, .el-table::after { background: transparent !important; }
	.el-table th, .el-table th.el-table__cell {
		background: rgba(15, 23, 42, 0.4) !important;
		color: #94a3b8 !important;
		border-color: rgba(71, 85, 105, 0.2) !important;
	}
	.el-table td, .el-table td.el-table__cell {
		background: transparent !important;
		color: #cbd5e1 !important;
		border-color: rgba(71, 85, 105, 0.15) !important;
	}
	.el-table tr { background: transparent !important; }
	.el-table__body tr:hover > td { background: rgba(14, 165, 233, 0.06) !important; }
	.el-table__empty-block { background: transparent !important; }
	.el-table__empty-text { color: #64748b !important; }
}
</style>
