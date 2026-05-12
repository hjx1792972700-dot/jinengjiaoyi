<template>
	<div class="storeup-page" :class="{'dark-embedded': embedded}">
		<div class="back_box" v-if="!embedded">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-fanhui01"></span>
				<span class="text">返回</span>
			</el-button>
		</div>
		<div v-if="storeupType==1" class="section-title">我的收藏</div>
		<div v-if="storeupType==21" class="section-title">我的点赞</div>
		<div v-if="storeupType==81" class="section-title">我的评论</div>
		<div v-if="storeupType!=81" class="category-tabs">
			<div class="tab-item" :class="{active: activeCategory===''}" @click="switchCategory('')">
				<i class="el-icon-menu"></i> 全部
			</div>
			<div class="tab-item" :class="{active: activeCategory==='jinengxuqiu'}" @click="switchCategory('jinengxuqiu')">
				<i class="el-icon-goods"></i> 技能市场
			</div>
			<div class="tab-item" :class="{active: activeCategory==='forum'}" @click="switchCategory('forum')">
				<i class="el-icon-chat-dot-round"></i> 技能社区
			</div>
			<div class="tab-item" :class="{active: activeCategory==='xuexiziliao'}" @click="switchCategory('xuexiziliao')">
				<i class="el-icon-reading"></i> 学习资料
			</div>
		</div>
		<el-form v-if="storeupType!=81" :inline="true" :model="formSearch" class="list-form-pv">
			<el-form-item class="search-item">
				<el-input v-model="formSearch.name" placeholder="搜索名称..." clearable @keydown.enter="getStoreupList(1)"></el-input>
			</el-form-item>
			<div class="search-btn-item">
				<el-button class="searchBtn" type="primary" @click="getStoreupList(1)">
					<span class="icon iconfont icon-fangdajing07"></span>
					查询
				</el-button>
			</div>
		</el-form>
		<div v-if="storeupType!=81" class="storeup-card-grid">
			<div class="storeup-card-item" v-for="(item, index) in storeupList" :key="index" @click="toDetail(item)">
				<el-card class="storeup-card">
					<div class="card-img-wrap">
						<el-image v-if="item.picture && item.picture.substr(0,4)=='http'" :src="item.picture" fit="cover" class="image"></el-image>
						<el-image v-else-if="item.picture && item.picture.substr(0,4)!='http'" :src="baseUrl + item.picture" fit="cover" class="image"></el-image>
						<div class="no-image" v-else><i class="el-icon-picture"></i></div>
						<span class="source-badge" :class="'source-' + item.tablename">{{sourceLabel(item.tablename)}}</span>
					</div>
					<div class="storeup-card-body">
						<span>{{item.name}}</span>
					</div>
					<div class="storeup-card-actions">
						<el-button class="storeup-del-btn" type="danger" size="mini" @click.stop="delClick(item)">删除</el-button>
					</div>
				</el-card>
			</div>
		</div>
		<div v-else class="storeup-table-wrap">
			<el-table class="tables" :stripe='false'
				:border='true'
				:data="storeupList">
				<el-table-column :resizable='true' :sortable='false' prop="content" label="评论内容" show-overflow-tooltip>
					<template #default="scope">
						<span class="ql-snow ql-editor comment-cell-ellipsis" v-html="scope.row.content"></span>
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='false' prop="reply" label="回复内容" show-overflow-tooltip>
					<template #default="scope">
						<span class="ql-snow ql-editor comment-cell-ellipsis" v-html="scope.row.reply"></span>
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='false' prop="score" label="评分">
					<template #default="scope">
						<el-rate
							v-if="scope.row.score&&scope.row.score!=undefined"
							v-model="scope.row.score"
							class="comment-rate"
							:max='5'
							:allow-half='false'
							:low-threshold='2'
							:high-threshold='4'
							:show-text='false'
							:texts='["极差", "失望", "一般", "满意", "惊喜"]'
							text-color='#1F2D3D'
							:colors='["#F7BA2A", "#F7BA2A", "#F7BA2A"]'
							void-color='#C6D1DE'
							disabled-void-color='#EFF2F7'
							:icons="commentRateIcons"
							:void-icon="commentRateVoidIcon"
							:show-score='false'
							disabled
							>
						</el-rate>
					</template>
				</el-table-column>
				<el-table-column width="300" label="操作">
					<template #default="scope">
						<el-button class="view" type="success" size="mini"
							@click="toDetail(scope.row)">
							查看
						</el-button>
						<el-button class="del" type="danger" size="mini"
							@click="delClick(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>
	
		<el-pagination
			v-if="storeupType!=81"
			background
			id="pagination" class="pagination"
			:pager-count="7"
			:page-size="pageSize"
			:page-sizes="pageSizes"
			prev-text="<"
			next-text=">"
			:hide-on-single-page="true"
			:layout='["total","prev","pager","next","sizes","jumper"].join()'
			:total="total"
			@current-change="curChange"
			@prev-click="prevClick"
			@size-change="sizeChange"
			@next-click="nextClick"
			></el-pagination>
	
	</div>
