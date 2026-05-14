

<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search" v-if="!forumChild">
				<div class="search-field">
					<span class="field-label">帖子标题</span>
					<el-input v-model="searchForm.title" placeholder="帖子标题" @keydown.enter="search()" clearable size="small"></el-input>
				</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" icon="el-icon-search" @click="search()">查询</el-button>
					<el-button class="btn-plain" size="small" icon="el-icon-refresh" @click="searchForm.title='';search()">重置</el-button>
				</div>
			</div>
			<div class="admin-actions" v-if="forumChild">
				<div class="action-left">
					<el-button class="btn-primary" size="small" @click="search()">返回</el-button>
				</div>
			</div>

			<div class="admin-actions" v-if="!forumChild">
				<div class="action-left">
					<el-button class="btn-primary" size="small" icon="el-icon-plus" v-if="isAuth('forum','新增')" @click="addOrUpdateHandler()">新增</el-button>
				</div>
			</div>

			<div class="admin-table-wrap">
				<el-table
					v-if="isAuth('forum','查看')"
					:data="dataList"
					v-loading="dataListLoading"
					style="width:100%">
				<el-table-column type="index" :index="indexMethod" label="序号" width="55" align="center"></el-table-column>
			<el-table-column v-if="!forumChild" :resizable='true' prop="cover" width="80" label="封面">
					<template #default="scope">
						<div v-if="scope.row.cover">
							<img v-if="scope.row.cover.substring(0,4)=='http'&&scope.row.cover.split(',w').length>1" :src="scope.row.cover" width="56" height="56" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView(scope.row.cover)">
							<img v-else-if="scope.row.cover.substring(0,4)=='http'" :src="scope.row.cover.split(',')[0]" width="56" height="56" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView(scope.row.cover.split(',')[0])">
							<img v-else :src="$base.url+scope.row.cover.split(',')[0]" width="56" height="56" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView($base.url+scope.row.cover.split(',')[0])">
						</div>
						<div v-else style="color:#64748b;font-size:12px;">无图片</div>
					</template>
				</el-table-column>
				<el-table-column :resizable='true'											v-if="!forumChild"
					prop="title"
					label="帖子标题" min-width="140" show-overflow-tooltip>
					<template #default="scope">
						{{scope.row.title}}
					</template>
				</el-table-column>
				<el-table-column :resizable='true' v-if="forumChild" prop="content" label="帖子内容" min-width="200">
					<template #default="scope">
						<span class="ql-snow ql-editor" v-html="scope.row.content"></span>
					</template>
				</el-table-column>
				<el-table-column :resizable='true'											v-if="!forumChild"
					prop="typename"
					label="分类" min-width="90">
					<template #default="scope">
						{{scope.row.typename}}
					</template>
				</el-table-column>
				<el-table-column :resizable='true'											prop="username"
					label="用户名" min-width="100">
					<template #default="scope">
						{{scope.row.username}}
					</template>
				</el-table-column>
			<el-table-column :resizable='true'											v-if="!forumChild"
				prop="isdone"
				label="状态" min-width="80">
				<template #default="scope">
					{{scope.row.isdone}}
				</template>
			</el-table-column>
			<el-table-column :resizable='true' v-if="!forumChild" prop="isanon" label="匿名" min-width="80" align="center">
				<template #default="scope">
					{{ scope.row.isanon==1?'是':'否' }}
				</template>
			</el-table-column>
					
				<el-table-column width="130" label="操作" align="center">
					<template #default="scope">
						<el-button size="mini" v-if="!forumChild" type="success" plain @click="viewDetail(scope.row)">查看</el-button>
						<el-button size="mini" v-if="!forumChild" type="danger" plain @click="deleteHandler(scope.row.id)">删除</el-button>
					</template>
				</el-table-column>
				</el-table>
			</div>
			<div class="admin-pagination">
				<el-pagination
					@size-change="sizeChangeHandle"
					@current-change="currentChangeHandle"
					:current-page="pageIndex"
					background

					:page-size="pageSize"
					:layout="layouts.join()"
					:total="totalPage"
					prev-text="< "
					next-text="> "
					:hide-on-single-page="false"
				></el-pagination>
			</div>
		</template>
		<!-- 帖子详情页 -->
		<template v-if="detailFlag">
			<div class="forum-detail-page">
				<div class="detail-top-bar">
					<el-button class="back-btn" size="small" @click="closeDetail">
						<i class="el-icon-arrow-left"></i> 返回列表
					</el-button>
					<div class="top-bar-right">
						<span class="type-badge" v-if="detailData.typename">{{detailData.typename}}</span>
						<span class="anon-badge" v-if="detailData.isanon==1">匿名发布</span>
					</div>
				</div>

				<div class="detail-hero">
					<div class="hero-cover" v-if="detailData.cover">
						<img v-if="detailData.cover.substring(0,4)=='http'" :src="detailData.cover.split(',')[0]" />
						<img v-else :src="$base.url + detailData.cover.split(',')[0]" />
					</div>
					<div class="hero-info">
						<h1 class="hero-title">{{detailData.title}}</h1>
						<div class="hero-meta">
							<div class="author-row">
								<div class="author-avatar">{{(detailData.isanon==1 ? '匿' : (detailData.username||'?').substring(0,1))}}</div>
								<div class="author-info">
									<div class="author-name">{{detailData.isanon==1 ? '匿名用户' : detailData.username}}</div>
									<div class="author-time">{{detailData.addtime || '--'}}</div>
								</div>
							</div>
						</div>
						<div class="hero-stats">
							<div class="stat-item stat-like"><i class="el-icon-thumb"></i><span>{{detailData.thumbsupnum||0}}</span><label>点赞</label></div>
							<div class="stat-item stat-dislike"><i class="el-icon-bottom"></i><span>{{detailData.crazilynum||0}}</span><label>踩</label></div>
							<div class="stat-item stat-star"><i class="el-icon-star-on"></i><span>{{detailData.storeupnum||0}}</span><label>收藏</label></div>
						</div>
					</div>
				</div>

				<div class="detail-content-card" v-if="detailData.content">
					<div class="content-header">
						<div class="content-icon"><i class="el-icon-document"></i></div>
						<span>帖子正文</span>
					</div>
					<div class="content-body ql-snow ql-editor" v-html="detailData.content"></div>
				</div>
			</div>
		</template>
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>


		
		<el-dialog :title="batchIds.length > 1 ? '批量审核' : '审核'" :visible.sync="sfshBatchVisiable" width="480px" :close-on-click-modal="false">
			<el-form ref="shBatchForm" :model="shBatchForm" :rules="shRules" label-width="80px">
				<el-form-item label="审核状态" prop="sfsh">
					<el-select v-model="shBatchForm.sfsh" placeholder="审核状态">
						<el-option label="通过" value="是"></el-option>
						<el-option label="不通过" value="否"></el-option>
						<el-option label="待审核" value="待审核"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="内容" prop="shhf">
					<el-input type="textarea" :rows="8" v-model="shBatchForm.shhf"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<span class="dialog-footer">
				<el-button class="btn-plain" size="small" @click="sfshBatchVisiable=false">取消</el-button>
				<el-button class="btn-primary" size="small" @click="shBatchHandler">确定</el-button>
				</span>
			</template>
		</el-dialog>



		<el-dialog title="预览图" :visible.sync="previewVisible" width="480px" :close-on-click-modal="false">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update.vue";
	export default {
		data() {
			return {
				indexQueryCondition: '',
				typenameOptions: [],
				searchForm: {
					key: ""
				},
				form:{},
				sfshOptions: [],
				forumChild: false,
				forumChild2: false,
				parentid: 0,
				dataList: [],
				pageIndex: 1,
				pageSize: 7,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
				detailFlag: false,
				detailData: {},
				sfshVisiable: false,
				shForm: {},
				sfshBatchVisiable: false,
				shBatchForm: {
					sfsh:'',
					shhf:''
				},
				shRules: {
					sfsh:[{ required: true, message: '审核状态不能为空', trigger: 'blur' },],
				},
				batchIds:[],
				shList: [],
				addOrUpdateFlag:false,
				layouts: ["total","prev","pager","next","jumper"],
				previewImg: '',
				previewVisible: false,
			};
		},
		created() {
			if(this.statType) {
				return false
			}
			this.init();
			this.getDataList();
		},
		mounted() {
		},
		watch: {
		},
		computed: {
			tablename(){
				return this.$storage.get('sessionTable')
			},
			role(){
				return this.$storage.get('role')
			},
		},
		components: {
			AddOrUpdate},
		methods: {
			htmlfilter(val) {
				return String(val == null ? '' : val).replace(/<[^>]*>/g, '').replace(/undefined/g, '');
			},
			queryChange(arr){
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
				
			},
			init () {
				this.sfshOptions = "是,否,待审核".split(',');
				this.$http({
					url: `option/forumtype/typename`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.typenameOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
			},
			indexMethod(index) {
				return (this.pageIndex - 1) * this.pageSize + index + 1;
			},
			search(id,flag) {
				if(id!='' && id!=undefined) {
					if(this.forumChild) {
						this.forumChild2 = true
					}else {
						this.parentid = id
					}
					this.forumChild = true;
					
				} else {
					if(this.forumChild2) {
						this.forumChild2 = false
					}else {
						this.forumChild = false;
						this.parentid = 0
					}
					
				}
				this.pageIndex = 1;
				this.getDataList(id,flag);
			},

			forumistopChange(e,row){
				if(e==1){
					row.toptime = this.getCurDateTime()
				}else {
					row.toptime = row.addtime
				}
				
				this.$http({
					url:'forum/update',
					method:'post',
					data:row
				}).then(() => {})
			},
			getDataList(id,flag) {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					parentid : 0,
					sort: 'id',
				}
				if(this.searchForm.title!='' && this.searchForm.title!=undefined){
					params['title'] = '%' + this.searchForm.title + '%'
				}
				if(this.searchForm.typename!='' && this.searchForm.typename!=undefined){
					params['typename'] = this.searchForm.typename
				}
				if(this.searchForm.sfsh!='' && this.searchForm.sfsh!=undefined){
					params['sfsh'] = this.searchForm.sfsh
				}
				if(id!='' && id!=undefined){
					params['parentid'] = id;
				}else if(this.parentid) {
					params['parentid'] = this.parentid;
				}
				this.$http({
					url: "forum/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.dataList = data.data.list;
						this.totalPage = data.data.total;
					} else {
						this.dataList = [];
						this.totalPage = 0;
					}
					this.dataListLoading = false;
				});
			},
			// 每页数
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 多选
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			// 添加/修改
			addOrUpdateHandler(id,type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				if(type!='info'&&type!='msg'){
					type = 'else';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id,type );
				});
			},
			//批量审核窗口
			shBatchDialog(){
				for(let x in this.dataListSelections){
					if(this.dataListSelections[x].sfsh&&this.dataListSelections[x].sfsh!='待审核'){
						this.$message.error('存在已审核数据，不能继续操作');
						this.batchIds = []
						return false
					}
					this.batchIds.push(this.dataListSelections[x].id)
				}
				this.shList = this.dataListSelections
				this.sfshBatchVisiable = true
			},
			//批量审核
			shBatchHandler(){
				this.$refs["shBatchForm"].validate(valid => {
					if(valid){
						this.$confirm(`是否${this.batchIds.length>1?'一键审核':'审核'}选中数据?`, "提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning"
						}).then(async() => {
							this.$http({
								url: "forum/shBatch?sfsh="+this.shBatchForm.sfsh+"&shhf="+this.shBatchForm.shhf,
								method: "post",
								data: this.batchIds
							}).then(async ({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: "操作成功",
										type: "success",
										duration: 1500,
										onClose: () => {
											this.getDataList();
											this.sfshBatchVisiable = false
											this.batchIds = []
										}
									});
								} else {
									this.$message.error(data.msg);
								}
							});
						});
					}
				})
			},
			viewDetail(row) {
				this.$http({ url: `forum/info/${row.id}`, method: 'get' }).then(({ data }) => {
					if (data && data.code === 0) {
						this.detailData = data.data;
						this.showFlag = false;
						this.detailFlag = true;
					}
				});
			},
			closeDetail() {
				this.detailFlag = false;
				this.showFlag = true;
			},
			// 删除
			async deleteHandler(id ) {
				var ids = id? [Number(id)]: this.dataListSelections.map(item => {
					return Number(item.id);
				});
				await this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(async () => {
					await this.$http({
						url: "forum/delete",
						method: "post",
						data: ids
					}).then(async ({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
			
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},


		}

	};
</script>
<style lang="scss" scoped>
.audit-badge {
	display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; font-weight: 600;
}
.audit-pass { background: rgba(34,197,94,0.15); color: #4ade80; border: 1px solid rgba(34,197,94,0.3); }
.audit-pending { background: rgba(245,158,11,0.15); color: #fbbf24; border: 1px solid rgba(245,158,11,0.3); }
.audit-fail { background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3); }

.chat-content {
	padding-bottom: 20px; width: 100%; margin-bottom: 10px;
	max-height: 300px; height: 300px; overflow-y: auto;
	border: 1px solid rgba(255,255,255,0.06); background: #1e293b; border-radius: 6px;
	.addtime { width: 100%; text-align: center; font-size: 12px; color: #64748b; }
	.left-content { float: left; margin-bottom: 10px; padding: 10px; max-width: 80%; display: flex; align-items: center; }
	.right-content { float: right; margin-bottom: 10px; padding: 10px; max-width: 80%; display: flex; align-items: center; }
}
.clear-float { clear: both; }
.noList { color: #64748b; width: 100%; text-align: center; padding: 60px 0; }

$primary: #38bdf8;
$accent: #6d5cfc;
$green: #34d399;
$border: rgba(14,165,233,0.15);
$card-bg: #1e293b;
$bg: #0f172a;

.forum-detail-page {
	display: flex; flex-direction: column; gap: 16px;
}
.detail-top-bar {
	display: flex; justify-content: space-between; align-items: center;
	.back-btn { border-radius: 20px; border: 1px solid $border; color: #cbd5e1; font-size: 12px; padding: 6px 16px; background: transparent; transition: all .2s; &:hover { color: $primary; border-color: $primary; background: rgba(56,189,248,0.05); } }
	.top-bar-right { display: flex; gap: 8px; align-items: center; }
	.type-badge { padding: 4px 14px; border-radius: 14px; font-size: 12px; font-weight: 600; background: linear-gradient(135deg, rgba(0,180,216,0.12), rgba(109,92,252,0.1)); color: #7dd3fc; border: 1px solid rgba(0,180,216,0.25); }
	.anon-badge { padding: 4px 12px; border-radius: 14px; font-size: 11px; font-weight: 600; background: rgba(148,163,184,0.1); color: #94a3b8; border: 1px solid rgba(148,163,184,0.2); }
}
.detail-hero {
	display: flex; gap: 20px; background: $card-bg; border-radius: 14px; border: 1px solid $border; overflow: hidden;
	.hero-cover {
		width: 280px; min-height: 200px; flex-shrink: 0; overflow: hidden; position: relative;
		img { width: 100%; height: 100%; object-fit: cover; display: block; }
		&::after { content: ''; position: absolute; inset: 0; background: linear-gradient(90deg, transparent 70%, rgba(30,41,59,0.6)); }
	}
	.hero-info { flex: 1; padding: 24px 24px 24px 0; display: flex; flex-direction: column; justify-content: center; }
}
.hero-title { font-size: 22px; font-weight: 800; color: #f1f5f9; margin: 0 0 16px 0; line-height: 1.4; }
.hero-meta {
	margin-bottom: 20px;
	.author-row { display: flex; align-items: center; gap: 12px; }
	.author-avatar { width: 40px; height: 40px; border-radius: 50%; background: linear-gradient(135deg, $primary, $accent); display: flex; align-items: center; justify-content: center; font-size: 16px; font-weight: 700; color: #fff; flex-shrink: 0; }
	.author-info { display: flex; flex-direction: column; }
	.author-name { font-size: 14px; font-weight: 600; color: #e2e8f0; }
	.author-time { font-size: 12px; color: #64748b; margin-top: 2px; }
}
.hero-stats {
	display: flex; gap: 20px;
	.stat-item { display: flex; align-items: center; gap: 6px; padding: 8px 16px; border-radius: 10px; background: $bg; border: 1px solid $border;
		i { font-size: 16px; } span { font-size: 16px; font-weight: 700; color: #f1f5f9; } label { font-size: 11px; color: #64748b; }
		&.stat-like { i { color: $primary; } }
		&.stat-dislike { i { color: #f87171; } }
		&.stat-star { i { color: #fbbf24; } }
	}
}
.detail-content-card {
	background: $card-bg; border-radius: 14px; border: 1px solid $border; overflow: hidden;
	.content-header { display: flex; align-items: center; gap: 10px; padding: 16px 24px; border-bottom: 1px solid rgba(255,255,255,0.04);
		.content-icon { width: 32px; height: 32px; border-radius: 8px; background: linear-gradient(135deg, rgba(56,189,248,0.1), rgba(109,92,252,0.08)); display: flex; align-items: center; justify-content: center; i { font-size: 16px; color: $primary; } }
		span { font-size: 15px; font-weight: 700; color: #e2e8f0; }
	}
	.content-body { padding: 24px; font-size: 14px; color: #cbd5e1; line-height: 2; min-height: 100px;
		img { max-width: 100%; border-radius: 8px; margin: 10px 0; }
		p { margin: 0 0 12px 0; }
	}
}
</style>
