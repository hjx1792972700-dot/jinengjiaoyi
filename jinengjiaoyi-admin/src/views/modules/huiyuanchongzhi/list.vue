  
<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search">
				<div class="search-field">
					<span class="field-label">用户</span>
					<el-input v-model="searchForm.yonghuxingming" placeholder="用户姓名" @keydown.enter="search()" clearable size="small"></el-input>
				</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" icon="el-icon-search" @click="search()">查询</el-button>
					<el-button class="btn-plain" size="small" icon="el-icon-refresh" @click="searchForm={ key: '' }; search()">重置</el-button>
				</div>
			</div>
			<div class="admin-table-wrap">
				<el-table class="tables"
					:stripe='false'
					:border='false'
					v-if="isAuth('huiyuanchongzhi','查看')"
					:data="dataList"
					v-loading="dataListLoading">
					<el-table-column :resizable='true' :sortable='true' label="序号" type="index" width="60" />
				<el-table-column :resizable='true' :sortable='true'
											prop="yonghuxingming"
					label="用户" min-width="100">
					<template #default="scope">
						{{scope.row.yonghuxingming}}
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='true'
											prop="taocan"
					label="套餐" min-width="120">
					<template #default="scope">
						{{scope.row.taocan || '-'}}
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='true'
											prop="chongzhijine"
					label="充值金额" min-width="100">
					<template #default="scope">
						<span style="color:#e74c3c;font-weight:bold;">¥{{scope.row.chongzhijine}}</span>
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='true'
											prop="zhifufangshi"
					label="支付方式" min-width="100">
					<template #default="scope">
						{{scope.row.zhifufangshi || '-'}}
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='true'
											prop="zhuangtai"
					label="状态" min-width="90">
					<template #default="scope">
				<span class="status-badge status-paid" v-if="scope.row.zhuangtai=='已支付'">已支付</span>
				<span class="status-badge status-pending" v-else-if="scope.row.zhuangtai=='待支付'">待支付</span>
				<span class="status-badge status-cancelled" v-else-if="scope.row.zhuangtai=='已取消'">已取消</span>
				<span class="status-badge status-default" v-else>{{scope.row.zhuangtai || '-'}}</span>
					</template>
				</el-table-column>
				<el-table-column :resizable='true' :sortable='true'
											prop="chongzhiriqi"
					label="充值日期" min-width="110">
					<template #default="scope">
						{{scope.row.chongzhiriqi}}
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
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>





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
		components: {
			AddOrUpdate,
			Search,
			Refresh,
		},
		data() {
			return {
				indexQueryCondition: '',
				searchForm: {
					key: ""
				},
				form:{},
				dataList: [],
				pageIndex: 1,
				pageSize: 15,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
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
			statType() {
				return this.$route.path.endsWith('stat')
			}
		},
		methods: {
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
			},
			search() {
				this.pageIndex = 1;
				this.getDataList();
			},

			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
					order: 'desc',
				}
				if(this.searchForm.yonghuxingming!='' && this.searchForm.yonghuxingming!=undefined){
					params['yonghuxingming'] = '%' + this.searchForm.yonghuxingming + '%'
				}
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "huiyuanchongzhi/page",
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
						url: "huiyuanchongzhi/delete",
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
.status-badge {
	display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; font-weight: 600;
}
.status-paid { background: rgba(34,197,94,0.15); color: #4ade80; border: 1px solid rgba(34,197,94,0.3); }
.status-pending { background: rgba(245,158,11,0.15); color: #fbbf24; border: 1px solid rgba(245,158,11,0.3); }
.status-cancelled { background: rgba(148,163,184,0.15); color: #94a3b8; border: 1px solid rgba(148,163,184,0.3); }
.status-default { background: rgba(148,163,184,0.1); color: #94a3b8; border: 1px solid rgba(148,163,184,0.2); }
</style>
