<template>
	<div class="my-skill-wrap" :class="{'dark-embedded': embedded}">
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
					<span :class="scope.row.sfsh==='是'?'tag-pass':'tag-wait'">{{scope.row.sfsh==='是'?'已通过':'待审核'}}</span>
				</template>
			</el-table-column>
			<el-table-column label="热度" width="120">
				<template #default="scope">
					<span class="stat"><i class="el-icon-view"></i>{{scope.row.clicknum||0}}</span>
					<span class="stat"><i class="el-icon-star-off"></i>{{scope.row.storeupnum||0}}</span>
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
			this.$http.get('jinengxuqiu/page', { params }).then(res => {
				this.loading = false;
				if (res.data.code == 0) {
					this.tableData = res.data.data.list;
					this.total = Number(res.data.data.total);
				}
			}).catch(() => { this.loading = false; });
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
