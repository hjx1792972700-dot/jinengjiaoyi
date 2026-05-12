  
<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search">
				<el-button class="btn-primary" v-if="isAuth('jinengfenlei','新增')" size="small" :icon="Plus" @click="addOrUpdateHandler()">新增</el-button>
				<div class="search-field">
					<span class="field-label">技能分类</span>
					<el-input v-model="searchForm.jinengfenlei" placeholder="搜索技能分类..." @keydown.enter="search()" clearable size="small"></el-input>
				</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" :icon="Search" @click="search()">查询</el-button>
					<el-button class="btn-plain" size="small" :icon="Refresh" @click="searchForm={ key: '' }; search()">重置</el-button>
				</div>
			</div>
			<div class="admin-table-wrap">
				<el-table class="tables"
					:stripe='false'
					:border='false'
					v-if="isAuth('jinengfenlei','查看')"
					:data="dataList"
					v-loading="dataListLoading"
					@selection-change="selectionChangeHandler">
					<el-table-column type="selection" align="center" width="50"></el-table-column>
				<el-table-column :sortable='true' label="序号" type="index" align="center" />
				<el-table-column :sortable='true' prop="jinengfenlei" label="技能分类" align="center" />
				<el-table-column label="操作" align="center">
					<template #default="scope">
						<el-button v-if="isAuth('jinengfenlei','修改')" type="warning" plain size="small" @click="handleEdit(scope.row)">修改</el-button>
						<el-button v-if="isAuth('jinengfenlei','删除')" type="danger" plain size="small" @click="deleteHandler(scope.row.id)">删除</el-button>
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





	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update.vue";
	import { Search, Refresh, Plus } from '@element-plus/icons-vue';
	export default {
		components: {
			AddOrUpdate,
			Search,
			Refresh,
			Plus,
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
				if(this.searchForm.jinengfenlei!='' && this.searchForm.jinengfenlei!=undefined){
					params['jinengfenlei'] = '%' + this.searchForm.jinengfenlei + '%'
				}
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "jinengfenlei/page",
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
		handleEdit(row) {
			this.$http({
				url: "jinengxuqiu/page",
				method: "get",
				params: { page: 1, limit: 1, jinengfenlei: row.jinengfenlei }
			}).then(({ data }) => {
				if (data && data.code === 0 && data.data.total > 0) {
					this.$message({ type: 'warning', message: '该分类下已有技能数据，不允许修改！', duration: 2000 });
				} else {
					this.addOrUpdateHandler(row.id);
				}
			}).catch(() => {
				this.addOrUpdateHandler(row.id);
			});
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
						url: "jinengfenlei/delete",
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
</style>