</template>

<script>
	import config from '@/config/config'
	export default {
		props: { embedded: { type: Boolean, default: false } },
		data() {
			return {
				commentRateIcons: [StarFilled, StarFilled, StarFilled],
				commentRateVoidIcon: Star,
				layouts: '',
				baseUrl: config.baseUrl,
				formSearch: {
					name: ''
				},
				activeCategory: '',
				storeupType: 1,
				storeupList: [],
				total: 1,
				pageSize: 8,
				pageSizes: [],
				totalPage: 1
			}
		},
		created() {
			this.storeupType = localStorage.getItem('storeupType');
			this.getStoreupList(1);
		},
		methods: {
			backClick() {
				this.$router.push('/main/center')
			},
			sourceLabel(tablename) {
				const map = { jinengxuqiu: '技能市场', forum: '技能社区', xuexiziliao: '学习资料' };
				return map[tablename] || tablename;
			},
			switchCategory(cat) {
				this.activeCategory = cat;
				this.getStoreupList(1);
			},
			getStoreupList(page) {
				if(this.storeupType==81) {
					this.$http.get('comment/list', {}).then(res => {
						this.storeupList = res.data.data;
					})
					return false
				}
				let params = {page, limit: this.pageSize, type: this.storeupType, userid: Number(localStorage.getItem('frontUserid')),sort:"addtime",order:"desc"};
				let searchWhere = {};
				if (this.formSearch.name != '') searchWhere.name = '%' + this.formSearch.name + '%';
				if (this.activeCategory) searchWhere.tablename = this.activeCategory;
				this.$http.get('storeup/list', {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.storeupList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [8, 16, 24];
						}
					}
				});
			},
			curChange(page) {
				this.getStoreupList(page);
			},
			prevClick(page) {
				this.getStoreupList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getStoreupList(1);
			},
			nextClick(page) {
				this.getStoreupList(page);
			},
			toDetail(item) {
				let params = {
					id: item.refid,
					storeupType: 1,
				}
				if(this.storeupType == 81) {
					params.discussId = item.id
				}
				this.$router.push({path: `/main/${item.tablename}Detail`, query: params});
			},
			async delClick(row){
				await this.$confirm(`是否删除此记录？`) .then(async _ => {
					if(this.storeupType==81) {
						await this.$http.post(`discuss${row.tablename}/delete`, [row.id]).then(async res => {
							if (res.data.code == 0) {
								await this.$http.get(`${row.tablename}/info/${row.refid}`).then(async rs=>{
									rs.data.data.discussnum--
									await this.$http.post(`${row.tablename}/update`,rs.data.data).then(rs2=>{})
								})
								this.$message({
									type: 'success',
									message: '删除成功!',
									duration: 1500,
									onClose: () => {
										this.getStoreupList(1);
									}
								});
							}
						});
						return false
					}
					this.$http.post('storeup/delete', [row.id]).then(async res => {
						if (res.data.code == 0) {
							if(this.storeupType==1) {
								await this.$http.get(`${row.tablename}/info/${row.refid}`).then(async rs=>{
									rs.data.data.storeupnum--
									await this.$http.post(`${row.tablename}/update`,rs.data.data).then(rs2=>{})
								})
							}
							if(this.storeupType==21) {
								await this.$http.get(`${row.tablename}/info/${row.refid}`).then(async rs=>{
									rs.data.data.thumbsupnum--
									await this.$http.post(`${row.tablename}/update`,rs.data.data).then(rs2=>{})
								})
							}
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									this.getStoreupList(1);
								}
							});
						}
					});
				}).catch(_ => {});
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$purple: #7c3aed;
$text-dark: #e2e8f0;
$text-gray: #94a3b8;
$border: rgba(0,212,255,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 12px;
$shadow: 0 4px 20px rgba(0,0,0,0.3);

.storeup-page {
	position: relative;
	display: flex;
	flex-wrap: wrap;
	align-content: flex-start;
	align-items: flex-start;
	width: 100%;
	margin: 0 auto;
	padding: 0 10%;
	background: none;
}

.back_box {
	position: absolute;
	top: 0;
	right: 10%;
	z-index: 2;
}

.backBtn {
	border-radius: 20px;
	padding: 8px 16px;
	border: 1px solid $border;
	background: $white;
	color: $text-dark;
	box-shadow: $shadow;

	&:hover {
		border-color: $primary;
		color: $primary;
		background: $white;
	}
}

.section-title {
	width: 100%;
	margin: 10px 0 16px;
	padding: 0;
	padding-top: 44px;
	font-size: 22px;
	font-weight: 700;
	color: $text-dark;
	text-align: left;
	line-height: 1.3;
}

.category-tabs {
	width: 100%;
	display: flex;
	gap: 8px;
	margin-bottom: 12px;
	flex-wrap: wrap;

	.tab-item {
		padding: 8px 18px;
		border-radius: 20px;
		font-size: 13px;
		font-weight: 500;
		color: $text-gray;
		background: $white;
		border: 1px solid $border;
		cursor: pointer;
		transition: all 0.25s;
		display: flex;
		align-items: center;
		gap: 4px;

		.el-icon { font-size: 14px; vertical-align: middle; }

		&:hover {
			color: $primary;
			border-color: rgba(14,165,233,0.3);
			background: rgba(14,165,233,0.04);
		}

		&.active {
			color: #fff;
			background: linear-gradient(135deg, $primary, $purple);
			border-color: transparent;
			box-shadow: 0 2px 10px rgba(14,165,233,0.25);
		}
	}
}

.card-img-wrap {
	position: relative;
	overflow: hidden;
	height: 180px;

	.no-image {
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
		justify-content: center;
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(124,58,237,0.08));
		.el-icon { font-size: 40px; color: $text-gray; vertical-align: middle; }
	}
}

.source-badge {
	position: absolute;
	top: 10px;
	right: 10px;
	font-size: 11px;
	font-weight: 600;
	padding: 3px 10px;
	border-radius: 12px;
	color: #fff;
	letter-spacing: 0.5px;
	box-shadow: 0 2px 6px rgba(0,0,0,0.2);

	&.source-jinengxuqiu {
		background: linear-gradient(135deg, #0ea5e9, #0284c7);
	}
	&.source-forum {
		background: linear-gradient(135deg, #8b5cf6, #7c3aed);
	}
	&.source-xuexiziliao {
		background: linear-gradient(135deg, #f59e0b, #d97706);
	}
}

.list-form-pv {
	display: flex;
	flex-wrap: wrap;
	justify-content: center;
	align-items: center;
	width: 100%;
	padding: 10px 0;
	margin: 0 0 8px;
	background: none;

	.search-item {
		margin: 0 10px 10px;

		::v-deep .el-form-item__content {
			display: flex;
			align-items: center;
		}

		.el-input {
			width: 100%;
		}

		::v-deep .el-input__inner {
			height: 42px;
			line-height: 42px;
			padding: 0 12px;
			font-size: 15px;
			color: $text-dark;
			border: 1px solid $border;
			border-radius: 8px;
			background: $white;
			width: 500px;
			max-width: 100%;
			outline: none;
			transition: border-color 0.2s;

			&:focus {
				border-color: $primary;
			}
		}
	}

	.search-btn-item {
		display: flex;
		margin-bottom: 10px;

		.searchBtn {
			height: 42px;
			line-height: 42px;
			padding: 0 24px;
			font-size: 16px;
			border: none;
			border-radius: 24px;
			background: linear-gradient(135deg, $primary, $purple);
			color: $white;
			cursor: pointer;
			box-shadow: 0 2px 10px rgba(14,165,233,0.2);
			transition: all 0.3s;

			.icon {
				margin: 0 5px 0 0;
				color: $white;
			}

			&:hover,
			&:focus {
				box-shadow: 0 4px 16px rgba(14,165,233,0.3);
				transform: translateY(-1px);
			}
		}
	}
}

.storeup-card-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 20px 16px;
	width: 100%;
	margin-bottom: 8px;

	@media (max-width: 1200px) {
		grid-template-columns: repeat(3, 1fr);
	}
	@media (max-width: 900px) {
		grid-template-columns: repeat(2, 1fr);
	}
	@media (max-width: 520px) {
		grid-template-columns: 1fr;
	}
}

.storeup-card-item {
	min-width: 0;
	cursor: pointer;
}

.storeup-card {
	border-radius: $radius;
	border: 1px solid $border;
	overflow: hidden;
	transition: box-shadow 0.2s, transform 0.2s;
	box-shadow: $shadow;

	&:hover {
		box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
	}

	::v-deep .el-card__body {
		padding: 0;
	}
}

.image {
	display: block;
	width: 100%;
	height: 180px;
	::v-deep img { object-fit: cover; }
}

.storeup-card-body {
	padding: 14px;
	color: $text-dark;
}

.storeup-card-actions {
	padding: 0 5px 12px;
}

.storeup-del-btn {
	border-radius: 15px;
	padding: 6px 12px;
	font-size: 12px;
}

.storeup-table-wrap {
	width: 100%;
}

.tables {
	width: 100%;
	padding: 0;
	background: $white;
	border-radius: $radius;
	overflow: hidden;
	border: 1px solid $border;
	box-shadow: $shadow;
}

.comment-cell-ellipsis {
	display: block;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.comment-rate {
	display: inline-block;
	line-height: 1;
	height: 20px;
}

::v-deep .el-table {
	background: $white;
	border: none;
}

::v-deep .el-table th {
	background: $bg !important;
	color: $text-dark;
	font-weight: 600;
	border-color: $border;
}

::v-deep .el-table td {
	border-color: $border;
	color: $text-gray;
}

::v-deep .el-table--border th,
::v-deep .el-table--border td {
	border-right: 1px solid $border;
}

::v-deep .el-table--border {
	border: 1px solid $border;
	border-radius: $radius;
}

::v-deep .el-table--border::after,
::v-deep .el-table--group::after,
::v-deep .el-table::before {
	background: $border;
}

::v-deep .el-table__body tr:hover > td {
	background: rgba(14, 165, 233, 0.06) !important;
}

.view {
	border-radius: 8px;
}

.del {
	border-radius: 15px;
	font-size: 12px;
	padding: 6px 12px;
}

.pagination {
	width: 100%;
	margin: 20px auto;
	padding: 0;
	font-size: 16px;
	font-weight: 500;
	color: $text-gray;
	text-align: center;
	white-space: nowrap;
	order: 50;
}

::v-deep .pagination.el-pagination {
	text-align: center;
	justify-content: center;
}

::v-deep .pagination .el-pager li:not(.disabled).active,
::v-deep .pagination .el-pager li:not(.disabled).active:hover {
	background: linear-gradient(135deg, $primary, $purple) !important;
	color: $white;
}

::v-deep .pagination.is-background .el-pager li:not(.disabled):hover {
	color: $primary;
}

::v-deep .pagination.is-background .btn-prev,
::v-deep .pagination.is-background .btn-next,
::v-deep .pagination.is-background .el-pager li {
	background: $white;
	border: 1px solid $border;
}

.dark-embedded {
	&.storeup-page {
		padding: 0 !important;
	}
	.section-title {
		color: #e2e8f0 !important;
	}
	.category-tabs .tab-item {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.08) !important;
		color: #94a3b8 !important;
		&:hover {
			color: #00d4ff !important;
			border-color: rgba(0,212,255,0.2) !important;
		}
		&.active {
			background: linear-gradient(135deg, #00d4ff, #7c3aed) !important;
			border-color: transparent !important;
			color: #fff !important;
		}
	}
	.list-form-pv {
		background: none !important;
		.search-item ::v-deep .el-input__inner {
			background: rgba(255,255,255,0.04) !important;
			border: 1px solid rgba(255,255,255,0.08) !important;
			color: #e2e8f0 !important;
			&::placeholder { color: rgba(255,255,255,0.25) !important; }
		}
		.searchBtn {
			background: linear-gradient(135deg, #00d4ff, #7c3aed) !important;
		}
	}
	.storeup-card {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		box-shadow: none !important;
		&:hover {
			border-color: rgba(0,212,255,0.2) !important;
			box-shadow: 0 8px 24px rgba(0,212,255,0.08) !important;
		}
	}
	::v-deep .el-card {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		box-shadow: none !important;
	}
	.storeup-card-body {
		color: #e2e8f0 !important;
	}
	::v-deep .el-table {
		background: transparent !important;
		tr { background: transparent !important; }
		th { background: rgba(0,212,255,0.06) !important; color: #e2e8f0 !important; border-bottom: 1px solid rgba(255,255,255,0.08) !important; }
		td { border-bottom: 1px solid rgba(255,255,255,0.04) !important; color: #94a3b8 !important; }
		&::before { background: rgba(255,255,255,0.06) !important; }
	}
	::v-deep .el-table--border {
		border: 1px solid rgba(255,255,255,0.06) !important;
		&::after { background: rgba(255,255,255,0.06) !important; }
		td, th { border-right: 1px solid rgba(255,255,255,0.04) !important; }
	}
	::v-deep .el-table__body tr:hover > td {
		background: rgba(0,212,255,0.04) !important;
	}
	::v-deep .el-button--primary {
		background: linear-gradient(135deg, #00d4ff, #7c3aed) !important;
		border: none !important;
	}
	::v-deep .el-button--danger {
		background: rgba(239,68,68,0.15) !important;
		border: 1px solid rgba(239,68,68,0.3) !important;
		color: #ef4444 !important;
	}
	.pagination ::v-deep .el-pager li {
		background: rgba(255,255,255,0.04) !important;
		color: #94a3b8 !important;
		border: none !important;
		&.active {
			background: linear-gradient(135deg, #00d4ff, #7c3aed) !important;
			color: #fff !important;
		}
	}
}
</style>
